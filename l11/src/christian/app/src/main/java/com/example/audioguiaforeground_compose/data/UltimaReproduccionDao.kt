package com.example.audioguiaforeground_compose.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface UltimaReproduccionDao {
    @Upsert
    suspend fun guardar(ultima: UltimaReproduccionEntity)

    @Query("SELECT nombreEdificio FROM ultima_reproduccion WHERE id = 1")
    suspend fun obtenerUltimoEdificio(): String?
}
