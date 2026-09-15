package com.example.navcompose_loginregistro

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navcompose_loginregistro.theme.NavComposeLoginRegistroTheme
import java.io.FileNotFoundException

// Lista de cuentas registradas, compartida en memoria durante la ejecución.
// Cuenta de prueba para el login (Paso 3 guía). La persistencia en
// cuentas.txt (Ej. 2-3) complementa esta lista, no la reemplaza.
val cuentasRegistradas = mutableStateListOf("admin" to "1234")

@Composable
fun LoginScreen(
  onLoginExitoso: (String) -> Unit,
  onIrARegistro: () -> Unit,
) {
  var usuario by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var mensajeError by remember { mutableStateOf("") }
  val context = LocalContext.current

  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
  ) {
    Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
      value = usuario,
      onValueChange = { usuario = it },
      label = { Text("Usuario") },
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Contraseña") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (mensajeError.isNotEmpty()) {
      Text(mensajeError, color = MaterialTheme.colorScheme.error)
      Spacer(modifier = Modifier.height(8.dp))
    }
    Button(
      onClick = {
        // Reto 4: validación de campos vacíos.
        if (usuario.isBlank() || password.isBlank()) {
          mensajeError = "Complete todos los campos"
          return@Button
        }
        // Ej. 3: buscar coincidencia en cuentas.txt (persistencia) + lista en memoria.
        val lineas =
          try {
            context.openFileInput("cuentas.txt").bufferedReader().readLines()
          } catch (e: FileNotFoundException) {
            emptyList()
          }
        val todas = listOf("admin:1234") + lineas +
          cuentasRegistradas.map { "${it.first}:${it.second}" }
        if (todas.any { it == "$usuario:$password" }) {
          mensajeError = ""
          onLoginExitoso(usuario)
        } else {
          mensajeError = "Cuenta no encontrada"
        }
      },
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text("Ingresar")
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
      onClick = onIrARegistro,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text("Crear cuenta")
    }
  }
}

@Composable
fun RegistroScreen(
  onRegistroExitoso: () -> Unit,
  onCancelar: () -> Unit,
) {
  var usuario by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var mensajeError by remember { mutableStateOf("") }
  val context = LocalContext.current

  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
  ) {
    Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
      value = usuario,
      onValueChange = { usuario = it },
      label = { Text("Nuevo usuario") },
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Nueva contraseña") },
      visualTransformation = PasswordVisualTransformation(),
      modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (mensajeError.isNotEmpty()) {
      Text(mensajeError, color = MaterialTheme.colorScheme.error)
      Spacer(modifier = Modifier.height(8.dp))
    }
    Button(
      onClick = {
        // Reto 4: validación de campos vacíos.
        if (usuario.isBlank() || password.isBlank()) {
          mensajeError = "Complete todos los campos"
          return@Button
        }
        // Ej. 2: persistencia en cuentas.txt (MODE_APPEND) + lista en memoria.
        context.openFileOutput("cuentas.txt", Context.MODE_APPEND).bufferedWriter().use {
          it.append("$usuario:$password\n")
        }
        cuentasRegistradas.add(usuario to password)
        Toast.makeText(context, "Cuenta creada", Toast.LENGTH_SHORT).show()
        onRegistroExitoso()
      },
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text("Aceptar")
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
      onClick = onCancelar,
      modifier = Modifier.fillMaxWidth(),
    ) {
      Text("Cancelar")
    }
  }
}

@Composable
fun HomeScreen(
  usuario: String,
  onCerrarSesion: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
  ) {
    Text("Bienvenido $usuario", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth()) {
      Text("Cerrar sesión")
    }
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      NavComposeLoginRegistroTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val navController = rememberNavController()
          NavHost(navController = navController, startDestination = "login") {
            composable("login") {
              LoginScreen(
                onLoginExitoso = { usuario ->
                  Toast.makeText(
                    this@MainActivity,
                    "Bienvenido $usuario",
                    Toast.LENGTH_SHORT,
                  ).show()
                  navController.navigate("home/$usuario")
                },
                onIrARegistro = { navController.navigate("registro") },
              )
            }
            composable("registro") {
              RegistroScreen(
                onRegistroExitoso = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() },
              )
            }
            composable("home/{usuario}") { backStackEntry ->
              val usuario = backStackEntry.arguments?.getString("usuario") ?: ""
              HomeScreen(usuario = usuario, onCerrarSesion = {
                navController.popBackStack("login", inclusive = false)
              })
            }
          }
        }
      }
    }
  }
}
