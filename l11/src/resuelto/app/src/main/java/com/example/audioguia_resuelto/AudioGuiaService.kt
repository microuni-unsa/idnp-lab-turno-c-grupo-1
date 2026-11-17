package com.example.audioguia_resuelto

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioGuiaService : Service() {

    private var job: Job? = null

    companion object {
        const val ACCION_REPRODUCIR = "com.example.audioguia_resuelto.REPRODUCIR"
        const val ACCION_PAUSAR = "com.example.audioguia_resuelto.PAUSAR"
        const val ACCION_DETENER = "com.example.audioguia_resuelto.DETENER"
        private const val ID_CANAL = "canal_audioguia"
        private const val ID_NOTIFICACION = 1
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID_CANAL, "Audio-guía", NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACCION_REPRODUCIR -> {
                startForeground(ID_NOTIFICACION, construirNotificacion("Biblioteca Central"))
                iniciarAvance()
            }
            ACCION_PAUSAR -> job?.cancel()
            ACCION_DETENER -> {
                job?.cancel()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun iniciarAvance() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            var segundos = 0
            while (true) {
                delay(1000)
                segundos++
            }
        }
    }

    private fun construirNotificacion(titulo: String): Notification {
        return NotificationCompat.Builder(this, ID_CANAL)
            .setContentTitle(titulo)
            .setContentText("Reproduciendo audio-guía")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
