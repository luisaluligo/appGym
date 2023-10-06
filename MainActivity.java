package com.luligosoft.appgym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private TextView labelSpinner;
    private EditText passwordEditText;
    private Spinner userSpinner;
    private List<String> usersList;
    private SharedPreferences sharedPreferences;

    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("usersbd", Context.MODE_PRIVATE);

        labelSpinner = findViewById(R.id.textViewLabel);
        passwordEditText = findViewById(R.id.editTextPass);
        userSpinner = findViewById(R.id.spn_usu);
        buttonLogin =findViewById(R.id.btnlogin);

        // Inicializa el Spinner y configura los elementos
        configurarSpinner();


    }

    public void InvocaRegistroUsuario(View v){
        Intent iniActParametros = new Intent(this, Registro.class);
        startActivity(iniActParametros);
    }


    private ArrayList<String> obtenerUsuarios() {
        ArrayList<String> Usuarios = new ArrayList<>();
        SharedPreferences shp = getSharedPreferences("usersbd", Context.MODE_PRIVATE);
        String usuariosJson = shp.getString("usuarios", "[]");

        try {
            JSONArray usuariosArray = new JSONArray(usuariosJson);
            if(usuariosArray.length() > 0){
                Usuarios.add("Seleccione uno");
            }
            for (int i = 0; i < usuariosArray.length(); i++) {
                JSONObject usuario = usuariosArray.getJSONObject(i);
                Usuarios.add(usuario.getString("usuario"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Usuarios;
    }


    private JSONObject obtenerUsuario(String usuario) {
        JSONObject userObject = null;
        String usuariosJson = sharedPreferences.getString("usuarios", "[]");

        try {
            JSONArray usuariosArray = new JSONArray(usuariosJson);
            for (int i = 0; i < usuariosArray.length(); i++) {
                JSONObject usuarioSelected = usuariosArray.getJSONObject(i);
                if (usuarioSelected.getString("usuario").equals(usuario)) {
                    userObject = usuarioSelected;
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userObject;
    }

    private void configurarSpinner() {
        ArrayList<String> nombresUsuarios = obtenerUsuarios();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresUsuarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spn_usu);
        spinner.setAdapter(adapter);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Cuando se selecciona un usuario diferente a "Seleccione uno", muestra el campo de contraseña
                if (position >0 ) {
                    selectedUser = nombresUsuarios.get(position);
                    labelSpinner.setVisibility(View.VISIBLE);
                    userSpinner.setVisibility(View.VISIBLE);
                    passwordEditText.setVisibility(View.VISIBLE);
                    buttonLogin.setVisibility(View.VISIBLE);
                } else if(position == 0 ){
                    selectedUser = null;
                    labelSpinner.setVisibility(View.VISIBLE);
                    userSpinner.setVisibility(View.VISIBLE);
                }else {
                    selectedUser = null;
                    passwordEditText.setVisibility(View.GONE);
                    labelSpinner.setVisibility(View.GONE);
                    userSpinner.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void Ingresar(View v) {
        String claveDigitada = passwordEditText.getText().toString();
        JSONObject userObject = obtenerUsuario(selectedUser);

        if (userObject != null) {
            String claveSP = userObject.optString("clave", "");
            String nombreUsuario = userObject.optString("usuarioNombre", "");

            if (claveDigitada.equals(claveSP)) {
                Intent intent = new Intent(this, PrincipalActividad.class);
                intent.putExtra("nombreUsuario", nombreUsuario);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error, usuario o clave incorrecta", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error, usuario no encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

    public Button getButtonLogin() {
        return buttonLogin;
    }




}