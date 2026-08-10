package com.espol.grupo03.logica;

import com.espol.grupo03.modelo.Tablero;

public class AnalisisJugada {

    private Tablero tablero;
    private int utilidadMinima;
    private boolean elegida;

    public AnalisisJugada(
            Tablero tablero,
            int utilidadMinima,
            boolean elegida) {

        this.tablero = tablero;
        this.utilidadMinima = utilidadMinima;
        this.elegida = elegida;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getUtilidadMinima() {
        return utilidadMinima;
    }

    public boolean isElegida() {
        return elegida;
    }
    public void setElegida(boolean elegida) {
        this.elegida = elegida;
    }
}
