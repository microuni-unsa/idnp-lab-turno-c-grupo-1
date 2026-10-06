package com.example.tarjetascampus_compose

// Paso 2: importaciones del ejercicio resuelto (Guía L6, pág. 3-4)
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarjetascampus_compose.theme.TarjetasCampusComposeTheme

// Paso 4: TarjetaLugar vacía (firma del primer estilo: franja horizontal con imagen circular)
@Composable
fun TarjetaLugar(
    nombre: String,
    descripcion: String,
    imagen: Painter,
    colorFondo: Color
) {
    // Paso 5: Surface con bordes redondeados + Row centrado con padding
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colorFondo
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            // Paso 6: imagen circular + espacio + Column con nombre y descripción
            // Orden Modifier (Fig. 2 guía): .size() antes de .clip() para recortar a 90dp
            Image(
                painter = imagen,
                contentDescription = nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = descripcion,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Paso 7: lista con al menos dos lugares del campus
@Composable
fun ListaTarjetasLugar() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TarjetaLugar(
            nombre = "Biblioteca Central",
            descripcion = "Zona de estudio y préstamo de libros",
            imagen = painterResource(id = R.drawable.biblioteca),
            colorFondo = Color(0xFFE3F2FD)
        )
        TarjetaLugar(
            nombre = "Comedor Universitario",
            descripcion = "Servicio de alimentación para estudiantes",
            imagen = painterResource(id = R.drawable.comedor),
            colorFondo = Color(0xFFFFF3E0)
        )
    }
}

// Paso 8: segundo estilo apilado con Box (superposición Fig. 1 guía)
@Composable
fun TarjetaCompacta(
    nombre: String,
    imagen: Painter
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = imagen,
            contentDescription = nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        // Paso 9: scrim semitransparente + texto abajo-izquierda (se agregan aquí)
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )
        Text(
            text = nombre,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        )
    }
}

// Paso 10: MainActivity muestra primero TarjetaLugar y luego dos TarjetaCompacta
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TarjetasCampusComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaTarjetas()
                }
            }
        }
    }
}

@Composable
fun PantallaTarjetas() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Estilo 1: tarjetas con Row (imagen circular + texto)
        ListaTarjetasLugar()
        Spacer(modifier = Modifier.height(8.dp))
        // Estilo 2: tarjetas apiladas con Box (imagen de fondo + scrim + texto)
        TarjetaCompacta(
            nombre = "Biblioteca Central",
            imagen = painterResource(id = R.drawable.biblioteca)
        )
        Spacer(modifier = Modifier.height(12.dp))
        TarjetaCompacta(
            nombre = "Comedor Universitario",
            imagen = painterResource(id = R.drawable.comedor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaTarjetasPreview() {
    TarjetasCampusComposeTheme {
        PantallaTarjetas()
    }
}
