package com.example.ferreteria.ui.carrito;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CarritoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CarritoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Generar Pedido");
    }

    public LiveData<String> getText() {
        return mText;
    }
}