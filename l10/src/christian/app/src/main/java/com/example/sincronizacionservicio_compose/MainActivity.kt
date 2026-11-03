package com.example.sincronizacionservicio_compose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.sincronizacionservicio_compose.data.AppDatabase
import com.example.sincronizacionservicio_compose.data.EdificioEntity
import com.example.sincronizacionservicio_compose.ui.theme.SincronizacionServicio_ComposeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface SyncUiState {
    data object Idle : SyncUiState
    data object InProgress : SyncUiState
    data class Success(val count: Int) : SyncUiState
    data class Error(val message: String) : SyncUiState
    data object Stopped : SyncUiState
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SincronizacionServicio_ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaSincronizacion()
                }
            }
        }
    }
}

@Composable
fun PantallaSincronizacion() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dao = remember { AppDatabase.getInstance(context).edificioDao() }

    val edificios by dao.observarEdificios().collectAsState(initial = emptyList())
    var syncState by remember { mutableStateOf<SyncUiState>(SyncUiState.Idle) }

    // Inicializar datos si la base de datos está vacía
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            if (dao.contar() == 0) {
                dao.insertarEdificios(
                    listOf(
                        EdificioEntity(nombre = "Biblioteca Central", categoria = "Servicios", sincronizado = false),
                        EdificioEntity(nombre = "Facultad de Ingeniería", categoria = "Académico", sincronizado = false),
                        EdificioEntity(nombre = "Pabellón de Sistemas", categoria = "Académico", sincronizado = false),
                        EdificioEntity(nombre = "Comedor Universitario", categoria = "Bienestar", sincronizado = false),
                        EdificioEntity(nombre = "Centro de Cómputo", categoria = "Investigación", sincronizado = false)
                    )
                )
            }
        }
    }

    // Registro de los BroadcastReceivers
    DisposableEffect(Unit) {
        // Receptor 1: Éxito de sincronización (Paso 8 y Ejercicio 2)
        val receiverExito = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                Log.d(SyncConstants.TAG_SYNC, "3. Aviso recibido en la pantalla")
                val cantidad = intent?.getIntExtra(SyncConstants.EXTRA_EDIFICIOS_ACTUALIZADOS, 0) ?: 0
                syncState = SyncUiState.Success(cantidad)
            }
        }

        // Receptor 2: Error de sincronización (Ejercicio Propuesto 3)
        val receiverError = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                Log.d(SyncConstants.TAG_SYNC, "Aviso de error recibido en la pantalla")
                val errorMsg = intent?.getStringExtra(SyncConstants.EXTRA_ERROR_MENSAJE)
                    ?: "Error desconocido durante la sincronización."
                syncState = SyncUiState.Error(errorMsg)
            }
        }

        val filtroExito = IntentFilter(SyncConstants.ACTION_SYNC_LISTA)
        val filtroError = IntentFilter(SyncConstants.ACTION_SYNC_ERROR)

        ContextCompat.registerReceiver(
            context,
            receiverExito,
            filtroExito,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        ContextCompat.registerReceiver(
            context,
            receiverError,
            filtroError,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        onDispose {
            context.unregisterReceiver(receiverExito)
            context.unregisterReceiver(receiverError)
        }
    }

    val isInProgress = syncState is SyncUiState.InProgress

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sincronización en Segundo Plano",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acción (Paso 9 y Ejercicio Propuesto 1)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    syncState = SyncUiState.InProgress
                    context.startService(Intent(context, SincronizacionService::class.java))
                },
                enabled = !isInProgress
            ) {
                Text("Sincronizar")
            }

            // Ejercicio Propuesto 1: Botón para detener sincronización en curso
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    context.stopService(Intent(context, SincronizacionService::class.java))
                    syncState = SyncUiState.Stopped
                },
                enabled = isInProgress,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Detener")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón auxiliar para restablecer los estados y permitir pruebas continuas
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    dao.reiniciarSincronizacion()
                }
                syncState = SyncUiState.Idle
            },
            enabled = !isInProgress
        ) {
            Text("Restablecer a No Sincronizado")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visualización del estado actual
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when (syncState) {
                    is SyncUiState.Error -> MaterialTheme.colorScheme.errorContainer
                    is SyncUiState.Success -> MaterialTheme.colorScheme.primaryContainer
                    is SyncUiState.Stopped -> MaterialTheme.colorScheme.tertiaryContainer
                    is SyncUiState.InProgress, is SyncUiState.Idle -> MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val state = syncState) {
                    is SyncUiState.Idle -> {
                        Text(
                            text = "Presiona 'Sincronizar' para iniciar el servicio en segundo plano.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    is SyncUiState.InProgress -> {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Sincronizando con base de datos Room...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    is SyncUiState.Success -> {
                        Text(
                            text = "Sincronización completa: ${state.count} edificios actualizados",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    is SyncUiState.Error -> {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    is SyncUiState.Stopped -> {
                        Text(
                            text = "Sincronización detenida manualmente por el usuario.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de edificios persistidos en Room (Laboratorio 09 / Ejercicio Propuesto 2)
        Text(
            text = "Edificios en Base de Datos Room:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(edificios, key = { it.id }) { edificio ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = edificio.nombre,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Categoría: ${edificio.categoria}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = if (edificio.sincronizado) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.secondaryContainer
                            }
                        ) {
                            Text(
                                text = if (edificio.sincronizado) "Sincronizado" else "Pendiente",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (edificio.sincronizado) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaSincronizacionPreview() {
    SincronizacionServicio_ComposeTheme {
        PantallaSincronizacion()
    }
}
