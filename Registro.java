package com.luligosoft.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {

    private EditText usuario;
    private EditText usuario_nombre;
    private EditText clave;
    private EditText clave_verificacion;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // se capturan los objetos de la interfaz grafica
        usuario = findViewById(R.id.editTextUsu);
        usuario_nombre = findViewById(R.id.editTextUsuNom);
        clave = findViewById(R.id.editTextClave);
        clave_verificacion = findViewById(R.id.editTextClave2);

    }

    public boolean validaRegistro(View v) {
        String usuarioLogin = usuario.getText().toString();
        String nombreCompleto = usuario_nombre.getText().toString();
        String contrasena = clave.getText().toString();
        String contrasenaVerificacion = clave_verificacion.getText().toString();

        if (usuarioLogin.isEmpty() || nombreCompleto.isEmpty() || contrasena.isEmpty() || contrasenaVerificacion.isEmpty()) {
            Toast.makeText(this, "Error: Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!contrasena.equals(contrasenaVerificacion)) {
            Toast.makeText(this, "Error: Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return false;
        }

        SharedPreferences shp = getSharedPreferences("usersbd", Context.MODE_PRIVATE);
        String usuariosJson = shp.getString("usuarios", "[]");

        try {
            JSONArray usuariosArray = new JSONArray(usuariosJson);

            // Validar que el nombre de usuario no exista
            for (int i = 0; i < usuariosArray.length(); i++) {
                JSONObject usuarioExistente = usuariosArray.getJSONObject(i);
                String nombreUsuarioExistente = usuarioExistente.getString("usuario");
                if (nombreUsuarioExistente.equals(usuarioLogin)) {
                    Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }


    public void Registrar(View v){

       Boolean valido= validaRegistro(v);

       if(valido){

           SharedPreferences shp = getSharedPreferences("usersbd", Context.MODE_PRIVATE);
           SharedPreferences.Editor edt = shp.edit();
           String usuariosJson = shp.getString("usuarios", "[]");

           try {
               JSONArray usuariosArray = new JSONArray(usuariosJson);
               JSONObject nuevoUsuario = new JSONObject();
               nuevoUsuario.put("usuario", usuario.getText().toString());
               nuevoUsuario.put("usuarioNombre", usuario_nombre.getText().toString());
               nuevoUsuario.put("clave", clave.getText().toString());

               usuariosArray.put(nuevoUsuario);

               edt.putString("usuarios", usuariosArray.toString());
               edt.apply();

               Toast.makeText(this, "Usuario creado exitosamente !!", Toast.LENGTH_LONG).show();

               Intent ActivityPrin = new Intent(this, MainActivity.class);
               startActivity(ActivityPrin);

           } catch (JSONException e) {
               e.printStackTrace();
           }

       }


    }


}