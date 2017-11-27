package com.sitecdesarro.gymapp.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.R;

import java.util.Date;

public class FunctionalActivity extends AppCompatActivity implements
        DayFragment.Callbacks, HourFragment.Callbacks {

    private FragmentManager fragmentManager;
    private DatabaseReference refCli;
    private DatabaseReference refHor;
    private String fechaId = "";
    private FirebaseAuth mAuth;
    private long plazasLibres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional);
        mAuth = FirebaseAuth.getInstance();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        findViewById(R.id.btnReservas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fechaId.equals("")) {
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference
                                    ("usuarios").child(mAuth
                                    .getCurrentUser().getUid()).child
                                    ("reservas_funcional");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(fechaId)) {
                                if (plazasLibres >0){
                                    refCli = FirebaseDatabase.getInstance()
                                            .getReference
                                                    ("usuarios").child(mAuth.getCurrentUser().getUid())
                                            .child("reservas_funcional").child(fechaId);

                                    refHor = FirebaseDatabase.getInstance()
                                            .getReference
                                                    ("horario_funcional").child(fechaId).child("usuarios")
                                            .child(mAuth.getCurrentUser().getUid());
                                    Snackbar.make(getCurrentFocus(), "Desea " +
                                            "confirmar reserva?", Snackbar.LENGTH_LONG)
                                            .setActionTextColor(getResources().getColor(R.color
                                                    .colorPrimary))
                                            .setAction("Aceptar", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    refCli.setValue(true);
                                                    refHor.setValue(true);

                                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                    startActivityIfNeeded(intent,0);


                                                }
                                            }).show();

                                }else{
                                    Snackbar.make(getCurrentFocus(),"No quedan plazas libres en este " +
                                            "horario",Snackbar
                                            .LENGTH_LONG).show();
                                }



                            } else {

                                Snackbar.make(getCurrentFocus(), "Ya " +
                                        "tiene un reserva en este " +
                                        "horario", Snackbar
                                        .LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // ...
                        }


                    });
                } else {
                    Snackbar.make(getCurrentFocus(), "Seleccione el " +
                            "día y la hora de la reserva ", Snackbar
                            .LENGTH_LONG).show();
                }
            }
        });


        fragmentManager = getSupportFragmentManager();


        DayFragment dayFragment = new DayFragment();
        fragmentManager.beginTransaction()
                .add(R.id.day_container, dayFragment)
                .commit();

        Bundle arguments = new Bundle();
        arguments.putString("dato", "no hay argumentos hour");
        HourFragment fragment = new HourFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().add(R.id.hour_container, fragment).commit();


        DetailFragment fragmen = new DetailFragment();
        Bundle argument = new Bundle();
        argument.putString("dato", "no hay argumentos detail1");
        fragmen.setArguments(argument);
        getSupportFragmentManager().beginTransaction().add(R.id.detail_container, fragmen)
                .commit();
    }

    @Override
    public void onEntradaSelecionada(Date id) {
        this.fechaId = "";
        Bundle arguments = new Bundle();
        arguments.putLong("dato", id.getTime());

        HourFragment fragment = new HourFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.hour_container, fragment).commit();
    }

    @Override
    public void onEntradaSelecionadaH(String id, long fecha, String fechaId, long plazasLibres) {
        this.fechaId = fechaId;
        this.plazasLibres = plazasLibres;
        DetailFragment fragmen = new DetailFragment();
        Bundle argument = new Bundle();
        argument.putString("dato", id);
        argument.putLong("fecha", fecha);
        fragmen.setArguments(argument);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, fragmen).commit();

    }
}
