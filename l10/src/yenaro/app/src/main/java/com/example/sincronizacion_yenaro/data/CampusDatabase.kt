package com.example.sincronizacion_yenaro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sincronizacion_yenaro.ConstantesCampus

@Database(entities = [SedeEntity::class], version = 1, exportSchema = false)
abstract class CampusDatabase : RoomDatabase() {
    abstract fun sedes(): SedeDao

    companion object {
        @Volatile
        private var UNICA: CampusDatabase? = null

        fun abrir(ctx: Context): CampusDatabase {
            return UNICA ?: synchronized(this) {
                val creada = Room.databaseBuilder(
                    ctx.applicationContext,
                    CampusDatabase::class.java,
                    ConstantesCampus.NOMBRE_BD
                ).build()
                UNICA = creada
                creada
            }
        }
    }
}
