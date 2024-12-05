package com.example.ferreteria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ferreteria.databinding.ActivityMenuBinding;
import com.example.ferreteria.login.LoginActivity;
import com.example.ferreteria.modelo.dao.UsuarioDAO;
import com.example.ferreteria.modelo.dto.Usuario;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    private static final String TAG = "----MenuActivity";
    private boolean searchViewClosed = false;
    private EditText etxNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    //   Log.i(TAG, "onCreate llamado");
        confBarraDeAccion();
        confBotonFlotante();
        confMenuLateral();
        confNavegacion();

        actualizarBotonLogin();
        

    }

    public void iniciarSesion(View view) {
        Log.i(TAG, "Iniciar Sesión clickeado");

        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent); // Abre la actividad de inicio de sesión
    }


    //---------------- Configura la barra de acción ----------------
    private void confBarraDeAccion() {
        setSupportActionBar(binding.appBarMenu.toolbar);
    //    Log.i(TAG, "Barra de acción configurada");
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
    //    Log.i(TAG, "Menú lateral configurado");
    }

    //---------------- Configura la navegación usando NavController ----------------
    private void confNavegacion() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
      //  Log.i(TAG, "Navegación configurada");
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    //---------------- Configura la barra del menu----------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.barra_contenedor, menu);
       // Log.i(TAG, "Barra de menu configurada" );

        return true;
    }

    //---------------- Configura las navegaciones----------------
    @Override
    public boolean onSupportNavigateUp() {
        Log.d(TAG, "Navegación iniciada");
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.boton_buscar) { // Si presionas el botón
            Log.i(TAG, "BTN BUSCAR");
            Intent intent = new Intent(MenuActivity.this, buscar_Activity.class);
            startActivity(intent); // Abre la nueva actividad
            return true;
        }

        return super.onOptionsItemSelected(item); // Llama al método de la clase padre para manejar otros casos
    }

    private void actualizarBotonLogin() {
        Log.i(TAG,"Actualizar");
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        boolean sesionActiva = preferences.getBoolean("sesionActiva", false);
        String nombreUsuario = preferences.getString("nombreUsuario", "Invitado");

        // Encuentra el botón en el menú lateral
        View headerView = binding.navView.getHeaderView(0);
        Button loginButton = headerView.findViewById(R.id.btnIniciarSesion);
        TextView etxNombre = headerView.findViewById(R.id.txUsuariologeado);



        if (sesionActiva) {
            loginButton.setText("Cerrar Sesión");
            Log.i(TAG ,"Sesion Activa "+nombreUsuario);

           etxNombre.setText(" ( "+nombreUsuario+" ) ");
            loginButton.setOnClickListener(v -> cerrarSesion());
        } else {
            loginButton.setText("Iniciar Sesión");
            loginButton.setOnClickListener(v -> iniciarSesion());
        }
    }

    private void iniciarSesion() {
        Log.i(TAG ,"btn Iniciar sesion");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void cerrarSesion() {

        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        preferences.edit().putBoolean("sesionActiva", false).apply();
        Log.i(TAG,"Cerrar sesion");
        mtd.exito(binding.getRoot(),"Cerrar Sesion");
        mtd.redirigirConDelay(MenuActivity.this, MenuActivity.class, 800,true);

    }




}
