package com.example.ferreteria;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ferreteria.modelo.dao.CategoriaDAO;
import com.example.ferreteria.modelo.dto.Categoria;

import java.util.List;

public class CategoriaList {

    private Context context;
    private ListView listView;
    private CategoriaDAO categoriaDAO;

    // Constructor que recibe el contexto y el ListView
    public CategoriaList(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        this.categoriaDAO = new CategoriaDAO(context);
    }

    // Método para inicializar el ListView con las categorías
    public void initListView() {
        // Obtener la lista de categorías
        List<Categoria> categorias = categoriaDAO.getList();

        // Crear un ArrayAdapter para el ListView
        ArrayAdapter<Categoria> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, categorias);

        // Asignar el adaptador al ListView
        listView.setAdapter(adapter);
    }
}
