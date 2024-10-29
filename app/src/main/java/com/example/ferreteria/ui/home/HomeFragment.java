package com.example.ferreteria.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.CategoriaAdapter;
import com.example.ferreteria.R;
import com.example.ferreteria.databinding.FragmentHomeBinding;
import com.example.ferreteria.interfaces.ConstantesApp;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private CategoriaAdapter adapter;
    private List<Categoria> categorias;
    private static final String TAG = "HOME FRAGMENT";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView iniciado");
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar el RecyclerView
        recyclerView = root.findViewById(R.id.recyclerViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i(TAG, "RecyclerView configurado con LinearLayoutManager");

        // Inicializar la lista de categorías
        categorias = new ArrayList<>();
        Log.i(TAG, "Lista de categorías inicializada");

        // Cargar categorías desde la base de datos
        cargarCategorias();

        // Configurar el adaptador del RecyclerView
        adapter = new CategoriaAdapter(categorias);
        recyclerView.setAdapter(adapter);
        Log.i(TAG, "Adaptador del RecyclerView configurado");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.i(TAG, "onDestroyView llamado y binding establecido a null");
    }

    private void cargarCategorias() {
        // Conexión a la base de datos
        ConectaDB conectaDB = new ConectaDB(getContext());
        SQLiteDatabase db = conectaDB.getReadableDatabase();
        Log.i(TAG, "Conexión a la base de datos establecida");

        // Ejecutar la consulta
        Cursor cursor = db.rawQuery("SELECT * FROM " + ConstantesApp.TABLA_CATEGORIAS, null);
        Log.i(TAG, "Consulta a la base de datos ejecutada");

        // Iterar a través de los resultados
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String descripcion = cursor.getString(2);
                int imagen = cursor.getInt(3);

                // Agregar categoría a la lista
                categorias.add(new Categoria(id, nombre, descripcion, imagen));
                Log.i(TAG, "Categoría agregada: " + nombre);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.i(TAG, "Cursor cerrado después de la iteración");
    }


}
