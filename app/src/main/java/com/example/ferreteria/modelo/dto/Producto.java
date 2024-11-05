package com.example.ferreteria.modelo.dto;

import androidx.annotation.NonNull;

public class Producto {
    private int id; // ID del producto
    private String marca; // Nombre del producto
    private String descripcion; // Descripción del producto
    private double precio; // Precio del producto
    private int stock; // Stock disponible
    private int categoriaId; // ID de la categoría a la que pertenece el producto
    private int imagenProducto;//

    public Producto() {
    }

    public Producto(int id, String marca, String descripcion, double precio, int stock, int categoriaId, int imagenProducto) {
        this.id = id;
        this.marca = marca;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
        this.imagenProducto = imagenProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(int imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    @NonNull
    @Override
    public String toString() {
        return getMarca(); // Retorna el nombre del producto
    }
}
