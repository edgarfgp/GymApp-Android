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
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.Adapter.DetailAdapter;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.DetailModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private List<DetailModel> listDetail;
    private DetailAdapter adapter;

    String hora = "";
    long fecha = 0;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_detail, container, false);

        if (getArguments().containsKey("dato") && getArguments().containsKey("fecha")) {
            hora = getArguments().getString("dato");
            fecha = getArguments().getLong("fecha");
        }

        RecyclerView recyclerView =(RecyclerView)rootView.findViewById(R.id
                .rclDetail);

        LinearLayoutManager layoutManager = new LinearLayoutManager
                (getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listDetail = new ArrayList<>();
        adapter =new DetailAdapter(listDetail);
        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef;

        String currentActivity = getActivity().getLocalClassName();

        if (currentActivity.equals("ui.BikeActivity")){

            dataRef = database.getReference("horario_bicicletas");

        }else{

            dataRef = database.getReference("horario_funcional");
        }

        dataRef.orderByChild("fecha").equalTo(fecha).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDetail.removeAll(listDetail);

                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    if(snapshot.child("hora").getValue().toString().equals(hora.replace(":",""))) {
                        String name = snapshot.child("entrenador").getValue().toString();
                        DetailModel detailModel = new DetailModel();
                        detailModel.setName(name);
                        listDetail.add(detailModel);
                    }

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DetailModel aux = new DetailModel();

        return rootView;
    }

}
