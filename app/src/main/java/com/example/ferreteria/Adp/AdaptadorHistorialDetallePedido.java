package com.example.ferreteria.Adp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.R;
import com.example.ferreteria.modelo.dto.Historial;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdaptadorHistorialDetallePedido extends RecyclerView.Adapter<AdaptadorHistorialDetallePedido.HistorialDetallePedidoViewHolder> {

    private List<Historial> listHistorial;

    public AdaptadorHistorialDetallePedido(List<Historial> listHistorial) {
        this.listHistorial = listHistorial;
    }

    @NonNull
    @Override
    public HistorialDetallePedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial_pedido, parent, false);
        return new HistorialDetallePedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialDetallePedidoViewHolder holder, int position) {
        Historial historial = listHistorial.get(position);

        holder.hpMarca.setText(historial.getMarca());
        holder.hpDescip.setText(historial.getDescrip());
        holder.hpCantidad.setText(String.valueOf(historial.getCantidad()));
        holder.hpPrecioUnit.setText(String.format("Precio Unitario: S/. %.2f", historial.getPrecioUnit()));
        holder.hpFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(historial.getFecha())));
        Log.i("Fecha", new SimpleDateFormat("dd/MM/yyyy").format(new Date(historial.getFecha())));
        holder.hpImageView.setImageResource(historial.getImagenProducto());
    }

    @Override
    public int getItemCount() {
        return listHistorial != null ? listHistorial.size() : 0;
    }

    static class HistorialDetallePedidoViewHolder extends RecyclerView.ViewHolder {

        public TextView hpFecha, hpMarca, hpCantidad, hpDescip, hpPrecioUnit;
        public ImageView hpImageView;
        public HistorialDetallePedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            hpFecha = itemView.findViewById(R.id.hpFecha);
            hpMarca = itemView.findViewById(R.id.hpMarca);
            hpPrecioUnit = itemView.findViewById(R.id.hpPrecioUnit);
            hpCantidad = itemView.findViewById(R.id.hpCantidad);
            hpDescip = itemView.findViewById(R.id.hpDescrip);
            hpImageView = itemView.findViewById(R.id.itemHistorialImageView);
        }
    }

}
