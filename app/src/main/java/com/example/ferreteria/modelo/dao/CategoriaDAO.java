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
    private  String TAG = "CategoriaDAO";



    // Constructor que inicializa la conexión a la base de datos
    public CategoriaDAO(Context context) {
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION).
                getWritableDatabase();

    }


    // obtener una lista de todas las categorías
    public List<Categoria> getListCat() {
        List<Categoria> lista = new ArrayList<>();
        String cadSQL = "SELECT * FROM " + ConstantesApp.TABLA_CATEGORIAS + ";";
        Cursor c = db.rawQuery(cadSQL, null);

        if (c != null) {
            Log.i("CategoriaDAO", "Cursor inicializado correctamente");
            if (c.moveToFirst()) {
                Log.i("CategoriaDAO", "Se encontraron registros en la tabla de categorías");
                do {
                    Categoria categoria = new Categoria();
                    categoria.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                    categoria.setNombre(c.getString(c.getColumnIndexOrThrow("nombre")));
                    categoria.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcion")));
                    categoria.setImagen(c.getInt(c.getColumnIndexOrThrow("imagen")));
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


    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.i(TAG, "Base de datos cerrada");
        }
    }
}
