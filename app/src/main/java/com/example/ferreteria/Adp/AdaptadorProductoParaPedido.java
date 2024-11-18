package com.example.ferreteria.Adp;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.R;
import com.example.ferreteria.modelo.dao.ProductoDAO;
import com.example.ferreteria.modelo.dto.ListaProductoParaPedido;
import com.example.ferreteria.modelo.dto.Producto;
import com.example.ferreteria.ui.pedidos.PedidosFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorProductoParaPedido extends RecyclerView.Adapter<AdaptadorProductoParaPedido.PedidoViewHolder> {
    ListaProductoParaPedido single = ListaProductoParaPedido.getInstance();
    private static Map<Integer, Integer> cantidades = new HashMap<>();
    private PedidosFragment fragment;

    public AdaptadorProductoParaPedido(PedidosFragment fragment) {
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_seleccionado, parent, false);
        return new AdaptadorProductoParaPedido.PedidoViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        List<Producto> productosSeleccionadoNonStatic = new ArrayList<>();

        productosSeleccionadoNonStatic.addAll(single.listProductoToPedido);

        Producto productoSeleccionado = productosSeleccionadoNonStatic.get(position);
        holder.bind(productoSeleccionado, position);
    }

    @Override
    public int getItemCount() {
        return single.listProductoToPedido.size();
    }

    public Map<Integer, Integer> getCantidades() {
        return cantidades;
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        /// pdp == "Producto del Pedido"
        private ImageView pdpImageView;
        private TextView pdpMarca, pdpDescrip, pdpPrecio, pdpFecha;
        private Button btnEliminar;
        private AdaptadorProductoParaPedido adaptadorProductoParaPedido;
        private EditText txtNumberCantidad;

        public PedidoViewHolder(@NonNull View itemView, AdaptadorProductoParaPedido adaptador) {
            super(itemView);
            this.adaptadorProductoParaPedido = adaptador;
            pdpImageView = itemView.findViewById(R.id.pdpImageView);
            pdpMarca = itemView.findViewById(R.id.pdpMarca);
            pdpDescrip = itemView.findViewById(R.id.pdpDescrip);
            pdpPrecio = itemView.findViewById(R.id.pdpPrecio);
            btnEliminar = itemView.findViewById(R.id.pdpBtnEliminarProducto);
            pdpFecha = itemView.findViewById(R.id.pptextViewFecha);
            txtNumberCantidad = itemView.findViewById(R.id.editTextNumberCantidad);
        }

        int obtenerIdProducto(Producto productoSeleccionado) {
            ProductoDAO productoDAO = new ProductoDAO(itemView.getContext());

            return productoDAO.getProductoIdByDescription(productoSeleccionado.getDescripcion());
        }

        public void bind(Producto productoSeleccionado, int position) {

            int obtenerIdDelProducto = obtenerIdProducto(productoSeleccionado);
            if (!adaptadorProductoParaPedido.cantidades.containsKey(productoSeleccionado.getId())) {
                adaptadorProductoParaPedido.cantidades.put(obtenerIdDelProducto, Integer.parseInt(txtNumberCantidad.getText().toString()));  // Inicializar con cantidad 1
            }

            pdpMarca.setText(productoSeleccionado.getMarca());
            pdpDescrip.setText(productoSeleccionado.getDescripcion());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("es_ES"));
            Log.i("Fechaaaaaaaa: ", simpleDateFormat.format(new Date()));
            pdpFecha.setText(simpleDateFormat.format(new Date()));

            // precio con descuento si está en oferta
            if (productoSeleccionado.isTieneOferta()) {
                pdpPrecio.setText(String.format("Oferta: S./%.2f", productoSeleccionado.getPrecioConDescuento()));
                pdpPrecio.setTextColor(Color.RED);
            } else {
                pdpPrecio.setText(String.format("S./%.2f", productoSeleccionado.getPrecio()));
                pdpPrecio.setTextColor(Color.BLACK);
            }

            if (productoSeleccionado.getImagenProducto() != 0) {
                pdpImageView.setImageResource(productoSeleccionado.getImagenProducto());
            }


            btnEliminar.setOnClickListener(v -> {
                ListaProductoParaPedido instance = ListaProductoParaPedido.getInstance();
                instance.listProductoToPedido.remove(position);

                adaptadorProductoParaPedido.cantidades.remove(productoSeleccionado.getId());

                adaptadorProductoParaPedido.notifyItemRemoved(position);
                adaptadorProductoParaPedido.fragment.actualizarTotal();
            });

            /*Integer cantidad = adaptadorProductoParaPedido.cantidades.get(productoSeleccionado.getId());
            if (cantidad != null) {
                txtNumberCantidad.setText(String.valueOf(cantidad));
            } else {
                txtNumberCantidad.setText("0");
            }*/

            txtNumberCantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    String cantidadTexto = s.toString();
                    int cantidad = 0;
                    if (!cantidadTexto.isEmpty()) {
                        cantidad = Integer.parseInt(cantidadTexto);
                    }

                    int obtenerIdDelProducto = obtenerIdProducto(productoSeleccionado);
                    adaptadorProductoParaPedido.getCantidades().put(obtenerIdDelProducto, cantidad);

                    adaptadorProductoParaPedido.fragment.actualizarTotal();
                }
            });
        }
    }
}
