package com.sitecdesarro.gymapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.DateModel;
import com.sitecdesarro.gymapp.ui.DayFragment;
import com.sitecdesarro.gymapp.ui.HourFragment;

import java.util.List;

import static com.sitecdesarro.gymapp.R.id.parent;


public class DateAdapter extends RecyclerView.Adapter<DateAdapter
        .MyViewHolder> {

    private  List <DateModel> objectList;

    DayFragment.Callbacks mCallbacks;
    HourFragment.Callbacks mCallbacksHour;

    public DateAdapter(List<DateModel> objectList,DayFragment.Callbacks mCallbacks, HourFragment.Callbacks mCallbacksHour) {
        this.objectList = objectList;
        this.mCallbacks = mCallbacks;
        this.mCallbacksHour = mCallbacksHour;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_date,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }





    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        DateModel dateModel = objectList.get(position);
        holder.day.setText(dateModel.getDay());
        holder.detail.setText(dateModel.getDetail());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int posicion) {

                mCallbacks.onEntradaSelecionada(objectList.get(posicion).getDate());
                mCallbacksHour.onEntradaSelecionadaH("",0,"",0);


            }
        });

    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        private TextView day;
        private TextView detail;
        private  ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            day = (TextView)itemView.findViewById(R.id.txtDay);
            detail = (TextView)itemView.findViewById(R.id.txtDayDetail);
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

