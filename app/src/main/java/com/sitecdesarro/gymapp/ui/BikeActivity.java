package com.sitecdesarro.gymapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.Adapter.DateAdapter;
import com.sitecdesarro.gymapp.R;

import java.util.Date;


public class BikeActivity extends AppCompatActivity implements DayFragment
        .Callbacks , HourFragment.Callbacks{
    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private String fechaId= "";
    private long plazasLibres;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        //podemos agregarle un Titulo y subtitulo
        fragmentManager = getSupportFragmentManager();
        DayFragment dayFragment = new DayFragment();
        fragmentManager.beginTransaction()
                .add(R.id.day_container, dayFragment)
                .commit();


        HourFragment fragment = new HourFragment();
        Bundle arguments = new Bundle();
        arguments.putLong("dato", 0);
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().add(R.id.hour_container, fragment).commit();


        DetailFragment fragmen = new DetailFragment();
        Bundle argument = new Bundle();
        argument.putString("dato", "no hay argumentos detail1");
        fragmen.setArguments(argument);
        getSupportFragmentManager().beginTransaction().add(R.id.detail_container, fragmen)
                .commit();


        findViewById(R.id.btnReservas).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!fechaId.equals("")){
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios").child(mAuth.getCurrentUser().getUid()).child("reservas_bici");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(fechaId)) {
                                if (plazasLibres >0){
                                    startActivity(new Intent(getApplicationContext(), SelectBikeActivity.class).putExtra("ID", fechaId));
                                }else{
                                    Snackbar.make(getCurrentFocus(),"No quedan plazas libres en este " +
                                            "horario",Snackbar
                                            .LENGTH_LONG).show();
                                }

                            }else{

                                Snackbar.make(getCurrentFocus(),"Ya " +
                                        "tiene un reserva en este " +
                                        "horario",Snackbar
                                        .LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });
                }else{
                    Snackbar.make(getCurrentFocus(),"Seleccione el " +
                            "día y la hora de la reserva " ,Snackbar
                            .LENGTH_LONG).show();
                }
            }
        });

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
    public void onEntradaSelecionadaH(String id, long fecha, String fechaId, long plazas) {

        this.fechaId = fechaId;
        plazasLibres = plazas;



        DetailFragment fragmen = new DetailFragment();
        Bundle argument = new Bundle();
        argument.putString("dato", id);
        argument.putLong("fecha", fecha);
        fragmen.setArguments(argument);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, fragmen).commit();

    }
}
