package com.example.audioguiaforeground_compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.audioguiaforeground_compose.data.AppDatabase
import com.example.audioguiaforeground_compose.ui.theme.AudioGuiaForeground_ComposeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AudioGuiaForeground_ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaAudioGuia()
                }
            }
        }
    }
}

@Composable
fun PantallaAudioGuia() {
    val context = LocalContext.current
    var edificio by remember { mutableStateOf(AudioGuiaService.EDIFICIOS.first()) }
    var reproduciendo by remember { mutableStateOf(false) }
    var enSegundoPlano by remember { mutableStateOf(false) }
    var ultimoGuardado by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> enSegundoPlano = true
                Lifecycle.Event.ON_START -> enSegundoPlano = false
                else -> {}
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)

        onDispose {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        val guardado = withContext(Dispatchers.IO) {
            AppDatabase.getInstance(context).ultimaReproduccionDao().obtenerUltimoEdificio()
        }
        if (guardado != null && guardado in AudioGuiaService.EDIFICIOS && guardado != edificio) {
            ultimoGuardado = guardado
        }
    }

    fun enviar(accion: String, nombreEdificio: String = edificio) {
        context.startService(
            Intent(context, AudioGuiaService::class.java)
                .setAction(accion)
                .putExtra(AudioGuiaService.EXTRA_EDIFICIO, nombreEdificio)
        )
    }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        Text("Ahora suena", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(edificio, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    if (reproduciendo) "Reproduciendo audio-guía..." else "Audio-guía detenida",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (enSegundoPlano && reproduciendo) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("La app está en segundo plano: revisa la notificación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Reproducción", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = {
                reproduciendo = true
                enviar(AudioGuiaService.ACCION_REPRODUCIR)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reproducir", maxLines = 1)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                onClick = {
                    reproduciendo = false
                    enviar(AudioGuiaService.ACCION_PAUSAR)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Pausar", maxLines = 1)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    reproduciendo = false
                    enviar(AudioGuiaService.ACCION_DETENER)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Detener", maxLines = 1)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Edificios", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedButton(
            onClick = {
                val indice = (AudioGuiaService.EDIFICIOS.indexOf(edificio) + 1) % AudioGuiaService.EDIFICIOS.size
                edificio = AudioGuiaService.EDIFICIOS[indice]
                if (reproduciendo) {
                    enviar(AudioGuiaService.ACCION_SIGUIENTE, edificio)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Siguiente edificio", maxLines = 1)
        }

        if (ultimoGuardado != null) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    edificio = ultimoGuardado!!
                    ultimoGuardado = null
                    reproduciendo = true
                    enviar(AudioGuiaService.ACCION_REPRODUCIR, edificio)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar: $ultimoGuardado", maxLines = 1)
            }
        }
    }
}
