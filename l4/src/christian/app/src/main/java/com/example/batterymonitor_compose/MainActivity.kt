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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.batterymonitor_compose.ui.theme.BatteryMonitor_ComposeTheme

private const val TAG = "BatteryScreen"
private const val LOG_BATTERY_RECEIVER_REGISTERED = "Receiver registrado"
private const val LOG_BATTERY_RECEIVER_UNREGISTERED = "Receiver desregistrado"
private const val LOG_MANUAL_RECEIVER_REGISTERED = "Receiver manual registrado"
private const val LOG_MANUAL_RECEIVER_UNREGISTERED = "Receiver manual desregistrado"
private const val LOG_MANUAL_TRIGGERED = "Recarga manual disparada mediante PendingIntent"
private const val LOG_MANUAL_READ_PREFIX = "Recarga manual ejecutada: valor leído = "

private const val ACTION_ACTUALIZAR_BATERIA = "com.example.batterymonitor_compose.ACTUALIZAR_BATERIA"
private const val BUTTON_TEXT_ACTUALIZAR = "Actualizar manualmente"
private const val BATTERY_LABEL_PREFIX = "Batería: "
private const val BATTERY_LABEL_SUFFIX = "%"
private const val STATUS_LABEL_PREFIX = "Estado: "
private const val STATUS_CHARGING = "Cargando"
private const val STATUS_NOT_CHARGING = "No cargando"

private const val INITIAL_PERCENTAGE = 0
private const val INVALID_BATTERY_VALUE = -1
private const val PERCENTAGE_SCALE = 100
private const val PENDING_INTENT_REQUEST_CODE = 0
private const val CONTENT_SPACING_DP = 16

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BatteryMonitor_ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BatteryScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BatteryScreen(modifier: Modifier = Modifier) {
    var porcentaje by remember { mutableStateOf(INITIAL_PERCENTAGE) }
    var estaCargando by remember { mutableStateOf(false) }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val nivel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, INVALID_BATTERY_VALUE) ?: INVALID_BATTERY_VALUE
                val escala = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, INVALID_BATTERY_VALUE) ?: INVALID_BATTERY_VALUE
                if (nivel != INVALID_BATTERY_VALUE && escala > 0) {
                    porcentaje = (nivel * PERCENTAGE_SCALE) / escala
                } else {
                    porcentaje = INITIAL_PERCENTAGE
                }

                val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, INVALID_BATTERY_VALUE) ?: INVALID_BATTERY_VALUE
                estaCargando = (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL)
            }
        }

        context.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        Log.d(TAG, LOG_BATTERY_RECEIVER_REGISTERED)

        onDispose {
            context.unregisterReceiver(batteryReceiver)
            Log.d(TAG, LOG_BATTERY_RECEIVER_UNREGISTERED)
        }
    }

    DisposableEffect(Unit) {
        val manualReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (context != null) {
                    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
                    if (batteryManager != null) {
                        val nivel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                        if (nivel != INVALID_BATTERY_VALUE) {
                            porcentaje = nivel
                        } else {
                            porcentaje = INITIAL_PERCENTAGE
                        }
                        estaCargando = batteryManager.isCharging
                        Log.d(TAG, "$LOG_MANUAL_READ_PREFIX$porcentaje% (Cargando: $estaCargando)")
                    } else {
                        porcentaje = INITIAL_PERCENTAGE
                        estaCargando = false
                        Log.d(TAG, "$LOG_MANUAL_READ_PREFIX$porcentaje% (BatteryManager no disponible)")
                    }
                } else {
                    porcentaje = INITIAL_PERCENTAGE
                    estaCargando = false
                    Log.d(TAG, "$LOG_MANUAL_READ_PREFIX$porcentaje% (Contexto nulo)")
                }
            }
        }

        ContextCompat.registerReceiver(
            context,
            manualReceiver,
            IntentFilter(ACTION_ACTUALIZAR_BATERIA),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        Log.d(TAG, LOG_MANUAL_RECEIVER_REGISTERED)

        onDispose {
            context.unregisterReceiver(manualReceiver)
            Log.d(TAG, LOG_MANUAL_RECEIVER_UNREGISTERED)
        }
    }

    val estadoTexto = if (estaCargando) STATUS_CHARGING else STATUS_NOT_CHARGING

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$BATTERY_LABEL_PREFIX$porcentaje$BATTERY_LABEL_SUFFIX"
        )
        Spacer(modifier = Modifier.height(CONTENT_SPACING_DP.dp))
        Text(
            text = "$STATUS_LABEL_PREFIX$estadoTexto"
        )
        Spacer(modifier = Modifier.height(CONTENT_SPACING_DP.dp))
        Button(
            onClick = {
                Log.d(TAG, LOG_MANUAL_TRIGGERED)
                val intent = Intent(ACTION_ACTUALIZAR_BATERIA).apply {
                    setPackage(context.packageName)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    PENDING_INTENT_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                pendingIntent.send()
            }
        ) {
            Text(text = BUTTON_TEXT_ACTUALIZAR)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BatteryScreenPreview() {
    BatteryMonitor_ComposeTheme {
        BatteryScreen()
    }
}