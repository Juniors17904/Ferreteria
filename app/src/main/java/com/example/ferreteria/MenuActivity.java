package com.example.ferreteria;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.ferreteria.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    private static final String TAG = "-------MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.i(TAG, "onCreate llamado");
        confBarraDeAccion();
        confBotonFlotante();
        confMenuLateral();
        confNavegacion();
    }

    //---------------- Configura la barra de acción ----------------
    private void confBarraDeAccion() {
        setSupportActionBar(binding.appBarMenu.toolbar);
        Log.i(TAG, "Barra de acción configurada");
    }

    //---------------- Configura el Botón Flotante ----------------
    private void confBotonFlotante() {
        binding.appBarMenu.fab.setOnClickListener(view -> {
            Log.i(TAG, "Botón flotante clickeado");
        });
    }

    //---------------- Configura el Menú Lateral ----------------
    private void confMenuLateral() {
        DrawerLayout drawer = binding.drawerLayout;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        Log.i(TAG, "Menú lateral configurado");
    }

    //---------------- Configura la navegación usando NavController ----------------
    private void confNavegacion() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        Log.i(TAG, "Navegación configurada");
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.i(TAG, "Menú inflado");
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Navegación iniciada");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
