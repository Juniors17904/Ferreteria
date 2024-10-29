package com.example.ferreteria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.modelo.dto.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> categorias;

    public CategoriaAdapter(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);
        holder.nombreTextView.setText(categoria.getNombre());
        holder.descripcionTextView.setText(categoria.getDescripcion());
        holder.imagenImageView.setImageResource(categoria.getImagen());
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    static class CategoriaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenImageView;
        TextView nombreTextView, descripcionTextView;

        CategoriaViewHolder(View itemView) {
            super(itemView);
            imagenImageView = itemView.findViewById(R.id.imagenCategoria);
            nombreTextView = itemView.findViewById(R.id.nombreCategoria);
            descripcionTextView = itemView.findViewById(R.id.descripcionCategoria);
        }
    }
}
