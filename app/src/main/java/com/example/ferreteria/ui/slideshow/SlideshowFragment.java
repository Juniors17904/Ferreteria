package com.example.ferreteria.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.ferreteria.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

private FragmentSlideshowBinding binding;
private String TAG = "----SLIDESSHOW";
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        Log.i(TAG, "-------");
                   binding = FragmentSlideshowBinding.inflate(inflater, container, false);
                     View root = binding.getRoot();

                   final TextView textView = binding.textSlideshow;
                     slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
                      return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}