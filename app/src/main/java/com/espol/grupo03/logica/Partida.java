package com.espol.grupo03.logica;
import com.espol.grupo03.modelo.Tablero;
import java.util.ArrayList;
public class Partida {

    private Tablero tablero; //tablero real de la partida
    private final Minimax minimax;  //La partida tendrá un Minimax cuando llegue el turno de la compu
    //Las siguientes cosas que creamos es debido a
    //que se pueds ver con que jugar, ya sea X O o O X
    private final char simboloHumano;
    private final char simboloComputadora;

    // Variable para representar a quien le toca jugar
    private char turnoActual;

    public Partida(char simboloHumano, char simboloComputadora, boolean iniciaHumano){
        this.tablero = new Tablero(); //ahora la partida tendrá ese tablero
        this.minimax = new Minimax();

        this.simboloHumano = simboloHumano;
        this.simboloComputadora = simboloComputadora;

        //Validacion para ver quien inicia.
        if(iniciaHumano){
            this.turnoActual = simboloHumano;
        }else{
            this.turnoActual = simboloComputadora;
        }
    }
    public Tablero getTablero(){
        return tablero;
    }
    public char getTurnoActual(){
        return turnoActual;
    }
    public boolean jugarHumano(int fila, int columna){

        //solo continuar si no es el turno del humano
        if(turnoActual != simboloHumano){
            return false;
        }

        //validamos si la partida ya acabo
        if(tablero.isFull()||tablero.isWinner(simboloHumano)||tablero.isWinner(simboloComputadora)){
            return false;
        }
        //Intentamos colocar el simbolo del humano

        if(!tablero.colocarSimbolo(fila, columna, simboloHumano)){
            return false;
        }
        //SI la partida sigue, pasa el turno a la computadora

        if(!tablero.isFull()&& !tablero.isWinner(simboloHumano)){
            turnoActual = simboloComputadora;
        }
        return true;
    }
    public boolean jugarComputadora(){

        //SI no le toca al simbolo que le toco a la computadora, no jugamos
        if(turnoActual!= simboloComputadora){
            return false;
        }

        //No podemos seguir jugando si ya se acabo el juego por empate o victoria de uno de los dos
        if(tablero.isFull()||tablero.isWinner(simboloHumano)||tablero.isWinner(simboloComputadora)){
            return false;
        }

        //Minimax busca la mejor posicion para la computadora
        int[] movimiento = minimax.obtenerMejorMovimiento(tablero, simboloComputadora, simboloHumano);

        //Si no hay mov disponible, no hacemos nd pues
        if(movimiento ==null){
            return false;
        }

        //colocamos el simbolo de la computadora.
        tablero.colocarSimbolo(movimiento[0],movimiento[1],simboloComputadora);

        //si la partida continua, regresamos al humano
        if(!tablero.isFull()&& !tablero.isWinner(simboloComputadora)){
            turnoActual = simboloHumano;
        }
        return true;



    }

    public boolean partidaTerminada(){
        return tablero.isFull()||tablero.isWinner(simboloHumano)||tablero.isWinner(simboloComputadora);

    }
    public char obtenerGanador(){

        if(tablero.isWinner(simboloHumano)){
            return simboloHumano;
        }
        if(tablero.isWinner(simboloComputadora)){
            return simboloComputadora;
        }
        return Tablero.vacio;
    }
    public boolean esEmpate(){
        return tablero.isFull()&&!tablero.isWinner(simboloHumano)&&!tablero.isWinner(simboloComputadora);
    }
    //Metodo para recomendar jugada
    public int[] recomendarJugadaHumano(){
        //solo recomendaremos si le toca al humano o la partirda ya terminó
        if(turnoActual!=simboloHumano||partidaTerminada()){
            return null;
        }
        /*Creamos un minimax adicional para esta recomendacion
        no queremos afectar el minimax de la computadora
         */
        Minimax minimaxRecomendacion=  new Minimax();
        /*COnsideramos al humano como jugador que busca maximizar su utilidad
        por eso invertimos los simbolos respecto al minimax normal de la computadora

         */
        return minimaxRecomendacion.obtenerMejorMovimiento(tablero,simboloHumano,simboloComputadora);

    };
    //Nos permitirá obtener desde la partida
    //el ultimo analisis realizado por la computadora
    public ArrayList<AnalisisJugada> getAnalisisUltimaJugada() {

        return minimax.getAnalisisUltimaJugada();
    }
    public char getSimboloHumano(){
        return simboloHumano;
    }





}

