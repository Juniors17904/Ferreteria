package com.example.ferreteria.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ferreteria.Adp.AdaptadorCategorias;
import com.example.ferreteria.Adp.AdaptadorOfertas;
import com.example.ferreteria.R;
import com.example.ferreteria.databinding.FragmentHomeBinding;
import com.example.ferreteria.modelo.dao.CategoriaDAO;
import com.example.ferreteria.modelo.dao.OfertaDAO;
import com.example.ferreteria.modelo.dto.Categoria;
import com.example.ferreteria.modelo.dto.Oferta;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerCat, recyclerOfe;
    private AdaptadorCategorias catAdapter;
    private AdaptadorOfertas ofAdapter;
    private static final String TAG = "----HOME FRAGMENT";
    private Handler handler;
    private Timer timer;
    private int positionOfe = 0;
    private int directionOfe = 1;
    private LinearLayoutManager layoutManager;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView iniciado");
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.d(TAG, "RecyclerView CATEGORIAS");
        recyclerCat = root.findViewById(R.id.recyclerViewCategorias);
        recyclerCat.setLayoutManager(new LinearLayoutManager(getContext()));


        Log.d(TAG, "RecyclerView OFERTAS");
        recyclerOfe = root.findViewById(R.id.recyclerViewOferta);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerOfe.setLayoutManager(layoutManager);



        reciclerCat();
        reciclerOf();
        inicioAutoScroll();

        return root;
    }

    private void reciclerOf() {
        OfertaDAO ofertaDAO = new OfertaDAO(getContext());
        List<Oferta> ofertas = ofertaDAO.getListOfertas();
        ofAdapter = new AdaptadorOfertas(ofertas);
        recyclerOfe.setAdapter(ofAdapter);
        Log.d(TAG, "Adaptador de ofertas configurado con " + ofertas.size() + " ofertas");
        ofertaDAO.closeDB();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    //    Log.i(TAG, "HOME FRAGMENT");
    }

    private void reciclerCat() {
        CategoriaDAO categoriaDAO = new CategoriaDAO(getContext());
        List<Categoria> categorias = categoriaDAO.getListCat();
        catAdapter = new AdaptadorCategorias(categorias);
        recyclerCat.setAdapter(catAdapter);
        Log.d(TAG, "Adaptador del categorias configurado con " + categorias.size() + " categorías");
        categoriaDAO.closeDB();
    }

    private void inicioAutoScroll() {
        handler = new Handler();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (recyclerOfe != null) {
                            if (positionOfe >= ofAdapter.getItemCount() - 1) {
                                directionOfe = -1;
                            } else if (positionOfe <= 0) {
                                directionOfe = 1;
                            }
                            positionOfe += directionOfe;
                            recyclerOfe.smoothScrollToPosition(positionOfe);
                        }
                    }
                });
            }
        }, 10000, 10000);
    }
}
