package com.espol.grupo03.logica;

import com.espol.grupo03.modelo.Tablero;

//Clase creada analizar lo que el humano hace jiji
public class AnalisisRespuesta {

    private Tablero tablero;
    private int utilidad;

    public AnalisisRespuesta(Tablero tablero, int utilidad) {

        this.tablero = tablero;
        this.utilidad = utilidad;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getUtilidad() {
        return utilidad;
    }
}
