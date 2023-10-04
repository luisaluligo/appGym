package com.luligosoft.appgym;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Map;

public class RegistroAlumnos extends AppCompatActivity {
    private ArrayList<Alumno> alumnos = new ArrayList<>();
    private EditText nombreAlumno;
    private EditText patologias;
    private EditText edadAlumno;
    private EditText fechaInscripcion;
    private EditText condicionesFisicas;
    private Button btnMedida;

    private Spinner spinnerMedidas;
    private TextView unidadMedidad;
    private EditText valorUnidadMedidad;

    private ArrayList<Medida> medidasTemporales = new ArrayList<>();
    private Spinner spinnerMeses;
    private ArrayAdapter<String> mesesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_alumnos);

        nombreAlumno = findViewById(R.id.txtNombre);
        edadAlumno = findViewById(R.id.editTextNumEdad);
        patologias = findViewById(R.id.txtPatologias);
        condicionesFisicas = findViewById(R.id.txtCondicionesFisicas);

        unidadMedidad = findViewById(R.id.textViewUnidadMedida);
        valorUnidadMedidad = findViewById(R.id.editTextValor);
        spinnerMedidas = findViewById(R.id.spinnerMedidas);
        btnMedida =findViewById(R.id.botonAgregarMedida);
        cargarMedidasEnSpinner();
        configurarSpinner();

        spinnerMeses = findViewById(R.id.spinnerMeses);
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        mesesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, meses);
        mesesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeses.setAdapter(mesesAdapter);
    }

    private Medida obtenerMedidaSeleccionada() {
        ArrayList<Medida> medidasList = obtenerMedidasDesdeSharedPreferences();
        int position = spinnerMedidas.getSelectedItemPosition();
        if (position >= 0 && position < medidasList.size()) {
            return medidasList.get(position);
        }
        return null;
    }

    private void cargarMedidasEnSpinner() {
        ArrayList<Medida> medidasList = obtenerMedidasDesdeSharedPreferences();
        System.out.println("medidas cargadas");
        System.out.println(medidasList);
        ArrayList<String> nombresMedidas = new ArrayList<>();
        for (Medida medida : medidasList) {
            nombresMedidas.add(medida.getNombre());
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresMedidas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedidas.setAdapter(adapter);
    }

    private void configurarSpinner() {
        spinnerMedidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<Medida> medidasList = obtenerMedidasDesdeSharedPreferences();
                Medida medidaSeleccionada = obtenerMedidaSeleccionada();

                if (medidaSeleccionada != null) {
                    unidadMedidad.setText("UM: " + medidaSeleccionada.getUnidadMedida());
                    unidadMedidad.setVisibility(View.VISIBLE);
                    valorUnidadMedidad.setVisibility(View.VISIBLE);
                    btnMedida.setVisibility(View.VISIBLE);
                } else {
                    unidadMedidad.setVisibility(View.GONE);
                    valorUnidadMedidad.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void guardarAlumnosEnSharedPreferences(ArrayList<Alumno> alumnos) {
        JSONArray jsonArray = new JSONArray();

        for (Alumno alumno : alumnos) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nombre", alumno.getNombre());
                jsonObject.put("edad", alumno.getEdad());
                jsonObject.put("condicionesFisicas", alumno.getCondicionesFisicas());
                jsonObject.put("patologias", alumno.getPatologias());

                Map<String, ArrayList<Medida>> medidasPorMes = alumno.obtenerTodasLasMedidas();
                JSONArray jsonArrayMedidas = new JSONArray();

                for (Map.Entry<String, ArrayList<Medida>> entry : medidasPorMes.entrySet()) {
                    JSONObject jsonMedida = new JSONObject();
                    jsonMedida.put("mes", entry.getKey());

                    JSONArray jsonArrayMedidasPorMes = new JSONArray();

                    for (Medida medida : entry.getValue()) {
                        JSONObject jsonMedidaItem = new JSONObject();
                        jsonMedidaItem.put("nombre", medida.getNombre());
                        jsonMedidaItem.put("unidadMedida", medida.getUnidadMedida());
                        jsonMedidaItem.put("valor", medida.getValor());
                        jsonArrayMedidasPorMes.put(jsonMedidaItem);
                    }

                    jsonMedida.put("medidas", jsonArrayMedidasPorMes);
                    jsonArrayMedidas.put(jsonMedida);
                }

                jsonObject.put("medidasPorMes", jsonArrayMedidas);
                jsonArray.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("alumnos", jsonArray.toString());
        editor.apply();
    }

    private ArrayList<Medida> obtenerMedidasDesdeSharedPreferences() {
        System.out.println("shared");
        SharedPreferences sharedPreferences = getSharedPreferences("medidasPreferences", Context.MODE_PRIVATE);
        System.out.println("shared");
        String medidasJson = sharedPreferences.getString("medidas", "[]");
        System.out.println(medidasJson);
        return Medida.obtenerListaDesdeJson(medidasJson);
    }

    public void registrarAlumno(View v) {
        String nombre = nombreAlumno.getText().toString();
        int edad = (edadAlumno.getText().toString().isEmpty()) ? 0 : Integer.parseInt(edadAlumno.getText().toString());
        String patologiasText = patologias.getText().toString();
        String condicionesFisicasText = condicionesFisicas.getText().toString();
        String mes = String.valueOf(spinnerMeses.getSelectedItemPosition() + 1);
        if ( medidasTemporales.isEmpty() ) {
            Toast.makeText(RegistroAlumnos.this, "Por favor, seleccione al menos una medida y digite su valor", Toast.LENGTH_SHORT).show();
        } else if (nombre.isEmpty() || edad <= 0 ||patologiasText.isEmpty()||condicionesFisicasText.isEmpty()) {
            Toast.makeText(RegistroAlumnos.this, "Todos los campos del formualario son obligatorios", Toast.LENGTH_SHORT).show();

        } else {
            Alumno alumno = new Alumno(nombre, edad, condicionesFisicasText, patologiasText);
            ArrayList<Medida> medidas = alumno.obtenerMedidasPorMes(mes);

            if (medidas == null) {
                medidas = new ArrayList<>();
            }


            if (!medidasTemporales.isEmpty()) {
                medidas.addAll(medidasTemporales);
                medidasTemporales.clear();
            }

            alumno.agregarMedidas(mes, medidas);
            alumnos.add(alumno);

            guardarAlumnosEnSharedPreferences(alumnos);
            Toast.makeText(RegistroAlumnos.this, "Alumno guardado en SharedPreferences", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    private void limpiarCampos() {
        unidadMedidad.setText("");
        valorUnidadMedidad.setText("");
    }


    public void agregarMedidaTemporal(View v) {
        Medida medidaSeleccionada = obtenerMedidaSeleccionada();

        if ((medidaSeleccionada != null) && !valorUnidadMedidad.getText().toString().isEmpty()) {
            medidaSeleccionada.setValor(valorUnidadMedidad.getText().toString());
            medidasTemporales.add(medidaSeleccionada);
            Toast.makeText(RegistroAlumnos.this, "Medida agregada temporalmente", Toast.LENGTH_SHORT).show();
            limpiarCampos();

        } else {

            Toast.makeText(RegistroAlumnos.this, "Por favor, seleccione una medida y su respectivo valor", Toast.LENGTH_SHORT).show();
        }
    }

    public void Salir(View v) {
        // Se instancia el objeto tipo cuadro de diálogo
        AlertDialog.Builder DialogoSalida = new AlertDialog.Builder(RegistroAlumnos.this);

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
                .setNegativeButton("Continuar Registrando el alumno", new DialogInterface.OnClickListener() {
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
