package com.example.sincronizacion_yenaro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sedes")
data class SedeEntity(
    @PrimaryKey(autoGenerate = true)
    val codigo: Int = 0,
    val titulo: String,
    val rubro: String,
    val alDia: Boolean = false
)
