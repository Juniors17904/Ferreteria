package com.example.ferreteria.modelo.dto;

import java.util.ArrayList;
import java.util.List;

public class ListaProductoParaPedido {
    private static ListaProductoParaPedido theOne;
    public static List<Producto> listProductoToPedido;

    private ListaProductoParaPedido() {
        listProductoToPedido = new ArrayList<>();
    }

    public static ListaProductoParaPedido getInstance() {
        if (theOne == null) {
            theOne = new ListaProductoParaPedido();
        }

        return theOne;
    }
}
