package com.luligosoft.appgym;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Alumno {
    private String nombre ;
    private int edad;
    private String condicionesFisicas;
    private String patologias;
    private Map<String, ArrayList<Medida>> medidasPorMes;


    public Alumno(String nombre, int edad, String condicionesFisicas,String patologias){
        this.nombre =nombre;
        this.edad = edad;
        this.condicionesFisicas  = condicionesFisicas ;
        this.patologias = patologias;
        this.medidasPorMes =  new HashMap<>();
    }

    public Alumno() {
        medidasPorMes = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }


    public int getEdad() {
        return edad;
    }



    public String getCondicionesFisicas() {
        return condicionesFisicas;
    }

    public void setCondicionesFisicas(String condicionesFisicas) {
        this.condicionesFisicas = condicionesFisicas;
    }

    public String getPatologias() {
        return patologias;
    }

    public void setPatologias(String patologias) {
        this.patologias = patologias;
    }


    public void agregarMedidas(String mes, ArrayList<Medida> medidas) {
        this.medidasPorMes.put(mes, medidas);
    }

    public ArrayList<Medida> obtenerMedidasPorMes(String mes) {
        return medidasPorMes.get(mes);
    }

    public Map<String, ArrayList<Medida>> obtenerTodasLasMedidas() {
        return medidasPorMes;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //logica
}

