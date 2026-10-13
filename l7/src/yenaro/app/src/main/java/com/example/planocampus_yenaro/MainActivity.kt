package com.example.planocampus_yenaro

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planocampus_yenaro.theme.PlanoCampusYenaroTheme
import org.json.JSONArray

data class Ambiente(
  val nombre: String,
  val left: Float,
  val top: Float,
  val right: Float,
  val bottom: Float,
  val color: Color
)

private val ambientes = listOf(
  Ambiente("Lab. Cómputo 1", 0f, 0f, 0.5f, 0.44f, Color(0xFFBBDEFB)),
  Ambiente("Lab. Cómputo 2", 0.5f, 0f, 1f, 0.44f, Color(0xFFC8E6C9)),
  Ambiente("Aula 201", 0f, 0.44f, 0.5f, 1f, Color(0xFFFFF9C4)),
  Ambiente("Patio Central", 0.5f, 0.44f, 1f, 1f, Color(0xFFFFE0B2))
)

fun cargarMarcadores(context: Context): List<MarcadorPlano> {
  val json = context.assets.open("marcadores.json").bufferedReader().use { it.readText() }
  val array = JSONArray(json)
  return List(array.length()) { i ->
    val o = array.getJSONObject(i)
    MarcadorPlano(
      fx = o.getDouble("fx").toFloat(),
      fy = o.getDouble("fy").toFloat(),
      radioDp = o.getDouble("radioDp").toFloat(),
      titulo = o.getString("titulo"),
      descripcion = o.getString("descripcion")
    )
  }
}

class PlanoViewModelFactory(
  private val marcadores: List<MarcadorPlano>
) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return PlanoViewModel(marcadores) as T
  }
}

@Composable
fun PlanoEdificioScreen(
  viewModel: PlanoViewModel = viewModel(),
  modifier: Modifier = Modifier
) {
  val marcadores by viewModel.marcadores.collectAsState()
  val seleccionado by viewModel.seleccionado.collectAsState()

  Column(modifier = modifier.padding(16.dp)) {
    Text("Pabellón de Sistemas", style = MaterialTheme.typography.headlineSmall)
    Text(
      "Toca un marcador para ver su información",
      style = MaterialTheme.typography.bodyMedium
    )
    Spacer(Modifier.height(8.dp))
    Canvas(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(min = 420.dp)
        .weight(1f, fill = false)
        .height(460.dp)
        .pointerInput(marcadores) {
          detectTapGestures { tap ->
            marcadores.forEach { marcador ->
              val center = Offset(marcador.fx * size.width, marcador.fy * size.height)
              if ((tap - center).getDistance() <= marcador.radioDp * density) {
                viewModel.seleccionar(marcador)
              }
            }
          }
        }
    ) {
      // Muros exteriores
      drawRect(color = Color.DarkGray, size = size)
      val inset = 4.dp.toPx()
      drawRect(
        color = Color(0xFFF5F5F5),
        topLeft = Offset(inset, inset),
        size = Size(size.width - 2 * inset, size.height - 2 * inset)
      )
      // Ambientes
      ambientes.forEach { ambiente ->
        val tl = Offset(
          inset + ambiente.left * (size.width - 2 * inset),
          inset + ambiente.top * (size.height - 2 * inset)
        )
        val rectSize = Size(
          (ambiente.right - ambiente.left) * (size.width - 2 * inset),
          (ambiente.bottom - ambiente.top) * (size.height - 2 * inset)
        )
        drawRect(color = ambiente.color, topLeft = tl, size = rectSize)
      }
      // Paredes divisorias
      val wall = Color.DarkGray
      val w = 4.dp.toPx()
      drawLine(
        wall,
        Offset(size.width / 2f, inset),
        Offset(size.width / 2f, size.height - inset),
        strokeWidth = w
      )
      drawLine(
        wall,
        Offset(inset, size.height * 0.44f),
        Offset(size.width - inset, size.height * 0.44f),
        strokeWidth = w
      )
      // Etiquetas de cada ambiente
      drawContext.canvas.nativeCanvas.apply {
        val paint = Paint().apply {
          color = android.graphics.Color.DKGRAY
          textAlign = Paint.Align.CENTER
          textSize = 13.sp.toPx()
          isFakeBoldText = true
        }
        ambientes.forEach { ambiente ->
          val cx = (inset + ambiente.left * (size.width - 2 * inset) +
            inset + ambiente.right * (size.width - 2 * inset)) / 2f
          val top = inset + ambiente.top * (size.height - 2 * inset)
          drawText(ambiente.nombre, cx, top + 20.sp.toPx(), paint)
        }
      }
      // Marcadores
      marcadores.forEachIndexed { index, marcador ->
        val center = Offset(marcador.fx * size.width, marcador.fy * size.height)
        val radio = marcador.radioDp * density
        val color = listOf(Color.Red, Color(0xFF1565C0), Color(0xFF2E7D32), Color(0xFFE65100))[index % 4]
        drawCircle(color = Color.White, radius = radio + 3.dp.toPx(), center = center)
        drawCircle(color = color, radius = radio, center = center)
      }
    }
  }

  seleccionado?.let { marcador ->
    AlertDialog(
      onDismissRequest = { viewModel.limpiarSeleccion() },
      title = { Text(marcador.titulo) },
      text = { Text(marcador.descripcion) },
      confirmButton = {
        TextButton(onClick = { viewModel.limpiarSeleccion() }) {
          Text("Cerrar")
        }
      }
    )
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val marcadores = cargarMarcadores(this)
    setContent {
      PlanoCampusYenaroTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          val vm: PlanoViewModel = viewModel(
            factory = PlanoViewModelFactory(marcadores)
          )
          PlanoEdificioScreen(viewModel = vm)
        }
      }
    }
  }
}

private val marcadoresPreview = listOf(
  MarcadorPlano(0.25f, 0.22f, 18f, "Lab. Cómputo 1", "Sala de cómputo 1"),
  MarcadorPlano(0.75f, 0.22f, 18f, "Lab. Cómputo 2", "Sala de cómputo 2"),
  MarcadorPlano(0.25f, 0.72f, 18f, "Aula 201", "Aula 201"),
  MarcadorPlano(0.75f, 0.72f, 18f, "Patio Central", "Patio central")
)

@Preview(showBackground = true)
@Composable
fun PlanoEdificioPreview() {
  PlanoCampusYenaroTheme {
    PlanoEdificioScreen(viewModel = PlanoViewModel(marcadoresPreview))
  }
}
