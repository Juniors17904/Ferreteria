package com.example.ferreteria.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar el RecyclerView
        recyclerView = root.findViewById(R.id.recyclerViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar la lista de categorías
        categorias = new ArrayList<>();

        // Conexión a la base de datos
        ConectaDB conectaDB = new ConectaDB(getContext());
        SQLiteDatabase db = conectaDB.getReadableDatabase();

        // Ejecutar la consulta
        Cursor cursor = db.rawQuery("SELECT * FROM " + ConstantesApp.TABLA_CATEGORIAS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String descripcion = cursor.getString(2);
                int imagen = cursor.getInt(3);

                // Agregar categoría a la lista
                categorias.add(new Categoria(id, nombre, descripcion, imagen));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Configurar el adaptador del RecyclerView
        adapter = new CategoriaAdapter(categorias);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
