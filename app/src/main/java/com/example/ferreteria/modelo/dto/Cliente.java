package com.example.ferreteria.modelo.dto;

import androidx.annotation.NonNull;

public class Cliente {
    private int id;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccion;

    public Cliente() {
    }

    public Cliente(int id, String nombre, String correo, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

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

    @NonNull
    @Override
    public String toString() {
        return getNombre(); // Esto permitirá que el objeto Cliente se muestre por su nombre en listas
    }
}
