
package com.espol.grupo03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espol.grupo03.logica.Partida
import com.espol.grupo03.modelo.Tablero
import com.espol.grupo03.ui.theme.Grupo_03Theme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private val FondoAplicacionColor = Color(0xFF202124)
private val FondoSeccion = Color(0xFF292A2D)

private val AzulX = Color(0xFF60A5FA)
private val RojoO = Color(0xFFF87171)

private val VerdePrincipal = Color(0xFF4CAF6A)

private val CasillaLibre = Color(0xFF202124)
private val CasillaDeshabilitada = Color(0xFF202124)
private val BordeCasilla = Color(0xFF6B6B6B)

private val TextoPrincipal = Color(0xFFF9FAFB)
private val TextoSecundario = Color(0xFFB8B8B8)


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            Grupo_03Theme {

                AplicacionTresEnRaya()
            }
        }
    }
}


@Composable
fun AplicacionTresEnRaya() {

    var partida by remember {
        mutableStateOf<Partida?>(null)
    }

    if (partida == null) {

        PantallaInicio(
            onComenzar = { simboloHumano, iniciaHumano ->

                val simboloComputadora =
                    if (simboloHumano == 'X') {
                        'O'
                    } else {
                        'X'
                    }

                val nuevaPartida = Partida(simboloHumano, simboloComputadora, iniciaHumano)

                if (!iniciaHumano) {

                    nuevaPartida.jugarComputadora()
                }

                partida = nuevaPartida
            }
        )

    } else {

        PantallaJuego(partida = partida!!, onNuevaPartida = {
            partida = null
        }
        )
    }
}


