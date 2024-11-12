package com.example.ferreteria;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.Adp.AdaptadorProductos;
import com.example.ferreteria.modelo.dao.ProductoDAO;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.modelo.dto.Producto;

import java.util.ArrayList;
import java.util.List;

public class ProductosActivity extends AppCompatActivity {

    private String TAG = "----PRODUCTOSACTIVITY";
    private EditText edtCategoria,edtOtrasCategorias;
    private RecyclerView rwCategoria,rwOtrasCategorias;
    private Categoria c;
    private AdaptadorProductos AdpPr , adpter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);
        enlazarControles();
        obtener();
        rwProductos();
        rwOtrosProductos();


    }

    private void rwOtrosProductos() {
        adpter=new AdaptadorProductos(new ArrayList<>());
        rwOtrasCategorias.setLayoutManager(new LinearLayoutManager(this));
        rwOtrasCategorias.setAdapter(adpter);
        ProductoDAO pDAO = new ProductoDAO(this);
        List<Producto> otrosProductos=pDAO.mostrarOtrosProductos(c.getId());
        Log.i(TAG, "Otros productos obtenidos: " + otrosProductos.size());
        adpter.setListaProductos(otrosProductos);

    }

    private void rwProductos() {
        AdpPr =new AdaptadorProductos(new ArrayList<>());
        rwCategoria.setLayoutManager(new LinearLayoutManager(this));
        rwCategoria.setAdapter(AdpPr);
        ProductoDAO pDAO = new ProductoDAO(this);
        List<Producto> listaProductos = pDAO.mostrarProdSelect(c.getId());
        Log.i(TAG, "Productos obtenidos: " + listaProductos.size());
        AdpPr.setListaProductos(listaProductos);
    }

    private void obtener() {
        int posicionCategoria = getIntent().getIntExtra("posicion_categoria", -1);

        c = (Categoria) getIntent().getSerializableExtra("categoria");
        if (c != null) {
            Log.i(TAG,"Obj Obtenido :"+c.getNombre());
                edtCategoria.setText("Categoria: "+c.getNombre());
        } else {
            Log.i(TAG, "No se recibió la posición.");
        }

    }

    private void enlazarControles() {
        edtCategoria=findViewById(R.id.edtCategoriaSeleccionada);
        edtOtrasCategorias=findViewById(R.id.edtOtrasCategorias);
        rwCategoria=findViewById(R.id.recyCat);
        rwOtrasCategorias=findViewById(R.id.recyOtras);
    }



}