package com.example.planocampus_yenaro

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Immutable
data class MarcadorPlano(
  val fx: Float,
  val fy: Float,
  val radioDp: Float,
  val titulo: String,
  val descripcion: String
)

class PlanoViewModel(marcadoresIniciales: List<MarcadorPlano>) : ViewModel() {
  private val _marcadores = MutableStateFlow(marcadoresIniciales)
  val marcadores: StateFlow<List<MarcadorPlano>> = _marcadores.asStateFlow()

  private val _seleccionado = MutableStateFlow<MarcadorPlano?>(null)
  val seleccionado: StateFlow<MarcadorPlano?> = _seleccionado.asStateFlow()

  fun seleccionar(marcador: MarcadorPlano) {
    _seleccionado.value = marcador
  }

  fun limpiarSeleccion() {
    _seleccionado.value = null
  }
}
