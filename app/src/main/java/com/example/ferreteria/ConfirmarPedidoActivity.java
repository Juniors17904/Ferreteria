package com.example.ferreteria;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ferreteria.modelo.dto.Usuario;

public class ConfirmarPedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmarpedido);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recibir el objeto Usuario enviado
        Usuario usuario = getIntent().getParcelableExtra("usuario");

        if (usuario != null) {
            Log.i("ConfirmarPedidoActivity", "Usuario recibido: " + usuario.getNombre());
            // Aquí puedes usar el objeto 'usuario' como lo necesites
        } else {
            Log.i("ConfirmarPedidoActivity", "No se recibió ningún usuario");
        }
    }
}
