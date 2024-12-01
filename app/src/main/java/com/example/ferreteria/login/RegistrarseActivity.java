package com.example.ferreteria.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ferreteria.MenuActivity;
import com.example.ferreteria.R;
import com.example.ferreteria.modelo.dao.UsuarioDAO;
import com.example.ferreteria.modelo.dto.Usuario;
import com.example.ferreteria.mtd;
import com.google.android.material.snackbar.Snackbar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText etnombre , etdni, etapellido ,  ettelefono, etCorreo, etContra, eteConfContra;
    private CheckBox cbAcepto;
    private Button btnRegistrase;
    private String TAG = "----RegistrarseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrarse);
        cambiarColorBarraEstado();
        enlazarControles();



    }

    public void Registrarse(View v) {
        String resp;
        Log.i(TAG , "REGISTRARSE");
        if (cbAcepto.isChecked()) { //si esta marcado
            if (    etnombre.getText().toString().isEmpty()||
                    etdni.getText().toString().isEmpty()||              //si esta alguno esta vacio
                    etapellido.getText().toString().isEmpty()||
                    ettelefono.getText().toString().isEmpty()||
                    etCorreo.getText().toString().isEmpty()||
                    etContra.getText().toString().isEmpty()||
                    eteConfContra.getText().toString().isEmpty())
            {

                mtd.Alerta(v,"Algunos Campos estan Vacios");

                return;
            }

            if (!etContra.getText().toString().equals(eteConfContra.getText().toString()))
            {
                mtd.Alerta(v,"Las contraseñas no coinciden");
                return;
            }

            Log.i(TAG ,"LOGICA ");
            UsuarioDAO uDAO = new UsuarioDAO(this);
            Usuario u = new Usuario();

            u.setNombre(etnombre.getText().toString());
            u.setApellido(etapellido.getText().toString());
            u.setDni(etdni.getText().toString());
            u.setTelefono(ettelefono.getText().toString());
            u.setEmail(etCorreo.getText().toString());

            String contrasenaEncriptada = uDAO.encriptarSHA256(etContra.getText().toString());
            u.setContrasena(contrasenaEncriptada);


            Log.i(TAG,u.getNombre()+contrasenaEncriptada);

            resp = uDAO.insertar(u);

            if(resp.equals(""))
            {
                mtd.exito(v,"Cliente "+u.getEmail()+" Registrado Con EXITO");

                mtd.redirigirConDelay(RegistrarseActivity.this, LoginActivity.class, 800);

            }




        } else {
            mtd.Alerta(v,"Debe Aceptar los Terminos y Condiciones");
            return;
        }
    }

    private void enlazarControles() {
        etnombre= findViewById(R.id.etNombre);
        etdni=findViewById(R.id.etDni);
        etapellido=findViewById(R.id.etApellido);
        ettelefono=findViewById(R.id.etTelefono);
        etCorreo=findViewById(R.id.etEmail);
        etContra=findViewById(R.id.etPassword);
        eteConfContra=findViewById(R.id.etConfirmPassword);
        btnRegistrase=findViewById(R.id.btnRegister);
        cbAcepto = findViewById(R.id.cbAcepto);

    }


    private void cambiarColorBarraEstado() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }






}