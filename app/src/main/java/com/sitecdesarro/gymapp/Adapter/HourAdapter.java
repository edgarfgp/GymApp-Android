package com.sitecdesarro.gymapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.HourModel;
import com.sitecdesarro.gymapp.ui.HourFragment;

import java.util.List;

/**
 * Created by estav on 08/05/2017.
 */

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.MyViewHolder> {

    private List<HourModel> objectList;
    HourFragment.Callbacks mCallbacks;
    long fecha;


    public HourAdapter(List<HourModel> objectList, HourFragment.Callbacks mCallbacks, long fecha) {
        this.objectList = objectList;
        this.mCallbacks = mCallbacks;
        this.fecha = fecha;
    }

    @Override
    public HourAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_hour, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HourModel hourModel = objectList.get(position);
        holder.hour.setText(hourModel.getHour());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int posicion) {

                mCallbacks.onEntradaSelecionadaH(objectList.get(posicion)
                        .getHour().toString(), fecha,objectList.get(posicion).getHourId(), objectList.get(posicion).getPlazasLibres());
            }
        });

    }


    @Override
    public int getItemCount() {
        return objectList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView hour;
        private  ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            hour = (TextView) itemView.findViewById(R.id.txtClass);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;

        }

        @Override
        public void onClick(View v) {

            itemClickListener.onClick(v,getAdapterPosition());

        }
    }
}
