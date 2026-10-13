package com.example.planointeractivo_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planointeractivo_compose.ui.theme.PlanoInteractivo_ComposeTheme

enum class Pantalla(val etiqueta: String) {
    Edificios("Edificios"),
    Mapa("Mapa")
}

data class LugarCampus(
    val nombre: String,
    val descripcion: String,
    val tienePlano: Boolean = false
)

val listaLugares = listOf(
    LugarCampus(
        nombre = "Pabellón de Sistemas",
        descripcion = "Aulas y laboratorios especializados FIPS",
        tienePlano = true
    ),
    LugarCampus(
        nombre = "Biblioteca Central",
        descripcion = "Zona de estudio y préstamo de libros"
    ),
    LugarCampus(
        nombre = "Comedor Universitario",
        descripcion = "Servicio de alimentación para estudiantes"
    ),
    LugarCampus(
        nombre = "Auditorio Central",
        descripcion = "Eventos académicos y conferencias"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanoInteractivo_ComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var pantallaActual by remember { mutableStateOf(Pantalla.Edificios) }
    val planoViewModel: PlanoViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = pantallaActual == Pantalla.Edificios,
                    onClick = { pantallaActual = Pantalla.Edificios },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Edificios") },
                    label = { Text("Edificios") }
                )
                NavigationBarItem(
                    selected = pantallaActual == Pantalla.Mapa,
                    onClick = { pantallaActual = Pantalla.Mapa },
                    icon = { Icon(Icons.Default.Place, contentDescription = "Mapa") },
                    label = { Text("Mapa") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (pantallaActual) {
                Pantalla.Edificios -> {
                    EdificiosScreen(
                        onVerPlano = { pantallaActual = Pantalla.Mapa }
                    )
                }
                Pantalla.Mapa -> {
                    PlanoAmbiente(viewModel = planoViewModel)
                }
            }
        }
    }
}

@Composable
fun EdificiosScreen(onVerPlano: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(listaLugares) { lugar ->
            val esSistemas = lugar.tienePlano

            val cardColors = if (esSistemas) {
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = cardColors,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = lugar.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (esSistemas) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = lugar.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (esSistemas) {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            }
                        )
                    }
                    if (lugar.tienePlano) {
                        Button(
                            onClick = onVerPlano,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Ver plano")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanoAmbiente(viewModel: PlanoViewModel = viewModel()) {
    val marcadores by viewModel.marcadores.collectAsState()
    val marcadorSeleccionado by viewModel.marcadorSeleccionado.collectAsState()
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Plano - Pabellón de Sistemas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Toca un marcador rojo para ver información",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val padding = 16f
                        val gap = 16f
                        val roomWidth = (size.width - (2 * padding) - gap) / 2f
                        val roomHeight = (size.height - (2 * padding) - gap) / 2f

                        val centros = listOf(
                            Offset(padding + roomWidth / 2f, padding + roomHeight / 2f),
                            Offset(padding + roomWidth + gap + roomWidth / 2f, padding + roomHeight / 2f),
                            Offset(padding + roomWidth / 2f, padding + roomHeight + gap + roomHeight / 2f),
                            Offset(padding + roomWidth + gap + roomWidth / 2f, padding + roomHeight + gap + roomHeight / 2f)
                        )
                        val radioMarcador = 32f

                        viewModel.seleccionarMarcadorEn(offset, centros, radioMarcador)
                    }
                }
        ) {
            val padding = 16f
            val gap = 16f
            val roomWidth = (size.width - (2 * padding) - gap) / 2f
            val roomHeight = (size.height - (2 * padding) - gap) / 2f

            drawRect(
                color = Color(0xFF90CAF9),
                topLeft = Offset(padding, padding),
                size = Size(roomWidth, roomHeight)
            )
            drawRect(
                color = Color(0xFF37474F),
                topLeft = Offset(padding, padding),
                size = Size(roomWidth, roomHeight),
                style = Stroke(width = 3f)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "Lab 101",
                topLeft = Offset(padding + 16f, padding + 16f),
                style = TextStyle(
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            drawRect(
                color = Color(0xFFA5D6A7),
                topLeft = Offset(padding + roomWidth + gap, padding),
                size = Size(roomWidth, roomHeight)
            )
            drawRect(
                color = Color(0xFF37474F),
                topLeft = Offset(padding + roomWidth + gap, padding),
                size = Size(roomWidth, roomHeight),
                style = Stroke(width = 3f)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "Lab 102",
                topLeft = Offset(padding + roomWidth + gap + 16f, padding + 16f),
                style = TextStyle(
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            drawRect(
                color = Color(0xFFFFCC80),
                topLeft = Offset(padding, padding + roomHeight + gap),
                size = Size(roomWidth, roomHeight)
            )
            drawRect(
                color = Color(0xFF37474F),
                topLeft = Offset(padding, padding + roomHeight + gap),
                size = Size(roomWidth, roomHeight),
                style = Stroke(width = 3f)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "Aula 103",
                topLeft = Offset(padding + 16f, padding + roomHeight + gap + 16f),
                style = TextStyle(
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            drawRect(
                color = Color(0xFFCE93D8),
                topLeft = Offset(padding + roomWidth + gap, padding + roomHeight + gap),
                size = Size(roomWidth, roomHeight)
            )
            drawRect(
                color = Color(0xFF37474F),
                topLeft = Offset(padding + roomWidth + gap, padding + roomHeight + gap),
                size = Size(roomWidth, roomHeight),
                style = Stroke(width = 3f)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "Patio Central",
                topLeft = Offset(padding + roomWidth + gap + 16f, padding + roomHeight + gap + 16f),
                style = TextStyle(
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            drawLine(
                color = Color(0xFF263238),
                start = Offset(padding + roomWidth + gap / 2f, padding),
                end = Offset(padding + roomWidth + gap / 2f, size.height - padding),
                strokeWidth = 4f
            )
            drawLine(
                color = Color(0xFF263238),
                start = Offset(padding, padding + roomHeight + gap / 2f),
                end = Offset(size.width - padding, padding + roomHeight + gap / 2f),
                strokeWidth = 4f
            )

            val centros = listOf(
                Offset(padding + roomWidth / 2f, padding + roomHeight / 2f),
                Offset(padding + roomWidth + gap + roomWidth / 2f, padding + roomHeight / 2f),
                Offset(padding + roomWidth / 2f, padding + roomHeight + gap + roomHeight / 2f),
                Offset(padding + roomWidth + gap + roomWidth / 2f, padding + roomHeight + gap + roomHeight / 2f)
            )
            val radioMarcador = 32f

            centros.forEach { centro ->
                drawCircle(
                    color = Color(0xFFD32F2F),
                    radius = radioMarcador,
                    center = centro
                )
                drawCircle(
                    color = Color.White,
                    radius = radioMarcador * 0.45f,
                    center = centro
                )
            }
        }

        marcadorSeleccionado?.let { marcador ->
            AlertDialog(
                onDismissRequest = { viewModel.cerrarDialogo() },
                title = {
                    Text(
                        text = marcador.titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = marcador.descripcion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.cerrarDialogo() }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PlanoInteractivo_ComposeTheme {
        MainScreen()
    }
}
