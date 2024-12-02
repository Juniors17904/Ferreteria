package com.example.ferreteria.ui.carrito;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.Adp.AdaptadorProductoParaPedido;
import com.example.ferreteria.R;
import com.example.ferreteria.databinding.FragmentCarritoBinding;
import com.example.ferreteria.modelo.dao.PedidoDAO;
import com.example.ferreteria.modelo.dao.ProductoDAO;
import com.example.ferreteria.modelo.dto.ListaProductoParaPedido;
import com.example.ferreteria.modelo.dto.Producto;

import java.math.BigDecimal;
import java.util.Map;

public class CarritoFragment extends Fragment {

    private FragmentCarritoBinding binding;
    private AdaptadorProductoParaPedido adaptadorProductoParaPedido;
    private String TAG = "----PEDIDOS";
    private ListaProductoParaPedido instance = ListaProductoParaPedido.getInstance();
    private TextView precioTotal;
    private Button btnLimpiarListaPedido, btnRegistrarPedido, btnHistorialPedidos;
    private BigDecimal total = BigDecimal.ZERO;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CarritoViewModel galleryViewModel = new ViewModelProvider(this).get(CarritoViewModel.class);
        Log.i(TAG, "-------");
        binding = FragmentCarritoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        precioTotal = root.findViewById(R.id.labelprecioTotalPedido);
        btnLimpiarListaPedido = root.findViewById(R.id.btnLimpiarListaPedido);
        btnRegistrarPedido = root.findViewById(R.id.btnRegistrarPedido);
        btnHistorialPedidos = root.findViewById(R.id.btnHistorialPedidos);

        // Calcular el total de la lista de productos para el pedido
        for (Producto producto : instance.listProductoToPedido) {
            total = total.add(BigDecimal.valueOf(producto.getPrecioConDescuento()));
        }

        precioTotal.setText(String.format("S./%.2f", total.doubleValue()));

        // Limpiar la lista de productos en el pedido
        btnLimpiarListaPedido.setOnClickListener(v -> limpiarListPedido());

        // Registrar el pedido
        btnRegistrarPedido.setOnClickListener(v -> registrarPedido());

        // Mostrar historial de pedidos
        btnHistorialPedidos.setOnClickListener(v -> {
            Log.i("Botón de historial Pedidos: ", "clicked!");
            abrirVentanaDeHistorialPedidos(root.getContext());
        });

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewProductoByPedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (instance.listProductoToPedido.isEmpty()) {
            // Ocultar el RecyclerView si la lista está vacía
            recyclerView.setVisibility(View.GONE);

            // Mostrar el diálogo flotante si la lista está vacía
            mostrarDialogoCarritoVacio(root.getContext());
        } else {
            // Configurar el adaptador si la lista no está vacía
            adaptadorProductoParaPedido = new AdaptadorProductoParaPedido(this);
            recyclerView.setAdapter(adaptadorProductoParaPedido);
            recyclerView.setVisibility(View.VISIBLE); // Asegurarse de que el RecyclerView se vea si no está vacío
        }

        return root;
    }

    // Limpiar la lista de productos del pedido
    public void limpiarListPedido() {
        instance.listProductoToPedido.clear();
        precioTotal.setText("S./0.00");
        adaptadorProductoParaPedido.notifyDataSetChanged();
    }

    // Actualizar el total del pedido al cambiar las cantidades de los productos
    public void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Producto producto : instance.listProductoToPedido) {
            Log.i("Id producto Fragment Pedido: ", String.valueOf(producto.getId()));
            Integer cantidad = adaptadorProductoParaPedido.getCantidades().get(obtenerIdProducto(producto));
            Log.i("Cantiddddddd: ", String.valueOf(cantidad));
            if (cantidad != null) {
                BigDecimal precioConDescuento = BigDecimal.valueOf(producto.getPrecioConDescuento());
                total = total.add(precioConDescuento.multiply(BigDecimal.valueOf(cantidad)));
            }
        }
        precioTotal.setText(String.format("S./%.2f", total.doubleValue()));
    }

    // Obtener el ID de un producto por su descripción
    int obtenerIdProducto(Producto productoSeleccionado) {
        ProductoDAO productoDAO = new ProductoDAO(getContext());
        return productoDAO.getProductoIdByDescription(productoSeleccionado.getDescripcion());
    }

    // Registrar un nuevo pedido
    public void registrarPedido() {
        PedidoDAO pedidoDAO = new PedidoDAO(getContext());
        ProductoDAO productoDAO = new ProductoDAO(getContext());
        Map<Integer, Integer> cantidades = adaptadorProductoParaPedido.getCantidades();

        if (cantidades.isEmpty()) {
            Log.i("Tell me", "You are empty");
        }

        for (Map.Entry<Integer, Integer> entry : cantidades.entrySet()) {
            Log.i("Cantidades en el MAP: ", "Llave: " + entry.getKey() + ", Valor: " + entry.getValue());
        }



        Toast.makeText(getContext(), "Pedido registrado con éxito", Toast.LENGTH_SHORT).show();



        limpiarListPedido();
    }

    // Abrir la ventana de historial de pedidos
    public void abrirVentanaDeHistorialPedidos(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_historial_pedido);
        dialog.setCancelable(true);

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewHistorialPedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        // Código para obtener historial de pedidos...

        Button btnClose = dialog.findViewById(R.id.btnCerrarDelHistorialPedido);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    // Método para mostrar un diálogo flotante si el carrito está vacío
    private void mostrarDialogoCarritoVacio(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogo_carrito_vacio); // Crea un layout para el diálogo
        dialog.setCancelable(true);

        TextView mensaje = dialog.findViewById(R.id.mensajeCarritoVacio);
        mensaje.setText("Aún no hay ningún producto agregado al carrito.");

        Button btnCerrar = dialog.findViewById(R.id.btnCerrarCarritoVacio);
        btnCerrar.setOnClickListener(v -> {
            dialog.dismiss(); // Cerrar el diálogo
        });

        // Asegúrate de que el diálogo esté centrado y cubra la pantalla correctamente
        dialog.getWindow().setGravity(Gravity.CENTER); // Centrar el diálogo
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); // Ajustar tamaño
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
