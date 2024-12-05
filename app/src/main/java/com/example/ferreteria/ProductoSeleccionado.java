package com.example.ferreteria;

import com.example.ferreteria.modelo.dto.Producto;

public class ProductoSeleccionado {
    private Producto producto;
    private int cantidad;

    public ProductoSeleccionado(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
