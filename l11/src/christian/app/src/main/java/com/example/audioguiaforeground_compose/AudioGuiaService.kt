package com.example.audioguiaforeground_compose

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.audioguiaforeground_compose.data.AppDatabase
import com.example.audioguiaforeground_compose.data.UltimaReproduccionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AudioGuiaService : Service() {

    private var player: MediaPlayer? = null
    private var edificioActual = EDIFICIOS.first()
    private var edificioDelPlayer: String? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        const val ACCION_REPRODUCIR = "com.example.audioguiaforeground_compose.REPRODUCIR"
        const val ACCION_PAUSAR = "com.example.audioguiaforeground_compose.PAUSAR"
        const val ACCION_DETENER = "com.example.audioguiaforeground_compose.DETENER"
        const val ACCION_SIGUIENTE = "com.example.audioguiaforeground_compose.SIGUIENTE"
        const val EXTRA_EDIFICIO = "edificio"
        private const val ID_CANAL = "canal_audioguia"
        private const val ID_NOTIFICACION = 1

        val EDIFICIOS = listOf(
            "Biblioteca Central",
            "Comedor Universitario",
            "Pabellón de Ingeniería de Sistemas",
            "Coliseo Universitario",
            "Auditorio Central"
        )

        private val AUDIOS = mapOf(
            "Biblioteca Central" to R.raw.narracion_biblioteca,
            "Comedor Universitario" to R.raw.narracion_comedor,
            "Pabellón de Ingeniería de Sistemas" to R.raw.narracion_sistemas,
            "Coliseo Universitario" to R.raw.narracion_coliseo,
            "Auditorio Central" to R.raw.narracion_auditorio
        )
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
        intent?.getStringExtra(EXTRA_EDIFICIO)?.let { edificioActual = it }
        when (intent?.action) {
            ACCION_REPRODUCIR -> reproducir()
            ACCION_PAUSAR -> pausar()
            ACCION_DETENER -> detener()
            ACCION_SIGUIENTE -> siguiente()
        }
        return START_NOT_STICKY
    }

    private fun reproducir() {
        if (player == null || edificioDelPlayer != edificioActual) {
            detenerPlayerSilencioso()
            player = MediaPlayer.create(this, AUDIOS.getValue(edificioActual)).apply {
                setOnCompletionListener {
                    NotificationManagerCompat.from(this@AudioGuiaService)
                        .notify(ID_NOTIFICACION, construirNotificacion(edificioActual, enReproduccion = false))
                }
            }
            edificioDelPlayer = edificioActual
        }
        player?.start()
        startForeground(ID_NOTIFICACION, construirNotificacion(edificioActual, enReproduccion = true))
        scope.launch {
            AppDatabase.getInstance(applicationContext).ultimaReproduccionDao()
                .guardar(UltimaReproduccionEntity(nombreEdificio = edificioActual))
        }
    }

    private fun pausar() {
        player?.takeIf { it.isPlaying }?.pause()
        NotificationManagerCompat.from(this)
            .notify(ID_NOTIFICACION, construirNotificacion(edificioActual, enReproduccion = false))
    }

    private fun detener() {
        detenerPlayerSilencioso()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun siguiente() {
        reproducir()
    }

    private fun detenerPlayerSilencioso() {
        try {
            player?.stop()
        } catch (_: IllegalStateException) {
            // stop() lanza esta excepción si el reproductor está en estado de error;
            // igual se libera abajo para no dejar un reproductor colgado.
        }
        player?.release()
        player = null
        edificioDelPlayer = null
    }

    private fun pendienteAccion(accion: String, codigo: Int): PendingIntent {
        val intent = Intent(this, AudioGuiaService::class.java)
            .setAction(accion)
            .putExtra(EXTRA_EDIFICIO, edificioActual)
        return PendingIntent.getService(this, codigo, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun construirNotificacion(edificio: String, enReproduccion: Boolean): Notification {
        val abrirApp = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, ID_CANAL)
            .setContentTitle(edificio)
            .setContentText(if (enReproduccion) "Reproduciendo audio-guía" else "Audio-guía en pausa")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(abrirApp)
            .setOngoing(enReproduccion)
            .addAction(android.R.drawable.ic_media_pause, "Pausar", pendienteAccion(ACCION_PAUSAR, 1))
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Detener", pendienteAccion(ACCION_DETENER, 2))
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        detenerPlayerSilencioso()
        scope.cancel()
        super.onDestroy()
    }
}
