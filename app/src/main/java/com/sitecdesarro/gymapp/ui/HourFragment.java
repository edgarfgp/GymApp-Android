package com.sitecdesarro.gymapp.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.Adapter.HourAdapter;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.HourModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HourFragment extends Fragment {

    private List<HourModel> listHour;

    private HourAdapter adapter;
    private Callbacks mCallbacks = CallbacksVacios;
    private String fechaId = "";
    private boolean visible = false;

    private long fecha = 0;

    public interface Callbacks {
        public void onEntradaSelecionadaH(String id, long fecha, String
                fechaId, long plazasLibres);
    }

    private static Callbacks CallbacksVacios = new Callbacks() {
        @Override
        public void onEntradaSelecionadaH(String id, long fecha, String
                fechaId, long plazasLibres) {
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Error: La actividad debe implementar el callback del fragmento");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = CallbacksVacios;
    }


    public HourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_hour, container, false);

        if (getArguments().containsKey("dato")) {
            fecha = getArguments().getLong("dato");

        }

        RecyclerView recyclerView =(RecyclerView)rootView.findViewById(R.id
                .rclHour);

        LinearLayoutManager layoutManager = new LinearLayoutManager
                (getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        listHour = new ArrayList<>();
        adapter = new HourAdapter(listHour, mCallbacks, fecha);

        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef;

        String currentActivity = getActivity().getLocalClassName();

        if (currentActivity.equals("ui.BikeActivity")){

            dataRef = database.getReference("horario_bicicletas");

        }else{

            dataRef = database.getReference("horario_funcional");
        }


        dataRef.orderByChild("hora").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listHour.removeAll(listHour);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    if((long)(snapshot.child("fecha").getValue())== (fecha)) {
                        String hour = snapshot.child("hora").getValue().toString();
                        long plazasLibres = Long.parseLong(snapshot.child("plazas_libres").getValue().toString());
                        if(hour.length()>3){
                            hour = hour.substring(0,2)+":"+hour.substring(2,4);
                        }else{
                            hour = hour.substring(0,1)+":"+hour.substring(1,3);
                        }
                        HourModel hourModel = new HourModel();
                        hourModel.setHour(hour);
                        hourModel.setPlazasLibres(plazasLibres);
                        hourModel.setHourId(snapshot.getKey());
                        listHour.add(hourModel);
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;


    }

}
