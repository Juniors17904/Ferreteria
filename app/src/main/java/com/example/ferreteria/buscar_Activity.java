package com.example.ferreteria;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.modelo.dao.ProductoDAO;
import com.example.ferreteria.modelo.dto.Producto;

import java.util.ArrayList;
import java.util.List;

public class buscar_Activity extends AppCompatActivity {

    private EditText editTextText;
    private String TAG = "----BuscarActivity";
    private RecyclerView recyclerView;
    private AdaptadorProductos productosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "INICIANDO ACTIVIDAD BUSCAR");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        editTextText = findViewById(R.id.editTextText);
        recyclerView = findViewById(R.id.recyclerView);

        // Configurar RecyclerView y el adaptador con una lista vacía
        productosAdapter = new AdaptadorProductos(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productosAdapter);

        mostrarTeclado();

        editTextText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence textoIngresado, int pInicio, int cant, int eliminados) {}

            @Override
            public void onTextChanged(CharSequence textoIngresado, int c_Totales, int cBorrados, int cAgregados) {
                ProductoDAO pDAO = new ProductoDAO(buscar_Activity.this);
                List<Producto> listaFiltrada = pDAO.getProductosPorDescripcion(textoIngresado.toString());

                productosAdapter.setListaProductos(listaFiltrada);  // Actualizar la lista en el adaptador
                Log.i(TAG, "Texto ingresado: '" + textoIngresado+"'  Encontrados: '"+ listaFiltrada.size()+"'");
            }

            @Override
            public void afterTextChanged(android.text.Editable textoIngresado) {}
        });

        cambiarColorBarraEstado();
        configurarInsetsDeVentana();
    }

    private void mostrarTeclado() {
        new Handler().postDelayed(() -> {
            editTextText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editTextText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);  // Retraso de 200 milisegundos
    }

    private void cambiarColorBarraEstado() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void configurarInsetsDeVentana() {
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }
}
