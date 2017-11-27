package com.sitecdesarro.gymapp.model;

import android.content.Context;
import android.widget.Button;

/**
 * Created by estav on 28/05/2017.
 */

public class Botones  extends android.support.v7.widget.AppCompatImageView{
    private  boolean disponible;
    private  String uid;
    private boolean seleccionado;



    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }





    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Botones(Context context) {
        super(context);
        seleccionado = false;
    }


}
