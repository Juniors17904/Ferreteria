package com.example.ferreteria.servicios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dao.CategoriaDAO;
import com.example.ferreteria.modelo.dto.Categoria;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConectaDB extends SQLiteOpenHelper {

    private final Context context;
    private  String TAG = "CONECTADB";
    private SQLiteDatabase db;

    // Constructor
    public ConectaDB(
            @Nullable Context context,                         // 1. Contexto de la aplicación o actividad que está utilizando la base de datos
            @Nullable String name,                             // 2. Nombre de la base de datos
            @Nullable SQLiteDatabase.CursorFactory factory,    // 3. Fábrica de cursores (puedes dejarlo como null)
            int version)                                       // 4. Versión de la base de datos
    {
        super(context, name, factory, version);
        this.context = context; // Inicializar el context
    }

    public SQLiteDatabase abrir() {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        return db;
    }

    public void cerrar() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Inicializando ONCREATE");
        try {
            // Crear todas las tablas
            db.execSQL(ConstantesApp.TABLA_CLIENTES_DDL);
            Log.i(TAG, "Tabla clientes creada");
            db.execSQL(ConstantesApp.TABLA_CATEGORIAS_DDL);
            Log.i(TAG, "Tabla categorias creada");
            db.execSQL(ConstantesApp.TABLA_PRODUCTOS_DDL);
            Log.i(TAG, "Tabla productos creada");
            db.execSQL(ConstantesApp.TABLA_PEDIDOS_DDL);
            Log.i(TAG, "Tabla pedidos creada");
            db.execSQL(ConstantesApp.TABLA_DETALLES_PEDIDOS_DDL);
            Log.i(TAG, "Tabla detalles_pedidos creada");
            Log.i(TAG, "------------------------------------------");
            Log.i(TAG, "-------ejecutar metodos para insertar ----- ");
            insertarCategorias(db);
            Log.i(TAG, "Todas las categorias fueron insertados");
            insertarProductos(db);
            Log.d(TAG, "Todos los productos insertados");


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

            // Usar ContentValues para insertar
            ContentValues valores = new ContentValues();
            valores.put("nombre", categoria[0]);
            valores.put("descripcion", categoria[1]);
            valores.put("imagen", imagenId);

            try {
                db.insertOrThrow(ConstantesApp.TABLA_CATEGORIAS, null, valores);
                Log.i(TAG, "Categoría insertada: " + categoria[0]);
            } catch (SQLException e) {
                Log.e(TAG, "Error al insertar categoría " + categoria[0] + ": " + e.getMessage());
            }
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
                {"Cemento", "Cemento Wari de alta resistencia.", "5.00", "500", "2", "cemento.png"},
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
                {"LLave Termomagnetica", "LLave de voltaje 1000VA.", "35.00", "30", "4", "llave.png"},

                // Categoría 5
                {"Tubería PVC", "Tubería PVC de 2 pulgadas.", "7.00", "200", "5", "tuberia.png"},
                {"Grifo", "Grifo de cocina de acero inoxidable.", "40.00", "50", "5", "canio.png"},
                {"Codo PVC", "Codo PVC de 1 pulgada.", "2.00", "300", "5", "codo_pvc.png"},
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

            // Usar ContentValues para insertar
            ContentValues valores = new ContentValues();
            valores.put("nombre", producto[0]);
            valores.put("descripcion", producto[1]);
            valores.put("precio", producto[2]);
            valores.put("stock", producto[3]);
            valores.put("categoriaId", producto[4]);
            valores.put("imagen", imagenId);

            try {
                db.insertOrThrow(ConstantesApp.TABLA_PRODUCTOS, null, valores);
                Log.i(TAG, "Producto insertado: " + producto[0]);
            } catch (SQLException e) {
                Log.e(TAG, "Error al insertar producto " + producto[0] + ": " + e.getMessage());
            }
        }

        Log.i(TAG, "Todos los productos fueron insertados correctamente.");
    }



}

