package com.example.listafiltrable_compose

import android.content.res.AssetManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listafiltrable_compose.ui.theme.ListaFiltrable_ComposeTheme

private const val DELIMITER = "|"
private const val CATEGORIA_TODAS = "Todas"
private const val ARCHIVO_EDIFICIOS = "edificios.txt"
private val FORMA_CAMPO = RoundedCornerShape(16.dp)

data class Edificio(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String
)

private fun parseEdificio(linea: String): Edificio? {
    if (linea.isBlank()) return null
    val partes = linea.split(DELIMITER)
    if (partes.size != 4) return null
    val id = partes[0].trim().toIntOrNull() ?: return null
    return Edificio(
        id = id,
        nombre = partes[1].trim(),
        categoria = partes[2].trim(),
        descripcion = partes[3].trim()
    )
}

fun cargarEdificios(assets: AssetManager): List<Edificio> {
    return assets.open(ARCHIVO_EDIFICIOS).bufferedReader().useLines { seq ->
        seq.mapNotNull(::parseEdificio).toList()
    }
}

fun iconoParaCategoria(categoria: String): ImageVector {
    return when (categoria) {
        "Académico" -> Icons.Filled.School
        "Servicios" -> Icons.Filled.Business
        "Deportes" -> Icons.Filled.SportsSoccer
        "Eventos" -> Icons.Filled.Event
        else -> Icons.Filled.Place
    }
}

@Composable
fun colorParaCategoria(categoria: String): Color {
    return when (categoria) {
        "Académico" -> Color(0xFFE3F2FD)
        "Servicios" -> Color(0xFFFFF3E0)
        "Deportes" -> Color(0xFFE8F5E9)
        "Eventos" -> Color(0xFFF3E5F5)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

@Composable
fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest
)

@Composable
fun TarjetaLugarIcono(
    nombre: String,
    descripcion: String,
    categoria: String,
    icono: ImageVector,
    colorFondo: Color
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = colorFondo,
        contentColor = Color(0xFF1C1B1F),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = categoria,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = categoria,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = LocalContentColor.current.copy(alpha = 0.7f)
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
fun TarjetaCompactaIcono(
    nombre: String,
    descripcion: String,
    icono: ImageVector,
    colorFondo: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorFondo)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = Color(0xFF1C1B1F).copy(alpha = 0.12f),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(120.dp)
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = nombre,
                color = Color(0xFF1C1B1F),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = descripcion,
                color = Color(0xFF1C1B1F),
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun FilaEdificio(edificio: Edificio, index: Int) {
    val icono = iconoParaCategoria(edificio.categoria)
    val colorFondo = colorParaCategoria(edificio.categoria)
    if (index % 2 == 0) {
        TarjetaLugarIcono(
            nombre = edificio.nombre,
            descripcion = edificio.descripcion,
            categoria = edificio.categoria,
            icono = icono,
            colorFondo = colorFondo
        )
    } else {
        TarjetaCompactaIcono(
            nombre = edificio.nombre,
            descripcion = edificio.descripcion,
            icono = icono,
            colorFondo = colorFondo
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEdificios() {
    val context = LocalContext.current
    val edificios = remember { cargarEdificios(context.assets) }
    var texto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf(CATEGORIA_TODAS) }
    var menuExpandido by remember { mutableStateOf(false) }
    val categorias = remember(edificios) {
        listOf(CATEGORIA_TODAS) + edificios.map { it.categoria }.distinct().sorted()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Buscar edificio") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = FORMA_CAMPO,
            colors = coloresCampo(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = it }
        ) {
            OutlinedTextField(
                value = categoriaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                leadingIcon = {
                    Icon(
                        iconoParaCategoria(categoriaSeleccionada),
                        contentDescription = null
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(menuExpandido) },
                shape = FORMA_CAMPO,
                colors = coloresCampo(),
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria) },
                        leadingIcon = {
                            Icon(
                                iconoParaCategoria(categoria),
                                contentDescription = null
                            )
                        },
                        onClick = {
                            categoriaSeleccionada = categoria
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val edificiosFiltrados = edificios.filter {
            (categoriaSeleccionada == CATEGORIA_TODAS || it.categoria == categoriaSeleccionada) &&
                it.nombre.contains(texto, ignoreCase = true)
        }

        if (edificiosFiltrados.isEmpty()) {
            Text(
                text = "Sin resultados para la búsqueda",
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(edificiosFiltrados, key = { _, e -> e.id }) { index, edificio ->
                    FilaEdificio(edificio, index)
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaFiltrable_ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ListaEdificios()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaEdificiosPreview() {
    ListaFiltrable_ComposeTheme {
        ListaEdificios()
    }
}
