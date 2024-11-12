package com.example.ferreteria.ui.pedidos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.ferreteria.databinding.FragmentGalleryBinding;

public class PedidosFragment extends Fragment {

private FragmentGalleryBinding binding;
    private String TAG = "----PEDIDOS";
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
              PedidosViewModel galleryViewModel =
                new ViewModelProvider(this).get(PedidosViewModel.class);
            Log.i(TAG, "-------");
              binding = FragmentGalleryBinding.inflate(inflater, container, false);
             View root = binding.getRoot();

             final TextView textView = binding.textGallery;
              galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
              return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}