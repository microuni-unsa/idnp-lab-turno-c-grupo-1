package com.example.listafiltrable_yenaro

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listafiltrable_yenaro.theme.ListaFiltrableYenaroTheme
import org.json.JSONArray

// Paleta neobrutalista: colores planos brillantes + negro puro.
val NegroNeo = Color.Black
val FondoCrema = Color(0xFFFFFBEB)
val GrosorBorde = 3.dp
val DesplazamientoSombra = 6.dp
val FormaNeo = RoundedCornerShape(12.dp)

fun colorParaCategoria(categoria: String): Color {
  return when (categoria) {
    "Servicios" -> Color(0xFFFFDC58)
    "Académico" -> Color(0xFF88AAEE)
    "Deportes" -> Color(0xFFB6F09C)
    "Eventos" -> Color(0xFFFF90E8)
    else -> Color.White
  }
}

data class Edificio(
  val id: Int,
  val nombre: String,
  val categoria: String,
  val descripcion: String
)

fun cargarEdificios(context: Context): List<Edificio> {
  val json = context.assets.open("edificios.json").bufferedReader().use { it.readText() }
  val arr = JSONArray(json)
  return List(arr.length()) { i ->
    val o = arr.getJSONObject(i)
    Edificio(
      id = o.getInt("id"),
      nombre = o.getString("nombre"),
      categoria = o.getString("categoria"),
      descripcion = o.getString("descripcion")
    )
  }
}

fun iconoParaCategoria(categoria: String): ImageVector {
  return when (categoria) {
    "Servicios" -> Icons.Filled.Business
    "Académico" -> Icons.Filled.School
    "Deportes" -> Icons.Filled.Star
    "Eventos" -> Icons.Filled.Celebration
    else -> Icons.Filled.Place
  }
}

// Sombra dura neobrutalista: bloque negro sólido desplazado, sin difuminar.
@Composable
fun SombraDura(content: @Composable () -> Unit) {
  Box {
    Box(
      modifier = Modifier
        .matchParentSize()
        .offset(x = DesplazamientoSombra, y = DesplazamientoSombra)
        .background(NegroNeo, FormaNeo)
    )
    content()
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEdificios() {
  val context = LocalContext.current
  val edificios = remember { cargarEdificios(context) }
  var texto by remember { mutableStateOf("") }
  var categoria by remember { mutableStateOf("Todas") }
  var expandido by remember { mutableStateOf(false) }
  val categorias = remember { listOf("Todas") + edificios.map { it.categoria }.distinct() }
  val coloresCampo = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = NegroNeo,
    unfocusedBorderColor = NegroNeo,
    focusedLabelColor = NegroNeo,
    unfocusedLabelColor = NegroNeo,
    cursorColor = NegroNeo
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(FondoCrema)
      .padding(16.dp)
  ) {
    val etiquetaNeo: @Composable (String) -> Unit = { textoEtiqueta ->
      Text(
        textoEtiqueta,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .background(Color.White)
          .padding(horizontal = 4.dp)
      )
    }
    SombraDura {
      OutlinedTextField(
        value = texto,
        onValueChange = { texto = it },
        label = { etiquetaNeo("Buscar edificio") },
        shape = FormaNeo,
        colors = coloresCampo,
        modifier = Modifier.fillMaxWidth()
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    ExposedDropdownMenuBox(
      expanded = expandido,
      onExpandedChange = { expandido = !expandido }
    ) {
      SombraDura {
        OutlinedTextField(
          value = categoria,
          onValueChange = {},
          readOnly = true,
          label = { etiquetaNeo("Categoría") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
          shape = FormaNeo,
          colors = coloresCampo,
          modifier = Modifier.fillMaxWidth().menuAnchor()
        )
      }
      ExposedDropdownMenu(
        expanded = expandido,
        onDismissRequest = { expandido = false }
      ) {
        categorias.forEach { cat ->
          DropdownMenuItem(
            text = { Text(cat, fontWeight = FontWeight.Bold) },
            onClick = { categoria = cat; expandido = false }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    val edificiosFiltrados = edificios.filter {
      (categoria == "Todas" || it.categoria == categoria) &&
        it.nombre.contains(texto, ignoreCase = true)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(edificiosFiltrados, key = { it.id }) { edificio ->
        FilaEdificio(edificio)
      }
    }
  }
}

@Composable
fun FilaEdificio(edificio: Edificio) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp, horizontal = 2.dp)
  ) {
    SombraDura {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .border(BorderStroke(GrosorBorde, NegroNeo), FormaNeo)
          .background(colorParaCategoria(edificio.categoria), FormaNeo)
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .border(BorderStroke(2.dp, NegroNeo), RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(6.dp),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = iconoParaCategoria(edificio.categoria),
            contentDescription = edificio.categoria,
            tint = NegroNeo,
            modifier = Modifier.fillMaxSize()
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = edificio.nombre,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 19.sp,
            color = NegroNeo
          )
          Text(
            text = edificio.categoria.uppercase(),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = NegroNeo
          )
          Text(
            text = edificio.descripcion,
            fontSize = 14.sp,
            color = NegroNeo
          )
        }
      }
    }
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ListaFiltrableYenaroTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = FondoCrema
        ) {
          ListaEdificios()
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ListaEdificiosPreview() {
  ListaFiltrableYenaroTheme {
    // Preview sin assets: datos de ejemplo
    val ejemplo = listOf(
      Edificio(1, "Biblioteca Central", "Servicios", "Préstamo de libros y salas de estudio")
    )
    LazyColumn(
      modifier = Modifier.background(FondoCrema)
    ) {
      items(ejemplo, key = { it.id }) { FilaEdificio(it) }
    }
  }
}
