package com.example.ferreteria.modelo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Pedido;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Clase para manejar operaciones sobre la tabla de pedidos
public class PedidoDAO {
    private SQLiteDatabase db;

    // Constructor que inicializa la conexión a la base de datos
    public PedidoDAO(Context context) {
        db = new ConectaDB(context).getWritableDatabase();
    }

    // Método para insertar un nuevo pedido en la base de datos
    public String insertar(Pedido pedido) {
        String resp = "";
        ContentValues registro = new ContentValues();
        registro.put("ClienteID", pedido.getClienteId());
        registro.put("FechaPedido", pedido.getFechaPedido().getTime()); // Almacena la fecha como long

        try {
            db.insertOrThrow(ConstantesApp.TABLA_PEDIDOS, null, registro);
        } catch (SQLException ex) {
            resp = ex.getMessage();
        }
        return resp;
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
                    pedido.setFechaPedido(new Date(c.getLong(c.getColumnIndexOrThrow("FechaPedido")))); // Convierte long a Date
                    lista.add(pedido);
                } while (c.moveToNext());
            }
            c.close();
        }
        return lista;
    }
}
