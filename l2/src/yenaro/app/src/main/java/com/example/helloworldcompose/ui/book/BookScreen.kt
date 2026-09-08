package com.example.helloworldcompose.ui.book

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helloworldcompose.theme.HelloWorldComposeTheme
import java.io.FileNotFoundException
import java.io.IOException

private const val TAG = "BookRegistry"
private const val FILE_NAME = "registro_libro.txt"

@Composable
fun BookScreen(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  var titulo by remember { mutableStateOf("") }
  var autor by remember { mutableStateOf("") }
  var paginas by remember { mutableStateOf("") }
  var registroLeido by remember { mutableStateOf("") }
  var mensaje by remember { mutableStateOf("") }

  Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
    Text("Registro de libro")

    Spacer(Modifier.height(8.dp))

    TextField(
      value = titulo,
      onValueChange = { titulo = it },
      label = { Text("Título del libro") },
      singleLine = true,
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(8.dp))
    TextField(
      value = autor,
      onValueChange = { autor = it },
      label = { Text("Autor") },
      singleLine = true,
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(8.dp))
    TextField(
      value = paginas,
      onValueChange = { paginas = it },
      label = { Text("Páginas leídas") },
      singleLine = true,
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
      modifier = Modifier.fillMaxWidth(),
    )

    Spacer(Modifier.height(12.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      Button(onClick = { mensaje = saveBook(context, titulo, autor, paginas) }) {
        Text("Guardar")
      }
      Button(
        onClick = {
          val (logText, status) = readBook(context)
          registroLeido = logText
          mensaje = status
        }
      ) {
        Text("Ver registro")
      }
    }

    if (mensaje.isNotEmpty()) {
      Spacer(Modifier.height(8.dp))
      Text(mensaje)
    }
    if (registroLeido.isNotEmpty()) {
      Spacer(Modifier.height(8.dp))
      Text(registroLeido)
    }
  }
}

private fun saveBook(context: Context, titulo: String, autor: String, paginas: String): String {
  val contenido = "Título: $titulo\nAutor: $autor\nPáginas leídas: $paginas"
  return try {
    context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
      it.write(contenido.toByteArray())
    }
    Log.d(TAG, "Guardado: $contenido")
    "Guardado OK"
  } catch (e: IOException) {
    Log.d(TAG, "Error al guardar: ${e.message}")
    "Error al guardar: ${e.message}"
  }
}

private fun readBook(context: Context): Pair<String, String> {
  return try {
    val contenido = context.openFileInput(FILE_NAME).bufferedReader().readText()
    Log.d(TAG, contenido)
    contenido to "Registro leído (ver Logcat $TAG)"
  } catch (e: FileNotFoundException) {
    Log.d(TAG, "Sin registro guardado")
    "" to "Sin registro guardado"
  } catch (e: IOException) {
    Log.d(TAG, "Error al leer: ${e.message}")
    "" to "Error al leer: ${e.message}"
  }
}

@Preview(showBackground = true)
@Composable
fun BookScreenPreview() {
  HelloWorldComposeTheme { BookScreen() }
}
