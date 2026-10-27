package com.example.persistenciaroom_compose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import com.example.persistenciaroom_compose.ui.theme.PersistenciaRoom_ComposeTheme
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
            childColumns = ["edificioId"]
        )
    ]
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
            childColumns = ["ambienteId"]
        )
    ]
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
    suspend fun insertarEdificio(edificio: EdificioEntity): Long

    @Insert
    suspend fun insertarAmbiente(ambiente: AmbienteEntity): Long

    @Insert
    suspend fun insertarNota(nota: NotaEntity)

    @Update
    suspend fun actualizarEdificio(edificio: EdificioEntity)

    @Delete
    suspend fun eliminarEdificio(edificio: EdificioEntity)

    @Update
    suspend fun actualizarAmbiente(ambiente: AmbienteEntity)

    @Delete
    suspend fun eliminarAmbiente(ambiente: AmbienteEntity)

    @Update
    suspend fun actualizarNota(nota: NotaEntity)

    @Delete
    suspend fun eliminarNota(nota: NotaEntity)

    @Query("DELETE FROM NotaEntity WHERE ambienteId = :ambienteId")
    suspend fun eliminarNotasDeAmbiente(ambienteId: Int)

    @Query("DELETE FROM AmbienteEntity WHERE edificioId = :edificioId")
    suspend fun eliminarAmbientesDeEdificio(edificioId: Int)

    @Transaction
    suspend fun eliminarAmbienteConNotas(ambiente: AmbienteEntity) {
        eliminarNotasDeAmbiente(ambiente.id)
        eliminarAmbiente(ambiente)
    }

    @Transaction
    suspend fun eliminarEdificioConTodo(item: EdificioConAmbientes) {
        item.ambientes.forEach { ambiente ->
            eliminarNotasDeAmbiente(ambiente.id)
        }
        eliminarAmbientesDeEdificio(item.edificio.id)
        eliminarEdificio(item.edificio)
    }

    @Query("SELECT * FROM EdificioEntity")
    suspend fun obtenerTodos(): List<EdificioEntity>

    @Transaction
    @Query("SELECT * FROM EdificioEntity")
    suspend fun obtenerConAmbientes(): List<EdificioConAmbientes>

    @Transaction
    @Query("SELECT * FROM AmbienteEntity")
    suspend fun obtenerAmbientesConNotas(): List<AmbienteConNotas>
}

@Database(entities = [EdificioEntity::class, AmbienteEntity::class, NotaEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun edificioDao(): EdificioDao
}

fun crearBaseDeDatos(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_notas.db"
    ).build()
}

class EdificiosViewModel(private val dao: EdificioDao) : ViewModel() {

    private val _edificios = MutableStateFlow<List<EdificioConAmbientes>>(emptyList())
    val edificios: StateFlow<List<EdificioConAmbientes>> = _edificios

    private val _ambientesConNotas = MutableStateFlow<List<AmbienteConNotas>>(emptyList())
    val ambientesConNotas: StateFlow<List<AmbienteConNotas>> = _ambientesConNotas

    init {
        viewModelScope.launch {
            if (dao.obtenerTodos().isEmpty()) {
                val id1 = dao.insertarEdificio(
                    EdificioEntity(nombre = "Biblioteca Central", categoria = "Servicios")
                )
                val salaId = dao.insertarAmbiente(AmbienteEntity(edificioId = id1.toInt(), nombre = "Sala de lectura"))
                val hemerotecaId = dao.insertarAmbiente(AmbienteEntity(edificioId = id1.toInt(), nombre = "Hemeroteca"))

                val id2 = dao.insertarEdificio(
                    EdificioEntity(nombre = "Facultad de Ingeniería", categoria = "Académico")
                )
                val labId = dao.insertarAmbiente(
                    AmbienteEntity(edificioId = id2.toInt(), nombre = "Laboratorio de cómputo")
                )
                dao.insertarNota(NotaEntity(ambienteId = salaId.toInt(), texto = "El proyector no funciona"))
                dao.insertarNota(NotaEntity(ambienteId = salaId.toInt(), texto = "Se reserva los martes"))
                dao.insertarNota(NotaEntity(ambienteId = hemerotecaId.toInt(), texto = "Ventana rota en el ala norte"))
                dao.insertarNota(NotaEntity(ambienteId = labId.toInt(), texto = "Faltan teclados en el módulo 3"))
            }
            recargar()
        }
    }

