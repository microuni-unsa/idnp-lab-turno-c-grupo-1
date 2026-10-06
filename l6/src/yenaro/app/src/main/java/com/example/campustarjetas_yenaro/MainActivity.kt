package com.example.campustarjetas_yenaro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campustarjetas_yenaro.data.Lugar
import com.example.campustarjetas_yenaro.data.lugares
import com.example.campustarjetas_yenaro.theme.CampusTarjetasYenaroTheme
import com.example.campustarjetas_yenaro.ui.DetalleLugarScreen
import com.example.campustarjetas_yenaro.ui.EdificiosScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusTarjetasYenaroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var elegido by remember { mutableStateOf<Lugar?>(null) }
    if (elegido == null) {
        EdificiosScreen(lugares) { elegido = it }
    } else {
        Column {
            Text(
                text = "< Volver",
                modifier = Modifier
                    .clickable { elegido = null }
                    .padding(16.dp)
            )
            DetalleLugarScreen(elegido!!)
        }
    }
}
