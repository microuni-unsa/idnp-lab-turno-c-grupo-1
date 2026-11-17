package com.example.audioguia_yenaro

import android.app.Notification
import android.app.PendingIntent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.audioguia_yenaro.data.GuiaDatabase
import com.example.audioguia_yenaro.data.UltimoEdificioEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioGuiaService : Service() {

    private var player: MediaPlayer? = null
    private var edificioActual = "Biblioteca Central"

    companion object {
        const val ACCION_REPRODUCIR = "com.example.audioguia_yenaro.REPRODUCIR"
        const val ACCION_PAUSAR = "com.example.audioguia_yenaro.PAUSAR"
        const val ACCION_DETENER = "com.example.audioguia_yenaro.DETENER"
        const val ACCION_SIGUIENTE = "com.example.audioguia_yenaro.SIGUIENTE"
        const val EXTRA_EDIFICIO = "extra_edificio"
        private const val ID_CANAL = "canal_audioguia"
        private const val ID_NOTIFICACION = 1

        val EDIFICIOS = listOf(
            "Biblioteca Central",
            "Comedor Universitario",
            "Facultad de Ingeniería de Producción y Servicios",
            "Pabellón de Ingeniería de Sistemas",
            "Coliseo Universitario",
            "Estadio Universitario",
            "Auditorio Central",
            "Facultad de Ciencias Naturales y Formales"
        )
    }

    private val db by lazy {
        Room.databaseBuilder(applicationContext, GuiaDatabase::class.java, "guia_db").build()
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                ID_CANAL, "Audio-guía", NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
        }
    }

    private fun pendingAccion(accion: String, req: Int): PendingIntent {
        val i = Intent(this, AudioGuiaService::class.java).setAction(accion)
        return PendingIntent.getService(this, req, i, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun construirNotificacion(titulo: String): Notification {
        return NotificationCompat.Builder(this, ID_CANAL)
            .setContentTitle(titulo)
            .setContentText("Reproduciendo audio-guía")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_media_pause, "Pausar", pendingAccion(ACCION_PAUSAR, 1))
            .addAction(android.R.drawable.ic_delete, "Detener", pendingAccion(ACCION_DETENER, 2))
            .build()
    }

    private fun reproducir(edificio: String) {
        edificioActual = edificio
        startForeground(ID_NOTIFICACION, construirNotificacion(edificio))
        CoroutineScope(Dispatchers.IO).launch {
            db.ultimoEdificioDao().guardar(UltimoEdificioEntity(nombre = edificio))
        }
        player?.release()
        player = MediaPlayer.create(this, R.raw.guia_biblioteca)?.apply {
            isLooping = true
            start()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACCION_REPRODUCIR -> {
                val e = intent.getStringExtra(EXTRA_EDIFICIO) ?: edificioActual
                reproducir(e)
            }
            ACCION_SIGUIENTE -> {
                val idx = EDIFICIOS.indexOf(edificioActual).takeIf { it >= 0 } ?: 0
                reproducir(EDIFICIOS[(idx + 1) % EDIFICIOS.size])
            }
            ACCION_PAUSAR -> player?.pause()
            ACCION_DETENER -> {
                player?.stop()
                player?.release()
                player = null
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        player?.release()
        player = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
