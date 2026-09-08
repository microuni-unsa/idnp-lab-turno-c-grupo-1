package com.example.helloworldcompose

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helloworldcompose.theme.HelloWorldComposeTheme

object BookTrackerConstants {
    const val FILE_NAME: String = "libro_actual.txt"
    const val LOG_TAG: String = "BookTracker"
    const val LOG_SAVED_PREFIX: String = "Archivo guardado exitosamente:\n"

    const val STUDENT_NAME: String = "Christian Mestas"
    const val APP_TITLE: String = "Registro de Lectura Actual"
    const val SECTION_FORM_TITLE: String = "Datos del Libro"
    const val SECTION_RECORD_TITLE: String = "Registro en Almacenamiento Interno"

    const val LABEL_TITLE: String = "Título del libro"
    const val LABEL_AUTHOR: String = "Autor"
    const val LABEL_PAGES: String = "Número de páginas leídas"

    const val BUTTON_SAVE_LABEL: String = "Guardar"

    const val FIELD_TITLE_PREFIX: String = "Título: "
    const val FIELD_AUTHOR_PREFIX: String = "Autor: "
    const val FIELD_PAGES_PREFIX: String = "Páginas leídas: "
    const val NEWLINE: String = "\n"

    const val STATUS_IDLE_HINT: String = "Complete los campos y presione 'Guardar'."
    const val SUCCESS_SAVED_MESSAGE: String = "¡Datos guardados correctamente en almacenamiento interno!"
    const val ERROR_EMPTY_TITLE: String = "El título del libro no puede estar vacío."
    const val ERROR_EMPTY_AUTHOR: String = "El autor no puede estar vacío."
    const val ERROR_EMPTY_PAGES: String = "El número de páginas leídas no puede estar vacío."
    const val ERROR_INVALID_PAGES_NOT_NUMBER: String = "El número de páginas debe ser un valor numérico."
    const val ERROR_INVALID_PAGES_NEGATIVE: String = "El número de páginas debe ser mayor a cero."
    const val ERROR_SAVING_PREFIX: String = "Error al guardar el archivo: "

    const val RECORD_PLACEHOLDER: String = "Ningún registro guardado aún."
    const val RECORD_NONE: String = "Sin datos disponibles."

    const val SPACING_SMALL_DP: Int = 8
    const val SPACING_MEDIUM_DP: Int = 16
}

sealed interface RecordUiState {
    data object Idle : RecordUiState
    data class Success(val message: String, val record: String) : RecordUiState
    data class Error(val errorMessage: String) : RecordUiState
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloWorldComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BookTrackerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun BookTrackerApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var bookTitle by remember { mutableStateOf("") }
    var bookAuthor by remember { mutableStateOf("") }
    var pagesRead by remember { mutableStateOf("") }
    var uiState by remember { mutableStateOf<RecordUiState>(RecordUiState.Idle) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(BookTrackerConstants.SPACING_MEDIUM_DP.dp),
        verticalArrangement = Arrangement.spacedBy(BookTrackerConstants.SPACING_MEDIUM_DP.dp)
    ) {
        Greeting(
            name = BookTrackerConstants.STUDENT_NAME,
            modifier = Modifier.padding(bottom = BookTrackerConstants.SPACING_SMALL_DP.dp)
        )

        Text(
            text = BookTrackerConstants.APP_TITLE,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = BookTrackerConstants.SECTION_FORM_TITLE,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = bookTitle,
            onValueChange = { bookTitle = it },
            label = { Text(BookTrackerConstants.LABEL_TITLE) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = bookAuthor,
            onValueChange = { bookAuthor = it },
            label = { Text(BookTrackerConstants.LABEL_AUTHOR) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pagesRead,
            onValueChange = { pagesRead = it },
            label = { Text(BookTrackerConstants.LABEL_PAGES) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (bookTitle.isBlank()) {
                    uiState = RecordUiState.Error(BookTrackerConstants.ERROR_EMPTY_TITLE)
                } else if (bookAuthor.isBlank()) {
                    uiState = RecordUiState.Error(BookTrackerConstants.ERROR_EMPTY_AUTHOR)
                } else if (pagesRead.isBlank()) {
                    uiState = RecordUiState.Error(BookTrackerConstants.ERROR_EMPTY_PAGES)
                } else {
                    val parsedPages = pagesRead.trim().toIntOrNull()
                    if (parsedPages == null) {
                        uiState = RecordUiState.Error(BookTrackerConstants.ERROR_INVALID_PAGES_NOT_NUMBER)
                    } else if (parsedPages <= 0) {
                        uiState = RecordUiState.Error(BookTrackerConstants.ERROR_INVALID_PAGES_NEGATIVE)
                    } else {
                        uiState = executeSaveRecord(
                            context = context,
                            title = bookTitle.trim(),
                            author = bookAuthor.trim(),
                            pages = parsedPages
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(BookTrackerConstants.BUTTON_SAVE_LABEL)
        }

        when (val state = uiState) {
            is RecordUiState.Idle -> {
                Text(
                    text = BookTrackerConstants.STATUS_IDLE_HINT,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            is RecordUiState.Success -> {
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            is RecordUiState.Error -> {
                Text(
                    text = state.errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(BookTrackerConstants.SPACING_SMALL_DP.dp))

        Text(
            text = BookTrackerConstants.SECTION_RECORD_TITLE,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(BookTrackerConstants.SPACING_MEDIUM_DP.dp)) {
                when (val state = uiState) {
                    is RecordUiState.Idle -> {
                        Text(
                            text = BookTrackerConstants.RECORD_PLACEHOLDER,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    is RecordUiState.Success -> {
                        Text(
                            text = state.record,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    is RecordUiState.Error -> {
                        Text(
                            text = BookTrackerConstants.RECORD_NONE,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

private fun executeSaveRecord(
    context: Context,
    title: String,
    author: String,
    pages: Int
): RecordUiState {
    val fileContent = buildString {
        append(BookTrackerConstants.FIELD_TITLE_PREFIX).append(title).append(BookTrackerConstants.NEWLINE)
        append(BookTrackerConstants.FIELD_AUTHOR_PREFIX).append(author).append(BookTrackerConstants.NEWLINE)
        append(BookTrackerConstants.FIELD_PAGES_PREFIX).append(pages).append(BookTrackerConstants.NEWLINE)
    }

    return try {
        context.openFileOutput(BookTrackerConstants.FILE_NAME, Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(fileContent.toByteArray(Charsets.UTF_8))
        }
        Log.d(BookTrackerConstants.LOG_TAG, "${BookTrackerConstants.LOG_SAVED_PREFIX}$fileContent")
        RecordUiState.Success(
            message = BookTrackerConstants.SUCCESS_SAVED_MESSAGE,
            record = fileContent
        )
    } catch (exception: Exception) {
        val errorText = "${BookTrackerConstants.ERROR_SAVING_PREFIX}${exception.localizedMessage}"
        Log.e(BookTrackerConstants.LOG_TAG, errorText, exception)
        RecordUiState.Error(errorText)
    }
}


@Preview(showBackground = true)
@Composable
fun BookTrackerAppPreview() {
    HelloWorldComposeTheme {
        BookTrackerApp()
    }
}