@Composable
fun PantallaInicio(
    onComenzar: (Char, Boolean) -> Unit
) {

    var simboloSeleccionado by remember {
        mutableStateOf('X')
    }

    var iniciaHumano by remember {
        mutableStateOf(true)
    }

    FondoAplicacion {

        Column(
            modifier = Modifier.fillMaxSize().padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Tres en raya",
                color = TextoPrincipal,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Grupo 03",
                color = TextoSecundario,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Elige tu símbolo",
                color = TextoPrincipal,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                OpcionSimbolo(
                    simbolo = 'X',
                    seleccionado = simboloSeleccionado == 'X',
                    colorSimbolo = AzulX,
                    onClick = { simboloSeleccionado = 'X' }
                )

                Spacer(modifier = Modifier.width(12.dp))

                OpcionSimbolo(
                    simbolo = 'O',
                    seleccionado = simboloSeleccionado == 'O',
                    colorSimbolo = RojoO,
                    onClick = { simboloSeleccionado = 'O' }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "¿Quién comienza?",
                color = TextoPrincipal,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OpcionInicio(
                    texto = "Yo",
                    seleccionado = iniciaHumano,
                    modifier = Modifier.weight(1f),
                    onClick = { iniciaHumano = true }
                )

                OpcionInicio(
                    texto = "Computadora",
                    seleccionado = !iniciaHumano,
                    modifier = Modifier.weight(1f),
                    onClick = { iniciaHumano = false }
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            Button(
                modifier = Modifier
                    .width(140.dp)
                    .height(45.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color(0xFF06220F)
                ),
                onClick = { onComenzar(simboloSeleccionado, iniciaHumano) }
            ) {
                Text(text = "Jugar")
            }
        }
    }
}


@Composable
fun OpcionSimbolo(
    simbolo: Char,
    seleccionado: Boolean,
    colorSimbolo: Color,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier.size(78.dp).clickable { onClick() }
            .then(if (seleccionado) {

                Modifier.border(width = 2.dp, color = colorSimbolo, shape = RoundedCornerShape(4.dp))

            } else {
                Modifier
            }
            ),

        shape = RoundedCornerShape(4.dp),

        color =
            if (seleccionado) {
                Color(0xFF333438)
            } else {
                Color(0xFF242528)
            }
    ) {

        Box(contentAlignment = Alignment.Center) {

            Text(text = simbolo.toString(), color = colorSimbolo, fontSize = 40.sp, fontWeight = FontWeight.Bold)
        }
    }
}


@Composable
fun OpcionInicio(
    texto: String,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Surface(
        modifier = modifier.height(46.dp).clickable { onClick() },

        shape = RoundedCornerShape(4.dp),

        color =
            if (seleccionado) {
                Color(0xFF3A3B3F)
            } else {
                Color(0xFF242528)
            }
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = texto,

                color =
                    if (seleccionado) {
                        TextoPrincipal
                    } else {
                        TextoSecundario
                    },

                fontWeight =
                    if (seleccionado) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },

                textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
fun PantallaJuego(
    partida: Partida,
    onNuevaPartida: () -> Unit
) {

    var versionTablero by remember {
        mutableStateOf(0)
    }

    //Controla si mostramos la ventana del resultado
    var mostrarResultado by remember {
        mutableStateOf(false)
    }

    //Controla si mostramos el analisis que hizo la computadora
    var mostrarAnalisis by remember {
        mutableStateOf(false)
    }

    //Vamos a guardar recomendaciones
    var recomendacionHumano by remember {
        mutableStateOf<IntArray?>(null)
    }
    var scope = rememberCoroutineScope()


    FondoAplicacion {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "TRES EN RAYA",
                color = TextoPrincipal,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )


            Text(
                text = "Grupo 03 • Estructura de Datos",
                color = TextoSecundario,
                fontSize = 14.sp
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            EstadoPartida(
                partida = partida,
                version = versionTablero
            )


            Spacer(
                modifier = Modifier.height(25.dp)
            )


            TableroGrafico(
                partida = partida,
                version = versionTablero,
                recomendacionHumano = recomendacionHumano,

                onCasillaClick = { fila, columna ->

                    //Cuando el humano juega,
                    //quitamos la recomendacion anterior
                    recomendacionHumano = null

                    val jugadaHumano =
                        partida.jugarHumano(
                            fila,
                            columna
                        )


                    if (jugadaHumano) {

                        versionTablero++


                        //Revisamos si el humano termino la partida
                        if (partida.partidaTerminada()) {

                            mostrarResultado = true

                        } else {

                            //Si no termino, esperamos un poco
                            //antes de que juegue la computadora
                            scope.launch {

                                delay(850)

                                partida.jugarComputadora()

                                versionTablero++

                                //Revisamos si la computadora termino la partida
                                if (partida.partidaTerminada()) {
                                    mostrarResultado = true
                                }
                            }
                        }
                    }
                }
            )


            Spacer(
                modifier = Modifier.height(28.dp)
            )


            //Boton para recomendar una jugada al humano
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                shape = RoundedCornerShape(4.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdePrincipal,
                    contentColor = Color(0xFF102516)
                ),

                enabled =
                    partida.getTurnoActual() ==
                            partida.getSimboloHumano()
                            &&
                            !partida.partidaTerminada(),

                onClick = {

                    recomendacionHumano =
                        partida.recomendarJugadaHumano()
                }
            ) {

                Text(
                    text = "RECOMENDAR JUGADA",
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            //Boton para mostrar el analisis realizado por Minimax
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                shape = RoundedCornerShape(4.dp),

                enabled =
                    partida
                        .getAnalisisUltimaJugada()
                        .isNotEmpty(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A3B3F),
                    contentColor = TextoPrincipal
                ),

                onClick = {

                    mostrarAnalisis = true
                }
            ) {

                Text(
                    text = "VER ANÁLISIS",
                    fontWeight = FontWeight.Bold
                )
            }


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            //Boton para iniciar una nueva partida
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                shape = RoundedCornerShape(4.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A3B3F),
                    contentColor = TextoPrincipal
                ),

                onClick = onNuevaPartida
            ) {

                Text(
                    text = "NUEVA PARTIDA",
                    fontWeight = FontWeight.Bold
                )
            }

        } //Cierra Column

    } //Cierra FondoAplicacion


    //Ventana que aparece cuando termina la partida
    if (mostrarResultado) {

        val ganador =
            partida.obtenerGanador()


        val tituloResultado =
            if (partida.esEmpate()) {

                "¡EMPATE!"

            } else {

                "¡GANÓ $ganador!"
            }


        val mensajeResultado =
            if (partida.esEmpate()) {

                "La partida terminó sin ganador."

            } else {

                "El ganador de la partida es $ganador."
            }


        AlertDialog(

            onDismissRequest = {

                mostrarResultado = false
            },

            title = {

                Text(
                    text = tituloResultado,
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Text(
                    text = mensajeResultado
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        mostrarResultado = false

                        onNuevaPartida()
                    }
                ) {

                    Text("Nueva Partida")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        mostrarResultado = false
                    }
                ) {

                    Text("Ver Tablero")
                }
            }
        )
    }


    //Ventana donde mostraremos el analisis realizado
    //por la computadora durante su ultimo turno
    if (mostrarAnalisis) {

        val analisis =
            partida.getAnalisisUltimaJugada()


        AlertDialog(

            onDismissRequest = {

                mostrarAnalisis = false
            },

            title = {

                Text(
                    text = "ANÁLISIS DE LA COMPUTADORA",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                if (analisis.isNotEmpty()) {

                    Column(
                        modifier = Modifier
                            .heightIn(max = 500.dp)
                            .verticalScroll(
                                rememberScrollState()
                            ),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text =
                                "La computadora analizó " +
                                        analisis.size +
                                        " posibles jugadas."
                        )


                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )


                        //Recorremos todas las jugadas
                        //que analizo la computadora
                        analisis.forEachIndexed { indice, jugada ->


                            Text(
                                text = "Jugada ${indice + 1}",
                                fontWeight = FontWeight.Bold
                            )


                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )


                            //Mostramos el tablero que podria
                            //generar la computadora
                            MiniTablero(
                                tablero =
                                    jugada.getTablero()
                            )


                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )


                            //Este es el MIN encontrado entre
                            //las respuestas de esta familia
                            if (
                                jugada.getUtilidadMinima()
                                == Integer.MIN_VALUE
                            ) {

                                Text(
                                    text =
                                        "Resultado: ⚠ Derrota posible",

                                    fontWeight =
                                        FontWeight.SemiBold,

                                    color =
                                        Color(0xFFFACC15)
                                )

                            } else {

                                Text(
                                    text =
                                        "Utilidad mínima: " +
                                                jugada.getUtilidadMinima(),

                                    fontWeight =
                                        FontWeight.SemiBold
                                )
                            }


                            //Si Minimax escogio esta jugada
                            //la marcamos en la pantalla
                            if (jugada.isElegida()) {

                                Spacer(
                                    modifier =
                                        Modifier.height(6.dp)
                                )

                                Text(
                                    text =
                                        "✓ Jugada Elegida",

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        VerdePrincipal
                                )
                            }


                            Spacer(
                                modifier =
                                    Modifier.height(18.dp)
                            )


                            /*
                            Debajo de cada jugada de la computadora
                            mostraremos las respuestas que podria
                            hacer el humano.
                            */
                            if (
                                jugada
                                    .getRespuestas()
                                    .isNotEmpty()
                            ) {

                                Text(
                                    text =
                                        "Respuestas Del Humano",

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        TextoSecundario
                                )


                                Spacer(
                                    modifier =
                                        Modifier.height(12.dp)
                                )


                                //Recorremos las respuestas
                                //que pertenecen a esta familia
                                jugada
                                    .getRespuestas()
                                    .forEachIndexed {
                                            indiceRespuesta,
                                            respuesta ->


                                        Text(
                                            text =
                                                "Respuesta " +
                                                        "${indiceRespuesta + 1}",

                                            fontWeight =
                                                FontWeight.SemiBold
                                        )


                                        Spacer(
                                            modifier =
                                                Modifier.height(8.dp)
                                        )


                                        //Mostramos el tablero generado
                                        //por esa respuesta del humano
                                        MiniTablero(
                                            tablero =
                                                respuesta.getTablero()
                                        )


                                        Spacer(
                                            modifier =
                                                Modifier.height(8.dp)
                                        )


                                        //Mostramos la utilidad individual
                                        //de esta respuesta
                                        Text(
                                            text =
                                                "Utilidad: " +
                                                        respuesta.getUtilidad()
                                        )


                                        //Si esta respuesta hace ganar
                                        //inmediatamente al humano
                                        if (
                                            respuesta
                                                .getTablero()
                                                .isWinner(
                                                    partida.getSimboloHumano()
                                                )
                                        ) {

                                            Spacer(
                                                modifier =
                                                    Modifier.height(6.dp)
                                            )

                                            Text(
                                                text =
                                                    "Victoria del Humano DDX",

                                                color =
                                                    RojoO,

                                                fontWeight =
                                                    FontWeight.Bold
                                            )
                                        }


                                        Spacer(
                                            modifier =
                                                Modifier.height(18.dp)
                                        )
                                    }

                            } else {

                                /*
                                Puede ocurrir que no existan respuestas
                                porque la computadora gano directamente
                                o porque el tablero termino.
                                */
                                Text(
                                    text =
                                        "No existen respuestas posteriores.",

                                    color =
                                        TextoSecundario
                                )
                            }


                            //Separamos una familia de la siguiente
                            Spacer(
                                modifier =
                                    Modifier.height(32.dp)
                            )
                        }
                    }
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        mostrarAnalisis = false
                    }
                ) {

                    Text("Cerrar" +
                            "")
                }
            }
        )
    }
}

