package com.sitecdesarro.gymapp.ui;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.Usuario;

import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button access, password, signUp;
    private EditText emailUser;
    private EditText passwordUser;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    public ProgressDialog mProgressDialog;
    private NavigationView navView;
    private TextView nom;
    private TextView cor;
    private MainActivity activity;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        navView.getMenu().findItem(R.id.itemProfile).setVisible(false);
        navView.getMenu().findItem(R.id.itemReservation).setVisible(false);
        navView.getMenu().findItem(R.id.itemLogOut).setVisible(false);
        nom = (TextView) activity.findViewById(R.id.txtNameNav);
        cor = (TextView) activity.findViewById(R.id.txtEmailNav);

        if (nom != null || cor != null) {
            nom.setText("");
            cor.setText("");
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        navView = (NavigationView) activity.findViewById(R.id
                .navigationView);


    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        emailUser = (EditText) rootView.findViewById(R.id.edtUser);
        passwordUser = (EditText) rootView.findViewById(R.id.edtPassword);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        rootView.findViewById(R.id.btnAccess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//                SelectionFragment selectionFragment = new SelectionFragment();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.aux_container, selectionFragment)
//                        .addToBackStack(null)
//                        .commit();


                signIn(emailUser.getText().toString().trim(), passwordUser
                        .getText().toString().trim());


            }
        });


        rootView.findViewById(R.id.btnSigUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.aux_container, registerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        rootView.findViewById(R.id.btnPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PasswordFragment passwordFragment = new PasswordFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.aux_container, passwordFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        return rootView;
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();


        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase.getInstance().getReference().child("usuarios")
                                    .child(user.getUid()).child("token").child(FirebaseInstanceId.getInstance()
                                    .getToken()).setValue(true);
                            SelectionFragment selectionFragment = new SelectionFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.aux_container, selectionFragment)
                                    .addToBackStack(null)
                                    .commit();



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Snackbar.make(getView(), "Datos incorrectos. " +
                                    "Vuelva a intentarlo",Snackbar.LENGTH_LONG).show();

                        }

                        hideProgressDialog();


                    }
                });
        // [END sign_in_with_email]
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = emailUser.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailUser.setError("Required.");
            valid = false;
        } else {
            emailUser.setError(null);
        }

        String password = passwordUser.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordUser.setError("Required.");
            valid = false;
        } else {
            passwordUser.setError(null);
        }

        return valid;
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }





}


