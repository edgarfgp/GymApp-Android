
package com.sitecdesarro.gymapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sitecdesarro.gymapp.R;
import com.sitecdesarro.gymapp.model.DateModel;
import com.sitecdesarro.gymapp.model.DetailModel;
import com.sitecdesarro.gymapp.model.HourModel;
import com.sitecdesarro.gymapp.ui.DetailFragment;

import java.util.List;

/**
 * Created by estav on 08/05/2017.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

    private List<DetailModel> objectList;

    public DetailAdapter(List<DetailModel> objectList) {
        this.objectList = objectList;

    }

    @Override
    public DetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_detail, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DetailAdapter.MyViewHolder holder, int position) {

        DetailModel detailModel = objectList.get(position);
        holder.name.setText(detailModel.getName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int posicion) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private  ItemClickListener itemClickListener;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.txtName);
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