@Composable
fun EstadoPartida(
    partida: Partida,
    version: Int
) {

    key(version) {

        Card(
            shape = RoundedCornerShape(4.dp),

            colors = CardDefaults.cardColors(
                containerColor = FondoSeccion
            )
        ) {

            Box(
                modifier = Modifier.fillMaxWidth().padding(
                    horizontal = 22.dp,
                    vertical = 16.dp
                ),

                contentAlignment = Alignment.Center
            ) {

                when {

                    partida.esEmpate() -> {

                        Text(
                            text = "EMPATE",
                            color = TextoPrincipal,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    partida.obtenerGanador() != Tablero.vacio -> {

                        val ganador =
                            partida.obtenerGanador()

                        Text(
                            text = "GANADOR: $ganador",

                            color =
                                if (ganador == 'X') {
                                    AzulX
                                } else {
                                    RojoO
                                },

                            fontSize = 22.sp,

                            fontWeight = FontWeight.Bold
                        )
                    }


                    else -> {

                        val turno = partida.getTurnoActual()

                        Text(text = "Turno: $turno",

                            color =
                                if (turno == 'X') {
                                    AzulX
                                } else {
                                    RojoO
                                },

                            fontSize = 21.sp,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun TableroGrafico(
    partida: Partida,
    version: Int,
    recomendacionHumano: IntArray?,
    onCasillaClick: (Int, Int) -> Unit
) {

    key(version, recomendacionHumano) {

        Column(
            modifier = Modifier
                .size(300.dp)
                .background(BordeCasilla),

            verticalArrangement =
                Arrangement.spacedBy(5.dp)
        ) {

            for (fila in 0 until Tablero.tamanio) {

                Row(
                    modifier = Modifier.weight(1f),

                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    for (columna in 0 until Tablero.tamanio) {

                        val contenido =
                            partida.getTablero().obtenerCasilla(fila, columna)


                        val habilitada =
                            contenido == Tablero.vacio && !partida.partidaTerminada()


                        //Revisamos si esta es la casilla
                        //que Minimax recomienda al humano
                        val esRecomendada =
                            recomendacionHumano != null &&
                                    recomendacionHumano[0] == fila &&
                                    recomendacionHumano[1] == columna


                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clickable(
                                    enabled = habilitada
                                ) {

                                    onCasillaClick(
                                        fila,
                                        columna
                                    )
                                },

                            shape =
                                RoundedCornerShape(0.dp),

                            color =
                                if (esRecomendada) {
                                    //Color de la recomendacion
                                    Color(0xFF315E3B)

                                } else if (habilitada) {

                                    CasillaLibre

                                } else {

                                    CasillaDeshabilitada
                                }
                        ) {

                            Box(
                                contentAlignment =
                                    Alignment.Center
                            ) {

                                if (contenido != Tablero.vacio) {

                                    Text(
                                        text =
                                            contenido.toString(),

                                        color =
                                            if (contenido == 'X') {
                                                AzulX
                                            } else {
                                                RojoO
                                            },

                                        fontSize = 58.sp,

                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MiniTablero(
    tablero: Tablero
) {

    Column(modifier = Modifier.size(150.dp),

        verticalArrangement = Arrangement.spacedBy(0.dp)) {

        for (fila in 0 until Tablero.tamanio) {

            Row(modifier = Modifier.weight(1f),

                horizontalArrangement = Arrangement.spacedBy(0.dp)) {

                for (columna in 0 until Tablero.tamanio) {

                    val contenido = tablero.obtenerCasilla(fila, columna)


                    Surface(modifier = Modifier.weight(1f).aspectRatio(1f).border(width = 1.dp, color = BordeCasilla,
                        shape = RoundedCornerShape(0.dp)),

                        shape = RoundedCornerShape(0.dp),

                        color = CasillaLibre) {

                        Box(contentAlignment = Alignment.Center) {

                            if (contenido != Tablero.vacio) {

                                Text(text = contenido.toString(),

                                    color =
                                        if (contenido == 'X') {
                                            AzulX
                                        } else {
                                            RojoO
                                        },

                                    fontSize = 25.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FondoAplicacion(
    contenido: @Composable () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAplicacionColor)
    ) {

        contenido()
    }
}
