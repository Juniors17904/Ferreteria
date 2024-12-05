package com.example.ferreteria.modelo.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.modelo.dto.Producto;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.List;

// Clase para manejar operaciones sobre la tabla de productos
public class ProductoDAO {
    private SQLiteDatabase db;
    private String TAG = "----ProductoDAO";

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
        registro.put("Marca", p.getMarca());
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

    public List<Producto> getProductosPorDescripcion(String descripcion) {
        List<Producto> lista = new ArrayList<>();

        String query = "SELECT " +
                "p.marca AS nombreProducto, " +
                "p.descripcion AS descripcionProducto, " +
                "p.precio AS precioOriginal, " +
                "CASE WHEN o.descuento IS NOT NULL THEN ROUND(p.precio * (1 - (o.descuento / 100.0)), 2) ELSE p.precio END AS precioConDescuento, " +
                "CASE WHEN o.descuento IS NOT NULL THEN 1 ELSE 0 END AS tieneOferta, " +
                "p.imagen AS imagenProducto " +
                "FROM " + ConstantesApp.TABLA_PRODUCTOS + " p " +
                "LEFT JOIN " + ConstantesApp.TABLA_OFERTAS + " o ON p.id = o.productoId " +
                "WHERE (o.fechaFin IS NULL OR o.fechaFin >= DATE('now')) " +
                "AND p.descripcion LIKE ?;";

        String[] parametros = new String[]{"%" + descripcion + "%"}; // Parámetro para buscar en la descripción

        Cursor cursor = db.rawQuery(query, parametros);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Producto producto = new Producto();
                    producto.setMarca(cursor.getString(cursor.getColumnIndexOrThrow("nombreProducto")));
                    producto.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("descripcionProducto")));
                    producto.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow("precioOriginal")));
                    producto.setPrecioConDescuento(cursor.getDouble(cursor.getColumnIndexOrThrow("precioConDescuento")));
                    producto.setTieneOferta(cursor.getInt(cursor.getColumnIndexOrThrow("tieneOferta")) == 1);
                    producto.setImagenProducto(cursor.getInt(cursor.getColumnIndexOrThrow("imagenProducto")));
                    lista.add(producto);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return lista;
    }

    public List<Producto> mostrarProdSelect(int id) {
        Log.i(TAG, "mostrar productos por categoira : " + id);
        List<Producto> lista = new ArrayList<>();

        // Consulta SQL para obtener los productos con descuento
        String consulta = "SELECT " +
                "p.marca AS nombreProducto, " +
                "p.descripcion AS descripcionProducto, " +
                "p.precio AS precioOriginal, " +
                "CASE " +
                "   WHEN o.descuento IS NOT NULL THEN ROUND(p.precio * (1 - (o.descuento / 100.0)), 2) " +
                "   ELSE p.precio " +
                "END AS precioConDescuento, " +
                "CASE " +
                "   WHEN o.descuento IS NOT NULL THEN 1 " +
                "   ELSE 0 " +
                "END AS tieneOferta, " +
                "COALESCE(p.imagen, 0) AS imagenProducto " +
                "FROM " + ConstantesApp.TABLA_PRODUCTOS + " p " +
                "LEFT JOIN " + ConstantesApp.TABLA_OFERTAS + " o ON p.id = o.productoId " +
                "WHERE p.categoriaid = ?";

        // Ejecutar la consulta con el ID de categoría proporcionado
        Cursor c = db.rawQuery(consulta, new String[]{String.valueOf(id)});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Producto producto = new Producto();
                    producto.setMarca(c.getString(c.getColumnIndexOrThrow("nombreProducto")));
                    producto.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcionProducto")));
                    producto.setPrecio(c.getDouble(c.getColumnIndexOrThrow("precioOriginal")));
                    producto.setPrecioConDescuento(c.getDouble(c.getColumnIndexOrThrow("precioConDescuento")));
                    producto.setTieneOferta(c.getInt(c.getColumnIndexOrThrow("tieneOferta")) == 1);
                    producto.setImagenProducto(c.getInt(c.getColumnIndexOrThrow("imagenProducto")));
                    lista.add(producto);
                } while (c.moveToNext());
            }
            c.close();
        }

        Log.i(TAG, "Número de productos encontrados: " + lista.size());
        return lista;
    }

    public List<Producto> mostrarOtrosProductos(int id) {
        List<Producto> lista = new ArrayList<>();
        Log.i(TAG, "mostrar otros productos ");
        // Consulta SQL para obtener los productos con descuento
        String consulta = "SELECT " +
                "p.marca AS nombreProducto, " +
                "p.descripcion AS descripcionProducto, " +
                "p.precio AS precioOriginal, " +
                "CASE " +
                "   WHEN o.descuento IS NOT NULL THEN ROUND(p.precio * (1 - (o.descuento / 100.0)), 2) " +
                "   ELSE p.precio " +
                "END AS precioConDescuento, " +
                "CASE " +
                "   WHEN o.descuento IS NOT NULL THEN 1 " +
                "   ELSE 0 " +
                "END AS tieneOferta, " +
                "COALESCE(p.imagen, 0) AS imagenProducto " +
                "FROM " + ConstantesApp.TABLA_PRODUCTOS + " p " +
                "LEFT JOIN " + ConstantesApp.TABLA_OFERTAS + " o ON p.id = o.productoId " +
                "WHERE p.categoriaid != ?";

        // Ejecutar la consulta con el ID de categoría proporcionado
        Cursor c = db.rawQuery(consulta, new String[]{String.valueOf(id)});

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    Producto producto = new Producto();
                    producto.setMarca(c.getString(c.getColumnIndexOrThrow("nombreProducto")));
                    producto.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcionProducto")));
                    producto.setPrecio(c.getDouble(c.getColumnIndexOrThrow("precioOriginal")));
                    producto.setPrecioConDescuento(c.getDouble(c.getColumnIndexOrThrow("precioConDescuento")));
                    producto.setTieneOferta(c.getInt(c.getColumnIndexOrThrow("tieneOferta")) == 1);
                    producto.setImagenProducto(c.getInt(c.getColumnIndexOrThrow("imagenProducto")));
                    lista.add(producto);
                } while (c.moveToNext());
            }
            c.close();
        }
        Log.i(TAG, "Número de productos encontrados: " + lista.size());
        return lista;
    }

    public double obtenerPrecioPorId(int idProducto) {
        double precio = 0.0;
        String cadSQL = "SELECT Precio FROM " + ConstantesApp.TABLA_PRODUCTOS + " WHERE id = ?;";
        Cursor c = db.rawQuery(cadSQL, new String[]{String.valueOf(idProducto)});

        if (c != null) {
            if (c.moveToFirst()) {
                precio = c.getDouble(c.getColumnIndexOrThrow("Precio"));
            }
            c.close();
        }
        return precio;
    }





    @SuppressLint("Range")

    public int getProductoIdByDescription(String descrip) {
        String query = "SELECT id FROM " + ConstantesApp.TABLA_PRODUCTOS + " WHERE descripcion = ? LIMIT 1;";
        int idProducto = -1;

        // Asegurarse de que la descripción no sea nula o vacía
        if (descrip == null || descrip.isEmpty()) {
            Log.i(TAG, "Descripción no válida: " + descrip);  // Log de descripción no válida
            return idProducto;  // Retorna -1 si la descripción es inválida
        }

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, new String[]{descrip});

            if (cursor != null && cursor.moveToFirst()) {
                idProducto = cursor.getInt(cursor.getColumnIndex("id"));
                Log.i(TAG, "-----DAO  Producto encontrado con ID: " + idProducto);  // Log del ID del producto encontrado
            } else {
                Log.i(TAG, "No se encontró producto para la descripción: " + descrip);  // Log si no se encuentra el producto
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al consultar producto por descripción: " + descrip, e);  // Log del error con excepción
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return idProducto;
    }

    public List<Producto> getTodosLosProductos() {
        List<Producto> listaProductos = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM productos"; // Ensure the table name is correct.
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Producto producto = new Producto(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("marca")),
                            cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("precio")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("precioConDescuento")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("tieneOferta")) > 0,
                            cursor.getInt(cursor.getColumnIndexOrThrow("stock")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("categoriaId")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("imagenProducto")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
                    );
                    listaProductos.add(producto);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("ProductoDAO", "Error al obtener los productos: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return listaProductos;
    }


    public List<Producto> Productos(String NombreCategoria){
        List<Producto> lista =new ArrayList<>();
        String Consulta = "SELECT*FROM PRODUCTOS WHERE categoriaid = 2";


        return lista;
    }

}
