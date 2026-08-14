package com.espol.grupo03.logica;
import com.espol.grupo03.modelo.*;
import com.espol.grupo03.estructuras.*;
import java.util.ArrayList;
import java.util.Random;
public class Minimax {
    //Aqui guardaremos el analisis de las jugadas
    //que hizo la computadora en su ultimo turno
    private ArrayList<AnalisisJugada> analisisUltimaJugada = new ArrayList<>();
    //Crearemos un metodo que podamos analizar el minimax,
    //estado desde el cual la computadora empieza a pensar
    public int[] obtenerMejorMovimiento(Tablero tableroActual, char simboloComputadora, char simboloOponente) {
        //Limpiamos el analisis anterior para guardar
        //solamente lo que se analice en este turno
        analisisUltimaJugada.clear();
        //validamos si esta lleno el tablero o si gano uno de los dos
        if (tableroActual.isFull() || tableroActual.isWinner(simboloOponente) || tableroActual.isWinner(simboloComputadora)) {

            return null;
        }

        //Creamos el arbol desde el tablero actual
        Tree<Tablero> arbolEstados = new Tree<>(tableroActual);

        //Generamos los posibles estados de acuerdo a la computadora
        ArrayList<Tablero> estados = tableroActual.generateStates(simboloComputadora);

        /*
        estados nos devolverá las posibilidades de cada tablero.
        Es decir, que a partir de la jugada podriamos generar n cantidad
        de movimientos posibles, donde n es un numero que va a depender
        netamente de cuan lleno este el tablero.
        */
        for (Tablero estado : estados) {

            Tree<Tablero> hijo = new Tree<>(estado);

            //Este arbol tendrá las posibilidades
            //de la configuracion del tablero
            arbolEstados.getRoot().addChild(hijo);
        }


        //Recorreremos cada posible jugada que generó la computadora
        for (Tree<Tablero> hijo : arbolEstados.getRoot().getChildren()) {

            //Sacamos el tablero que está guardado dentro de ese hijo
            Tablero tableroHijo = hijo.getRoot().getContent();


            //Si la computadora ya ganó con esta jugada,
            //no tiene sentido generar una respuesta del oponente
            if (tableroHijo.isWinner(simboloComputadora)) {
                continue;
            }


            //Si el tablero quedó lleno, es empate
            //y ya no se puede generar otra respuesta
            if (tableroHijo.isFull()) {
                continue;
            }


            /*
            Aqui generamos un arreglo que respondera segun
            lo que hay en ese tablero hijo.

            Algo como:
            si la computadora hiciera esto,
            ¿que podría hacer el jugador despues?
            */
            ArrayList<Tablero> respuestasOponente = tableroHijo.generateStates(simboloOponente);

            //Recorreremos cada respuesta posible
            for (Tablero respuesta : respuestasOponente) {

                //Conectamos eso debajo de la jugada de la computadora
                Tree<Tablero> nieto = new Tree<>(respuesta);

                hijo.getRoot().addChild(nieto);
            }
        }


        //Guardaremos el mejor valor encontrado
        //entre todas las utilidades minimas
        int utilidadMaxima = Integer.MIN_VALUE;

        //Esta lista nos ayudará a guardar los arboles
        //cuyos contenidos son las mejores jugadas encontradas
        ArrayList<Tree<Tablero>> mejoresJugadas = new ArrayList<>();

        //Recorremos cada hijo,
        //es decir cada jugada posible de la computadora
        for (Tree<Tablero> hijo : arbolEstados.getRoot().getChildren()) {

            Tablero tableroHijo = hijo.getRoot().getContent();


            /*
            Si el hijo da la victoria de la compu,
            devolvemos la posicion donde se produjo esa victoria.

            No tiene sentido seguir analizando las respuestas
            porque la partida ya termino.
            */
            if (tableroHijo.isWinner(simboloComputadora)) {

                //Calculamos la utilidad del tablero ganador
                //para poder guardar tambien este resultado en el analisis
                int utilidadVictoria = tableroHijo.calculateUtility(simboloComputadora);

                //Como esta jugada hace ganar inmediatamente
                //a la computadora, la marcamos como elegida
                AnalisisJugada analisisGanador = new AnalisisJugada(tableroHijo, utilidadVictoria, true);

                analisisUltimaJugada.add(analisisGanador);

                return obtenerMovimiento(tableroActual, tableroHijo);
            }


            int utilidadMinima;

            //Aqui vamos a guardar las respuestas q se analizaron para la posible jugada de la computadora
            ArrayList<AnalisisRespuesta> respuestasAnalizadas = new ArrayList<>();


            /*
            Si el hijo es hoja puede ser porque el tablero
            quedo lleno y no existen respuestas del oponente.

            En ese caso evaluamos directamente ese tablero.
            */
            if (hijo.isLeaf()) {

                utilidadMinima = tableroHijo.calculateUtility(simboloComputadora);

            } else {

                /*
                Si no es hoja, buscamos la utilidad minima
                dentro de todas las respuestas posibles
                que podría realizar el oponente.
                */
                utilidadMinima = Integer.MAX_VALUE;

                //Servirá para saber si alguna de las respuestas
                //hace ganar al humano
                boolean hayDerrotaInmediata = false;


                for (Tree<Tablero> respuesta : hijo.getRoot().getChildren()) {

                    Tablero tableroRespuesta = respuesta.getRoot().getContent();

                    //Calculamos la utilidad de esta R. que puede hacer el oponente
                    int utilidad = tableroRespuesta.calculateUtility(simboloComputadora);

                    //Guardamos el tablero de la respuesta con la utilidad
                    AnalisisRespuesta analisisRespuesta = new AnalisisRespuesta(tableroRespuesta, utilidad);

                    respuestasAnalizadas.add(analisisRespuesta);

                    /*
                    si esta respuesta hace ganar al humano, esto se marca como derrota inmediata

                    */
                    if (tableroRespuesta.isWinner(simboloOponente)) {
                        hayDerrotaInmediata = true;
                    } else {
                        if (utilidad < utilidadMinima) {
                            utilidadMinima = utilidad;
                        }
                    }
                }
                /*Si por lo menos una respuesta permite que el humano
                gana inmediatamente, la jugada de la compu se considera
                derrota inmediata
                 */
                if (hayDerrotaInmediata) {
                    utilidadMinima = Integer.MIN_VALUE;
                }
            }
                //Guardamos el tablero que representa esta posible jugada
                //junto con la utilidad minima que obtuvo su familia
                AnalisisJugada analisis = new AnalisisJugada(tableroHijo, utilidadMinima, false);

                /*Vamos a agg las respuestas que pertenecen a esa jugada de la computadora

                 */

                for (AnalisisRespuesta respuestaAnalizada : respuestasAnalizadas) {
                    analisis.addRespuesta(respuestaAnalizada);

                }

                analisisUltimaJugada.add(analisis);

            /*
            Ahora buscamos el MAX entre todas
            las utilidades minimas.

            Si encontramos una utilidad minima
            mejor que la maxima anterior,
            esta pasa a ser nuestra nueva mejor jugada.
            */
                if (utilidadMinima > utilidadMaxima) {

                    utilidadMaxima = utilidadMinima;

                    //Las anteriores dejan de ser las mejores
                    mejoresJugadas.clear();

                    mejoresJugadas.add(hijo);

                } else if (utilidadMinima == utilidadMaxima) {

                /*
                Si tiene exactamente la misma utilidad maxima,
                tambien la guardamos porque las dos
                son consideradas igualmente buenas por Minimax.
                */
                    mejoresJugadas.add(hijo);
                }
            }


        /*
        Si varias jugadas tienen la misma utilidad maxima,
        escogemos una de ellas aleatoriamente.

        Importante:
        Random no se encargará de escoger cualquier casilla libre del tablero.
        Solo escoge entre las jugadas que Minimax ya considero
        como las mejores.
        */
            Random random = new Random();

            int posicionAleatoria = random.nextInt(mejoresJugadas.size());


            Tree<Tablero> jugadaElegida = mejoresJugadas.get(posicionAleatoria);


            Tablero estadoElegido = jugadaElegida.getRoot().getContent();

            //Recorremos los analisis para encontrar
            //cual fue la jugada que finalmente escogio la computadora
            for (AnalisisJugada analisis : analisisUltimaJugada) {

                if (analisis.getTablero() == estadoElegido) {

                    analisis.setElegida(true);
                    break;
                }
            }

            //Finalmente obtenemos la fila y columna
            //que cambiaron respecto al tablero original
            return obtenerMovimiento(tableroActual, estadoElegido);

    }



    //Compararemos los tableros con el original
    //para identificar que posicion seria la nueva jugada
    private int[] obtenerMovimiento(Tablero original, Tablero estado) {

        //Recorremos cada fila del tablero
        for (int fila = 0; fila < Tablero.tamanio; fila++) {

            //Recorremos cada columna
            for (int columna = 0; columna < Tablero.tamanio; columna++) {

                /*
                Si encontramos una casilla diferente,
                esa es precisamente la posicion
                donde se produjo la nueva jugada.
                */
                if (original.obtenerCasilla(fila, columna) != estado.obtenerCasilla(fila, columna)) {

                    return new int[]{fila, columna};
                }
            }
        }

        return null;
    }
    //Nos permitirá obtener el analisis que hizo
    //la computadora en su ultimo movimiento
    public ArrayList<AnalisisJugada> getAnalisisUltimaJugada() {

        return analisisUltimaJugada;
    }
}