    private suspend fun recargar() {
        _edificios.value = dao.obtenerConAmbientes()
        _ambientesConNotas.value = dao.obtenerAmbientesConNotas()
    }

    fun eliminarEdificio(item: EdificioConAmbientes) {
        viewModelScope.launch {
            dao.eliminarEdificioConTodo(item)
            recargar()
        }
    }

    fun renombrarEdificio(edificio: EdificioEntity) {
        viewModelScope.launch {
            dao.actualizarEdificio(edificio.copy(nombre = edificio.nombre + " (editado)"))
            recargar()
        }
    }

    fun eliminarAmbiente(ambiente: AmbienteEntity) {
        viewModelScope.launch {
            dao.eliminarAmbienteConNotas(ambiente)
            recargar()
        }
    }

    fun renombrarAmbiente(ambiente: AmbienteEntity) {
        viewModelScope.launch {
            dao.actualizarAmbiente(ambiente.copy(nombre = ambiente.nombre + " (editado)"))
            recargar()
        }
    }

    fun agregarAmbiente(edificioId: Int, nombre: String) {
        val limpio = nombre.trim()
        if (limpio.isEmpty()) return
        viewModelScope.launch {
            dao.insertarAmbiente(AmbienteEntity(edificioId = edificioId, nombre = limpio))
            recargar()
        }
    }

    fun agregarNota(ambienteId: Int, texto: String) {
        val limpio = texto.trim()
        if (limpio.isEmpty()) return
        viewModelScope.launch {
            dao.insertarNota(NotaEntity(ambienteId = ambienteId, texto = limpio))
            recargar()
        }
    }
}

class EdificiosViewModelFactory(private val dao: EdificioDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EdificiosViewModel(dao) as T
    }
}

@Composable
fun ListaEdificiosConAmbientes(viewModel: EdificiosViewModel) {
    val edificios by viewModel.edificios.collectAsState()
    val ambientesConNotas by viewModel.ambientesConNotas.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(edificios, key = { it.edificio.id }) { item ->
            var nombreAmbiente by remember(item.edificio.id) { mutableStateOf("") }
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = item.edificio.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row {
                    TextButton(onClick = { viewModel.renombrarEdificio(item.edificio) }) {
                        Text(text = "Renombrar")
                    }
                    TextButton(onClick = { viewModel.eliminarEdificio(item) }) {
                        Text(text = "Eliminar")
                    }
                }
                item.ambientes.forEach { ambiente ->
                    var textoNota by remember(ambiente.id) { mutableStateOf("") }
                    Text(text = "- ${ambiente.nombre}", fontSize = 14.sp)
                    Row {
                        TextButton(onClick = { viewModel.renombrarAmbiente(ambiente) }) {
                            Text(text = "Renombrar")
                        }
                        TextButton(onClick = { viewModel.eliminarAmbiente(ambiente) }) {
                            Text(text = "Eliminar")
                        }
                    }
                    Row {
                        OutlinedTextField(
                            value = textoNota,
                            onValueChange = { textoNota = it },
                            label = { Text(text = "Nueva nota") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = {
                                viewModel.agregarNota(ambiente.id, textoNota)
                                textoNota = ""
                            }
                        ) {
                            Text(text = "Añadir")
                        }
                    }
                }
                Row {
                    OutlinedTextField(
                        value = nombreAmbiente,
                        onValueChange = { nombreAmbiente = it },
                        label = { Text(text = "Nuevo ambiente") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            viewModel.agregarAmbiente(item.edificio.id, nombreAmbiente)
                            nombreAmbiente = ""
                        }
                    ) {
                        Text(text = "Añadir")
                    }
                }
            }
        }
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Notas por ambiente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                ambientesConNotas.forEach { grupo ->
                    Text(
                        text = grupo.ambiente.nombre,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    grupo.notas.forEach { nota ->
                        Text(text = "- ${nota.texto}", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = crearBaseDeDatos(applicationContext)
        val dao = database.edificioDao()
        val factory = EdificiosViewModelFactory(dao)

        setContent {
            PersistenciaRoom_ComposeTheme {
                val viewModel: EdificiosViewModel = viewModel(factory = factory)
                Surface(modifier = Modifier.fillMaxSize()) {
                    ListaEdificiosConAmbientes(viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PersistenciaRoom_ComposeTheme {
        Greeting("Android")
    }
}
