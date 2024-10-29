package com.example.ferreteria.servicios;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Categoria;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConectaDB extends SQLiteOpenHelper {

    private final Context context;
    private  String TAG = "CONECTADB";
    public ConectaDB(@Nullable Context context) {
        super(context, ConstantesApp.BDD, null, ConstantesApp.VERSION);
        this.context = context;
        Log.i(TAG, "Inicializando base de datos");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Inicializando ONCREATE");
        try {
            // Crear todas las tablas
            db.execSQL(ConstantesApp.TABLA_CATEGORIAS_DDL);
            Log.i(TAG, "Tabla categorias creada");

            db.execSQL(ConstantesApp.TABLA_PRODUCTOS_DDL);
            db.execSQL(ConstantesApp.TABLA_PEDIDOS_DDL);
            db.execSQL(ConstantesApp.TABLA_CLIENTES_DDL);
            db.execSQL(ConstantesApp.TABLA_DETALLES_PEDIDOS_DDL);

            // Log de creación exitosa
            Log.i(TAG, "Todas las tablas fueron creadas correctamente");

            // Insertar todas las categorías
            insertarCategorias(db);
            Log.i(TAG, "Todas las categorias fueron agregadas correctamente");


        } catch (Exception e) {
            Log.e(TAG, "Error al crear tablas o insertar datos iniciales: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Eliminar tablas si existen
            db.execSQL("DROP TABLE IF EXISTS " + ConstantesApp.TABLA_DETALLES_PEDIDOS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + ConstantesApp.TABLA_CLIENTES + ";");
            db.execSQL("DROP TABLE IF EXISTS " + ConstantesApp.TABLA_PEDIDOS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + ConstantesApp.TABLA_CATEGORIAS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + ConstantesApp.TABLA_PRODUCTOS + ";");

            // Crear tablas de nuevo
            onCreate(db);
            Log.i(TAG, "Actualización de base de datos realizada correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error al actualizar la base de datos: " + e.getMessage());
        }
    }

    public void ejecutarSQLDesdeArchivo(SQLiteDatabase db, String archivo) {
        Log.i(TAG, "LEYENDO EJECUTARSQL");
        try (InputStream is = context.getAssets().open(archivo);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            StringBuilder sql = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                sql.append(linea);
                // Ejecutar cada sentencia SQL al final de un comando SQL
                if (linea.trim().endsWith(";")) {
                    db.execSQL(sql.toString());
                    Log.i(TAG, "Ejecutando SQL: " + sql.toString().trim());
                    sql.setLength(0); // Limpiar el StringBuilder
                }
            }
            Log.i(TAG, "Archivo SQL " + archivo + " ejecutado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error al ejecutar el archivo SQL " + archivo + ": " + e.getMessage());
        }
    }

    public void listarTablas() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                Log.i(TAG, "Tabla: " + tableName);

                // Mostrar las columnas de cada tabla
                Cursor columnsCursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
                if (columnsCursor.moveToFirst()) {
                    do {
                        String columnName = columnsCursor.getString(1);
                        String columnType = columnsCursor.getString(2);
                        Log.i(TAG, "   Columna: " + columnName + " Tipo: " + columnType);
                    } while (columnsCursor.moveToNext());
                }
                columnsCursor.close();

            } while (cursor.moveToNext());
        } else {
            Log.i(TAG, "No se encontraron tablas en la base de datos.");
        }
        cursor.close();
    }

    public List<Categoria> leerCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i(TAG, "Leyendo categorías desde la base de datos");

        Cursor cursor = db.rawQuery("SELECT id, nombre, descripcion, imagen FROM " + ConstantesApp.TABLA_CATEGORIAS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String descripcion = cursor.getString(2);
                int imagen = cursor.getInt(3);
                categorias.add(new Categoria(id, nombre, descripcion, imagen));
                Log.i(TAG, "Categoría leída - ID: " + id + ", Nombre: " + nombre);
            } while (cursor.moveToNext());
        } else {
            Log.i(TAG, "No se encontraron categorías en la base de datos.");
        }

        cursor.close();
        Log.i(TAG, "Lectura de categorías finalizada. Total categorías: " + categorias.size());

        return categorias;
    }

    private void insertarCategorias(SQLiteDatabase db) {
        Log.i(TAG, "Insertando categorías...");

        // Lista de categorías a insertar
        String[][] categorias = {
                {"Manuales", "Instrumentos manuales para trabajos de bricolaje y mantenimiento.", "herramientas.png"},
                {"Construcción", "Materiales de construcción de alta calidad, como cemento y ladrillos.", "construccion.png"},
                {"Pinturas", "Pinturas y esmaltes para decorar y proteger superficies.", "pinturas.png"},
                {"Electricidad", "Artículos eléctricos esenciales para instalaciones seguras.", "electricidad.png"},
                {"Fontanería", "Materiales de fontanería, incluyendo tuberías y grifos.", "fontaneria.png"},
                {"Jardinería", "Suministros y equipos para el cuidado del jardín.", "jardineria.png"}

        };

        // Ejecutar inserciones
        for (String[] categoria : categorias) {
            // Obtener el ID de la imagen desde los recursos
            int imagenId = context.getResources().getIdentifier(categoria[2].replace(".png", ""), "drawable", context.getPackageName());

            String sql = "INSERT INTO " + ConstantesApp.TABLA_CATEGORIAS + " (nombre, descripcion, imagen) VALUES (?, ?, ?)";
            db.execSQL(sql, new Object[]{categoria[0], categoria[1], imagenId});
            Log.i(TAG, "Categoría insertada: " + categoria[0]);
        }

        Log.i(TAG, "Todas las categorías fueron insertadas correctamente.");
    }

}

