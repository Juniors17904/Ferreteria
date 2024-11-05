package com.example.ferreteria.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.CategoriaAdapter;
import com.example.ferreteria.ProductoAdapter;
import com.example.ferreteria.R;
import com.example.ferreteria.databinding.FragmentHomeBinding;
import com.example.ferreteria.interfaces.RecyclerViewItemListener;
import com.example.ferreteria.modelo.dao.CategoriaDAO;
import com.example.ferreteria.modelo.dao.ProductoDAO;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.modelo.dto.Producto;

import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewItemListener {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private CategoriaAdapter adapter;
    private List<Categoria> categorias;
    private static final String TAG = "HOME FRAGMENT";

    //Dialog
    private TextView dialogTextInputCat;
    private RecyclerView dialogRecyclerView;

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
        adapter = new CategoriaAdapter(categorias, this);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "Adaptador del RecyclerView configurado con " + categorias.size() + " categorías");
        categoriaDAO.closeDB(); // Cerrar la base de datos
    }
    @Override
    public void onItemClicked(Categoria categoria) {
        //Toast.makeText(getContext(), categoria.getNombre(), Toast.LENGTH_SHORT).show();
        showDialog(categoria);
    }

    private void showDialog(Categoria categoria) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_show_item_by_cat);
        ProductoDAO productDAO = new ProductoDAO(getContext());

        dialogTextInputCat = dialog.findViewById(R.id.textInputCat);
        dialogRecyclerView = dialog.findViewById(R.id.recyViewProduct);

        //Catch values
        dialogTextInputCat.setText(categoria.getNombre());

        List<Producto> listProduct = productDAO.buscarProductoPorCategoría(categoria.getId());

        //Configurando el RecyclerView de productos
        ProductoAdapter productoAdapter = new ProductoAdapter(listProduct);
        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogRecyclerView.setAdapter(productoAdapter);

        dialog.show();
    }
}
