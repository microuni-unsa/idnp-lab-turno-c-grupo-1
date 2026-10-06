package com.example.campustarjetas_yenaro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campustarjetas_yenaro.data.Lugar

@Composable
fun DetalleLugarScreen(lugar: Lugar) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(
                id = lugar.imagen
            ),
            contentDescription = lugar.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
        )
        Text(
            text = lugar.nombre,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = lugar.descripcion,
            fontSize = 16.sp
        )
        TarjetaCompacta(
            nombre = lugar.nombre,
            imagen = painterResource(
                id = lugar.imagen
            )
        )
    }
}
