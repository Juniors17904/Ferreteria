package com.example.ferreteria.modelo.dto;

import androidx.annotation.NonNull;

public class Categoria {
    private int id;
    private String nombre;
    private String descripcion; // Campo para la descripción
    private int imagen; // Campo para la imagen

    // Constructor por defecto
    public Categoria() {
    }

    // Constructor con parámetros
    public Categoria(int id, String nombre, String descripcion, int imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen; // Inicializa el campo imagen
    }

    // Getters y Setters
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

    public String getDescripcion() { // Getter para la descripción
        return descripcion;
    }

    public void setDescripcion(String descripcion) { // Setter para la descripción
        this.descripcion = descripcion;
    }

    public int getImagen() { // Getter para la imagen
        return imagen;
    }

    public void setImagen(int imagen) { // Setter para la imagen
        this.imagen = imagen;
    }

    @NonNull
    @Override
    public String toString() {
        return getNombre(); // Retorna el nombre para mostrar en una lista
    }
}
