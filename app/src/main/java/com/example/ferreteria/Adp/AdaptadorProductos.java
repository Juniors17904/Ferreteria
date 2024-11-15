package com.example.ferreteria.Adp;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.R;
import com.example.ferreteria.modelo.dto.ListaProductoParaPedido;
import com.example.ferreteria.modelo.dto.Producto;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ProductoViewHolder> {
    ListaProductoParaPedido single = ListaProductoParaPedido.getInstance();
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

    //-------------
    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    //--------------------

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.bind(producto, single.listProductoToPedido);
    }

    // Retorna la cantidad de items en la lista
    @Override
    public int getItemCount() {
        return listaProductos != null ? listaProductos.size() : 0;

    }

    //---------------------------
    // ViewHolder para contener las vistas de cada item
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMarca, tvDescripcion, tvPrecio;
        private ImageView ivImagen;
        private Button btnAgregarAlCarrito;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMarca = itemView.findViewById(R.id.tvMarca);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            btnAgregarAlCarrito = itemView.findViewById((R.id.btnAgregarCarrito));
        }

        public void bind(Producto producto, List<Producto> listaProductoToPedido) {
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

            btnAgregarAlCarrito.setOnClickListener(v -> {
                Producto nuevoProducto = new Producto();
                //nuevoProducto.setImagenProducto(ivImagen.getImageAlpha());
                nuevoProducto.setDescripcion(tvDescripcion.getText().toString());
                nuevoProducto.setMarca(tvMarca.getText().toString());

                String withoutPrefixPrecio = tvPrecio.getText().toString().contains("Oferta: S./") ?
                        tvPrecio.getText().toString().replaceAll("Oferta: S./", "") :
                        tvPrecio.getText().toString().replaceAll("S./", "");

                nuevoProducto.setPrecio(Double.parseDouble(withoutPrefixPrecio));
                if (producto.getImagenProducto() != 0) {
                    nuevoProducto.setImagenProducto(producto.getImagenProducto());
                }

                if (!listaProductoToPedido.contains(producto)) {
                    listaProductoToPedido.add(producto);
                }

                Toast.makeText(v.getContext(), tvMarca.getText().toString()+" agregado al carrito!", Toast.LENGTH_SHORT).show();

                /*for (int x = 0; x < listaProductoToPedido.size(); x++) {
                    Producto obproducto = listaProductoToPedido.get(x);
                    Log.i(null, obproducto.getMarca() + " - "+producto.getPrecio());
                }*/
            });
        }
    }
}
