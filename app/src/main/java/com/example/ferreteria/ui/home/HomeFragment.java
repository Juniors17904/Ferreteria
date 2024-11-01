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
import com.example.ferreteria.modelo.dao.CategoriaDAO;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.servicios.ConectaDB;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private CategoriaAdapter adapter;
    private List<Categoria> categorias;
    private static final String TAG = "HOME FRAGMENT";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView iniciado");
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar el RecyclerView
        recyclerView = root.findViewById(R.id.recyclerViewCategorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d(TAG, "RecyclerView configurado con LinearLayoutManager");

        // Obtener y configurar el adaptador con la lista de categorías
        confRecyclerView();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        Log.i(TAG, "onDestroyView llamado y binding establecido a null");
    }

    private void confRecyclerView() {
        CategoriaDAO categoriaDAO = new CategoriaDAO(getContext());
        List<Categoria> categorias = categoriaDAO.getList(); // Obtener categorías desde CategoriaDAO
        adapter = new CategoriaAdapter(categorias);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "Adaptador del RecyclerView configurado con " + categorias.size() + " categorías");
        categoriaDAO.closeDB(); // Cerrar la base de datos
    }


}
