package com.sitecdesarro.gymapp.model;

import com.sitecdesarro.gymapp.ui.SelectionFragment;

/**
 * Created by estav on 28/05/2017.
 */

public class Usuario {
    private String nombre;
    private  String email;
    private String telefono;

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    private String direccion;

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    private String apellido;
    private  String uid;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Usuario() {

    }

    public Usuario(String email, String nombre, String apellido ) {
        this.nombre = nombre;
        this.email = email;
        this.apellido = apellido;
        this.uid = uid;
    }


}
