package com.example.sincronizacion_yenaro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.sincronizacion_yenaro.data.CampusDatabase
import com.example.sincronizacion_yenaro.ui.theme.SincronizacionYenaroTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// ---------- paleta neobrutalista (misma del lab 09) ----------
val CremaFondo = Color(0xFFFFF6E5)
val AmarilloNeo = Color(0xFFFFD54F)
val AzulNeo = Color(0xFF7DA2FF)
val VerdeNeo = Color(0xFFA8E6A1)
val RosaNeo = Color(0xFFFF9ECF)
val GrisNeo = Color(0xFFE8E4D8)
val Tinta = Color(0xFF111111)

fun tintePorRubro(rubro: String): Color = when (rubro) {
    "Servicios" -> AmarilloNeo
    "Académico" -> AzulNeo
    "Bienestar" -> RosaNeo
    else -> VerdeNeo
}

@Composable
fun NeoTarjeta(color: Color, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier = Modifier.padding(end = 6.dp, bottom = 14.dp)) {
        Box(
            modifier = Modifier.matchParentSize()
                .offset(x = 6.dp, y = 6.dp)
                .background(Tinta, RoundedCornerShape(12.dp))
        )
        Column(
            modifier = Modifier
                .border(3.dp, Tinta, RoundedCornerShape(12.dp))
                .background(color, RoundedCornerShape(12.dp))
                .padding(20.dp),
            content = content
        )
    }
}

@Composable
fun NeoBoton(texto: String, habilitado: Boolean = true, fondo: Color = Color.White, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        enabled = habilitado,
        border = BorderStroke(2.dp, Tinta),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = fondo,
            contentColor = Tinta,
            disabledContainerColor = GrisNeo,
            disabledContentColor = Tinta.copy(alpha = 0.4f)
        )
    ) { Text(texto, fontWeight = FontWeight.Bold) }
}

@Composable
fun Insignia(alDia: Boolean) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (alDia) VerdeNeo else RosaNeo,
        border = BorderStroke(2.dp, Tinta)
    ) {
        Text(
            text = if (alDia) "AL DÍA" else "PENDIENTE",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Tinta
        )
    }
}

// ---------- estado de la pantalla ----------
sealed class FaseSincro {
    data object Reposo : FaseSincro()
    data object Trabajando : FaseSincro()
    data class Terminada(val cantidad: Int) : FaseSincro()
    data class Rota(val motivo: String) : FaseSincro()
    data object Frenada : FaseSincro()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(instancia: Bundle?) {
        super.onCreate(instancia)
        setContent {
            SincronizacionYenaroTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = CremaFondo) {
                    PanelSincron()
                }
            }
        }
    }
}

@Composable
fun PanelSincron() {
    val marco = LocalContext.current
    val alcanceUi = rememberCoroutineScope()
    val tabla = remember { CampusDatabase.abrir(marco).sedes() }

    val sedes by tabla.vigilarSedes().collectAsState(initial = emptyList())
    var fase by remember { mutableStateOf<FaseSincro>(FaseSincro.Reposo) }

    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            if (tabla.total() == 0) tabla.guardarVarias(catalogoInicial())
        }
    }

    DisposableEffect(Unit) {
        val oyenteOk = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, aviso: Intent?) {
                Log.d(ConstantesCampus.ETIQUETA_LOG, "3. Confirmación recogida en pantalla")
                val n = aviso?.getIntExtra(ConstantesCampus.DATO_SEDES_MARCADAS, 0) ?: 0
                fase = FaseSincro.Terminada(n)
            }
        }
        val oyenteFallo = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, aviso: Intent?) {
                Log.d(ConstantesCampus.ETIQUETA_LOG, "Fallo recogido en pantalla")
                val motivo = aviso?.getStringExtra(ConstantesCampus.DATO_MOTIVO_FALLO)
                    ?: "Fallo desconocido del motor."
                fase = FaseSincro.Rota(motivo)
            }
        }
        ContextCompat.registerReceiver(
            marco, oyenteOk,
            IntentFilter(ConstantesCampus.AVISO_SYNC_OK),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        ContextCompat.registerReceiver(
            marco, oyenteFallo,
            IntentFilter(ConstantesCampus.AVISO_SYNC_FALLO),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        onDispose {
            marco.unregisterReceiver(oyenteOk)
            marco.unregisterReceiver(oyenteFallo)
        }
    }

    val ocupado = fase is FaseSincro.Trabajando

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        item {
            Text("SYNC CAMPUS UNSA", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Tinta)
            Text(
                "Motor en 2do plano + Broadcast",
                fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Tinta.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(Modifier.weight(1f)) {
                    NeoBoton("▶ SINCRONIZAR", habilitado = !ocupado, fondo = AmarilloNeo) {
                        fase = FaseSincro.Trabajando
                        marco.startService(Intent(marco, MotorSincronCampus::class.java))
                    }
                }
                Box(Modifier.weight(1f)) {
                    NeoBoton("■ FRENAR", habilitado = ocupado, fondo = RosaNeo) {
                        marco.stopService(Intent(marco, MotorSincronCampus::class.java))
                        fase = FaseSincro.Frenada
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            NeoBoton("↺ REINICIAR A PENDIENTE", habilitado = !ocupado) {
                alcanceUi.launch(Dispatchers.IO) { tabla.marcarTodoPendiente() }
                fase = FaseSincro.Reposo
            }
            Spacer(Modifier.height(6.dp))
        }

        item {
            val (tinte, mensaje) = when (val f = fase) {
                is FaseSincro.Reposo -> GrisNeo to "Toca SINCRONIZAR: el motor corre fuera del hilo principal."
                is FaseSincro.Trabajando -> AmarilloNeo to "Subiendo sedes a Room en 2do plano…"
                is FaseSincro.Terminada -> VerdeNeo to "Listo: ${f.cantidad} sedes marcadas al día."
                is FaseSincro.Rota -> RosaNeo to f.motivo
                is FaseSincro.Frenada -> AzulNeo to "Motor frenado a mano por el usuario."
            }
            NeoTarjeta(color = tinte) {
                if (fase is FaseSincro.Trabajando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(34.dp), color = Tinta, strokeWidth = 4.dp
                    )
                    Spacer(Modifier.height(8.dp))
                }
                Text(mensaje, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Tinta)
            }
        }

        item {
            Text(
                "SEDES EN ROOM (${sedes.size})",
                fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Tinta,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
            )
        }

        items(sedes, key = { it.codigo }) { sede ->
            NeoTarjeta(color = tintePorRubro(sede.rubro)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(sede.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Tinta)
                        Text(
                            "RUBRO: ${sede.rubro.uppercase()}",
                            fontSize = 13.sp, fontWeight = FontWeight.Bold,
                            color = Tinta.copy(alpha = 0.65f)
                        )
                    }
                    Insignia(sede.alDia)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PanelSincronPreview() {
    SincronizacionYenaroTheme {
        Surface(color = CremaFondo) {
            NeoTarjeta(color = AmarilloNeo) {
                Text("SYNC CAMPUS UNSA", fontWeight = FontWeight.Bold, color = Tinta)
            }
        }
    }
}
