package com.sitecdesarro.gymapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.Adapter.ReservationAdapter;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.ReservationModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BikeReservationFragment extends Fragment {

    private List<ReservationModel> listRersevations;
    private ReservationAdapter adapter;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bike_reservation, container, false);


        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id
                .rclReservationBike);
        LinearLayoutManager layoutManager = new LinearLayoutManager
                (getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listRersevations = new ArrayList<>();
        adapter = new ReservationAdapter(listRersevations, R.drawable.bike1,"reservas_bici");
        recyclerView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(mAuth.getCurrentUser().getUid()).child
                        ("reservas_bici");
        databaseReference2 = FirebaseDatabase.getInstance().getReference()
                .child("horario_bicicletas");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    listRersevations.clear();

                for (DataSnapshot dt :
                        dataSnapshot.getChildren()) {
                    final String id =dt.getKey();
                    databaseReference2.child(dt.getKey())
                            .addListenerForSingleValueEvent
                                    (new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot2) {

                                            String nombreInstructor =  dataSnapshot2.child("entrenador").getValue().toString();
                                            String day = new SimpleDateFormat("dd-MM-yyyy")
                                                    .format
                                                            (new Date((long)dataSnapshot2.child("fecha") .getValue()));
                                            String hour = dataSnapshot2.child("hora").getValue().toString();
                                            if(hour.length()>3){
                                                hour = hour.substring(0,2)+":"+hour.substring(2,4);
                                            }else{
                                                hour = hour.substring(0,1)+":"+hour.substring(1,3);
                                            }
                                            ReservationModel reservaBici = new
                                                    ReservationModel();
                                            reservaBici.setId(id);
                                            reservaBici.setHour(hour);
                                            reservaBici.setInstructor(nombreInstructor);
                                            reservaBici.setDate(day);
                                            listRersevations.add(reservaBici);
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return rootView;
    }
}
