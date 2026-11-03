package com.example.sincronizacionservicio_compose.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EdificioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEdificio(edificio: EdificioEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEdificios(edificios: List<EdificioEntity>)

    @Update
    suspend fun actualizarEdificio(edificio: EdificioEntity)

    @Query("SELECT * FROM edificios ORDER BY id ASC")
    fun observarEdificios(): Flow<List<EdificioEntity>>

    @Query("SELECT * FROM edificios ORDER BY id ASC")
    suspend fun obtenerTodos(): List<EdificioEntity>

    @Query("SELECT COUNT(*) FROM edificios")
    suspend fun contar(): Int

    @Query("UPDATE edificios SET sincronizado = 0")
    suspend fun reiniciarSincronizacion()
}
