package com.example.ferreteria.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
                mtd.Alerta(v,"Debe llenar los Campos");
                return;
                }

            UsuarioDAO uDAO = new UsuarioDAO(this);
            Usuario usuario = uDAO.obtenerUsuarioPorEmail(etxCorreo.getText().toString());

            if (usuario != null) {
                String contrasenaEncriptada = uDAO.encriptarSHA256(etxContrasena.getText().toString());
                if (usuario.getContrasena().equals(contrasenaEncriptada)) { // Comparar la contraseña

                    mtd.ocultarTeclado(LoginActivity.this);
                    mtd.exito(v,"Inicio de sesion exitoso");
                    mtd.redirigirConDelay(LoginActivity.this, SesionActivity.class, 500);


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