package com.example.audioguia_yenaro.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "ultimo_edificio")
data class UltimoEdificioEntity(
    @PrimaryKey val id: Int = 1,
    val nombre: String
)

@Dao
interface UltimoEdificioDao {
    @Query("SELECT * FROM ultimo_edificio WHERE id = 1")
    suspend fun obtener(): UltimoEdificioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(e: UltimoEdificioEntity)
}

@Database(entities = [UltimoEdificioEntity::class], version = 1, exportSchema = false)
abstract class GuiaDatabase : RoomDatabase() {
    abstract fun ultimoEdificioDao(): UltimoEdificioDao
}
