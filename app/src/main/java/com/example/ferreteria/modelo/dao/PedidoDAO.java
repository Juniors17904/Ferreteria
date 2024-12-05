package com.example.ferreteria.modelo.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Pedido;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PedidoDAO {
    private SQLiteDatabase db;
    private String TAG = "----PedidoDAO";
    public PedidoDAO(Context context) {
        db = new ConectaDB(context,
                ConstantesApp.BDD,
                null,
                ConstantesApp.VERSION).
                getWritableDatabase();
    }


    public long insertarPedido(int usuarioID, String fechaPedido, double total) {
        Log.i(TAG,"Insertando Pedido");
        ContentValues registro = new ContentValues();
        registro.put("usuarioID", usuarioID); // Guardamos el usuarioID
        registro.put("FechaPedido", fechaPedido); // Guardamos la fecha
        registro.put("Total", total); // Guardamos el total del pedido

        long idPedido = -1;
        try {
            // Realizamos la inserción en la base de datos
            idPedido = db.insertOrThrow(ConstantesApp.TABLA_PEDIDOS, null, registro);
            if (idPedido == -1) {
                Log.i(TAG,"Error al insertar el pedido.");
            } else {
                Log.i(TAG,"Pedido insertado correctamente con ID: " + idPedido);
            }
        } catch (SQLException ex) {
            Log.i(TAG,"Inserción de Pedido:"+ ex.getMessage());
        }

        return idPedido; // Retorna el ID del pedido insertado
    }

    public int inse454rtarPedidok(int usuarioID, Date fechaPedido, double total) {
        ContentValues registro = new ContentValues();
        registro.put("ClienteID", usuarioID);
        registro.put("FechaPedido", fechaPedido.getTime());  // Convierte Date a long para almacenarlo

        try {
            long rowId = db.insertOrThrow(ConstantesApp.TABLA_PEDIDOS, null, registro);
            return (int) rowId;  // Retorna el ID generado como int
        } catch (SQLException ex) {
            Log.i("Inserción de Pedido: ", ex.getMessage());
            return -1;  // Retorna -1 si hubo un error
        }
    }

    public boolean insertarDetallePedido34(long idPedido, int idProducto, int cantidad, double precioUnitario) {
        Log.i(TAG,"Insertando Detalle Pedido");
        ContentValues registro = new ContentValues();
        registro.put("idPedido", idPedido);
        registro.put("idProducto", idProducto);
        registro.put("cantidad", cantidad);
        registro.put("precioUnitario", precioUnitario);

        try {
            long result = db.insertOrThrow(ConstantesApp.TABLA_DETALLES_PEDIDOS, null, registro);
            return result != -1;  // Si el resultado es -1, la inserción falló
        } catch (SQLException ex) {
            Log.e(TAG,"Inserción Detalle Pedido: "+ ex.getMessage());
            return false;
        }
    }

    public boolean insertarDetallePedido(long idPedido, int idProducto, int cantidad, double precioUnitario) {
        Log.i(TAG,"Insertando Detalle Pedido");

        // Preparamos los valores a insertar en la tabla detalles_pedidos
        ContentValues registro = new ContentValues();
        registro.put("idPedido", idPedido); // Relación con la tabla pedidos
        registro.put("idProducto", idProducto); // Relación con la tabla productos
        registro.put("cantidad", cantidad);
        registro.put("precioUnit", precioUnitario); // Precio unitario del producto

        // Intentamos insertar el registro en la base de datos
        try {
            long result = db.insertOrThrow(ConstantesApp.TABLA_DETALLES_PEDIDOS, null, registro);

            // Si la inserción fue exitosa (el ID no es -1), retornamos true
            if (result != -1) {
                Log.i(TAG, "Detalle del pedido insertado correctamente con ID: " + result);
                return true;
            } else {
                Log.e(TAG, "Error al insertar el detalle del pedido.");
                return false;
            }
        } catch (SQLException ex) {
            Log.e(TAG, "Inserción Detalle Pedido: " + ex.getMessage());
            return false;
        }
    }







    // Método para obtener una lista de pedidos de la base de datos
    public List<Pedido> getList() {
        List<Pedido> lista = new ArrayList<>();
        String cadSQL = "SELECT Id, ClienteID, FechaPedido FROM " + ConstantesApp.TABLA_PEDIDOS + ";";
        Cursor c = db.rawQuery(cadSQL, null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Pedido pedido = new Pedido();
                    pedido.setId(c.getInt(c.getColumnIndexOrThrow("Id")));
                    pedido.setClienteId(c.getInt(c.getColumnIndexOrThrow("ClienteID")));
                    pedido.setFechaPedido(c.getLong(c.getColumnIndexOrThrow("FechaPedido"))); // Convierte long a Date
                    lista.add(pedido);
                } while (c.moveToNext());
            }
            c.close();
        }
        return lista;
    }

    @SuppressLint("Range")
    public int getLastId() {
        String query = "SELECT MAX(id) as id FROM "+ ConstantesApp.TABLA_PEDIDOS + ";";

        Cursor cursor = db.rawQuery(query, null);

        int pedidoId = -1;

        if (cursor.moveToFirst()) {
            pedidoId = cursor.getInt(cursor.getColumnIndex("id"));
        }

        cursor.close();

        return pedidoId;
    }
}
