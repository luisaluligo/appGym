package com.luligosoft.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MedidasAlumno extends AppCompatActivity {
    private EditText editTextNombre, editTextUnidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidas_alumno);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextUnidad = findViewById(R.id.editTextUnidad);

    }

    public void grabar(View v){
        if (validarDatos()) {
            String nombre = editTextNombre.getText().toString().trim();
            String unidad = editTextUnidad.getText().toString().trim();

            Medida nuevaMedida = new Medida(nombre, unidad, "0");
            guardarMedidaEnSharedPreferences(nuevaMedida);
            Toast.makeText(MedidasAlumno.this, "Medida guardada con éxito", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            Toast.makeText(MedidasAlumno.this, "Por favor, ingrese todos los datos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarDatos() {
        String nombre = editTextNombre.getText().toString().trim();
        String unidad = editTextUnidad.getText().toString().trim();

        return !nombre.isEmpty() && !unidad.isEmpty() ;
    }

    private void guardarMedidaEnSharedPreferences(Medida medida) {
        SharedPreferences sharedPreferences = getSharedPreferences("medidasPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ArrayList<Medida> medidasList = obtenerMedidasGuardadas();
        medidasList.add(medida);
        System.out.println(medidasList);
        String medidasJson = Medida.convertirListaAMedidasJson(medidasList);
        System.out.println(medidasJson);
        editor.putString("medidas", medidasJson);
        editor.apply();
    }


    private ArrayList<Medida> obtenerMedidasGuardadas() {
        SharedPreferences sharedPreferences = getSharedPreferences("medidasPreferences", Context.MODE_PRIVATE);
        String medidasJson = sharedPreferences.getString("medidas", "[]");
        return Medida.obtenerListaDesdeJson(medidasJson);
    }

}