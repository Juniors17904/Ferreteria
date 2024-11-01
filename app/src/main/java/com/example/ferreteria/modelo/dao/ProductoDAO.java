package com.example.ferreteria.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Producto;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.List;

// Clase para manejar operaciones sobre la tabla de productos
public class ProductoDAO {
    private SQLiteDatabase db;

    // Constructor que inicializa la conexión a la base de datos
    public ProductoDAO(Context context) {
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION).
                getWritableDatabase();
    }

    // Método para insertar un nuevo producto en la base de datos
    public String insertar(Producto p) {
        String resp = "";
        ContentValues registro = new ContentValues();
        registro.put("Nombre", p.getNombre());
        registro.put("Descripcion", p.getDescripcion());
        registro.put("Precio", p.getPrecio());
        registro.put("Stock", p.getStock());
        registro.put("CategoriaId", p.getCategoriaId());

        try {
            db.insertOrThrow(ConstantesApp.TABLA_PRODUCTOS, null, registro);
        } catch (SQLException ex) {
            resp = ex.getMessage();
        }
        return resp;
    }

    // Método para obtener una lista de productos de la base de datos
    public List<Producto> getList() {
        List<Producto> lista = new ArrayList<>();
        String cadSQL = "SELECT Id, Nombre, Descripcion, Precio, Stock, CategoriaId FROM " + ConstantesApp.TABLA_PRODUCTOS + ";";
        Cursor c = db.rawQuery(cadSQL, null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Producto p = new Producto();
                    p.setId(c.getInt(c.getColumnIndexOrThrow("Id")));
                    p.setNombre(c.getString(c.getColumnIndexOrThrow("Nombre")));
                    p.setDescripcion(c.getString(c.getColumnIndexOrThrow("Descripcion")));
                    p.setPrecio(c.getDouble(c.getColumnIndexOrThrow("Precio")));
                    p.setStock(c.getInt(c.getColumnIndexOrThrow("Stock")));
                    p.setCategoriaId(c.getInt(c.getColumnIndexOrThrow("CategoriaId")));
                    lista.add(p);
                } while (c.moveToNext());
            }
            c.close();
        }
        return lista;
    }
}
