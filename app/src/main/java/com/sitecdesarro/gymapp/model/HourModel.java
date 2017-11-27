package com.sitecdesarro.gymapp.model;

/**
 * Created by estav on 08/05/2017.
 */

public class HourModel {

    private String hour;
    private String hourId;

    public Long getPlazasLibres() {
        return plazasLibres;
    }

    public void setPlazasLibres(Long plazasLibres) {
        this.plazasLibres = plazasLibres;
    }

    private  long plazasLibres;

    public String getHourId() {
        return hourId;
    }

    public void setHourId(String hourId) {
        this.hourId = hourId;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }


}
