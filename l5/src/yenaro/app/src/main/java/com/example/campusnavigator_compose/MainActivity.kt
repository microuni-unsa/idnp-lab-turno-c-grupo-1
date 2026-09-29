package com.example.campusnavigator_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campusnavigator_compose.theme.CampusNavigatorComposeTheme

// Resuelto P1 + Propuesto 5: cuatro rutas con pestaña Perfil.
sealed class Screen(val route: String, val label: String) {
  object Home : Screen("home", "Home")

  object Edificios : Screen("edificios", "Edificios")

  object Mapa : Screen("mapa", "Mapa")

  object Perfil : Screen("perfil", "Perfil")
}

// Propuesto 2: única instancia de SeleccionViewModel en la raíz.
@Composable
fun MainScreen(vm: SeleccionViewModel = viewModel()) {
  val navController = rememberNavController()
  val items = listOf(Screen.Home, Screen.Edificios, Screen.Mapa, Screen.Perfil)

  Scaffold(
    bottomBar = {
      NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
          val icon =
            when (screen) {
              Screen.Home -> Icons.Default.Home
              Screen.Edificios -> Icons.Default.List
              Screen.Mapa -> Icons.Default.Place
              Screen.Perfil -> Icons.Default.Person
            }
          NavigationBarItem(
            selected = currentRoute == screen.route,
            onClick = { navController.navigate(screen.route) },
            icon = { Icon(icon, contentDescription = screen.label) },
            label = { Text(screen.label) },
          )
        }
      }
    },
  ) { padding ->
    NavHost(
      navController = navController,
      startDestination = Screen.Home.route,
      modifier = Modifier.padding(padding),
    ) {
      composable(Screen.Home.route) {
        HomeScreen(vm = vm)
      }
      composable(Screen.Edificios.route) {
        EdificiosScreen(vm = vm)
      }
      composable(Screen.Mapa.route) {
        MapaScreen()
      }
      composable(Screen.Perfil.route) {
        PerfilScreen()
      }
    }
  }
}

// Propuesto 4: Home lee directo del ViewModel compartido.
@Composable
fun HomeScreen(vm: SeleccionViewModel) {
  Column(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(text = "Bienvenido a la app de Edificios")
    Text(text = "Último edificio consultado: ${vm.seleccionado}")
  }
}

// Propuesto 3: Edificios escribe directo al ViewModel, sin lambda.
@Composable
fun EdificiosScreen(vm: SeleccionViewModel) {
  val edificios = listOf("Biblioteca Central", "Pabellón A", "Pabellón B", "Auditorio")
  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(16.dp),
  ) {
    items(edificios) { nombre ->
      Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(text = nombre, modifier = Modifier.weight(1f))
        Button(onClick = { vm.seleccionar(nombre) }) {
          Text("Ver")
        }
      }
    }
  }
}

// Resuelto P8: marcador de posición del mapa.
@Composable
fun MapaScreen() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Text(text = "Mapa de ubicaciones (pendiente de integrar)")
  }
}

// Propuesto 5: cuarta pestaña Perfil, placeholder simple.
@Composable
fun PerfilScreen() {
  Box(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    contentAlignment = Alignment.Center,
  ) {
    Text(text = "Perfil de usuario (pendiente de integrar)")
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CampusNavigatorComposeTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          MainScreen()
        }
      }
    }
  }
}
