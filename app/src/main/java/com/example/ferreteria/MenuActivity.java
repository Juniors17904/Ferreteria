package com.example.ferreteria;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.databinding.ActivityMenuBinding;
import com.example.ferreteria.modelo.dto.Categoria;

import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    // RecyclerView para las categorías
    private RecyclerView recyclerView;
    private CategoriaAdapter adapter;
    private List<Categoria> categorias;

    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("----------------------------------", "--------------------------------------");

        //-------------- Binding: Usa View Binding para simplificar la gestión de vistas.----------------
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i(TAG, "onCreate: Actividad iniciada");

        // -----------------------Configuración del ActionBar y Navigation Drawer-----------------------
        setSupportActionBar(binding.appBarMenu.toolbar);
        Log.i(TAG, "onCreate: Binding inicializado");

        // --------------------Interacciones: Configura eventos como el clic del botón flotante.-----------------
        binding.appBarMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reemplaza con tu propia acción", Snackbar.LENGTH_LONG)
                        .setAction("Acción", null)
                        .setAnchorView(R.id.fab).show();
                Log.i(TAG, "onClick: FAB clickeado, Snackbar mostrado");
            }
        });

        // ------------------------Drawer Layout: Maneja un menú lateral para navegación.--------------------
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        Log.i(TAG, "onCreate: AppBarConfiguration inicializado");

        // Navegación: Implementa Jetpack Navigation para fragmentos.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        Log.i(TAG, "onCreate: ActionBar configurado con NavController");

        NavigationUI.setupWithNavController(navigationView, navController);
        Log.i(TAG, "onCreate: NavigationView configurado con NavController");

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
