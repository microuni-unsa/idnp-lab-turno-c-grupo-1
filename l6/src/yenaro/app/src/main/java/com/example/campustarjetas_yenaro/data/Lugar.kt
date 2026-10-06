package com.example.campustarjetas_yenaro.data

import com.example.campustarjetas_yenaro.R

data class Lugar(
    val nombre: String,
    val descripcion: String,
    val imagen: Int,
    val fondo: Long
)

val lugares = listOf(
    Lugar(
        "Biblioteca Central",
        "Estudio y prestamo",
        R.drawable.biblioteca,
        0xFFE3F2FD
    ),
    Lugar(
        "Comedor",
        "Alimentacion diaria",
        R.drawable.comedor,
        0xFFFFF3E0
    ),
    Lugar(
        "Pabellon A",
        "Aulas y laboratorios",
        R.drawable.pabellon_a,
        0xFFE8F5E9
    ),
    Lugar(
        "Auditorio",
        "Eventos y ceremonias",
        R.drawable.auditorio,
        0xFFF3E5F5
    )
)
