package com.example.audioguia_yenaro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.room.Room
import com.example.audioguia_yenaro.data.GuiaDatabase
import com.example.audioguia_yenaro.ui.theme.AudioGuiaForegroundComposeTheme

val Crema = Color(0xFFFFF6E5)
val AmarilloNeo = Color(0xFFFFD54F)
val AzulNeo = Color(0xFF7DA2FF)
val VerdeNeo = Color(0xFFA8E6A1)
val RosaNeo = Color(0xFFFF9ECF)
val NegroNeo = Color(0xFF111111)

fun colorPorCategoria(nombre: String): Color = when {
    nombre.contains("Biblioteca") || nombre.contains("Comedor") -> AmarilloNeo
    nombre.contains("Facultad") || nombre.contains("Pabellón") -> AzulNeo
    nombre.contains("Coliseo") || nombre.contains("Estadio") -> VerdeNeo
    else -> RosaNeo
}

@Composable
fun NeoCard(color: Color, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
        Box(
            modifier = Modifier.matchParentSize()
                .offset(x = 6.dp, y = 6.dp)
                .background(NegroNeo, RoundedCornerShape(12.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(3.dp, NegroNeo, RoundedCornerShape(12.dp))
                .background(color, RoundedCornerShape(12.dp))
                .padding(20.dp),
            content = content
        )
    }
}

@Composable
fun NeoButton(texto: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(2.dp, NegroNeo),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White, contentColor = NegroNeo
        )
    ) { Text(texto, fontWeight = FontWeight.Bold) }
}

@Composable
fun PantallaAudioGuiaYenaro() {
    val context = LocalContext.current
    var idx by remember { mutableIntStateOf(0) }
    var reproduciendo by remember { mutableStateOf(false) }
    var enSegundoPlano by remember { mutableStateOf(false) }
    var ultimo by remember { mutableStateOf<String?>(null) }
    val edificio = AudioGuiaService.EDIFICIOS[idx]

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> enSegundoPlano = true
                Lifecycle.Event.ON_START -> enSegundoPlano = false
                else -> {}
            }
        }
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
        onDispose { ProcessLifecycleOwner.get().lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        val db = Room.databaseBuilder(context, GuiaDatabase::class.java, "guia_db").build()
        ultimo = db.ultimoEdificioDao().obtener()?.nombre
    }

    fun lanzar(accion: String, nombre: String? = null) {
        val i = Intent(context, AudioGuiaService::class.java).setAction(accion)
        if (nombre != null) i.putExtra(AudioGuiaService.EXTRA_EDIFICIO, nombre)
        context.startService(i)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Crema).statusBarsPadding().padding(16.dp)
    ) {
        NeoCard(color = colorPorCategoria(edificio)) {
            Text("AUDIO-GUÍA UNSA", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NegroNeo)
            Text(edificio, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = NegroNeo)
            Text(
                if (reproduciendo) "Reproduciendo audio-guía..." else "Audio-guía detenida",
                fontSize = 14.sp, color = NegroNeo
            )
            if (enSegundoPlano && reproduciendo) {
                Text("En 2.º plano: revisa la notificación", fontSize = 13.sp, color = NegroNeo)
            }
            ultimo?.let {
                Text("Último: $it", fontSize = 13.sp, color = NegroNeo)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            NeoButton("Reproducir") {
                reproduciendo = true; lanzar(AudioGuiaService.ACCION_REPRODUCIR, edificio)
            }
            Spacer(modifier = Modifier.width(8.dp))
            NeoButton("Siguiente") {
                idx = (idx + 1) % AudioGuiaService.EDIFICIOS.size
                reproduciendo = true
                lanzar(AudioGuiaService.ACCION_SIGUIENTE)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            NeoButton("Pausar") {
                reproduciendo = false; lanzar(AudioGuiaService.ACCION_PAUSAR)
            }
            Spacer(modifier = Modifier.width(8.dp))
            NeoButton("Detener") {
                reproduciendo = false; lanzar(AudioGuiaService.ACCION_DETENER)
            }
        }
        if (ultimo != null && ultimo != edificio) {
            Spacer(modifier = Modifier.height(8.dp))
            NeoButton("Continuar: $ultimo") {
                val j = AudioGuiaService.EDIFICIOS.indexOf(ultimo).takeIf { it >= 0 } ?: 0
                idx = j; reproduciendo = true
                lanzar(AudioGuiaService.ACCION_REPRODUCIR, ultimo)
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AudioGuiaForegroundComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaAudioGuiaYenaro()
                }
            }
        }
    }
}
