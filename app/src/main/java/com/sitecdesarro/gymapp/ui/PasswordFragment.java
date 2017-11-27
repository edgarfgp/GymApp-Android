package com.sitecdesarro.gymapp.ui;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sitecdesarro.gymapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";
    private EditText emailResetPassw;
    private Button btnReset;


    public PasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View rootView =inflater.inflate(R.layout.fragment_password, container, false);
        emailResetPassw = (EditText)rootView.findViewById(R.id.edtForgotEmail);

        mAuth = FirebaseAuth.getInstance();

        btnReset =(Button)rootView.findViewById(R.id.btnResetPassword);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(emailResetPassw.getText().toString().trim());
            }
        });





        return rootView;
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }


    public void resetPassword(String userEmail){

        mAuth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Snackbar.make(getView(),"Revisa tu cuenta de " +
                                            "correo",Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }else{

                            Log.d(TAG, "Email sent failure.");
                            Snackbar.make(getView(),"Datos incorrectos",
                                    Snackbar
                                    .LENGTH_SHORT)
                                    .show();
                        }
                    }
                });



    }

}
