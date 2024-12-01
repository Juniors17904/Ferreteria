package com.example.ferreteria;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class mtd {

    public static void cambiarColorBarraEstado(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }
    }

    public static void exito (View v, String mensaje){
        Snackbar.make(v, mensaje, Snackbar.LENGTH_LONG)
                .setBackgroundTint(v.getContext().getColor(R.color.azul)).show();

    }

    public static void Alerta(View v,String mensaje){
        Snackbar.make(v, mensaje, Snackbar.LENGTH_LONG)
                .setBackgroundTint(v.getContext().getColor(R.color.rojo)).show();
    }


    public static void ocultarTeclado(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity); // Vista por defecto si no hay foco actual
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void redirigirConDelay(final Activity activity, final Class<?> actividadDestino, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, actividadDestino);
                activity.startActivity(intent);
                activity.finish();
            }
        }, delayMillis); // retraso
    }





}
