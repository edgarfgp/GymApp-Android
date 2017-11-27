package com.sitecdesarro.gymapp.ui;

import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.Usuario;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private EditText nom, ape, em, tel, dir;
    private FloatingActionButton edit, save;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mAuth.getCurrentUser().getUid());

        nom = (EditText) findViewById(R.id.edtProfNombre);
        ape = (EditText) findViewById(R.id.edtProfApellido);
        em = (EditText) findViewById(R.id.edtProfEmail);
        tel = (EditText) findViewById(R.id.edtProfTelefono);
        dir = (EditText) findViewById(R.id.edtProfDireccion);
        edit = (FloatingActionButton) findViewById(R.id.flotEdit);
        save = (FloatingActionButton) findViewById(R.id.flotSave);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue().toString();
                String correo = dataSnapshot.child("email").getValue().toString();
                String apellido = dataSnapshot.child("apellido").getValue().toString();
                String telefono = Objects.toString(dataSnapshot.child("telefono").getValue()
                       ,"");
                String direccion = Objects.toString(dataSnapshot.child("direccion").getValue()
                        ,"");

                nom.setText(nombre);
                em.setText(correo);
                ape.setText(apellido);

                if (telefono != null) tel.setText(telefono);

                if (direccion != null) dir.setText(direccion);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save.setVisibility(View.VISIBLE);
                tel.setEnabled(true);
                dir.setEnabled(true);
                v.setVisibility(View.INVISIBLE);


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Desea guardar datos?", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color
                                .colorPrimary))
                        .setAction("Aceptar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Usuario usuario = new Usuario();
                                usuario.setNombre(nom.getText().toString());
                                usuario.setApellido(ape.getText().toString());
                                usuario.setEmail(em.getText().toString());


                                if (!tel.getText().equals("")) {

                                    usuario.setTelefono(tel.getText().toString());


                                } else {
                                    usuario.setTelefono("");

                                }

                                if (!dir.getText().equals("")) {
                                    usuario.setDireccion(dir.getText().toString());


                                } else {
                                    usuario.setDireccion("");
                                }

                                reference.setValue(usuario);


                                tel.setEnabled(false);
                                dir.setEnabled(false);

                                Snackbar.make(v,("Datos guardados " +
                                        "corectamente"),Snackbar.LENGTH_LONG).show();

                            }
                        })
                        .show();
                v.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.VISIBLE);



            }
        });


    }
}
