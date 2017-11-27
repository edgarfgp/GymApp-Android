package com.sitecdesarro.gymapp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.Adapter.DateAdapter;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.DateModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayFragment extends Fragment {
    private List<DateModel> listDate;
    private DateAdapter adapter;
    private Callbacks mCallbacks = CallbacksVacios;
    private HourFragment.Callbacks mCallbacksHora;
    private Date fechaDate;
    public DayFragment() {

    }

    public interface Callbacks {
        public void onEntradaSelecionada(Date id);
    }

    private static Callbacks CallbacksVacios = new Callbacks() {
        @Override
        public void onEntradaSelecionada(Date id) {
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
        mCallbacksHora = (HourFragment.Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = CallbacksVacios;
    }

    public ProgressDialog mProgressDialog;
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
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id
                .rclDay);
        LinearLayoutManager layoutManager = new LinearLayoutManager
                (getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        listDate = new ArrayList<>();
        adapter = new DateAdapter(listDate, mCallbacks, mCallbacksHora);
        recyclerView.setAdapter(adapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef;
        String currentActivity = getActivity().getLocalClassName();
        if (currentActivity.equals("ui.BikeActivity")) {
            dataRef = database.getReference("horario_bicicletas");
            showProgressDialog();
        } else {
            dataRef = database.getReference("horario_funcional");
            showProgressDialog();
        }
        dataRef.orderByChild("fecha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listDate.removeAll(listDate);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {

                    String day =  new SimpleDateFormat("dd-MM-yyyy")
                            .format
                                    (new Date((long)snapshot.child("fecha").getValue()));


                    Date dt1 = new Date();
                    SimpleDateFormat format1 = new SimpleDateFormat
                            ("dd-MM-yyyy", new Locale("es", "ES"));
                    try {
                        dt1 = format1.parse(day);
                        String d = (String) DateFormat.format("EEEE", dt1);
                        String dt = (String) DateFormat.format("dd", dt1);
                        String dt2 = (String) DateFormat.format("MMMM", dt1);
                        DateModel dateModel = new DateModel();
                        dateModel.setDay(d);
                        dateModel.setDate(new Date((long)snapshot.child("fecha").getValue()));
                        dateModel.setDetail(dt + " " + dt2);
                        if(listDate.size()<=0){
                            listDate.add(dateModel);
                        }else if(!listDate.get(listDate.size()-1).getDate().equals(dateModel.getDate())) {
                            listDate.add(dateModel);
                        }
                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();
                hideProgressDialog();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return rootView;
    }

}
