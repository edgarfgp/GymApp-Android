package com.sitecdesarro.gymapp.Adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.ReservationModel;

import java.util.List;


public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter
        .MyViewHolder>{


    private List<ReservationModel> objectList;
    private int img;
    private FirebaseAuth mAuth;
    private DatabaseReference reference, reference2;
    private String actividad;


    public ReservationAdapter(List<ReservationModel> objectList, int img,
                              String actividad

    ) {
        this.objectList = objectList;
        this.img = img;
        this.actividad = actividad;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .list_item_reservations, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ReservationModel reservationModel = objectList.get(position);
        holder.hour.setText(reservationModel.getName());
        holder.date.setText(reservationModel.getDate());
        holder.instructor.setText(reservationModel.getInstructor());
        holder.logo.setImageResource(img);
        holder.delete();


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int posicion) {

                switch (view.getId()) {
                    case R.id.imgDelete:
                        removeItem(posicion);
                        mAuth = FirebaseAuth.getInstance();

                        if (actividad.equals("reservas_bici")) {
                            reference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mAuth.getCurrentUser().getUid()).child(actividad).child(reservationModel.getId());
                            reference2 =FirebaseDatabase.getInstance().getReference().child("horario_bicicletas").child(reservationModel.getId()).child("usuarios").child(mAuth.getCurrentUser().getUid());


                        }else if (actividad.equals("reservas_funcional")){
                            reference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(mAuth.getCurrentUser().getUid()).child(actividad).child(reservationModel.getId());
                            reference2 = FirebaseDatabase.getInstance().getReference().child("horario_funcional")
                                    .child(reservationModel.getId()).child("usuarios").child(mAuth.getCurrentUser().getUid());
                        }
                        reference.setValue(null);
                        reference2.setValue(null);

                }

            }
        });

    }

    public void removeItem(int position) {
        objectList.remove(position);
        notifyItemRemoved(position);

    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView hour;
        private TextView date;
        private TextView instructor;
        private ImageView logo, imageDelete;
        private ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            hour = (TextView) itemView.findViewById(R.id.txtReserhour);
            date = (TextView) itemView.findViewById(R.id.txtReserDate);
            instructor = (TextView) itemView.findViewById(R.id.txtReserMonitor);
            logo = (ImageView) itemView.findViewById(R.id.imgLogo);
            imageDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            itemView.setOnClickListener(this);

        }

        public void delete() {
            imageDelete.setOnClickListener(MyViewHolder.this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;

        }

        @Override
        public void onClick(View v) {

            itemClickListener.onClick(v, getAdapterPosition());


        }

    }

}
