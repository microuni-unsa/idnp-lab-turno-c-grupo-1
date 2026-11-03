package com.example.sincronizacionservicio_compose

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.sincronizacionservicio_compose.data.AppDatabase
import com.example.sincronizacionservicio_compose.data.EdificioEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SincronizacionService : Service() {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(SyncConstants.TAG_SYNC, "1. Servicio iniciado")

        serviceScope.launch {
            try {
                // Simulación de latencia de red inicial
                delay(1000)

                // Ejercicio propuesto 3: Simular caso de error con probabilidad aleatoria (30%)
                val esError = (1..100).random() <= 30
                if (esError) {
                    Log.e(SyncConstants.TAG_SYNC, "Error simulado en la sincronización")
                    val errorIntent = Intent(SyncConstants.ACTION_SYNC_ERROR).apply {
                        setPackage(packageName)
                        putExtra(
                            SyncConstants.EXTRA_ERROR_MENSAJE,
                            "Falla de conexión: El servidor central de campus no respondió (error simulado)."
                        )
                    }
                    sendBroadcast(errorIntent)
                    stopSelf()
                    return@launch
                }

                // Ejercicio propuesto 2: Obtener y recorrer edificios de Room (Lab 09)
                val dao = AppDatabase.getInstance(applicationContext).edificioDao()

                // Si la base de datos está vacía, inicializar datos de prueba
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

                // Simular tiempo adicional de procesamiento de sincronización (total ~3s según guía)
                delay(2000)

                val edificios = dao.obtenerTodos()
                var actualizados = 0

                // Condición: sincronizar edificios pendientes de sincronización (sincronizado == false)
                for (edificio in edificios) {
                    if (!isActive) break
                    if (!edificio.sincronizado) {
                        dao.actualizarEdificio(edificio.copy(sincronizado = true))
                        actualizados++
                    }
                }

                val resultado = Intent(SyncConstants.ACTION_SYNC_LISTA).apply {
                    setPackage(packageName)
                    putExtra(SyncConstants.EXTRA_EDIFICIOS_ACTUALIZADOS, actualizados)
                }
                sendBroadcast(resultado)
                Log.d(SyncConstants.TAG_SYNC, "2. Aviso enviado: $actualizados edificios")
                stopSelf()

            } catch (e: CancellationException) {
                Log.d(SyncConstants.TAG_SYNC, "Sincronización cancelada debido a detención del servicio")
            } catch (e: Exception) {
                Log.e(SyncConstants.TAG_SYNC, "Excepción inesperada en el servicio: ${e.message}")
                val errorIntent = Intent(SyncConstants.ACTION_SYNC_ERROR).apply {
                    setPackage(packageName)
                    putExtra(SyncConstants.EXTRA_ERROR_MENSAJE, "Error inesperado: ${e.message}")
                }
                sendBroadcast(errorIntent)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(SyncConstants.TAG_SYNC, "Servicio detenido / onDestroy")
        serviceJob.cancel()
    }
}
