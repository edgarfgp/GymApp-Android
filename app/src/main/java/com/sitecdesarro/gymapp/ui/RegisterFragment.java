package com.sitecdesarro.gymapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private EditText emailReg;
    private EditText passwordReg1,passwordReg2;
    private FirebaseAuth mAuth;
    private EditText nombre, apellido;
    private static final String TAG = "EmailPassword";
    private DatabaseReference databaseReference;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        nombre = (EditText) rootView.findViewById(R.id.edtUserReg);
        apellido = (EditText) rootView.findViewById(R.id.edtApelReg);
        emailReg = (EditText) rootView.findViewById(R.id.edtUserEmail);
        passwordReg1 = (EditText) rootView.findViewById(R.id.edtPasswordReg1);
        passwordReg2 = (EditText) rootView.findViewById(R.id.edtPasswordReg2);

        mAuth = FirebaseAuth.getInstance();

        rootView.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createAccount(emailReg.getText().toString().trim(), passwordReg2
                        .getText().toString().trim());


            }
        });

        return rootView;
    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
        // [END create_user_with_email]
    }

    private void sendEmailVerification() {


        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Snackbar.make(getView(),"Correo de verificacion " +
                                            "enviado" +
                                            " " + user.getEmail(),
                                    Snackbar
                                            .LENGTH_SHORT)
                                    .show();
                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference
                                            ("usuarios");
                            Usuario usuario = new Usuario(user.getEmail(),
                                    nombre.getText().toString(),apellido
                                    .getText().toString());
                            databaseReference.child(user.getUid()).setValue(usuario);
                            startActivity(new Intent(getContext(), MainActivity.class));

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Snackbar.make(getView(),"Correo electronico " ,
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });

    }


    private boolean validateForm() {
        boolean valid = true;

        String email = emailReg.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailReg.setError("Obligatorio.");
            valid = false;
        } else {
            emailReg.setError(null);
        }
        String nom = nombre.getText().toString();
        if (TextUtils.isEmpty(nom)) {
            nombre.setError("Obligatorio.");
            valid = false;
        } else {
            nombre.setError(null);
        }
        String ape = apellido.getText().toString();
        if (TextUtils.isEmpty(ape)) {
            apellido.setError("Obligatorio.");
            valid = false;
        } else {
            apellido.setError(null);
        }

        String password1 = passwordReg1.getText().toString();
        if (TextUtils.isEmpty(password1)) {
            passwordReg1.setError("Obligatorio.");
            valid = false;
        } else {
            passwordReg1.setError(null);
        }
        String password = passwordReg2.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordReg2.setError("Obligatorio.");
            valid = false;
        } else {
            passwordReg2.setError(null);
        }

        return valid;
    }


}
