package com.example.ferreteria.ui.carrito;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.Adp.AdaptadorProductoParaPedido;
import com.example.ferreteria.ConfirmarPedidoActivity;
import com.example.ferreteria.MenuActivity;
import com.example.ferreteria.R;
import com.example.ferreteria.SplashActivity;
import com.example.ferreteria.databinding.FragmentCarritoBinding;
import com.example.ferreteria.login.LoginActivity;
import com.example.ferreteria.login.SesionActivity;
import com.example.ferreteria.modelo.dao.PedidoDAO;
import com.example.ferreteria.modelo.dao.ProductoDAO;
import com.example.ferreteria.modelo.dto.ListaProductoParaPedido;
import com.example.ferreteria.modelo.dto.Producto;
import com.example.ferreteria.mtd;
import com.example.ferreteria.ui.home.HomeFragment;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CarritoFragment extends Fragment {

    private FragmentCarritoBinding binding;
    private AdaptadorProductoParaPedido adaptadorProductoParaPedido;
    private String TAG = "----xCarritoFrgment";
    private ListaProductoParaPedido instance = ListaProductoParaPedido.getInstance();
    private TextView precioTotal;
    private Button btnLimpiarListaPedido, btnRegistrarPedido, btnHistorialPedidos;
    private BigDecimal total = BigDecimal.ZERO;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CarritoViewModel galleryViewModel = new ViewModelProvider(this).get(CarritoViewModel.class);
        Log.i(TAG, "Ver carrito ");
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
            recyclerView.setVisibility(View.VISIBLE);
        }

        return root;
    }

    public void limpiarListPedido() {
        instance.listProductoToPedido.clear();
        precioTotal.setText("S./0.00");
        adaptadorProductoParaPedido.notifyDataSetChanged();
    }

    public void actualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Producto producto : instance.listProductoToPedido) {
           // Log.i(TAG ,"Id producto Fragment Pedido: "+ String.valueOf(producto.getId()));
            Integer cantidad = adaptadorProductoParaPedido.getCantidades().get(obtenerIdProducto(producto));
        //    Log.i(TAG,"Cantidad: "+ String.valueOf(cantidad));
            if (cantidad != null) {
                BigDecimal precioConDescuento = BigDecimal.valueOf(producto.getPrecioConDescuento());
                total = total.add(precioConDescuento.multiply(BigDecimal.valueOf(cantidad)));
            }
        }
        precioTotal.setText(String.format("S./%.2f", total.doubleValue()));
    }

    int obtenerIdProducto(Producto productoSeleccionado) {
        ProductoDAO productoDAO = new ProductoDAO(getContext());
        return productoDAO.getProductoIdByDescription(productoSeleccionado.getDescripcion());
    }

    public void registrarPedido3() {
        Log.i(TAG , "btn continuar compra");
        PedidoDAO pedidoDAO = new PedidoDAO(getContext());
        ProductoDAO productoDAO = new ProductoDAO(getContext());
        Map<Integer, Integer> cantidades = adaptadorProductoParaPedido.getCantidades();

        if (cantidades.isEmpty()) {
            Log.i(TAG, "You are empty");
        }

        for (Map.Entry<Integer, Integer> entry : cantidades.entrySet()) {
            Log.i(TAG, "Llave: " + entry.getKey() + ", Valor: " + entry.getValue());
        }


        mtd.redirigirConDelay(getActivity(), ConfirmarPedidoActivity.class, 500,false);

        Toast.makeText(getContext(), "Pedido registrado con éxito", Toast.LENGTH_SHORT).show();




    }

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

    private void mostrarDialogoCarritoVacio(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogo_carrito_vacio); // Asegúrate de tener un layout adecuado
        dialog.setCancelable(true);

        // Configurar el fondo blanco y que cubra toda la pantalla
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        }

        // Configuración del mensaje
        TextView mensaje = dialog.findViewById(R.id.mensajeCarritoVacio);
        mensaje.setText("Aún no hay ningún producto agregado al carrito.");

        // Configuración del botón "Cerrar"
        Button btnCerrar = dialog.findViewById(R.id.btnCerrarCarritoVacio);
        btnCerrar.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(requireContext(), MenuActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }







    public void registrarPedido() {
        Log.i(TAG , "btn continuar compra");

        int usuarioID = obtenerUsuarioID();
        Log.i(TAG,"Usuario ID :"+usuarioID);
        String fechaPedido = obtenerFechaActual();
        Log.i(TAG,"Fecha :"+fechaPedido);
        BigDecimal total = calcularTotalPedido();
        Log.i(TAG, "Total :"+total);

        PedidoDAO pedidoDAO = new PedidoDAO(getContext());

        long idPedido = pedidoDAO.insertarPedido(usuarioID, fechaPedido, total.doubleValue());

        if (idPedido != -1) {
            Log.i(TAG, "Pedido registrado con ID: " + idPedido);
            registrarDetallePedido(idPedido);
            mtd.redirigirConDelay(getActivity(), ConfirmarPedidoActivity.class, 500, false);
            Toast.makeText(getContext(), "Pedido registrado con éxito", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getContext(), "Error al registrar el pedido", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarDetallePedido5555(long idPedido) {
        Log.i(TAG, "registrar detalle pedido");
        Map<Integer, Integer> cantidades = adaptadorProductoParaPedido.getCantidades();
        ProductoDAO productoDAO = new ProductoDAO(getContext());

        for (Map.Entry<Integer, Integer> entry : cantidades.entrySet()) {
            int idProducto = entry.getKey();
            Log.i(TAG,"ID productos " + idProducto);
            int cantidad = entry.getValue();
            Log.i(TAG,"cantidad " + cantidad);
            double precioUnitario = productoDAO.obtenerPrecioPorId(idProducto);
            Log.i(TAG,"precioUnitario " + precioUnitario);

            // Insertar el detalle del pedido en la base de datos
//            PedidoDAO pedidoDAO = new PedidoDAO(getContext());
//            pedidoDAO.insertarDetallePedido(idPedido, idProducto, cantidad, precioUnitario);
        }
    }

    public void registrarDetallePedido2(long idPedido) {
        Log.i(TAG, "--------------------Registrando detalles del pedido...");

        // Obtener los productos seleccionados

        List<Producto> productosSeleccionados = adaptadorProductoParaPedido.getProductosSeleccionados();



        ProductoDAO productoDAO = new ProductoDAO(getContext());

        PedidoDAO pedidoDAO = new PedidoDAO(getContext());

        for (Producto producto : productosSeleccionados) {
            int idProducto = producto.getId();
            int cantidad = producto.getCantidad();
            Log.i(TAG,"Producto ID: " + idProducto);
            Log.i(TAG,"Cantidad: " + cantidad);

            // Obtener el precio unitario del producto
            double precioUnitario = productoDAO.obtenerPrecioPorId(idProducto);
            Log.i(TAG,"Precio Unitario: " + precioUnitario);

            // Insertar el detalle del pedido en la tabla detalles_pedidos
            boolean detalleRegistrado = pedidoDAO.insertarDetallePedido(idPedido, idProducto, cantidad, precioUnitario);

            if (detalleRegistrado) {
                Log.i(TAG, "Detalle del pedido registrado correctamente.");
            } else {
                Log.e(TAG, "Error al registrar el detalle del pedido");
            }
        }
    }

    public void registrarDetallePedido3(long idPedido) {
        Log.i(TAG, "--------------------Registrando detalles del pedido...");

        // Obtener los productos seleccionados
        List<Producto> productosSeleccionados = adaptadorProductoParaPedido.getProductosSeleccionados();

        // Verificar si la lista de productos seleccionados tiene datos
        Log.i(TAG, "Productos seleccionados: " + productosSeleccionados.size());

       Log.d(TAG,"----------");
        for (Producto producto : productosSeleccionados) {
            Log.i(TAG, "Producto ID: " + producto.getId());
            Log.i(TAG, "Cantidad: " + producto.getCantidad());
            Log.i(TAG, "Precio Unitario: " + producto.getPrecio());
        }
        Log.d(TAG,"----------");

        ProductoDAO productoDAO = new ProductoDAO(getContext());
        PedidoDAO pedidoDAO = new PedidoDAO(getContext());

        // Procesar los productos seleccionados
        for (Producto producto : productosSeleccionados) {
            int idProducto = producto.getId();
            int cantidad = producto.getCantidad();
            Log.i(TAG,"Producto ID: " + idProducto);
            Log.i(TAG,"Cantidad: " + cantidad);

            // Obtener el precio unitario del producto
            double precioUnitario = productoDAO.obtenerPrecioPorId(idProducto);
            Log.i(TAG,"Precio Unitario: " + precioUnitario);

            // Insertar el detalle del pedido en la tabla detalles_pedidos
            boolean detalleRegistrado = pedidoDAO.insertarDetallePedido(idPedido, idProducto, cantidad, precioUnitario);

            if (detalleRegistrado) {
                Log.i(TAG, "Detalle del pedido registrado correctamente.");
            } else {
                Log.e(TAG, "Error al registrar el detalle del pedido");
            }
        }
    }

    public void registrarDetallePedido4(long idPedido) {
        Log.i(TAG, "--------------------Registrando detalles del pedido...");

        // Obtener los productos seleccionados
        List<Producto> productosSeleccionados = adaptadorProductoParaPedido.getProductosSeleccionados();

        // Verificar si la lista de productos seleccionados tiene datos
        if (productosSeleccionados == null || productosSeleccionados.isEmpty()) {
            Log.e(TAG, "No hay productos seleccionados para registrar.");
            return;
        }

        Log.i(TAG, "Productos seleccionados: " + productosSeleccionados.size());

        ProductoDAO productoDAO = new ProductoDAO(getContext());
        PedidoDAO pedidoDAO = new PedidoDAO(getContext());

        // Procesar los productos seleccionados
        for (Producto producto : productosSeleccionados) {
            Log.i(TAG, "Producto ID: " + producto.getId());
            Log.i(TAG, "Cantidad: " + producto.getCantidad());
            Log.i(TAG, "Precio Unitario: " + producto.getPrecio());

            // Obtener el precio unitario del producto
            double precioUnitario = productoDAO.obtenerPrecioPorId(producto.getId());

            if (precioUnitario == 0) {
                Log.e(TAG, "No se pudo obtener el precio para el producto ID: " + producto.getId());
                continue;  // Si el precio no se encuentra, saltar este producto
            }

            // Insertar el detalle del pedido en la tabla detalles_pedidos
            boolean detalleRegistrado = pedidoDAO.insertarDetallePedido(idPedido, producto.getId(), producto.getCantidad(), precioUnitario);

            if (detalleRegistrado) {
                Log.i(TAG, "Detalle del pedido registrado correctamente para el producto ID: " + producto.getId());
            } else {
                Log.e(TAG, "Error al registrar el detalle del pedido para el producto ID: " + producto.getId());
            }
        }
    }

    public void registrarDetallePedido5(long idPedido) {
        Log.i(TAG, "--------------------Registrando detalles del pedido...");

        // Obtener los productos seleccionados
        List<Producto> productosSeleccionados = adaptadorProductoParaPedido.getProductosSeleccionados();

        // Verificar si la lista de productos seleccionados tiene datos
        if (productosSeleccionados == null || productosSeleccionados.isEmpty()) {
            Log.e(TAG, "No hay productos seleccionados para registrar.");
            return;
        }

        Log.i(TAG, "Productos seleccionados: " + productosSeleccionados.size());

        ProductoDAO productoDAO = new ProductoDAO(getContext());
        PedidoDAO pedidoDAO = new PedidoDAO(getContext());

        // Procesar los productos seleccionados
        for (Producto producto : productosSeleccionados) {
            int idProducto = producto.getId();
            int cantidad = producto.getCantidad();
            double precioUnitario = producto.getPrecio();

            Log.i(TAG, "Procesando Producto ID: " + idProducto);
            Log.i(TAG, "Cantidad: " + cantidad);
            Log.i(TAG, "Precio Unitario: " + precioUnitario);

            // Obtener el precio unitario del producto, solo si no es 0
            if (precioUnitario == 0) {
                precioUnitario = productoDAO.obtenerPrecioPorId(idProducto);
            }

            // Verificar si el precio es válido
            if (precioUnitario <= 0) {
                Log.e(TAG, "No se pudo obtener un precio válido para el producto ID: " + idProducto);
                continue;  // Si el precio no es válido, saltar este producto
            }

            // Insertar el detalle del pedido en la tabla detalles_pedidos
            boolean detalleRegistrado = pedidoDAO.insertarDetallePedido(idPedido, idProducto, cantidad, precioUnitario);

            if (detalleRegistrado) {
                Log.i(TAG, "Detalle del pedido registrado correctamente para el producto ID: " + idProducto);
            } else {
                Log.e(TAG, "Error al registrar el detalle del pedido para el producto ID: " + idProducto);
            }
        }
    }

    public void registrarDetallePedido23(long idPedido) {
        Log.i(TAG, "--------------------Registrando detalles del pedido...");

        // Obtener los productos seleccionados
        List<Producto> productosSeleccionados = adaptadorProductoParaPedido.getProductosSeleccionados();

        // Verificar si la lista de productos seleccionados tiene datos
        if (productosSeleccionados == null || productosSeleccionados.isEmpty()) {
            Log.e(TAG, "No hay productos seleccionados para registrar.");
            return;
        }

        Log.i(TAG, "Productos seleccionados: " + productosSeleccionados.size());

        ProductoDAO productoDAO = new ProductoDAO(getContext());
        PedidoDAO pedidoDAO = new PedidoDAO(getContext());

        // Procesar los productos seleccionados
        for (Producto producto : productosSeleccionados) {
            int idProducto = producto.getId();
            Log.i(TAG , "puto "+producto.getMarca());
            Log.i(TAG , "puto "+producto.getDescripcion());

            Log.i(TAG , "puto "+producto.getId());
            Log.i(TAG , "puto "+producto.getId());



            int cantidad = producto.getCantidad();
            double precioUnitario = producto.getPrecio();

            Log.i(TAG, "Procesando Producto ID: " + idProducto);
            Log.i(TAG, "Cantidad: " + cantidad);
            Log.i(TAG, "Precio Unitario: " + precioUnitario);

            // Obtener el precio unitario del producto, solo si no es 0
            if (precioUnitario == 0) {
                precioUnitario = productoDAO.obtenerPrecioPorId(idProducto);
                Log.i(TAG,"COMO VERGA OBTIENE EL PRECIO DE "+idProducto);
            }

            // Verificar si el precio es válido
            if (precioUnitario <= 0) {
                Log.e(TAG, "No se pudo obtener un precio válido para el producto ID: " + idProducto);
                continue;  // Si el precio no es válido, saltar este producto
            }

            // Insertar el detalle del pedido en la tabla detalles_pedidos
            boolean detalleRegistrado = pedidoDAO.insertarDetallePedido(idPedido, idProducto, cantidad, precioUnitario);

            if (detalleRegistrado) {
                Log.i(TAG, "Detalle del pedido registrado correctamente para el producto ID: " + idProducto);
            } else {
                Log.e(TAG, "Error al registrar el detalle del pedido para el producto ID: " + idProducto);
            }
        }
    }

    public void registrarDetallePedido(long idPedido) {
        Log.i(TAG, "--------------------Registrando detalles del pedido...");

        // Obtener los productos seleccionados
        List<Producto> productosSeleccionados = adaptadorProductoParaPedido.getProductosSeleccionados();

        // Verificar si la lista de productos seleccionados tiene datos
        if (productosSeleccionados == null || productosSeleccionados.isEmpty()) {
            Log.e(TAG, "No hay productos seleccionados para registrar.");
            return;
        }

        Log.i(TAG, "Productos seleccionados: " + productosSeleccionados.size());

        ProductoDAO productoDAO = new ProductoDAO(getContext());
        PedidoDAO pedidoDAO = new PedidoDAO(getContext());

        // Procesar los productos seleccionados
        for (Producto producto : productosSeleccionados) {
            int idProducto = producto.getId();
            Log.i(TAG, "ID Producto antes de verificar: " + idProducto);

            // Verificar si el ID del producto es válido
            if (idProducto == 0) {
                Log.e(TAG, "ID de producto no válido: " + producto.getDescripcion());
                continue;  // Saltar este producto
            }

            Log.i(TAG , "Marca: " + producto.getMarca());
            Log.i(TAG , "Descripción: " + producto.getDescripcion());

            int cantidad = producto.getCantidad();
            double precioUnitario = producto.getPrecio();

            Log.i(TAG, "Procesando Producto ID: " + idProducto);
            Log.i(TAG, "Cantidad: " + cantidad);
            Log.i(TAG, "Precio Unitario: " + precioUnitario);

            // Obtener el precio unitario del producto, solo si no es 0
            if (precioUnitario == 0) {
                precioUnitario = productoDAO.obtenerPrecioPorId(idProducto);
                Log.i(TAG,"Precio obtenido para el producto ID: " + idProducto + " -> Precio: " + precioUnitario);
            }

            // Verificar si el precio es válido
            if (precioUnitario <= 0) {
                Log.e(TAG, "No se pudo obtener un precio válido para el producto ID: " + idProducto);
                continue;  // Si el precio no es válido, saltar este producto
            }

            // Insertar el detalle del pedido en la tabla detalles_pedidos
            boolean detalleRegistrado = pedidoDAO.insertarDetallePedido(idPedido, idProducto, cantidad, precioUnitario);

            if (detalleRegistrado) {
                Log.i(TAG, "Detalle del pedido registrado correctamente para el producto ID: " + idProducto);
            } else {
                Log.e(TAG, "Error al registrar el detalle del pedido para el producto ID: " + idProducto);
            }
        }
    }













    public int obtenerUsuarioID() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Sesion", Context.MODE_PRIVATE);
        int usuarioID = sharedPreferences.getInt("usuarioID", -1);
        if (usuarioID == -1) {
            Log.d(TAG, "ObtenerUsuarioID :No se encontró el ID de usuario. El valor es: " + usuarioID);
        }
        return sharedPreferences.getInt("usuarioID", -1);
    }


    public BigDecimal calcularTotalPedido() {
        BigDecimal total = BigDecimal.ZERO;

        // Recorremos los productos en el carrito (instance.listProductoToPedido)
        for (Producto producto : instance.listProductoToPedido) {
            // Obtenemos la cantidad de este producto
            Integer cantidad = adaptadorProductoParaPedido.getCantidades().get(obtenerIdProducto(producto));

            if (cantidad != null) {
                // Calculamos el precio total para este producto: precio * cantidad
                BigDecimal precioConDescuento = BigDecimal.valueOf(producto.getPrecioConDescuento());
                total = total.add(precioConDescuento.multiply(BigDecimal.valueOf(cantidad)));
            }
        }

        return total;
    }

    public String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date()); // Devuelve la fecha y hora actuales
    }













}
