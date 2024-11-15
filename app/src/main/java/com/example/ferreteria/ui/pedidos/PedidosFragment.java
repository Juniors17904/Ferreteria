package com.example.ferreteria.ui.pedidos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.Adp.AdaptadorHistorialDetallePedido;
import com.example.ferreteria.Adp.AdaptadorProductoParaPedido;
import com.example.ferreteria.R;
import com.example.ferreteria.databinding.FragmentGalleryBinding;
import com.example.ferreteria.modelo.dao.ClienteDAO;
import com.example.ferreteria.modelo.dao.DetallePedidoDAO;
import com.example.ferreteria.modelo.dao.PedidoDAO;
import com.example.ferreteria.modelo.dao.ProductoDAO;
import com.example.ferreteria.modelo.dto.Cliente;
import com.example.ferreteria.modelo.dto.DetallePedido;
import com.example.ferreteria.modelo.dto.Historial;
import com.example.ferreteria.modelo.dto.ListaProductoParaPedido;
import com.example.ferreteria.modelo.dto.Pedido;
import com.example.ferreteria.modelo.dto.Producto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PedidosFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private AdaptadorProductoParaPedido adaptadorProductoParaPedido;
    private String TAG = "----PEDIDOS";
    private ListaProductoParaPedido instance = ListaProductoParaPedido.getInstance();
    /*
    * Componentes de la ventana de Pedidos
    * */
    private EditText txtNombres, txtEmail, txtTelf, txtDirecc;
    //private EditText txtNumberCantidad;
    private TextView precioTotal;
    private Button btnLimpiarListaPedido, btnRegistrarPedido, btnHistorialPedidos;
    private BigDecimal total = BigDecimal.ZERO;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PedidosViewModel galleryViewModel = new ViewModelProvider(this).get(PedidosViewModel.class);
        Log.i(TAG, "-------");
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtNombres = root.findViewById(R.id.editTextNombrePedido);
        txtEmail = root.findViewById(R.id.editTextEmailPedido);
        txtTelf = root.findViewById(R.id.editTextTelfPedido);
        txtDirecc = root.findViewById(R.id.editTextDirecPedido);
        //txtNumberCantidad = root.findViewById(R.id.editTextNumberCantidad);
        precioTotal = root.findViewById(R.id.labelprecioTotalPedido);

        btnLimpiarListaPedido = root.findViewById(R.id.btnLimpiarListaPedido);
        btnRegistrarPedido = root.findViewById(R.id.btnRegistrarPedido);
        btnHistorialPedidos = root.findViewById(R.id.btnHistorialPedidos);

        for (Producto producto : instance.listProductoToPedido) {
            total = total.add(BigDecimal.valueOf(producto.getPrecioConDescuento()));
        }

        precioTotal.setText(String.format("S./%.2f",total.doubleValue()));


        btnLimpiarListaPedido.setOnClickListener(v -> {
            limpiarListPedido();
        });

        btnRegistrarPedido.setOnClickListener(v -> {
            registrarPedido();
        });

        btnHistorialPedidos.setOnClickListener(v -> {
            Log.i("Botón de historial Pedidos: ", "clicked!");
            abrirVentanaDeHistorialPedidos(root.getContext());
        });

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewProductoByPedido);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final TextView textView = binding.textGallery;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        adaptadorProductoParaPedido = new AdaptadorProductoParaPedido(this);
        recyclerView.setAdapter(adaptadorProductoParaPedido);


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
            total = total.add(BigDecimal.valueOf(producto.getPrecioConDescuento()));
        }
        precioTotal.setText(String.format("S./%.2f", total.doubleValue()));
    }

    public void registrarPedido() {
        ClienteDAO clienteDAO = new ClienteDAO(getContext());
        Cliente cliente = new Cliente();

        cliente.setNombre(txtNombres.getText().toString());
        cliente.setCorreo(txtEmail.getText().toString());
        cliente.setTelefono(txtTelf.getText().toString());
        cliente.setDireccion(txtDirecc.getText().toString());

        boolean res = clienteDAO.insertar(cliente);
        Log.i("Salida del Insertar DAO: ", String.valueOf(res));
        Log.i(null, "Datos del cliente -> "+
                txtNombres.getText().toString()+", "+
                txtDirecc.getText().toString()+", "+
                txtEmail.getText().toString()+", "+
                txtTelf.getText().toString());

        if (res) {
            // El setTimeOut es para esperar que los datos del cliente hayan sido insertados
            // dándole un tiempo apropiado para que salte al evento de registro de pedido
            PedidoDAO pedidoDAO = new PedidoDAO(getContext());

            int idCliente = clienteDAO.getIdByClient(txtEmail.getText().toString());
            Log.i("ID CLIENTE DESDE EL DAO: ", String.valueOf(idCliente));
            Pedido nuevoPedido = new Pedido();
            Date fechaActual = new Date();

            nuevoPedido.setClienteId(idCliente);
            nuevoPedido.setFechaPedido(fechaActual);

            boolean insertedPedido = pedidoDAO.insertar(nuevoPedido);
            Log.i("Pedido fue insertado?: ", String.valueOf(insertedPedido));
            if (insertedPedido) {
                int idPedido = pedidoDAO.getIdByClienteId(idCliente);
                ProductoDAO productoDAO = new ProductoDAO(getContext());

                Map<Integer, Integer> cantidades = adaptadorProductoParaPedido.getCantidades();

                if (cantidades.isEmpty()) {
                    Log.i("Tell me", "You are empty");
                }
                for (Map.Entry<Integer, Integer> entry : cantidades.entrySet()) {
                    Log.i("Cantidades en el MAP: ", "Llave: " + entry.getKey() + ", Valor: " + entry.getValue());
                }

                Log.i("Asegurando que no es el Map", "Sí");

                for (Producto producto : instance.listProductoToPedido) {
                    DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO(getContext());
                    Log.i("Producto de la static list: ", producto.getMarca());
                    Log.i("Maybe the static list?", "idk");
                    DetallePedido detallePedido = new DetallePedido();
                    detallePedido.setPedidoId(idPedido);
                    detallePedido.setPrecio(producto.getPrecio()); // Precio unitario
                    int productoId = productoDAO.getProductoIdByMarca(producto.getMarca());
                    Log.i("ProductoDAO: ", "ID: "+String.valueOf(productoId));
                    detallePedido.setProductoId(productoId);
                    Log.i("Before null value!!!!!!", "Explainmeeeeeeeeeeeeeee");
                    detallePedido.setCantidad(cantidades.get(productoId));
                    Log.i("Before: ", "Antes de la inserción del detalle pedido");
                    detallePedidoDAO.insertar(detallePedido);
                }
                Toast.makeText(getContext(), "Pedido registrado con éxito: ", Toast.LENGTH_SHORT).show();
            }
            }

    }

    public void abrirVentanaDeHistorialPedidos(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_historial_pedido);
        dialog.setCancelable(true);

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewHistorialPedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO(dialog.getContext());
        List<Historial> historialList = detallePedidoDAO.getAllDetallePedidosByIdClient(21); //Solo un ejemplo, porque el login aún no existe
        AdaptadorHistorialDetallePedido adap = new AdaptadorHistorialDetallePedido(historialList);
        recyclerView.setAdapter(adap);

        Button btnClose = dialog.findViewById(R.id.btnCerrarDelHistorialPedido);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}