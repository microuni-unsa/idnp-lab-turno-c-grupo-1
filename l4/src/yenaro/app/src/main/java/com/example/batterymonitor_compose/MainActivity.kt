package com.example.batterymonitor_compose

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.batterymonitor_compose.theme.BatteryMonitor_ComposeTheme

@Composable
fun BatteryScreen() {
    var porcentaje by remember { mutableStateOf(0) }
    var enCarga by remember { mutableStateOf(false) }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val nivel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val escala = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                if (nivel != -1 && escala != -1) {
                    porcentaje = (nivel * 100) / escala
                }
                val st = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                enCarga = st == BatteryManager.BATTERY_STATUS_CHARGING ||
                    st == BatteryManager.BATTERY_STATUS_FULL
                Log.d("BatteryScreen", "Nivel: $porcentaje% carga=$enCarga")
            }
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        Log.d("BatteryScreen", "Receiver registrado")
        onDispose {
            context.unregisterReceiver(receiver)
            Log.d("BatteryScreen", "Receiver desregistrado")
        }
    }

    DisposableEffect(Unit) {
        val manual = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val sticky = context?.registerReceiver(
                    null, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                )
                val nivel = sticky?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val escala = sticky?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                if (nivel != -1 && escala != -1) {
                    porcentaje = (nivel * 100) / escala
                }
                Log.d("BatteryScreen", "Actualizacion manual: $porcentaje%")
            }
        }
        context.registerReceiver(manual, IntentFilter("com.tuapp.ACTUALIZAR_BATERIA"), Context.RECEIVER_NOT_EXPORTED)
        Log.d("BatteryScreen", "Receiver manual registrado")
        onDispose {
            context.unregisterReceiver(manual)
            Log.d("BatteryScreen", "Receiver manual desregistrado")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Batería: $porcentaje%", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { porcentaje / 100f },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(if (enCarga) "Estado: Cargando" else "Estado: No cargando")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val i = Intent("com.tuapp.ACTUALIZAR_BATERIA").setPackage(context.packageName)
                val pi = PendingIntent.getBroadcast(
                    context, 0, i,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                try {
                    pi.send()
                } catch (e: Exception) {
                    Log.e("BatteryScreen", "Error PendingIntent", e)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar manualmente")
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BatteryMonitor_ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BatteryScreen()
                }
            }
        }
    }
}
