package com.example.ferreteria.modelo.dto;

import androidx.annotation.NonNull;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;  // Apellido
    private String dni;       // DNI
    private String telefono;  // Teléfono
    private String email;     // Email
    private String contrasena;  // Contraseña
    private String roll;        // Roll (por defecto 'cliente')

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con todos los campos
    public Usuario(int id, String nombre, String apellido, String dni, String telefono, String email, String contrasena, String roll) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
        this.contrasena = contrasena;
        this.roll = roll;
    }

    // Getters y setters para todos los campos
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contraseaa) {
        this.contrasena = contraseaa;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    @NonNull
    @Override
    public String toString() {
        return getNombre(); // Esto permitirá que el objeto Usuario se muestre por su nombre en listas
    }
}
