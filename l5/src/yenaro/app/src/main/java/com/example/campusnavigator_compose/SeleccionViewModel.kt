package com.example.campusnavigator_compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Propuesto 1: ViewModel compartido con mutableStateOf simple.
class SeleccionViewModel : ViewModel() {
  var seleccionado by mutableStateOf("Ninguno")
    private set

  fun seleccionar(nombre: String) {
    seleccionado = nombre
  }
}
