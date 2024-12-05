package com.example.ferreteria.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ferreteria.ConfirmarPedidoActivity;
import com.example.ferreteria.MenuActivity;
import com.example.ferreteria.R;
import com.example.ferreteria.modelo.dao.UsuarioDAO;
import com.example.ferreteria.modelo.dto.Usuario;
import com.example.ferreteria.mtd;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
    private EditText etxCorreo, etxContrasena;
    private Button btnRegistrarse, btnLogin;
    private String TAG = "----LOGIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        configurarControles();
        mtd.cambiarColorBarraEstado(this);


    }



    private void configurarControles() {
        etxContrasena = findViewById(R.id.etxContrasena);
        etxCorreo =findViewById(R.id.etxUsuario);
        btnRegistrarse=findViewById(R.id.btnRegistrarse);
        btnLogin=findViewById(R.id.btnLogin);
    }

    public void registrarse(View v) {
        Log.i(TAG, "Registrar clickeado");
        Intent intent = new Intent(LoginActivity.this, RegistrarseActivity.class);
        startActivity(intent);
    }

    public void IniciarSesion(View v) {
        Log.i(TAG, "Login clickeado");

        if (etxContrasena.getText().toString().isEmpty()||           //si esta alguno esta vacio
                etxCorreo.getText().toString().isEmpty())
        {
            mtd.ocultarTeclado(this);
            mtd.Alerta(v,"Debe llenar los Campos");
            return;
        }

        UsuarioDAO uDAO = new UsuarioDAO(this);
        Usuario usuario = uDAO.obtenerUsuarioPorEmail(etxCorreo.getText().toString());

        if (usuario != null) {
            String contrasenaEncriptada = uDAO.encriptarSHA256(etxContrasena.getText().toString());
            if (usuario.getContrasena().equals(contrasenaEncriptada)) { // Comparar la contraseña

                SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("sesionActiva", true); // Guardar sesión como activa
                editor.putString("nombreUsuario", usuario.getNombre());

                editor.putInt("usuarioID", usuario.getId());
                editor.apply();
                Log.i(TAG, "Sesion Activa Usuario: "+usuario.getNombre());
                mtd.ocultarTeclado(LoginActivity.this);
                mtd.exito(v,"Inicio de sesion exitoso");

                // Aquí se envía el objeto 'usuario' a ConfirmarPedidoActivity
                Intent intent = new Intent(LoginActivity.this, ConfirmarPedidoActivity.class);
                intent.putExtra("usuario", usuario); // Enviar el objeto Usuario

                mtd.redirigirConDelay(LoginActivity.this, MenuActivity.class, 800,true);




            } else {
                mtd.ocultarTeclado(LoginActivity.this);
                mtd.Alerta(v,"Contraseña Incorrecta");
                Log.i(TAG, "Contraseña incorrecta");

            }
        } else {
            mtd.ocultarTeclado(LoginActivity.this);
            mtd.Alerta(v,"Usuario no encontrado");
            Log.i(TAG, "Usuario no encontrado");

        }



    }



}