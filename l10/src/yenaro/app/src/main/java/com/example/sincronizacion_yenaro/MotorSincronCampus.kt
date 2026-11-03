package com.example.sincronizacion_yenaro

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.sincronizacion_yenaro.data.CampusDatabase
import com.example.sincronizacion_yenaro.data.SedeEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class MotorSincronCampus : Service() {

    private val faena = SupervisorJob()
    private val alcance = CoroutineScope(Dispatchers.IO + faena)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, arranqueId: Int): Int {
        Log.d(ConstantesCampus.ETIQUETA_LOG, "1. Motor encendido")

        alcance.launch {
            try {
                delay(1000)

                // Fallo simulado ~30% (ejercicio propuesto 3)
                if (Random.nextInt(100) < 30) {
                    Log.e(ConstantesCampus.ETIQUETA_LOG, "Fallo fingido del servidor")
                    val avisoFallo = Intent(ConstantesCampus.AVISO_SYNC_FALLO).apply {
                        setPackage(packageName)
                        putExtra(
                            ConstantesCampus.DATO_MOTIVO_FALLO,
                            "Sin respuesta del servidor central del campus (fallo simulado)."
                        )
                    }
                    sendBroadcast(avisoFallo)
                    stopSelf()
                    return@launch
                }

                val tabla = CampusDatabase.abrir(applicationContext).sedes()

                if (tabla.total() == 0) {
                    tabla.guardarVarias(catalogoInicial())
                }

                // Latencia extra de subida (~2s, total ~3s según guía)
                delay(2000)

                var marcadas = 0
                for (sede in tabla.listarTodo()) {
                    if (!isActive) break
                    if (!sede.alDia) {
                        tabla.retocar(sede.copy(alDia = true))
                        marcadas++
                    }
                }

                val avisoOk = Intent(ConstantesCampus.AVISO_SYNC_OK).apply {
                    setPackage(packageName)
                    putExtra(ConstantesCampus.DATO_SEDES_MARCADAS, marcadas)
                }
                sendBroadcast(avisoOk)
                Log.d(ConstantesCampus.ETIQUETA_LOG, "2. Aviso lanzado: $marcadas sedes")
                stopSelf()
            } catch (corte: CancellationException) {
                Log.d(ConstantesCampus.ETIQUETA_LOG, "Faena interrumpida por stopService")
            } catch (fallo: Exception) {
                Log.e(ConstantesCampus.ETIQUETA_LOG, "Ruptura imprevista: ${fallo.message}")
                val avisoFallo = Intent(ConstantesCampus.AVISO_SYNC_FALLO).apply {
                    setPackage(packageName)
                    putExtra(ConstantesCampus.DATO_MOTIVO_FALLO, "Ruptura imprevista: ${fallo.message}")
                }
                sendBroadcast(avisoFallo)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(ConstantesCampus.ETIQUETA_LOG, "Motor apagado / onDestroy")
        faena.cancel()
    }
}

fun catalogoInicial(): List<SedeEntity> = listOf(
    SedeEntity(titulo = "Biblioteca Central", rubro = "Servicios"),
    SedeEntity(titulo = "Facultad de Ingeniería", rubro = "Académico"),
    SedeEntity(titulo = "Pabellón de Sistemas", rubro = "Académico"),
    SedeEntity(titulo = "Comedor Universitario", rubro = "Bienestar"),
    SedeEntity(titulo = "Centro de Cómputo", rubro = "Investigación")
)
