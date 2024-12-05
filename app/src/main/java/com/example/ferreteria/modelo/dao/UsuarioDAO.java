package com.example.ferreteria.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.util.Log;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Usuario;
import com.example.ferreteria.servicios.ConectaDB;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

// Clase para manejar operaciones sobre la tabla de Usuario
public class UsuarioDAO  {
    private SQLiteDatabase db;
    private String TAG = "----UsuarioDAO";

    // Constructor que inicializa la conexión a la base de datos
    public UsuarioDAO(Context context) {
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION).
                getWritableDatabase();
    }


    public String insertar(Usuario u) {
        Log.i(TAG,"insertando");
        String resp = "";
        ContentValues registro = new ContentValues();
        registro.put("nombre", u.getNombre());
        registro.put("apellido", u.getApellido());
        registro.put("dni", u.getDni());
        registro.put("telefono", u.getTelefono());
        registro.put("email", u.getEmail());
        registro.put("contrasena", u.getContrasena());

        if (u.getRoll() != null) {
            registro.put("roll", u.getRoll());
        }

        try {
            db.insertOrThrow(ConstantesApp.TABLA_USUARIOS, null, registro);
        } catch (SQLException ex) {
            resp = ex.getMessage();
            Log.i(TAG, ex.getMessage());

        }
        return resp;
    }

    public Usuario obtenerUsuarioPorEmail(String email) {
        Usuario usuario = null;
        String query = "SELECT * FROM usuarios WHERE email = ?";
        Cursor c = db.rawQuery(query, new String[] { email });

        if (c.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            usuario.setNombre(c.getString(c.getColumnIndexOrThrow("nombre")));
            usuario.setApellido(c.getString(c.getColumnIndexOrThrow("apellido")));
            usuario.setDni(c.getString(c.getColumnIndexOrThrow("dni")));
            usuario.setTelefono(c.getString(c.getColumnIndexOrThrow("telefono")));
            usuario.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
            usuario.setContrasena(c.getString(c.getColumnIndexOrThrow("contrasena")));
            usuario.setRoll(c.getString(c.getColumnIndexOrThrow("roll")));
        }

        c.close();
        return usuario;
    }

    public String encriptarSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error al encriptar contraseña: " + e.getMessage());
            return null;
        }
    }


    public List<Usuario> getList() {
        List<Usuario> lista = new ArrayList<>();
        String cadSQL = "SELECT * FROM " + ConstantesApp.TABLA_USUARIOS + ";";
        Cursor c = db.rawQuery(cadSQL, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Usuario usuario = new Usuario();
                    usuario.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    usuario.setNombre(c.getString(c.getColumnIndexOrThrow("nombre")));
                    usuario.setApellido(c.getString(c.getColumnIndexOrThrow("apellido")));
                    usuario.setDni(c.getString(c.getColumnIndexOrThrow("dni")));
                    usuario.setTelefono(c.getString(c.getColumnIndexOrThrow("telefono")));
                    usuario.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
                    usuario.setContrasena(c.getString(c.getColumnIndexOrThrow("contrasena")));
                    usuario.setRoll(c.getString(c.getColumnIndexOrThrow("roll")));
                    lista.add(usuario);
                } while (c.moveToNext());
            }
            c.close();
        }
        return lista;
    }
}
