package com.example.ferreteria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.modelo.dto.Producto;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productList;

    public ProductoAdapter(List<Producto> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductoAdapter.ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ProductoViewHolder holder, int position) {
        Producto producto = productList.get(position);

        // Asignar los datos del producto a las vistas correspondientes
        holder.txtNombre.setText(producto.getNombre());
        holder.txtDescrip.setText(producto.getDescripcion());
        holder.txtPrecio.setText(String.valueOf(producto.getPrecio()));
        holder.txtStock.setText(String.valueOf(producto.getStock()));

        holder.productImageView.setImageResource(producto.getImagen());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDescrip, txtPrecio, txtStock;
        ImageView productImageView;

        ProductoViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.itemProductTextNombre);
            txtDescrip = itemView.findViewById(R.id.itemProductTextDescrip);
            txtPrecio = itemView.findViewById(R.id.itemProductTextPrecio);
            txtStock = itemView.findViewById(R.id.itemProductTextStock);
            productImageView = itemView.findViewById(R.id.productImageView);
        }
    }
}
