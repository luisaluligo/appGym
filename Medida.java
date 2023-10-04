package com.luligosoft.appgym;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Medida {
    private String nombre;
    private String unidadMedida;
    private String valor;

    public Medida(String nombre, String unidadMedida, String valor) {
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.valor = valor;
    }

    // Getters para los atributos nombre, unidadMedida y valor

    public String getNombre() {
        return nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }


    public static String convertirListaAMedidasJson(List<Medida> medidas) {
        JSONArray jsonArray = new JSONArray();
        for (Medida medida : medidas) {
            try {
                JSONObject medidaJson = new JSONObject();
                medidaJson.put("nombre", medida.getNombre());
                medidaJson.put("unidadMedida", medida.getUnidadMedida());
                medidaJson.put("valor", medida.getValor());
                jsonArray.put(medidaJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public static ArrayList<Medida> obtenerListaDesdeJson(String json) {
        ArrayList<Medida> medidas = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Medida medida = fromJsonString(jsonObject.toString());
                if (medida != null) {
                    medidas.add(medida);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return medidas;
    }


    public static Medida fromJsonString(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String nombre = jsonObject.getString("nombre");
            String unidadMedida = jsonObject.getString("unidadMedida");
            String valor = jsonObject.getString("valor");

            return new Medida(nombre, unidadMedida, valor);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
