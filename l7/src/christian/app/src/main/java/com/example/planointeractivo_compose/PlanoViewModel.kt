package com.example.planointeractivo_compose

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

data class Marcador(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val salaIndex: Int
)

class PlanoViewModel(application: Application) : AndroidViewModel(application) {
    private val _marcadores = MutableStateFlow<List<Marcador>>(emptyList())
    val marcadores: StateFlow<List<Marcador>> = _marcadores.asStateFlow()

    private val _marcadorSeleccionado = MutableStateFlow<Marcador?>(null)
    val marcadorSeleccionado: StateFlow<Marcador?> = _marcadorSeleccionado.asStateFlow()

    init {
        cargarMarcadoresDesdeAssets()
    }

    private fun cargarMarcadoresDesdeAssets() {
        val jsonString = getApplication<Application>()
            .assets
            .open("marcadores.json")
            .bufferedReader()
            .use { it.readText() }

        val jsonArray = JSONArray(jsonString)
        val lista = mutableListOf<Marcador>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val id = obj.getString("id")
            val titulo = obj.getString("titulo")
            val descripcion = obj.getString("descripcion")
            val salaIndex = obj.getInt("salaIndex")

            lista.add(
                Marcador(
                    id = id,
                    titulo = titulo,
                    descripcion = descripcion,
                    salaIndex = salaIndex
                )
            )
        }
        _marcadores.value = lista
    }

    fun seleccionarMarcadorEn(offset: Offset, centros: List<Offset>, radio: Float) {
        val indice = centros.indexOfFirst { centro ->
            (offset - centro).getDistance() <= radio
        }
        if (indice in _marcadores.value.indices) {
            _marcadorSeleccionado.value = _marcadores.value[indice]
        } else {
            _marcadorSeleccionado.value = null
        }
    }

    fun cerrarDialogo() {
        _marcadorSeleccionado.value = null
    }
}
