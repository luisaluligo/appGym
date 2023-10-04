package com.luligosoft.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class PantallaSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Se invoca la Activity en memoria
                // 1er parámetro el nombre de la clase para la Actitivy del Splash
                // 2do parámetro la siguiente Activity que se mostrará después del Splash
                Intent ActivitySplash = new Intent(PantallaSplash.this, MainActivity.class);
                startActivity(ActivitySplash);
                finish();
            }
        }, 3000); // La pantalla Splash se mostrará por 5 segundos
    }
}