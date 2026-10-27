package com.example.persistenciaroom_yenaro

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import com.example.persistenciaroom_yenaro.theme.PersistenciaRoomYenaroTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Entity
data class EdificioEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val nombre: String,
  val categoria: String
)

@Entity(
  foreignKeys = [
    ForeignKey(
      entity = EdificioEntity::class,
      parentColumns = ["id"],
      childColumns = ["edificioId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("edificioId")]
)
data class AmbienteEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val edificioId: Int,
  val nombre: String
)

@Entity(
  foreignKeys = [
    ForeignKey(
      entity = AmbienteEntity::class,
      parentColumns = ["id"],
      childColumns = ["ambienteId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("ambienteId")]
)
data class NotaEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val ambienteId: Int,
  val texto: String
)

data class EdificioConAmbientes(
  @Embedded val edificio: EdificioEntity,
  @Relation(
    parentColumn = "id",
    entityColumn = "edificioId"
  )
  val ambientes: List<AmbienteEntity>
)

data class AmbienteConNotas(
  @Embedded val ambiente: AmbienteEntity,
  @Relation(
    parentColumn = "id",
    entityColumn = "ambienteId"
  )
  val notas: List<NotaEntity>
)

@Dao
interface EdificioDao {
  @Insert
  suspend fun insertarEdificio(
    edificio: EdificioEntity
  ): Long

  @Insert
  suspend fun insertarAmbiente(
    ambiente: AmbienteEntity
  ): Long

  @Insert
  suspend fun insertarNota(nota: NotaEntity)

  @Update
  suspend fun actualizarEdificio(edificio: EdificioEntity)

  @Update
  suspend fun actualizarAmbiente(ambiente: AmbienteEntity)

  @Delete
  suspend fun eliminarEdificio(edificio: EdificioEntity)

  @Delete
  suspend fun eliminarAmbiente(ambiente: AmbienteEntity)

  @Delete
  suspend fun eliminarNota(nota: NotaEntity)

  @Query("SELECT * FROM EdificioEntity")
  suspend fun obtenerTodos(): List<EdificioEntity>

  @Transaction
  @Query("SELECT * FROM EdificioEntity")
  suspend fun obtenerConAmbientes(): List<EdificioConAmbientes>

  @Transaction
  @Query("SELECT * FROM AmbienteEntity")
  suspend fun obtenerAmbientesConNotas(): List<AmbienteConNotas>
}

@Database(
  entities = [
    EdificioEntity::class,
    AmbienteEntity::class,
    NotaEntity::class
  ],
  version = 1
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun edificioDao(): EdificioDao
}

fun crearBaseDeDatos(context: Context): AppDatabase {
  return Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "app.db"
  ).build()
}

class EdificiosViewModel(private val dao: EdificioDao) : ViewModel() {
  private val _edificios =
    MutableStateFlow<List<EdificioConAmbientes>>(emptyList())
  val edificios: StateFlow<List<EdificioConAmbientes>> = _edificios

  private val _ambientes =
    MutableStateFlow<List<AmbienteConNotas>>(emptyList())
  val ambientes: StateFlow<List<AmbienteConNotas>> = _ambientes

  init {
    viewModelScope.launch {
      if (dao.obtenerTodos().isEmpty()) {
        sembrarDatos()
      }
      recargar()
    }
  }

  private suspend fun sembrarDatos() {
    val id1 = dao.insertarEdificio(
      EdificioEntity(
        nombre = "Biblioteca Central",
        categoria = "Servicios"
      )
    )
    val sala = dao.insertarAmbiente(
      AmbienteEntity(
        edificioId = id1.toInt(),
        nombre = "Sala de lectura"
      )
    )
    dao.insertarNota(
      NotaEntity(
        ambienteId = sala.toInt(),
        texto = "El proyector no funciona"
      )
    )
    dao.insertarNota(
      NotaEntity(
        ambienteId = sala.toInt(),
        texto = "Se reserva los martes"
      )
    )
    val hemeroteca = dao.insertarAmbiente(
      AmbienteEntity(
        edificioId = id1.toInt(),
        nombre = "Hemeroteca"
      )
    )
    dao.insertarNota(
      NotaEntity(
        ambienteId = hemeroteca.toInt(),
        texto = "Aforo maximo: 40 personas"
      )
    )
    val id2 = dao.insertarEdificio(
      EdificioEntity(
        nombre = "Facultad de Ingeniería",
        categoria = "Académico"
      )
    )
    val lab = dao.insertarAmbiente(
      AmbienteEntity(
        edificioId = id2.toInt(),
        nombre = "Laboratorio de cómputo"
      )
    )
    dao.insertarNota(
      NotaEntity(
        ambienteId = lab.toInt(),
        texto = "30 PCs operativas"
      )
    )
  }

  private suspend fun recargar() {
    _edificios.value = dao.obtenerConAmbientes()
    _ambientes.value = dao.obtenerAmbientesConNotas()
  }

  fun renombrarAmbiente(ambiente: AmbienteEntity, nuevo: String) {
    viewModelScope.launch {
      dao.actualizarAmbiente(ambiente.copy(nombre = nuevo))
      recargar()
    }
  }

  fun eliminarAmbiente(ambiente: AmbienteEntity) {
    viewModelScope.launch {
      dao.eliminarAmbiente(ambiente)
      recargar()
    }
  }

  fun eliminarEdificio(item: EdificioConAmbientes) {
    viewModelScope.launch {
      dao.eliminarEdificio(item.edificio)
      recargar()
    }
  }

  fun agregarNota(ambienteId: Int, texto: String) {
    viewModelScope.launch {
      dao.insertarNota(NotaEntity(ambienteId = ambienteId, texto = texto))
      recargar()
    }
  }

  fun eliminarNota(nota: NotaEntity) {
    viewModelScope.launch {
      dao.eliminarNota(nota)
      recargar()
    }
  }
}

val Crema = Color(0xFFFFF6E5)
val AmarilloNeo = Color(0xFFFFD54F)
val AzulNeo = Color(0xFF7DA2FF)
val VerdeNeo = Color(0xFFA8E6A1)
val RosaNeo = Color(0xFFFF9ECF)
val NegroNeo = Color(0xFF111111)

fun colorPorCategoria(categoria: String): Color {
  return when (categoria) {
    "Servicios" -> AmarilloNeo
    "Académico" -> AzulNeo
    else -> VerdeNeo
  }
}

@Composable
fun NeoCard(
  color: Color,
  content: @Composable ColumnScope.() -> Unit
) {
  Box(modifier = Modifier.padding(end = 6.dp, bottom = 14.dp)) {
    Box(
      modifier = Modifier.matchParentSize()
        .offset(x = 6.dp, y = 6.dp)
        .background(NegroNeo, RoundedCornerShape(12.dp))
    )
    Column(
      modifier = Modifier
        .border(3.dp, NegroNeo, RoundedCornerShape(12.dp))
        .background(color, RoundedCornerShape(12.dp))
        .padding(20.dp),
      content = content
    )
  }
}

@Composable
fun NeoButton(texto: String, onClick: () -> Unit) {
  OutlinedButton(
    onClick = onClick,
    border = BorderStroke(2.dp, NegroNeo),
    shape = RoundedCornerShape(8.dp),
    colors = ButtonDefaults.outlinedButtonColors(
      containerColor = Color.White,
      contentColor = NegroNeo
    )
  ) { Text(texto, fontWeight = FontWeight.Bold) }
}

class EdificiosViewModelFactory(
  private val dao: EdificioDao
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST")
    return EdificiosViewModel(dao) as T
  }
}

@Composable
fun ListaEdificiosConAmbientes(viewModel: EdificiosViewModel) {
  val edificios by viewModel.edificios.collectAsState()
  val ambientes by viewModel.ambientes.collectAsState()
  var editando by remember { mutableStateOf<AmbienteEntity?>(null) }
  var nombreNuevo by remember { mutableStateOf("") }
  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(16.dp)
  ) {
    item {
      Text(
        text = "Edificios y ambientes",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = NegroNeo
      )
    }
    items(edificios, key = { "edificio-${it.edificio.id}" }) { item ->
      NeoCard(color = colorPorCategoria(item.edificio.categoria)) {
        Text(
          text = item.edificio.nombre,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = NegroNeo
        )
        Text(
          text = item.edificio.categoria.uppercase(),
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = NegroNeo
        )
        item.ambientes.forEach { ambiente ->
          Row(modifier = Modifier.fillMaxWidth()) {
            Text(
              text = "- ${ambiente.nombre}",
              fontSize = 14.sp,
              color = NegroNeo,
              modifier = Modifier.weight(1f).padding(vertical = 14.dp)
            )
            NeoButton(texto = "Editar") {
              editando = ambiente
              nombreNuevo = ""
            }
            NeoButton(texto = "Borrar") {
              viewModel.eliminarAmbiente(ambiente)
            }
          }
          Row(modifier = Modifier.fillMaxWidth()) {
            val total = ambientes.firstOrNull {
              it.ambiente.id == ambiente.id
            }?.notas?.size ?: 0
            NeoButton(texto = "+ Nota") {
              viewModel.agregarNota(
                ambiente.id,
                "Nota ${total + 1}"
              )
            }
          }
        }
        NeoButton(texto = "Eliminar edificio") {
          viewModel.eliminarEdificio(item)
        }
      }
    }
    item {
      Text(
        text = "Ambientes y notas",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = NegroNeo,
        modifier = Modifier.padding(top = 8.dp)
      )
    }
    items(ambientes, key = { "ambiente-${it.ambiente.id}" }) { grupo ->
      NeoCard(color = Color.White) {
        Text(
          text = "${grupo.ambiente.nombre} (${grupo.notas.size})",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp,
          color = NegroNeo
        )
        grupo.notas.forEach { nota ->
          Row(modifier = Modifier.fillMaxWidth()) {
            Text(
              text = "- ${nota.texto}",
              fontSize = 14.sp,
              color = NegroNeo,
              modifier = Modifier.weight(1f).padding(vertical = 14.dp)
            )
            NeoButton(texto = "Quitar") {
              viewModel.eliminarNota(nota)
            }
          }
        }
      }
    }
  }
  editando?.let { ambiente ->
    AlertDialog(
      onDismissRequest = { editando = null },
      containerColor = Color.White,
      titleContentColor = NegroNeo,
      textContentColor = NegroNeo,
      title = { Text("Renombrar ambiente") },
      text = {
        OutlinedTextField(
          value = nombreNuevo,
          onValueChange = { nombreNuevo = it },
          label = { Text("Nuevo nombre") }
        )
      },
      confirmButton = {
        NeoButton(texto = "Guardar") {
          viewModel.renombrarAmbiente(ambiente, nombreNuevo)
          editando = null
        }
      },
      dismissButton = {
        NeoButton(texto = "Cancelar") { editando = null }
      }
    )
  }
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val database = crearBaseDeDatos(applicationContext)
    val dao = database.edificioDao()
    val factory = EdificiosViewModelFactory(dao)
    setContent {
      PersistenciaRoomYenaroTheme {
        val viewModel: EdificiosViewModel = viewModel(
          factory = factory
        )
        Surface(modifier = Modifier.fillMaxSize(), color = Crema) {
          ListaEdificiosConAmbientes(viewModel)
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ListaEdificiosPreview() {
  PersistenciaRoomYenaroTheme {
    Text("PersistenciaRoom Yenaro")
  }
}
