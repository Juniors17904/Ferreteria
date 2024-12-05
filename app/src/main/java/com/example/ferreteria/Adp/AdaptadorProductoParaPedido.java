package com.example.ferreteria.Adp;

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
import com.example.ferreteria.ui.carrito.CarritoFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdaptadorProductoParaPedido extends RecyclerView.Adapter<AdaptadorProductoParaPedido.PedidoViewHolder> {
    private static final String TAG = "----xAdaptadorProductoParaPedido";
    private static ListaProductoParaPedido listaProductos = ListaProductoParaPedido.getInstance();
    private static Map<Integer, Integer> productoSelecionado = new HashMap<>();
    private CarritoFragment fragment;

    public AdaptadorProductoParaPedido(CarritoFragment fragment) {
        this.fragment = fragment;
        //     Log.i(TAG, "AdaptadorProductoParaPedido inicializado.");
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //    Log.i(TAG, "onCreateViewHolder llamado.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_seleccionado, parent, false);
        return new PedidoViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Producto producto = listaProductos.listProductoToPedido.get(position);
        // Log.i(TAG, "Producto: " + producto.getDescripcion() + " en la posición: " + position);
        holder.bind(producto, position);
    }

    @Override
    public int getItemCount() {
        // Log.i(TAG, "getItemCount llamado, cantidad de productos: " + listaProductos.listProductoToPedido.size());
        return listaProductos.listProductoToPedido.size();
    }

    public Map<Integer, Integer> getCantidades() {
        return productoSelecionado;

    }


    public List<Producto> getProductosSeleccionados1() {
        Log.d(TAG, "---------------Obteniendo productos seleccionados-------------------");
        List<Producto> productosSeleccionados = new ArrayList<>();

        // Recorre todos los productos en la lista
        for (Producto producto : listaProductos.listProductoToPedido) {

            int idProducto = new ProductoDAO(fragment.getContext()).getProductoIdByDescription(producto.getDescripcion());


            if (productoSelecionado.containsKey(idProducto) && productoSelecionado.get(idProducto) > 0) {
                Log.i(TAG, "----");
                Log.d(TAG, "Id Producto: " + idProducto);
                Log.d(TAG, "Cantidad: " + productoSelecionado.get(idProducto));
                Log.d(TAG, "Precio Unitario: " + producto.getPrecio());
                Log.i(TAG, "----");

                // Agrega el producto a la lista de productos seleccionados
                productosSeleccionados.add(producto);
            }
        }

        Log.d(TAG, "Productos seleccionados: " + productosSeleccionados.size());
        Log.d(TAG, "---------------Obteniendo productos seleccionados-------------------");
        return productosSeleccionados;
    }

    public  List<Producto> getProductosSeleccionados2() {
        Log.d(TAG, "---------------Obteniendo productos seleccionados-------------------");
        List<Producto> productosSeleccionados = new ArrayList<>();

        // Recorre todos los productos en la lista
        for (Producto producto : listaProductos.listProductoToPedido) {

            int idProducto = new ProductoDAO(fragment.getContext()).getProductoIdByDescription(producto.getDescripcion());


            if (productoSelecionado.containsKey(idProducto) && productoSelecionado.get(idProducto) > 0) {
//                Log.i(TAG, "----");
//                Log.d(TAG, "Id Producto: " + idProducto);
//                Log.d(TAG, "Cantidad: " + productoSelecionado.get(idProducto));
//                Log.d(TAG, "Precio Unitario: " + producto.getPrecio());
//                Log.i(TAG, "----");

                // Agrega el producto a la lista de productos seleccionados
                productosSeleccionados.add(producto);
            }
        }

        Log.d(TAG, "Productos seleccionados: " + productosSeleccionados.size());
        Log.d(TAG, "---------------Obteniendo productos seleccionados-------------------");
        return productosSeleccionados;
    }

    public List<Producto> getProductosSeleccionados() {
        Log.d(TAG, "---------------Obteniendo productos seleccionados-------------------");
        List<Producto> productosSeleccionados = new ArrayList<>();

        // Recorre todos los productos en la lista
        for (Producto producto : listaProductos.listProductoToPedido) {

            // Obtén el ID del producto
            int idProducto = new ProductoDAO(fragment.getContext()).getProductoIdByDescription(producto.getDescripcion());
            Log.i(TAG , "ID PRODUCTO ?? "+idProducto);

            // Verifica si el producto está seleccionado y tiene una cantidad mayor a 0
            if (productoSelecionado.containsKey(idProducto) && productoSelecionado.get(idProducto) > 0) {
                Log.i(TAG, "----");
                Log.d(TAG, "Id Producto: " + idProducto);
                Log.d(TAG, "Cantidad: " + productoSelecionado.get(idProducto));
                Log.d(TAG, "Precio Unitario: " + producto.getPrecio());
                Log.i(TAG, "----");

                // Asigna la cantidad al producto y agrégalo a la lista de productos seleccionados
                producto.setCantidad(productoSelecionado.get(idProducto));
                productosSeleccionados.add(producto);
            }
        }

        // Agregar logs detallados de cada producto seleccionado
        for (Producto p : productosSeleccionados) {
            Log.d(TAG, "LEYENDOOOOO   Producto Seleccionado -> ID: " + p.getId() +
                    ", Marca: " + p.getMarca() +
                    ", Descripción: " + p.getDescripcion() +
                    ", Precio: " + p.getPrecio() +
                    ", Cantidad: " + p.getCantidad());
        }


        Log.d(TAG, "Productos seleccionados: " + productosSeleccionados.size());
        Log.d(TAG, "---------------Obteniendo productos seleccionados-------------------");
        return productosSeleccionados;
    }



    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        private ImageView pdpImageView;
        private TextView pdpMarca, pdpDescrip, pdpPrecio, pdpFecha, pdpPrecioTotal;
        private EditText txtCantidad;
        private Button btnIncrementar, btnDecrementar, btnEliminar;
        private AdaptadorProductoParaPedido adaptador;

        public PedidoViewHolder(@NonNull View itemView, AdaptadorProductoParaPedido adaptador) {
            super(itemView);
            this.adaptador = adaptador;

            pdpImageView = itemView.findViewById(R.id.pdpImageView);
            pdpMarca = itemView.findViewById(R.id.pdpMarca);
            pdpDescrip = itemView.findViewById(R.id.pdpDescrip);
            pdpPrecio = itemView.findViewById(R.id.pdpPreciounit);
            pdpFecha = itemView.findViewById(R.id.pptextViewFecha);
            pdpPrecioTotal = itemView.findViewById(R.id.pdpPrecioTotal);
            txtCantidad = itemView.findViewById(R.id.editTextNumberCantidad);
            btnIncrementar = itemView.findViewById(R.id.btnIncrementar);
            btnDecrementar = itemView.findViewById(R.id.btnDecrementar);
            btnEliminar = itemView.findViewById(R.id.pdpBtnEliminarProducto);

            //  Log.i(TAG, "PedidoViewHolder creado.");
        }

        public void bind(Producto producto, int position) {
            ProductoDAO productoDAO = new ProductoDAO(itemView.getContext());
            int idProducto = productoDAO.getProductoIdByDescription(producto.getDescripcion());
            Log.i(TAG, "Producto  con ID: " + idProducto);

            if (!productoSelecionado.containsKey(idProducto)) {
                productoSelecionado.put(idProducto, 1);
                //     Log.i(TAG, "Cantidad inicial para producto " + producto.getDescripcion() + " es 1.");
            }

            // Cargar la imagen desde los recursos locales
            int imageResourceId = producto.getImagenProducto();
            if (imageResourceId != 0) {
                pdpImageView.setImageResource(imageResourceId);
                //    Log.i(TAG, "Imagen cargada para producto " + producto.getDescripcion());
            } else {
                pdpImageView.setImageResource(R.drawable.ladrillos);
                //     Log.i(TAG, "Imagen predeterminada cargada para producto " + producto.getDescripcion());
            }

            pdpMarca.setText(producto.getMarca());
            pdpDescrip.setText(producto.getDescripcion());
            pdpFecha.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
            txtCantidad.setText(String.valueOf(productoSelecionado.get(idProducto)));

            // Verificar si el producto tiene oferta
            if (producto.isTieneOferta()) {
                pdpPrecio.setText(String.format(Locale.getDefault(), "Oferta S./%.2f", producto.getPrecioConDescuento()));
                pdpPrecio.setTextColor(Color.RED);
            } else {
                pdpPrecio.setText(String.format(Locale.getDefault(), "S./%.2f", producto.getPrecio()));
                pdpPrecio.setTextColor(Color.BLACK);
            }

            actualizarPrecioTotal(producto.isTieneOferta() ? producto.getPrecioConDescuento() : producto.getPrecio(), productoSelecionado.get(idProducto));

            btnIncrementar.setOnClickListener(v -> actualizarCantidad(idProducto, 1, producto.isTieneOferta() ? producto.getPrecioConDescuento() : producto.getPrecio()));
            btnDecrementar.setOnClickListener(v -> actualizarCantidad(idProducto, -1, producto.isTieneOferta() ? producto.getPrecioConDescuento() : producto.getPrecio()));

            btnEliminar.setOnClickListener(v -> {
                listaProductos.listProductoToPedido.remove(position);
                productoSelecionado.remove(idProducto);
                adaptador.notifyItemRemoved(position);
                adaptador.fragment.actualizarTotal();
                Log.i(TAG, "Producto eliminado en la posición " + position);
            });

            txtCantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int nuevaCantidad = Integer.parseInt(s.toString());
                        if (nuevaCantidad > 0) {
                            productoSelecionado.put(idProducto, nuevaCantidad);
                            actualizarPrecioTotal(producto.isTieneOferta() ? producto.getPrecioConDescuento() : producto.getPrecio(), nuevaCantidad);
                            adaptador.fragment.actualizarTotal();
                            // Log.i(TAG, "Cantidad actualizada a " + nuevaCantidad + " para producto " + producto.getDescripcion());
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Cantidad inválida ingresada para producto " + producto.getDescripcion(), e);
                    }
                }
            });
        }


        private void actualizarCantidad(int idProducto, int cambio, double precio) {
            int cantidadActual = productoSelecionado.containsKey(idProducto) ? productoSelecionado.get(idProducto) : 1;
            int nuevaCantidad = Math.max(1, cantidadActual + cambio);

            productoSelecionado.put(idProducto, nuevaCantidad);
            txtCantidad.setText(String.valueOf(nuevaCantidad));
            actualizarPrecioTotal(precio, nuevaCantidad);
            adaptador.fragment.actualizarTotal();

            Log.i(TAG, "Cantidad para producto ID " + idProducto + " actualizada a " + nuevaCantidad);
        }

        private void actualizarPrecioTotal(double precio, int cantidad) {
            double precioTotal = precio * cantidad;
            pdpPrecioTotal.setText(String.format(Locale.getDefault(), "Total: S./%.2f", precioTotal));
            Log.i(TAG, "Precio subTotal actualizado a S./" + precioTotal);
        }


    }
}
