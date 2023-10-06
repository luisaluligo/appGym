package com.luligosoft.appgym;

import android.annotation.SuppressLint;
import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.luligosoft.appgym.Alumno;
import com.luligosoft.appgym.Medida;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  SeguimientoAlumnos extends AppCompatActivity {
    private Alumno alumnoSeleccionado;
    private ArrayList<Medida> medidasActuales = new ArrayList<>();

    private EditText medidaEditText;
    private Spinner mesesSpinner;
    private Spinner spinnerMedidas;
    private Button agregarMedidaButton;
    private Button btnGrabarMed;
    private Button btnBuscarAlumno;
    private EditText edtTxtAlumno;
    private TextView UnMedidaEditText;
    private HashMap<String, ArrayList<Medida>> medidasPorMesHashMap = new HashMap<>();

    private ArrayList<Medida> medidasTemporales = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento_alumnos);

        medidaEditText = findViewById(R.id.ediTxtVlMed);
        UnMedidaEditText = findViewById(R.id.ediTxtUnMed);
        mesesSpinner = findViewById(R.id.spinnerMeses);
        spinnerMedidas= findViewById(R.id.spnMedidas);
        agregarMedidaButton = findViewById(R.id.buttonAgregarMedida);
        btnBuscarAlumno = findViewById(R.id.buttonBuscarAlumno);
        edtTxtAlumno = findViewById(R.id.editTextNombreAlumno);
        btnGrabarMed = findViewById(R.id.btnGrabar);


        cargarMedidasEnSpinner();
        configurarSpinner();


        // Configurar el spinner de meses (similar a como lo hiciste en RegistroAlumnosActivity)
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        ArrayAdapter<String> mesesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, meses);
        mesesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mesesSpinner.setAdapter(mesesAdapter);


    }

    private void configurarSpinner() {
        spinnerMedidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<Medida> medidasList = obtenerMedidasDesdeSharedPreferences();
                Medida medidaSeleccionada = obtenerMedidaSeleccionada();

                if (medidaSeleccionada != null) {
                    UnMedidaEditText.setText("UM: " + medidaSeleccionada.getUnidadMedida());
                    UnMedidaEditText.setVisibility(View.VISIBLE);
                    medidaEditText.setVisibility(View.VISIBLE);
                    agregarMedidaButton.setVisibility(View.VISIBLE);
                    spinnerMedidas.setVisibility(View.VISIBLE);
                } else {
                    UnMedidaEditText.setVisibility(View.GONE);
                    medidaEditText.setVisibility(View.GONE);
                    agregarMedidaButton.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
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

    public void buscarAlumno(View view) {
        EditText editTextNombreAlumno = findViewById(R.id.editTextNombreAlumno);
        String nombreAlumno = editTextNombreAlumno.getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", MODE_PRIVATE);
        String alumnosJson = sharedPreferences.getString("alumnos", "[]");

        try {
            JSONArray jsonArray = new JSONArray(alumnosJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject alumnoJson = jsonArray.getJSONObject(i);
                String nombre = alumnoJson.getString("nombre");

                if (nombre.equals(nombreAlumno)) {
                    // Alumno encontrado en SharedPreferences
                    alumnoSeleccionado = new Alumno();
                    alumnoSeleccionado.setNombre(nombre);

                    // Activar los campos para registrar medidas
                    inactivarCamposBusquedad(view);
                    activarCamposRegistroMedidas(view);
                    Toast.makeText(this, "Alumno encontrado: " + alumnoSeleccionado.getNombre(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void inactivarCamposBusquedad(View view) {
        btnBuscarAlumno.setVisibility(View.GONE);
        edtTxtAlumno.setEnabled(false);
    }


    private void activarCamposRegistroMedidas(View view) {
        spinnerMedidas.setVisibility(View.VISIBLE);
        medidaEditText.setVisibility(View.VISIBLE);
        mesesSpinner.setVisibility(View.VISIBLE);
        UnMedidaEditText.setVisibility(View.VISIBLE);
        agregarMedidaButton.setVisibility(View.VISIBLE);
        btnGrabarMed.setVisibility(View.VISIBLE);
    }

    public void guardarMedidasEnSharedPreferences(View view) {
        if (alumnoSeleccionado != null) {
            // Obtiene las medidas existentes del alumno desde SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("MiPref", MODE_PRIVATE);
            String alumnosJson = sharedPreferences.getString("alumnos", "[]");

            try {
                JSONArray jsonArray = new JSONArray(alumnosJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject alumnoJson = jsonArray.getJSONObject(i);
                    String nombre = alumnoJson.getString("nombre");

                    if (nombre.equals(alumnoSeleccionado.getNombre())) {
                        String mes = String.valueOf(mesesSpinner.getSelectedItemPosition());
                        JSONArray medidasJsonArrayMes;
                        boolean mesEncontrado = false;

                        JSONArray medidasPorMesJson = alumnoJson.getJSONArray("medidasPorMes");
                        for (int j = 0; j < medidasPorMesJson.length(); j++) {
                            JSONObject mesJson = medidasPorMesJson.getJSONObject(j);
                            String mesExistente = mesJson.getString("mes");
                            if (mesExistente.equals(mes)) {
                                // Obtiene las medidas existentes del mes
                                medidasJsonArrayMes = mesJson.getJSONArray("medidas");

                                // Combina las medidas existentes con las temporales (si las hay)
                                for (Medida medidaTemporal : medidasTemporales) {
                                    // Verifica si la medida ya existe en las medidas existentes
                                    boolean medidaExistente = false;
                                    for (int k = 0; k < medidasJsonArrayMes.length(); k++) {
                                        JSONObject medidaExistenteJson = medidasJsonArrayMes.getJSONObject(k);
                                        if (medidaExistenteJson.getString("nombre").equals(medidaTemporal.getNombre())) {
                                            medidaExistenteJson.put("valor", medidaTemporal.getValor());
                                            medidaExistenteJson.put("unidadMedida", medidaTemporal.getUnidadMedida());
                                            medidasJsonArrayMes.put(k, medidaExistenteJson);
                                            medidaExistente = true;
                                            break;
                                        }
                                    }

                                    if (!medidaExistente) {
                                        JSONObject nuevaMedidaJson = new JSONObject();
                                        nuevaMedidaJson.put("nombre", medidaTemporal.getNombre());
                                        nuevaMedidaJson.put("valor", medidaTemporal.getValor());
                                        nuevaMedidaJson.put("unidadMedida", medidaTemporal.getUnidadMedida());
                                        medidasJsonArrayMes.put(nuevaMedidaJson);
                                    }
                                }

                                mesJson.put("medidas", medidasJsonArrayMes);
                                mesEncontrado = true;
                                break;
                            }
                        }

                        if (!mesEncontrado) {
                            JSONObject nuevoMesJson = new JSONObject();
                            nuevoMesJson.put("mes", mes);

                            // Agregar las medidas temporales como medidas del nuevo mes
                            JSONArray medidasTemporalesJsonArray = new JSONArray();
                            for (Medida medidaTemporal : medidasTemporales) {
                                JSONObject nuevaMedidaJson = new JSONObject();
                                nuevaMedidaJson.put("nombre", medidaTemporal.getNombre());
                                nuevaMedidaJson.put("valor", medidaTemporal.getValor());
                                nuevaMedidaJson.put("unidadMedida", medidaTemporal.getUnidadMedida());
                                medidasTemporalesJsonArray.put(nuevaMedidaJson);
                            }

                            nuevoMesJson.put("medidas", medidasTemporalesJsonArray);
                            medidasPorMesJson.put(nuevoMesJson);
                        }

                        jsonArray.put(i, alumnoJson);
                        break;
                    }
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("alumnos", jsonArray.toString());
                editor.apply();

                Toast.makeText(this, "Seguimiento de Medidas guardadas correctamente", Toast.LENGTH_SHORT).show();
                finish();
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(this, "No hay medidas para guardar para el Seguimiento", Toast.LENGTH_SHORT).show();
    }


    private ArrayList<Medida> obtenerMedidasDesdeSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("medidasPreferences", Context.MODE_PRIVATE);
        String medidasJson = sharedPreferences.getString("medidas", "[]");
        return Medida.obtenerListaDesdeJson(medidasJson);
    }

    private Medida obtenerMedidaSeleccionada() {
        ArrayList<Medida> medidasList = obtenerMedidasDesdeSharedPreferences();
        int position = spinnerMedidas.getSelectedItemPosition();
        if (position >= 0 && position < medidasList.size()) {
            return medidasList.get(position);
        }
        return null;
    }

    public void agregarMedidaTemporal(View v) {
        Medida medidaSeleccionada = obtenerMedidaSeleccionada();

        if ((medidaSeleccionada != null) && !medidaEditText.getText().toString().isEmpty()) {
            medidaSeleccionada.setValor(medidaEditText.getText().toString());
            medidasTemporales.add(medidaSeleccionada);
            System.out.println(medidasTemporales.toArray());
            System.out.println(medidasTemporales.size());
            medidaEditText.setText("");
            Toast.makeText(SeguimientoAlumnos.this, "Medida agregada temporalmente", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(SeguimientoAlumnos.this, "Por favor, seleccione una medida y su respectivo valor", Toast.LENGTH_SHORT).show();
        }
    }
}
