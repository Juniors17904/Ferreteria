package com.example.ferreteria;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ferreteria.ui.MenuActivity;

public class SplashActivity extends AppCompatActivity {
    private String TAG = "----SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        int splashTime = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Inicio de SplashActivity");

                Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        }, splashTime);





    }
}
