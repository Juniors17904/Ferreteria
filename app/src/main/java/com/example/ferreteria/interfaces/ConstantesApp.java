package com.example.ferreteria.interfaces;

public interface ConstantesApp {
    String BDD = "ferreterias.db";
    int VERSION = 1;


    // Tabla Categorias
    String TABLA_CATEGORIAS = "categorias";
    String TABLA_CATEGORIAS_DDL = "CREATE TABLE categorias (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n" +
            "    nombre VARCHAR(60) NOT NULL,\n" +
            "    descripcion VARCHAR(60), \n" +
            "    imagen INTEGER\n" +
            ");\n";

    // Tabla Productos
    String TABLA_PRODUCTOS = "productos";
    String TABLA_PRODUCTOS_DDL = "CREATE TABLE productos (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n" +
            "    marca VARCHAR(60) NOT NULL,\n" +
            "    descripcion VARCHAR(150),\n" +
            "    precio NUMERIC(10, 2) NOT NULL,\n" +
            "    stock INTEGER NOT NULL,\n" +
            "    categoriaId INTEGER REFERENCES categorias(id) NOT NULL,\n" +
            "    imagen INTEGER\n" +
            ");\n";

    // Tabla Ofertas
    String TABLA_OFERTAS = "ofertas";
    String TABLA_OFERTAS_DDL = "CREATE TABLE ofertas (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n" +
            "    productoId INTEGER REFERENCES productos(id) NOT NULL,\n" +
            "    descuento NUMERIC(10, 2) NOT NULL,\n" +
            "    fechaInicio DATE NOT NULL,\n" +
            "    fechaFin DATE NOT NULL\n" +
            ");\n";


    // Tabla Pedidos
    String TABLA_PEDIDOS = "pedidos";
    String TABLA_PEDIDOS_DDL = "CREATE TABLE pedidos (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n" +
            "    usuarioID INTEGER,\n" +
            "    fechaPedido DATE\n" +
            "    total NUMERIC(10,2)\n" +
            ");\n";

    // Tabla DetallesPedidos
    String TABLA_DETALLES_PEDIDOS = "detalles_pedidos";
    String TABLA_DETALLES_PEDIDOS_DDL = "CREATE TABLE detalles_pedidos (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n" +
            "    idPedido INTEGER REFERENCES pedidos(id) NOT NULL,\n" +
            "    idProducto INTEGER REFERENCES productos(id) NOT NULL,\n" +
            "    cantidad INTEGER NOT NULL,\n" +
            "    precioUnit NUMERIC(10, 2) NOT NULL\n" +
            ");\n";


    // Tabla Clientes
    String TABLA_CLIENTES = "clientes";
    String TABLA_CLIENTES_DDL = "CREATE TABLE clientes (\n" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n" +
            "    nombre VARCHAR(60) NOT NULL,\n" +
            "    correo VARCHAR(100) UNIQUE NOT NULL,\n" +
            "    telefono VARCHAR(15),\n" +
            "    direccion VARCHAR(250)\n" +
            ");\n";
}
