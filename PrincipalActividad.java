package com.luligosoft.appgym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

    public void Salir(View v) {
        // Se instancia el objeto tipo cuadro de diálogo
        AlertDialog.Builder DialogoSalida = new AlertDialog.Builder(PrincipalActividad.this);

        // Se configura el mensaje a mostrar
        DialogoSalida.setMessage("¿Qué desea realizar?, seleccione una opción:")
                .setCancelable(false) // No se permite que el usuario haga clic fuera del cuadro de diálogo (diálogo modal)
                .setPositiveButton("Volver a inicio ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Se finaliza la Activity
                        finish();
                    }
                })
                .setNegativeButton("Continuar en esta ventana", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Se cierra el cuadro de diálogo, y la aplicación sigue funcionando normal
                        dialogInterface.cancel();
                    }
                });

        // Se muestra el cuadro de diálogo
        AlertDialog DialogoEmergente = DialogoSalida.create();
        DialogoEmergente.setTitle("Diálogo emergente de verificación");
        DialogoEmergente.show();
    }



}

