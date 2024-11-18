package com.example.ferreteria.modelo.dto;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private int id; // ID del pedido
    private int clienteId; // ID del cliente que realizó el pedido
    private long fechaPedido; // Fecha en que se realizó el pedido

    public Pedido() { }

    public Pedido(int id, int clienteId, long fechaPedido) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaPedido = fechaPedido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public long getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(long fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    @NonNull
    @Override
    public String toString() {
        return "Pedido ID: " + getId() + ", Cliente ID: " + getClienteId(); // Representación simple del pedido
    }
}
