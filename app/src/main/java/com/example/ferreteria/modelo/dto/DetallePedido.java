package com.example.ferreteria.modelo.dto;

// Clase para representar un detalle de pedido
public class DetallePedido {
    private int id;
    private int pedidoId; // ID del pedido al que pertenece
    private int productoId; // ID del producto
    private int cantidad; // Cantidad del producto
    private double precio; // Precio del producto

    // Constructor
    public DetallePedido() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
