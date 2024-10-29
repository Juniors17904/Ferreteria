package com.example.ferreteria;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.databinding.ActivityMenuBinding;
import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    // RecyclerView para las categorías
    private RecyclerView recyclerView;
    private CategoriaAdapter adapter;
    private List<Categoria> categorias;

    private static final String TAG = "MenuActivity"; // Etiqueta para los logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i(TAG, "onCreate: Actividad iniciada");

        // Configuración del ActionBar y Navigation Drawer
        setSupportActionBar(binding.appBarMenu.toolbar);
        Log.i(TAG, "onCreate: Binding inicializado");

        binding.appBarMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reemplaza con tu propia acción", Snackbar.LENGTH_LONG)
                        .setAction("Acción", null)
                        .setAnchorView(R.id.fab).show();
                Log.i(TAG, "onClick: FAB clickeado, Snackbar mostrado");
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        Log.i(TAG, "onCreate: AppBarConfiguration inicializado");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        Log.i(TAG, "onCreate: ActionBar configurado con NavController");

        NavigationUI.setupWithNavController(navigationView, navController);
        Log.i(TAG, "onCreate: NavigationView configurado con NavController");

        // Inicializar RecyclerView
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.i(TAG, "initRecyclerView: Inicializando RecyclerView");
        recyclerView = findViewById(R.id.recyclerViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.i(TAG, "initRecyclerView: LayoutManager configurado como LinearLayoutManager");

        // Inicializar la lista de categorías
        categorias = new ArrayList<>();
        Log.i(TAG, "initRecyclerView: Lista de categorías inicializada");

        // Conexión a la base de datos
        ConectaDB conectaDB = new ConectaDB(this);
        SQLiteDatabase db = conectaDB.getReadableDatabase(); // Obtener la base de datos
        Log.i(TAG, "initRecyclerView: Conectando a la base de datos");

        // Ejecutar la consulta
        Cursor cursor = db.rawQuery("SELECT * FROM " + ConstantesApp.TABLA_CATEGORIAS, null);
        Log.i(TAG, "initRecyclerView: Ejecutando consulta en la tabla " + ConstantesApp.TABLA_CATEGORIAS);

        if (cursor.moveToFirst()) {
            Log.i(TAG, "initRecyclerView: Se encontraron resultados en la consulta");
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String descripcion = cursor.getString(2);
                int imagen = cursor.getInt(3);

                // Agregar categoría a la lista
                categorias.add(new Categoria(id, nombre, descripcion, imagen));
                Log.i(TAG, "initRecyclerView: Agregando categoría - ID: " + id + ", Nombre: " + nombre + ", Descripción: " + descripcion + ", Imagen: " + imagen);
            } while (cursor.moveToNext());
        } else {
            Log.i(TAG, "initRecyclerView: No se encontraron categorías en la consulta");
        }
        cursor.close();
        Log.i(TAG, "initRecyclerView: Cursor cerrado");

        // Configurar el adaptador del RecyclerView
        adapter = new CategoriaAdapter(categorias);
        recyclerView.setAdapter(adapter);
        Log.i(TAG, "initRecyclerView: Adaptador configurado con " + categorias.size() + " categorías");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú; esto añade elementos a la barra de acción si está presente.
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.i(TAG, "onCreateOptionsMenu: Menú inflado");
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu2);
        Log.i(TAG, "onSupportNavigateUp: Navegando hacia arriba");
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
