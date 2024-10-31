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

    private void insertarProductos(SQLiteDatabase db) {
        Log.i(TAG, "Insertando productos...");

        // Lista de productos a insertar, cada producto pertenece a una categoría
        String[][] productos = {
                // Categoría 1
                {"Destornillador", "Destornillador plano de 6 pulgadas.", "10.50", "100", "1", "destornillador.png"},
                {"Martillo", "Martillo de acero de 16 oz.", "15.75", "50", "1", "martillo.png"},
                {"Cinta métrica", "Cinta métrica de 5 metros.", "8.25", "200", "1", "cinta_metrica.png"},
                {"Sierra", "Sierra manual para cortar madera.", "22.00", "30", "1", "sierra.png"},
                {"Alicate", "Alicate multiusos de 8 pulgadas.", "12.00", "80", "1", "alicate.png"},

                // Categoría 2
                {"Cemento", "Cemento Portland de alta resistencia.", "5.00", "500", "2", "cemento.png"},
                {"Ladrillos", "Ladrillos de construcción, 10 unidades.", "50.00", "300", "2", "ladrillos.png"},
                {"Arena", "Arena de construcción, 1 m3.", "40.00", "200", "2", "arena.png"},
                {"Piedra", "Piedra de río, 1 m3.", "30.00", "150", "2", "piedra.png"},
                {"Yeso", "Yeso blanco de secado rápido para acabados interiores.", "28.00", "120", "2", "yeso.png"},


                // Categoría 3
                {"Pintura acrílica", "Pintura acrílica de 1 litro.", "15.00", "75", "3", "pintura_acrilica.png"},
                {"Brocha", "Brocha de 2 pulgadas.", "3.50", "150", "3", "brocha.png"},
                {"Rodillo", "Rodillo de pintura de 9 pulgadas.", "8.00", "80", "3", "rodillo.png"},
                {"Pintura en aerosol", "Pintura en aerosol de 400 ml.", "10.00", "50", "3", "pintura_aerosol.png"},
                {"Sellador", "Sellador acrílico para exteriores.", "20.00", "60", "3", "sellador.png"},

                // Categoría 4
                {"Bombilla LED", "Bombilla LED de 9W.", "3.00", "200", "4", "bombilla.png"},
                {"Toma corriente", "Toma corriente de 3 salidas.", "5.00", "150", "4", "toma_corriente.png"},
                {"Cable eléctrico", "Cable eléctrico de 10 metros.", "10.00", "100", "4", "cable.png"},
                {"Interruptor", "Interruptor de luz sencillo.", "2.50", "250", "4", "interruptor.png"},
                {"Regulador", "Regulador de voltaje 1000VA.", "35.00", "30", "4", "regulador.png"},

                // Categoría 5
                {"Tubería PVC", "Tubería PVC de 2 pulgadas.", "7.00", "200", "5", "tuberia.png"},
                {"Grifo", "Grifo de cocina de acero inoxidable.", "40.00", "50", "5", "grifo.png"},
                {"Codo PVC", "Codo PVC de 2 pulgadas.", "2.00", "300", "5", "codo_pvc.png"},
                {"Válvula", "Válvula de cierre para tuberías.", "15.00", "150", "5", "valvula.png"},
                {"Cinta de teflón", "Cinta de teflón para sellar roscas.", "1.50", "400", "5", "cinta_teflon.png"},

                // Categoría 6
                {"Maceta", "Maceta de cerámica para plantas.", "12.00", "100", "6", "maceta.png"},
                {"Tijeras de podar", "Tijeras de podar de 8 pulgadas.", "18.00", "80", "6", "tijeras.png"},
                {"Guantes de jardinería", "Guantes de jardinería de tamaño mediano.", "5.00", "150", "6", "guantes.png"},
                {"Regadera", "Regadera de 5 litros.", "10.00", "70", "6", "regadera.png"},
                {"Rastrillo", "Rastrillo de 14 dientes.", "20.00", "60", "6", "rastrillo.png"}
        };

        // Ejecutar inserciones
        for (String[] producto : productos) {
            // Obtener el ID de la imagen desde los recursos
            int imagenId = context.getResources().getIdentifier(producto[5].replace(".png", ""), "drawable", context.getPackageName());

            String sql = "INSERT INTO " + ConstantesApp.TABLA_PRODUCTOS + " (nombre, descripcion, precio, stock, categoriaId, imagen) VALUES (?, ?, ?, ?, ?, ?)";
            db.execSQL(sql, new Object[]{producto[0], producto[1], producto[2], producto[3], producto[4], imagenId});
            Log.i(TAG, "Producto insertado: " + producto[0]);
        }

        Log.i(TAG, "Todos los productos fueron insertados correctamente.");
    }



}

