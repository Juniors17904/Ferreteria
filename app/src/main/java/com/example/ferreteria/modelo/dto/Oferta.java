package com.example.ferreteria.modelo.dto;

public class Oferta {
    private int id;
    private int productoId;
    private String marcaProducto;
    private String descripcionProducto;
    private double precioOriginal;
    private double descuento;
    private double precioConDescuento;
    private String fechaInicio;
    private String fechaFin;
    private int imagenProducto;

    public Oferta() {
    }

    public Oferta(int id, int productoId, String nombreProducto, String descripcionProducto, double precioOriginal,
                  double descuento, double precioConDescuento, String fechaInicio, String fechaFin, int imagenProducto) {
        this.id = id;
        this.productoId = productoId;
        this.marcaProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioOriginal = precioOriginal;
        this.descuento = descuento;
        this.precioConDescuento = precioConDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.imagenProducto= imagenProducto;
    }

    // Getters y Setters para cada atributo

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getMarcaProducto() {
        return marcaProducto;
    }

    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getPrecioConDescuento() {
        return precioConDescuento;
    }

    public void setPrecioConDescuento(double precioConDescuento) {
        this.precioConDescuento = precioConDescuento;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(int imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

}
