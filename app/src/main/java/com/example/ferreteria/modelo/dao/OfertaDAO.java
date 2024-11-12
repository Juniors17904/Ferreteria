package com.example.ferreteria.modelo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Oferta;
import com.example.ferreteria.servicios.ConectaDB;
import java.util.ArrayList;
import java.util.List;

public class OfertaDAO {

    private SQLiteDatabase db;
    private final String TAG = "----OfertaDAO";

    // Constructor que inicializa la conexión a la base de datos
    public OfertaDAO(Context context) {
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION).
                getWritableDatabase();
    }

    public List<Oferta> getListOfertas() {
        Log.i(TAG,"Obteniendo ofertas");
        List<Oferta> lista = new ArrayList<>();

        String cadSQL = "SELECT \n" +
                "    o.id AS ofertaId,    " +
                "    p.id AS productoId,  " +
                "    p.marca AS nombreProducto," +
                "    p.descripcion AS descripcionProducto, " +
                "    p.precio AS precioOriginal, " +
                "    o.descuento AS descuento," +
                "    ROUND(p.precio * (1 - (o.descuento / 100.0)), 2) AS precioConDescuento," +
                "    o.fechaInicio," +
                "    o.fechaFin, " +
                "    p.imagen AS imagenProducto " +
                "FROM " + ConstantesApp.TABLA_PRODUCTOS + " p " +
                "JOIN " + ConstantesApp.TABLA_OFERTAS + " o ON p.id = o.productoId " +
                "WHERE o.fechaFin >= DATE('now');";

        Cursor c = db.rawQuery(cadSQL, null);

        if (c != null) {
          //  Log.i(TAG, "Cursor inicializado correctamente");
            if (c.moveToFirst()) {
                do {
                    Oferta oferta = new Oferta();
                    oferta.setId(c.getInt(c.getColumnIndexOrThrow("ofertaId"))); // ID de la oferta
                    oferta.setProductoId(c.getInt(c.getColumnIndexOrThrow("productoId"))); // ID del producto
                    oferta.setMarcaProducto(c.getString(c.getColumnIndexOrThrow("nombreProducto")));
                    oferta.setDescripcionProducto(c.getString(c.getColumnIndexOrThrow("descripcionProducto")));
                    oferta.setPrecioOriginal(c.getDouble(c.getColumnIndexOrThrow("precioOriginal")));
                    oferta.setDescuento(c.getDouble(c.getColumnIndexOrThrow("descuento")));
                    oferta.setPrecioConDescuento(c.getDouble(c.getColumnIndexOrThrow("precioConDescuento")));
                    oferta.setFechaInicio(c.getString(c.getColumnIndexOrThrow("fechaInicio")));
                    oferta.setFechaFin(c.getString(c.getColumnIndexOrThrow("fechaFin")));
                    oferta.setImagenProducto(c.getInt(c.getColumnIndexOrThrow("imagenProducto")));

                    // Depuración para verificar valores
                  //  Log.i(TAG, "Oferta agregada: ID Oferta = " + oferta.getId() + ", Producto ID = " + oferta.getProductoId());

                    lista.add(oferta);
                } while (c.moveToNext());
            } else {
                Log.i(TAG, "No se encontraron registros en la tabla de ofertas");
            }
            c.close();
        } else {
            Log.i(TAG, "Cursor es nulo.");
        }

        Log.i(TAG, "Número total de ofertas obtenidas: " + lista.size());
        return lista;
    }



    public void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
            //Log.i(TAG, "Base de datos cerrada");
        }
    }
}
