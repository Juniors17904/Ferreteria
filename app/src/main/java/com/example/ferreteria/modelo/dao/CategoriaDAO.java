package com.example.ferreteria.modelo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.servicios.ConectaDB;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private SQLiteDatabase db;
    private  String TAG = "----CategoriaDAO";


    public CategoriaDAO(Context context) {
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION).
                getWritableDatabase();

    }


    public List<Categoria> getListCat() {
        Log.i(TAG,"Obteniendo Categorias");
        List<Categoria> lista = new ArrayList<>();
        String cadSQL = "SELECT * FROM " + ConstantesApp.TABLA_CATEGORIAS + ";";
        Cursor c = db.rawQuery(cadSQL, null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Categoria categoria = new Categoria();
                    categoria.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    categoria.setNombre(c.getString(c.getColumnIndexOrThrow("nombre")));
                    categoria.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcion")));
                    categoria.setImagen(c.getInt(c.getColumnIndexOrThrow("imagen")));
                    lista.add(categoria);
                    //Log.i(TAG, "Categoría agregada: " + categoria.getNombre());
                } while (c.moveToNext());
            } else {
                Log.i(TAG, "No se encontraron registros en la tabla de categorías");
            }
            c.close();
        } else {
            Log.i(TAG, "Cursor es nulo");
        }

        Log.i(TAG, "Número total de categorías obtenidas: " + lista.size());
        return lista;
    }


    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
            //Log.i(TAG, "Base de datos cerrada");
        }
    }
}
