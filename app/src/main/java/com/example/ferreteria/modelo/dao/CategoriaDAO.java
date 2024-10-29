package com.example.ferreteria.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private SQLiteDatabase db;

    // Constructor que inicializa la conexión a la base de datos
    public CategoriaDAO(Context context) {
        Log.i("CategoriaDAO", "leyendo conectadb");
        db = new ConectaDB(context).getWritableDatabase();
    }

    // Método para insertar una nueva categoría
    public String insertar(Categoria categoria) {
        String resp = "";
        ContentValues registro = new ContentValues();
        registro.put("nombre", categoria.getNombre()); // Cambiado a "nombre" en minúsculas
        registro.put("descripcion", categoria.getDescripcion()); // Añadido campo descripción
        registro.put("imagen", categoria.getImagen()); // Añadido campo imagen

        try {
            db.insertOrThrow(ConstantesApp.TABLA_CATEGORIAS, null, registro);
            Log.i("CategoriaDAO", "Categoría insertada: " + categoria.getNombre());
        } catch (SQLException ex) {
            resp = ex.getMessage();
            Log.e("CategoriaDAO", "Error al insertar categoría: " + resp);
        }
        return resp;
    }

    // Método para obtener una lista de todas las categorías
    public List<Categoria> getList() {
        List<Categoria> lista = new ArrayList<>();
        String cadSQL = "SELECT * FROM " + ConstantesApp.TABLA_CATEGORIAS + ";";
        Cursor c = db.rawQuery(cadSQL, null);

        if (c != null) {
            Log.i("CategoriaDAO", "Cursor inicializado correctamente");
            if (c.moveToFirst()) {
                Log.i("CategoriaDAO", "Se encontraron registros en la tabla de categorías");
                do {
                    Categoria categoria = new Categoria();
                    categoria.setId(c.getInt(c.getColumnIndexOrThrow("id"))); // Cambiado a "id" en minúsculas
                    categoria.setNombre(c.getString(c.getColumnIndexOrThrow("nombre"))); // Cambiado a "nombre" en minúsculas
                    categoria.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcion"))); // Añadido para obtener descripción
                    categoria.setImagen(c.getInt(c.getColumnIndexOrThrow("imagen"))); // Añadido para obtener imagen
                    lista.add(categoria);
                    Log.i("CategoriaDAO", "Categoría agregada: " + categoria.getNombre());
                } while (c.moveToNext());
            } else {
                Log.i("CategoriaDAO", "No se encontraron registros en la tabla de categorías");
            }
            c.close();
        } else {
            Log.i("CategoriaDAO", "Cursor es nulo. Revisa la consulta o la estructura de la base de datos.");
        }

        Log.i("CategoriaDAO", "Número total de categorías obtenidas: " + lista.size());
        return lista;
    }
}