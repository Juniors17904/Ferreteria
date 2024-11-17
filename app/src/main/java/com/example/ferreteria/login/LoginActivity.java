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

public class LoginActivity extends AppCompatActivity {
private EditText etxUsuario, etxContraseña;
private Button btnRegistrarse, btnLogin;
private String TAG = "----LOGIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        configurarControles();
        cambiarColorBarraEstado();




    }



    private void configurarControles() {
    etxContraseña = findViewById(R.id.etxContrasena);
    etxUsuario=findViewById(R.id.etxUsuario);
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
        Intent intent = new Intent(LoginActivity.this, IniciarSesionActivity.class);
        startActivity(intent);
    }

    private void cambiarColorBarraEstado() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

}