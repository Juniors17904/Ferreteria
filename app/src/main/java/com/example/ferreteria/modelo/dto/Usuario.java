package com.example.ferreteria.modelo.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Usuario implements Parcelable {
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
        this.roll = "cliente"; // Valor por defecto
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

    // Getters y setters
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

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // Métodos de Parcelable
    protected Usuario(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        apellido = in.readString();
        dni = in.readString();
        telefono = in.readString();
        email = in.readString();
        contrasena = in.readString();
        roll = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(dni);
        dest.writeString(telefono);
        dest.writeString(email);
        dest.writeString(contrasena);
        dest.writeString(roll);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}
