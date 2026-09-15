package com.example.navcompose_loginregistro

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private const val FILE_NAME = "cuentas.txt"
private const val ROUTE_LOGIN = "login"
private const val ROUTE_REGISTRO = "registro"
private const val ROUTE_HOME_ARG = "usuario"
private const val ROUTE_HOME = "home/{$ROUTE_HOME_ARG}"
private const val ERROR_CAMPOS_VACIOS = "Por favor complete todos los campos"
private const val ERROR_CUENTA_NO_ENCONTRADA = "Cuenta no encontrada"
private const val MSG_REGISTRO_EXITOSO = "Cuenta registrada exitosamente"
private const val DELIMITER = ","

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = ROUTE_LOGIN) {
                composable(ROUTE_LOGIN) {
                    LoginScreen(
                        onLoginExitoso = { usuario ->
                            navController.navigate("home/$usuario")
                        },
                        onIrARegistro = {
                            navController.navigate(ROUTE_REGISTRO)
                        }
                    )
                }
                composable(ROUTE_REGISTRO) {
                    RegistroScreen(
                        onRegistroExitoso = {
                            navController.popBackStack()
                        },
                        onCancelar = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(ROUTE_HOME) { backStackEntry ->
                    val usuario = backStackEntry.arguments?.getString(ROUTE_HOME_ARG).orEmpty()
                    HomeScreen(usuario = usuario)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(usuario: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Bienvenido $usuario", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun LoginScreen(
    onLoginExitoso: (String) -> Unit,
    onIrARegistro: () -> Unit
) {
    val context = LocalContext.current
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                if (usuario.isBlank() || password.isBlank()) {
                    mensajeError = ERROR_CAMPOS_VACIOS
                } else {
                    val coincide = try {
                        context.openFileInput(FILE_NAME).bufferedReader().useLines { lines ->
                            lines.any { line ->
                                val partes = line.split(DELIMITER)
                                if (partes.size == 2) {
                                    partes[0] == usuario && partes[1] == password
                                } else {
                                    false
                                }
                            }
                        }
                    } catch (e: Exception) {
                        false
                    }

                    if (coincide) {
                        mensajeError = ""
                        onLoginExitoso(usuario)
                    } else {
                        mensajeError = ERROR_CUENTA_NO_ENCONTRADA
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onIrARegistro,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear cuenta")
        }
    }
}

@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit,
    onCancelar: () -> Unit
) {
    val context = LocalContext.current
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Nuevo usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Nueva contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Button(
            onClick = {
                if (usuario.isBlank() || password.isBlank()) {
                    mensajeError = ERROR_CAMPOS_VACIOS
                } else {
                    mensajeError = ""
                    context.openFileOutput(FILE_NAME, Context.MODE_APPEND).use { output ->
                        val linea = "$usuario$DELIMITER$password\n"
                        output.write(linea.toByteArray())
                    }
                    Toast.makeText(context, MSG_REGISTRO_EXITOSO, Toast.LENGTH_SHORT).show()
                    onRegistroExitoso()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aceptar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onCancelar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}