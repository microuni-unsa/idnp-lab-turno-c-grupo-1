package com.example.campusnavigator_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campusnavigator_compose.theme.CampusNavigatorComposeTheme

data class Lugar(
    val nombre: String,
    val descripcion: String,
    val imagenRes: Int,
    val colorFondo: Color,
    val etiqueta: String
)

val lugaresCampus = listOf(
    Lugar(
        nombre = "Biblioteca Central",
        descripcion = "Zona de estudio y préstamo de libros",
        imagenRes = R.drawable.biblioteca,
        colorFondo = Color(0xFFE3F2FD),
        etiqueta = "Académico"
    ),
    Lugar(
        nombre = "Comedor Universitario",
        descripcion = "Servicio de alimentación para estudiantes",
        imagenRes = R.drawable.comedor,
        colorFondo = Color(0xFFFFF3E0),
        etiqueta = "Bienestar"
    ),
    Lugar(
        nombre = "Pabellón de Sistemas",
        descripcion = "Aulas y laboratorios especializados",
        imagenRes = R.drawable.pabellon_sistemas,
        colorFondo = Color(0xFFE8F5E9),
        etiqueta = "FIPS"
    ),
    Lugar(
        nombre = "Auditorio Central",
        descripcion = "Eventos académicos, conferencias y ceremonias",
        imagenRes = R.drawable.auditorio,
        colorFondo = Color(0xFFF3E5F5),
        etiqueta = "Cultural"
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusNavigatorComposeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var lugarSeleccionado by remember { mutableStateOf(lugaresCampus.first()) }

    NavHost(
        navController = navController,
        startDestination = "edificios"
    ) {
        composable("edificios") {
            EdificiosScreen(
                edificios = lugaresCampus,
                onLugarClick = { lugar ->
                    lugarSeleccionado = lugar
                    navController.navigate("detalle")
                }
            )
        }
        composable("detalle") {
            DetalleLugarScreen(
                nombre = lugarSeleccionado.nombre,
                descripcion = lugarSeleccionado.descripcion,
                imagen = painterResource(id = lugarSeleccionado.imagenRes),
                onVolver = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun TarjetaLugar(
    nombre: String,
    descripcion: String,
    imagen: Painter,
    colorFondo: Color,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colorFondo,
        contentColor = Color(0xFF1C1B1F),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
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

@Composable
fun TarjetaCompacta(
    nombre: String,
    imagen: Painter,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Image(
            painter = imagen,
            contentDescription = nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
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

@Composable
fun TarjetaConEtiqueta(
    nombre: String,
    descripcion: String,
    imagen: Painter,
    colorFondo: Color,
    etiqueta: String,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colorFondo,
        contentColor = Color(0xFF1C1B1F),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = imagen,
                contentDescription = nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descripcion,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = etiqueta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C1B1F)
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleLugarScreen(
    nombre: String,
    descripcion: String,
    imagen: Painter,
    onVolver: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFE3F2FD),
            contentColor = Color(0xFF1C1B1F),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = imagen,
                    contentDescription = nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = descripcion,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}

@Composable
fun EdificiosScreen(
    edificios: List<Lugar>,
    onLugarClick: (Lugar) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(edificios) { index, lugar ->
            when (index % 3) {
                0 -> TarjetaLugar(
                    nombre = lugar.nombre,
                    descripcion = lugar.descripcion,
                    imagen = painterResource(id = lugar.imagenRes),
                    colorFondo = lugar.colorFondo,
                    onClick = { onLugarClick(lugar) }
                )
                1 -> TarjetaCompacta(
                    nombre = lugar.nombre,
                    imagen = painterResource(id = lugar.imagenRes),
                    onClick = { onLugarClick(lugar) }
                )
                else -> TarjetaConEtiqueta(
                    nombre = lugar.nombre,
                    descripcion = lugar.descripcion,
                    imagen = painterResource(id = lugar.imagenRes),
                    colorFondo = lugar.colorFondo,
                    etiqueta = lugar.etiqueta,
                    onClick = { onLugarClick(lugar) }
                )
            }
        }
    }
}

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

@Composable
fun PantallaTarjetas() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ListaTarjetasLugar()
        Spacer(modifier = Modifier.height(8.dp))
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
    CampusNavigatorComposeTheme {
        PantallaTarjetas()
    }
}

@Preview(showBackground = true)
@Composable
fun EdificiosScreenPreview() {
    CampusNavigatorComposeTheme {
        EdificiosScreen(
            edificios = lugaresCampus,
            onLugarClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleLugarPreview() {
    CampusNavigatorComposeTheme {
        DetalleLugarScreen(
            nombre = "Biblioteca Central",
            descripcion = "Zona de estudio y préstamo de libros",
            imagen = painterResource(id = R.drawable.biblioteca)
        )
    }
}
