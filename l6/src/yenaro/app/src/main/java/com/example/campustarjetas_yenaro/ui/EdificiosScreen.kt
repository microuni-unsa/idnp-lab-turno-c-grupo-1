package com.example.campustarjetas_yenaro.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.campustarjetas_yenaro.data.Lugar

@Composable
fun EdificiosScreen(
    lista: List<Lugar>,
    onVer: (Lugar) -> Unit
) {
    LazyColumn(
        verticalArrangement =
            Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(lista) { lugar ->
            Box(
                modifier = Modifier.clickable {
                    onVer(lugar)
                }
            ) {
                TarjetaCompacta(
                    nombre = lugar.nombre,
                    imagen = painterResource(
                        id = lugar.imagen
                    )
                )
            }
        }
    }
}
