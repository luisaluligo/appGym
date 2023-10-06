package com.luligosoft.appgym;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ConsultaAlumnos extends AppCompatActivity {

    private ArrayList<Alumno> alumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_alumnos);
        // Obtén la lista de alumnos desde SharedPreferences
        alumnos = obtenerAlumnosDesdeSharedPreferences();
        System.out.println("create");
        System.out.println(alumnos);
    }

    private ArrayList<Alumno> obtenerAlumnosDesdeSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", MODE_PRIVATE);
        String alumnosJson = sharedPreferences.getString("alumnos", "[]");
        System.out.println("JSON");
        System.out.println(alumnosJson);
        ArrayList<Alumno> listaAlumnos = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(alumnosJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject alumnoJson = jsonArray.getJSONObject(i);
                String nombre = alumnoJson.getString("nombre");
                int edad = alumnoJson.getInt("edad");
                String condicionesFisicas = alumnoJson.getString("condicionesFisicas");
                String patologias = alumnoJson.getString("patologias");

                // Obtener medidasPorMes del alumno
                JSONArray medidasJsonArray = alumnoJson.getJSONArray("medidasPorMes");
                Alumno alumno = new Alumno(nombre, edad, condicionesFisicas, patologias);

                for (int j = 0; j < medidasJsonArray.length(); j++) {
                    JSONObject mesJson = medidasJsonArray.getJSONObject(j);
                    String mes = mesJson.getString("mes");

                    JSONArray medidasJsonArrayMes = mesJson.getJSONArray("medidas");
                    ArrayList<Medida> medidas = new ArrayList<>();

                    for (int k = 0; k < medidasJsonArrayMes.length(); k++) {
                        JSONObject medidaJson = medidasJsonArrayMes.getJSONObject(k);
                        String nombreMedida = medidaJson.getString("nombre");
                        String unidadMedida = medidaJson.getString("unidadMedida");
                        String valor = medidaJson.getString("valor");

                        Medida medida = new Medida(nombreMedida, unidadMedida, valor);
                        medidas.add(medida);
                    }

                    alumno.agregarMedidas(mes, medidas);
                }

                listaAlumnos.add(alumno);
            }
            System.out.println("lista obtner");
            System.out.println(listaAlumnos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listaAlumnos;
    }

    public void buscarAlumno(View v) {
        EditText editTextNombreBusqueda = findViewById(R.id.editTextNombreBusqueda);
        String nombreBusqueda = editTextNombreBusqueda.getText().toString().trim();

        if (!nombreBusqueda.isEmpty()) {
            Alumno alumnoEncontrado = null;
            for (Alumno alumno : alumnos) {
                System.out.println(alumno.getNombre());
                if (alumno.getNombre().equals(nombreBusqueda)) {
                    alumnoEncontrado = alumno;
                    break;
                }
            }

            if (alumnoEncontrado != null) {
                // Muestra la información del alumno encontrado en TextViews
                TextView textViewNombre = findViewById(R.id.textViewNombre);
                TextView textViewEdad = findViewById(R.id.textViewEdad);
                TextView textViewCondFisicas = findViewById(R.id.textViewCondFisicas);
                TextView textViewPatologias = findViewById(R.id.textViewPatologias);
                LinearLayout linearLayoutMedidas = findViewById(R.id.linearLayoutMedidas);


                textViewNombre.setText("Nombre: " + alumnoEncontrado.getNombre());
                textViewEdad.setText("Edad: " + alumnoEncontrado.getEdad());
                textViewCondFisicas.setText("Condiciones Físicas: " + alumnoEncontrado.getCondicionesFisicas());
                textViewPatologias.setText("Patologías: " + alumnoEncontrado.getPatologias());

                Map<String, ArrayList<Medida>> medidasPorMes = alumnoEncontrado.obtenerTodasLasMedidas();
                String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

                for (Map.Entry<String, ArrayList<Medida>> entry : medidasPorMes.entrySet()) {
                    String mes = entry.getKey();
                    ArrayList<Medida> medidas = entry.getValue();
                    TextView textViewMes = new TextView(this);
                    textViewMes.setTextSize(20);
                    textViewMes.setText("Mes: " + meses[Integer.parseInt(mes)]);
                    linearLayoutMedidas.addView(textViewMes);

                    for (Medida medida : medidas) {
                        TextView textViewMedida = new TextView(this);
                        textViewMedida.setText("Medida: " + medida.getNombre() +
                                ", Unidad de Medida: " + medida.getUnidadMedida() +
                                ", Valor: " + medida.getValor());
                        textViewMedida.setTextSize(20);
                        linearLayoutMedidas.addView(textViewMedida);
                    }
                }

            } else {
                Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Por favor,ingrese un nombre para buscar", Toast.LENGTH_SHORT).show();
        }
    }
}
