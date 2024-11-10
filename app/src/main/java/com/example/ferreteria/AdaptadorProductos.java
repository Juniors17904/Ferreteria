package com.example.ferreteria;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.modelo.dto.Producto;

import java.util.List;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ProductoViewHolder> {

    private List<Producto> listaProductos;
    private static final String TAG = "----adpPRODUCTOS";

    public AdaptadorProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
        Log.i(TAG, "Adaptador de productos  inicializado ");

    }

    // Método para actualizar la lista de productos
    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = listaProductos;
        notifyDataSetChanged();  // notificar cambios
    }

    // Crear el ViewHolder (el contenedor para cada item del RecyclerView)
    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    // Asignar los datos a los elementos del ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.bind(producto);
    }

    // Retorna la cantidad de items en la lista
    @Override
    public int getItemCount() {
        return listaProductos != null ? listaProductos.size() : 0;
    }

    // ViewHolder para contener las vistas de cada item
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMarca, tvDescripcion, tvPrecio;
        private ImageView ivImagen;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivImagen = itemView.findViewById(R.id.ivImagen);
        }

        public void bind(Producto producto) {
            tvMarca.setText(producto.getMarca());
            tvDescripcion.setText(producto.getDescripcion());

            // precio con descuento si está en oferta
            if (producto.isTieneOferta()) {
                tvPrecio.setText(String.format("Oferta: S./%.2f", producto.getPrecioConDescuento()));
                tvPrecio.setTextColor(Color.RED);
            } else {
                tvPrecio.setText(String.format("S./%.2f", producto.getPrecio()));
                tvPrecio.setTextColor(Color.BLACK);
            }

            if (producto.getImagenProducto() != 0) {
                ivImagen.setImageResource(producto.getImagenProducto());
            }
        }
    }
}
