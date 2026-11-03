package com.example.sincronizacion_yenaro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SedeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUna(sede: SedeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarVarias(sedes: List<SedeEntity>)

    @Update
    suspend fun retocar(sede: SedeEntity)

    @Query("SELECT * FROM sedes ORDER BY codigo ASC")
    fun vigilarSedes(): Flow<List<SedeEntity>>

    @Query("SELECT * FROM sedes ORDER BY codigo ASC")
    suspend fun listarTodo(): List<SedeEntity>

    @Query("SELECT COUNT(*) FROM sedes")
    suspend fun total(): Int

    @Query("UPDATE sedes SET alDia = 0")
    suspend fun marcarTodoPendiente()
}
