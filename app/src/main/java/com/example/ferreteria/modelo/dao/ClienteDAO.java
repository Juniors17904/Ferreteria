package com.example.ferreteria.modelo.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Cliente;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.List;

// Clase para manejar operaciones sobre la tabla de Cliente
    public class ClienteDAO {
        private SQLiteDatabase db;

    // Constructor que inicializa la conexión a la base de datos
    public ClienteDAO(Context context) {
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION).
                getWritableDatabase();
}

    // Método para insertar una nueva cliente
    public boolean insertar(Cliente c) {
            String resp = "";
            ContentValues registro = new ContentValues();
            registro.put("Nombre", c.getNombre());
            registro.put("Correo", c.getCorreo());
            registro.put("Telefono", c.getTelefono());
            registro.put("Direccion", c.getDireccion());

            try {
                db.insertOrThrow(ConstantesApp.TABLA_CLIENTES, null, registro);
            } catch (SQLException ex) {
                Log.i("Insertion Client: ", ex.getMessage());
                return false;
            }
            return true;
        }

    // Método para obtener una lista de todas las clientes
    public List<Cliente> getList() {
            List<Cliente> lista = new ArrayList<>();
            String cadSQL = "SELECT * FROM " + ConstantesApp.TABLA_CLIENTES + ";";
            Cursor c = db.rawQuery(cadSQL, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Cliente cliente = new Cliente();
                        cliente.setId(c.getInt(c.getColumnIndexOrThrow("Id")));
                        cliente.setNombre(c.getString(c.getColumnIndexOrThrow("Nombre")));
                        cliente.setCorreo(c.getString(c.getColumnIndexOrThrow("Correo")));
                        cliente.setTelefono(c.getString(c.getColumnIndexOrThrow("Telefono")));
                        cliente.setDireccion(c.getString(c.getColumnIndexOrThrow("Direccion")));
                        lista.add(cliente);
                    } while (c.moveToNext());
                }
                c.close();
            }
            return lista;
        }

    @SuppressLint("Range")
    public int getIdByClient(String email) {
        int clientId = -1;
        String query = "SELECT id FROM "+ ConstantesApp.TABLA_CLIENTES +" WHERE correo = ? LIMIT 1";

        try {
            Cursor cursor = db.rawQuery(query, new String[]{email});

            if (cursor.moveToFirst()) {
                clientId = cursor.getInt(cursor.getColumnIndex("id"));
            }

            cursor.close();
            return clientId;
        } catch (SQLException e) {
            Log.i("Obteniendo id del cliente: ", String.valueOf(clientId) + "Error: "+ e.getMessage());
            return clientId;
        }
    }
}


