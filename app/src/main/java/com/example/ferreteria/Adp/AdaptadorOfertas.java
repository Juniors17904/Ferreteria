package com.example.ferreteria.Adp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.R;
import com.example.ferreteria.modelo.dto.Oferta;

import java.util.List;

public class AdaptadorOfertas extends RecyclerView.Adapter<AdaptadorOfertas.OfertaViewHolder> {

    private List<Oferta> listOfertas;
    private static final String TAG = "----adpOFERTAS";

    public AdaptadorOfertas(List<Oferta> ofertas) {
        this.listOfertas = ofertas;
        Log.i(TAG, "Adaptador de ofertas inicializado con " + ofertas.size() + " ofertas.");
    }
    @NonNull
    @Override
    public OfertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.i(TAG, "onCreateViewHolder: Creando nueva vista para el elemento de oferta.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oferta, parent, false);
        return new OfertaViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull OfertaViewHolder holder, int position) {
        Oferta oferta = listOfertas.get(position);
        holder.marcaProductoTextView.setText(oferta.getMarcaProducto());
        holder.descripcionProductoTextView.setText(oferta.getDescripcionProducto());
        holder.precioOriginalTextView.setText("Precio: S./" + oferta.getPrecioOriginal());
        holder.descuentoTextView.setText("Descuento: " + oferta.getDescuento() + "%");
        holder.precioConDescuentoTextView.setText("Precio con descuento: S./" + oferta.getPrecioConDescuento());
        holder.fechaInicioTextView.setText("Inicio: " + oferta.getFechaInicio());
        holder.fechaFinTextView.setText("Fin: " + oferta.getFechaFin());
        holder.imagenProductoImageView.setImageResource(oferta.getImagenProducto());

       // Log.i(TAG, "onBindViewHolder: Asociando datos a la posición " + position + ", Producto: " + oferta.getMarcaProducto());
    }
    @Override
    public int getItemCount() {
        return listOfertas.size();
    }
    static class OfertaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenProductoImageView;
        TextView marcaProductoTextView, descripcionProductoTextView, precioOriginalTextView,
                descuentoTextView, precioConDescuentoTextView, fechaInicioTextView, fechaFinTextView;

        OfertaViewHolder(View itemView) {
            super(itemView);
            imagenProductoImageView = itemView.findViewById(R.id.imagenOf);
            marcaProductoTextView = itemView.findViewById(R.id.marcaOf);
            descripcionProductoTextView = itemView.findViewById(R.id.descripcionOf);
            precioOriginalTextView = itemView.findViewById(R.id.precio);
            descuentoTextView = itemView.findViewById(R.id.descuentoOf);
            precioConDescuentoTextView = itemView.findViewById(R.id.precioDescuentoOf);
            fechaInicioTextView = itemView.findViewById(R.id.fechaInicioOferta);
            fechaFinTextView = itemView.findViewById(R.id.fechaFinOferta);

           // Log.i(TAG, "OfertaViewHolder: Vista del elemento de oferta inicializada.");
        }
    }


}
