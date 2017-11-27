package com.sitecdesarro.gymapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.Botones;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SelectBikeActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference refCli;
    private DatabaseReference refHor;
    private DatabaseReference refHor1;
    private ArrayList<Botones> botonesArrayList;
    private TableLayout table ;
    private String fechaId;
    private Botones botonAnt;
    private String botonSelec;
    private Button finalizar;
    private  ImageView mainBike;
    private Map<String,String> bicisReser;

    public ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(SelectBikeActivity.this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bike);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        botonesArrayList = new ArrayList<>();
        botonAnt = new Botones(this);
        botonAnt.setUid("");
        mAuth = FirebaseAuth.getInstance();
        Bundle extras = getIntent().getExtras();
        fechaId = extras.getString("ID");
        table = (TableLayout)findViewById(R.id.tableLayout);
        finalizar = (Button)findViewById(R.id.btnSelecBici);
        mainBike = (ImageView)findViewById(R.id.imgMainBike);

        refCli = FirebaseDatabase.getInstance()
                .getReference
                        ("usuarios").child(mAuth.getCurrentUser().getUid()).child("reservas_bici").child(fechaId);

        refHor = FirebaseDatabase.getInstance().getReference("horario_bicicletas").child(fechaId).child("usuarios").child(mAuth.getCurrentUser().getUid());
        refHor1= FirebaseDatabase.getInstance().getReference("horario_bicicletas").child(fechaId).child("usuarios");
        refHor1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bicisReser = (Map<String,String>)dataSnapshot.getValue();
            if(botonesArrayList != null) {
                for (int i = 0; i < botonesArrayList.size(); i++) {
                    if (bicisReser != null && bicisReser.containsValue(botonesArrayList.get(i).getUid())) {

                        botonesArrayList.get(i).setImageResource(R.drawable.stacionary_red);
                        botonesArrayList.get(i).setEnabled(false);
                    } else{
                        botonesArrayList.get(i).setImageResource(R.drawable.ic_stationary_bike11);
                        botonesArrayList.get(i).setEnabled(true);
                }
                }
            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference("bicicletas");
        databaseReference.orderByKey().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                table.removeAllViews();
                botonesArrayList.clear();
                int i = 0;
                int x = 0;
                TableRow row = new TableRow(getApplicationContext());
                row.setWeightSum(5f);

                for (DataSnapshot d:
                        dataSnapshot.getChildren()) {

                    if((boolean)d.getValue()){

                        final Botones botones =  new Botones(getApplicationContext());

                        botones.setUid(d.getKey().toString());

                        if(bicisReser != null && bicisReser.containsValue(d.getKey())){
                            botones.setImageResource(R.drawable.stacionary_red);
                            botones.setEnabled(false);
                        }else{
                            botones.setImageResource(R.drawable.ic_stationary_bike11);
                        }
                        botones.setPadding(10,10,10,10);

                        botonesArrayList.add(botones);

                        row.addView(botonesArrayList.get(botonesArrayList.size()-1), new TableRow.LayoutParams(100,
                                100, 1F));

                        botonesArrayList.get(x).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Botones boton = (Botones)v;


                                boton.setImageResource(R.drawable.static_bike_green);
                                finalizar.setVisibility(View.VISIBLE);
                                if(botonAnt.getUid().equals(boton.getUid())) {
                                    if ((boton.isSeleccionado())) {
                                        botonSelec = "";
                                        boton.setSeleccionado(false);
                                        boton.setImageResource(R.drawable
                                                .ic_stationary_bike11);
                                        finalizar.setVisibility(View.INVISIBLE);

                                    }else{
                                        botonSelec = boton.getUid();
                                        boton.setSeleccionado(true);
                                        boton.setImageResource(R.drawable.static_bike_green);

                                    }
                                }else{
                                    botonSelec = boton.getUid();
                                    boton.setSeleccionado(true);
                                    boton.setImageResource(R.drawable.static_bike_green);
                                    botonAnt.setImageResource(R.drawable.ic_stationary_bike11);

                                }

                                botonAnt = boton;



                            }
                        });
                        x++;
                        i++;
                        if (i > 4){
                            table.addView(row, new TableLayout.LayoutParams
                                    (TableLayout.LayoutParams.WRAP_CONTENT,
                                            TableLayout.LayoutParams.WRAP_CONTENT));
                            row = new TableRow(getApplicationContext());
                            row.setWeightSum(5f);
                            i=0;
                        }

                    } }
                if(i>0){
                    for(int z=0;z<5-i;z++){
                        row.addView(new ImageView(getApplicationContext()), new TableRow.LayoutParams(100,
                                100, 1F));
                    }
                    table.addView(row, new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.WRAP_CONTENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        findViewById(R.id.btnSelecBici).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "Desea " +
                        "confirmar reserva?", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(R.color
                                .colorPrimary))
                        .setAction("Aceptar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                refCli.setValue(botonSelec);
                                refHor.setValue(botonSelec);
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivityIfNeeded(intent,0);

                            }
                        }).show();
                //Todo llamar al fragment de seleccion


            }

        });


    }
}
