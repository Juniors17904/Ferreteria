package com.example.ferreteria.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.DetallePedido;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.List;

// Clase para manejar operaciones sobre la tabla de detalles de pedido
public class DetallePedidoDAO {
    private SQLiteDatabase db;

    // Constructor que inicializa la conexión a la base de datos
    public DetallePedidoDAO(Context context) {
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION).
                getWritableDatabase();
    }

    // Método para insertar un nuevo detalle de pedido en la base de datos
    public String insertar(DetallePedido detalle) {
        String resp = "";
        ContentValues registro = new ContentValues();
        registro.put("PedidoID", detalle.getPedidoId());
        registro.put("ProductoID", detalle.getProductoId());
        registro.put("Cantidad", detalle.getCantidad());
        registro.put("Precio", detalle.getPrecio());

        try {
            db.insertOrThrow(ConstantesApp.TABLA_DETALLES_PEDIDOS, null, registro);
        } catch (SQLException ex) {
            resp = ex.getMessage();
        }
        return resp;
    }

    // Método para obtener una lista de detalles de pedido de la base de datos
    public List<DetallePedido> getList() {
        List<DetallePedido> lista = new ArrayList<>();
        String cadSQL = "SELECT Id, PedidoID, ProductoID, Cantidad, Precio FROM " + ConstantesApp.TABLA_DETALLES_PEDIDOS + ";";
        Cursor c = db.rawQuery(cadSQL, null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setId(c.getInt(c.getColumnIndexOrThrow("Id")));
                    detalle.setPedidoId(c.getInt(c.getColumnIndexOrThrow("PedidoID")));
                    detalle.setProductoId(c.getInt(c.getColumnIndexOrThrow("ProductoID")));
                    detalle.setCantidad(c.getInt(c.getColumnIndexOrThrow("Cantidad")));
                    detalle.setPrecio(c.getDouble(c.getColumnIndexOrThrow("Precio")));
                    lista.add(detalle);
                } while (c.moveToNext());
            }
            c.close();
        }
        return lista;
    }
}
