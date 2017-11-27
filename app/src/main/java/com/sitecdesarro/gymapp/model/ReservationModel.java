package com.sitecdesarro.gymapp.model;

/**
 * Created by estav on 24/05/2017.
 */

public class ReservationModel {

    private String date;
    private String instructor;
    private String hour;
    private int imagen;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public ReservationModel() {
    }


    public String getHour() {
        return hour;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getName() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }


}
