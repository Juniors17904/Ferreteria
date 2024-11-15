package com.example.ferreteria.modelo.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.DetallePedido;
import com.example.ferreteria.modelo.dto.Historial;
import com.example.ferreteria.servicios.ConectaDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        registro.put("idPedido", detalle.getPedidoId());
        registro.put("idProducto", detalle.getProductoId());
        registro.put("cantidad", detalle.getCantidad());
        registro.put("precioUnit", detalle.getPrecio());

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

    @SuppressLint("Range")
    public double getTotalById(int idDetallePedido) {
        double total = 0;
        String query = "SELECT SUM(precioUnit * cantidad) AS total FROM " + ConstantesApp.TABLA_DETALLES_PEDIDOS + " WHERE id = ?;";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(idDetallePedido)});

            if (cursor != null && cursor.moveToFirst()) {
                total += cursor.getDouble(cursor.getColumnIndex("total"));
            }
            cursor.close();

            return total;
        } catch (SQLException e) {
            Log.i("DAO detalle Pedido: getTotalById", e.getMessage());
            cursor.close();
            return total;
        }
    }

    @SuppressLint("Range")
    public List<Historial> getAllDetallePedidosByIdClient(int idClient) {
        List<Historial> listCadaDetallePedidoDelCliente = new ArrayList<>();
        String query = "SELECT pr.marca as marca, pr.descripcion as descrip, d.cantidad as cantidad, d.precioUnit as precioUnit, pe.fechaPedido as fecha, pr.imagen as imagen FROM " + ConstantesApp.TABLA_DETALLES_PEDIDOS +
                " d INNER JOIN " +ConstantesApp.TABLA_PRODUCTOS+" pr ON pr.id = d.idProducto INNER JOIN "+ConstantesApp.TABLA_PEDIDOS+
                " pe ON pe.id = d.idPedido INNER JOIN "+ConstantesApp.TABLA_CLIENTES+" c ON c.id = pe.clienteID WHERE c.id = ?;";
        try (Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idClient)})) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Historial historial = new Historial();
                        historial.setMarca(cursor.getString(cursor.getColumnIndex("marca")));
                        historial.setDescrip(cursor.getString(cursor.getColumnIndex("descrip")));
                        historial.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));
                        historial.setPrecioUnit(cursor.getDouble(cursor.getColumnIndex("precioUnit")));
                        long timestamp = cursor.getLong(cursor.getColumnIndex("fecha"));
                        historial.setFecha(new java.util.Date(timestamp));
                        historial.setImagenProducto(cursor.getInt(cursor.getColumnIndex("imagen")));

                        listCadaDetallePedidoDelCliente.add(historial);
                    } while(cursor.moveToNext());
                }
            }
        } catch (SQLException e) {
            Log.i("ObtenerHistorialDelCliente: ", e.getMessage());
        }
        return listCadaDetallePedidoDelCliente;
    }
}
