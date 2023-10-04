package com.luligosoft.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PrincipalActividad extends AppCompatActivity {

    private TextView usuario;
    private String usuarioName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        usuario = findViewById(R.id.txtNameUsu);
        usuarioName = getIntent().getStringExtra("nombreUsuario");
        usuario.setText("Bienvenido "+usuarioName);
    }



    public void InvocaRegistroAlumno(View v){
        // se declara cual es la pantalla que see quiere invocar
        Intent iniActParametros = new Intent(this, RegistroAlumnos.class);
        startActivity(iniActParametros);



    }

    public void InvocaConsultaAlumno(View v){
        Intent iniActParametros = new Intent(this, ConsultaAlumnos.class);
        // Se invoca la Activity (pantalla) destino
        startActivity(iniActParametros);
    }


    public void InvocaSeguimientoAlumno(View v){
        Intent iniActParametros = new Intent(this, SeguimientoAlumnos.class);
        startActivity(iniActParametros);
    }

    public void InvocaMedidasAlumno(View v){
        Intent iniActParametros = new Intent(this, MedidasAlumno.class);
        startActivity(iniActParametros);
    }

}

