##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
## Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 1

# GUÍA DE LABORATORIO

## INFORMACIÓN BÁSICA

## ASIGNATURA: INTRODUCCION AL DESARROLLO DE NUEVAS PLATAFORMAS (E)

## TÍTULO DE LA

## PRÁCTICA:

## Tarjetas de lugares con Row, Column, Box y Modifier en Jetpack Compose

## NÚMERO DE

## PRÁCTICA:

## 6 AÑO LECTIVO: 2026

## NRO.

## SEMESTRE:

## B

## TIPO DE

## PRÁCTICA:

## INDIVIDUAL

## GRUPAL X MÁXIMO DE ESTUDIANTES 2

## FECHA INICIO: 06/10/2026 FECHA FIN: 10/10/2026 DURACIÓN: 100 minutos

## RECURSOS A UTILIZAR:

## Android Studio (Quail o superior), Kotlin, Jetpack Compose, emulador Android o dispositivo físico (API

## 34 o superior)

## DOCENTE(s):

## Roxana Evelyn Limache Calatayud

## OBJETIVOS/TEMAS Y COMPETENCIAS

## OBJETIVOS:

## - Comprender los Composables básicos de disposición (Row, Column, Box) y su combinación con

## Modifier para construir interfaces.

## - Implementar tarjetas (cards) que muestren una imagen, un título y una descripción, en dos estilos de

## disposición distintos.

## - Aplicar recorte de formas (clip), fondos y tipografía mediante una cadena de Modifier, entendiendo

## cómo afecta el orden de aplicación.

## TEMAS:

## - Row, Column y Box en Jetpack Compose

## - Modifier: size, clip, background, padding y su orden de aplicación

## - Image, ContentScale y formas (Shape) en Compose

## COMPETENCIAS

## C.a Aplica de forma flexible técnicas, métodos, principios, normas, estándares y

## herramientas de ingeniería necesarias para la construcción de software e implementación

## de sistemas de información.

## C.b Investiga nuevos modelos, metodologías, técnicas, herramientas y tecnologías por ser

## necesarias para mantener la vigencia en el desempeño profesional.

## C.c

## C.d

## CONTENIDO DE LA GUÍA


##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
### Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 2

## I. MARCO CONCEPTUAL

## Revisar los siguientes recursos antes de la sesión:

- Video: "POSICIONA elementos con JETPACK COMPOSE de manera FÁCIL"

## https://www.youtube.com/watch?v=vTNFb44AHWs

- Android Developers, conceptos básicos de layout en Compose:

## https://developer.android.com/develop/ui/compose/layouts/basics

### De los Layouts clásicos a los Composables de disposición

## En el enfoque clásico de Views, una interfaz se armaba combinando LinearLayout, RelativeLayout

## o ConstraintLayout en un archivo XML, colocando dentro ImageView y TextView con atributos

## como orientation o layout_gravity. En Jetpack Compose no existen archivos XML de layout: la

## disposición se define directamente en Kotlin combinando tres Composables fundamentales —

## Row, Column y Box — cada uno con una función distinta, como se resume en la Figura 1.

#### Figura 1. Los tres Composables básicos de disposición en Jetpack Compose (elaboración propia).

### Row, Column y Box

## Row organiza a sus hijos en una fila horizontal (equivalente a un LinearLayout con

## orientation=“horizontal”); Column los apila verticalmente (equivalente a orientation=“vertical”); y

## Box los superpone unos sobre otros en el mismo espacio, permitiendo, por ejemplo, colocar un

## texto encima de una imagen. Los tres aceptan parámetros como horizontalAlignment,

## verticalArrangement o contentAlignment para controlar cómo se distribuyen y alinean sus

## elementos internos.

### Modifier: el equivalente a los atributos XML

## Donde antes se usaban atributos XML (layout_width, layout_margin, background, etc.), en

## Compose se usa una cadena de funciones Modifier encadenadas con el operador punto. Cada

## Modifier envuelve al anterior y produce un nuevo resultado, por lo que el orden en que se escriben

## cambia el efecto final: no es lo mismo aplicar .clip() antes que después de .background(), como se

## muestra en la Figura 2. Entre los Modifier más usados para tarjetas están .size(), .clip(CircleShape)

## para recortar una imagen en forma circular, .background(color) y .padding().


##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
### Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 3

#### Figura 2. Una cadena de Modifier aplicada paso a paso sobre un mismo elemento (elaboración propia).

## II. EJERCICIO/PROBLEMA RESUELTO POR EL DOCENTE

### A continuación se detallan, paso a paso, las instrucciones para construir dos estilos de tarjeta (card) que

### muestren un lugar del campus, combinando Row, Box, Image y una cadena de Modifier. También puede

### apoyarse en el siguiente video tutorial:

## Video de referencia: "Usando Cards de Material Design en Jetpack Compose"

## https://www.youtube.com/watch?v=iZiXpWRIl3U

## Paso 1. Crear un nuevo proyecto.

### nombre sugerido para el proyecto: “TarjetasCampus_Compose”, Package name:

### com.example.tarjetascampus_compose, Language: Kotlin, Minimum SDK: el que venga por defecto en la

### plantilla (recuerden usar un emulador o dispositivo con API 34 o superior, según la sección de Recursos a

### utilizar).

## Paso 2. Agregar, debajo del package y antes de la clase MainActivity, todas las importaciones que

## se usarán en este ejercicio:

### Código (importaciones):

```
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tarjetascampus_compose.ui.theme.TarjetasCampus_ComposeTheme
```

##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
### Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 4

## Paso 3. Agregar 2 o 3 imágenes de muestra a res/drawable (por ejemplo fotos propias de edificios

## del campus; si no cuentas con imágenes, puedes usar un Modifier.background(color) como

## reemplazo temporal del Image).

## Paso 4. Crear el Composable TarjetaLugar, que recibirá el nombre, la descripción, la imagen y el

## color de fondo del lugar, y que representará el primer estilo de tarjeta: una franja horizontal con

## imagen circular a la izquierda. Por ahora dejamos el cuerpo vacío; lo iremos completando en los

## siguientes pasos:

### Código (TarjetaLugar – Paso 4):

```
@Composable
fun TarjetaLugar(
nombre: String,
descripcion: String,
imagen: Painter,
colorFondo: Color
) {
// el contenido se agrega en el Paso 5
}
```
## Paso 5. Dentro de TarjetaLugar, envolver el contenido en un Surface con bordes redondeados que

## use el color de fondo recibido, y dentro de ese Surface, un Row alineado verticalmente al centro y

## con un poco de padding:

### Código (TarjetaLugar – Paso 5):

```
@Composable
fun TarjetaLugar(
nombre: String,
descripcion: String,
imagen: Painter,
colorFondo: Color
) {
Surface(
shape = RoundedCornerShape(24.dp),
color = colorFondo
) {
Row(
verticalAlignment = Alignment.CenterVertically,
modifier = Modifier.padding(16.dp)
) {
// el contenido se agrega en el Paso 6
}
}
}
```
## Paso 6. Dentro del Row, agregar una imagen circular, un espacio y una Column con el nombre (en

## negrita) y la descripción del lugar, respetando el orden de Modifier resumido en la Figura 2. Con

## esto, TarjetaLugar queda completo:

### Código (TarjetaLugar completo):

```
@Composable
fun TarjetaLugar(
nombre: String,
descripcion: String,
imagen: Painter,
colorFondo: Color
```

##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
### Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 5

##### ) {

```
Surface(
shape = RoundedCornerShape(24.dp),
color = colorFondo
) {
Row(
verticalAlignment = Alignment.CenterVertically,
modifier = Modifier.padding(16.dp)
) {
Image(
painter = imagen,
contentDescription = nombre,
contentScale = ContentScale.Crop,
modifier = Modifier
.size(90.dp)
.clip(CircleShape)
)
Spacer(modifier = Modifier.width(16.dp))
Column {
Text(
text = nombre,
fontWeight = FontWeight.Bold,
fontSize = 20.sp
)
Text(
text = descripcion,
fontSize = 14.sp
)
}
}
}
}
```
## Paso 7. Llamar a TarjetaLugar dentro de una Column con espaciado entre elementos, usando al

## menos dos lugares distintos del campus:

### Código (uso de TarjetaLugar):

```
@Composable
fun ListaTarjetasLugar() {
Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
TarjetaLugar(
nombre = "Biblioteca Central",
descripcion = "Zona de estudio y préstamo de libros",
imagen = painterResource(id = R.drawable.biblioteca),
colorFondo = Color(0xFFE3F2FD)
)
TarjetaLugar(
nombre = "Comedor Universitario",
descripcion = "Servicio de alimentación para estudiantes",
imagen = painterResource(id = R.drawable.comedor),
colorFondo = Color(0xFFFFF3E0)
)
}
}
```
## Paso 8. Crear un segundo Composable TarjetaCompacta, que reciba el nombre del lugar y su

## imagen, y que represente el estilo apilado: un Box que ocupe todo el ancho disponible, con una

## altura fija y bordes redondeados, conteniendo una Image de fondo que llene todo el espacio del

## Box, tal como se resume la superposición de elementos en la Figura 1:


##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
### Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 6

### Código (TarjetaCompacta – Paso 8):

```
@Composable
fun TarjetaCompacta(
nombre: String,
imagen: Painter
) {
Box(
modifier = Modifier
.fillMaxWidth()
.height(140.dp)
.clip(RoundedCornerShape(16.dp))
) {
Image(
painter = imagen,
contentDescription = nombre,
contentScale = ContentScale.Crop,
modifier = Modifier.matchParentSize()
)
// el scrim y el texto se agregan en el Paso 9
}
}
```
## Paso 9. Dentro del mismo Box, agregar un segundo Box semitransparente a modo de scrim, y un

## Text con el nombre alineado en la esquina inferior izquierda. Con esto, TarjetaCompacta queda

## completo:

### Código (TarjetaCompacta completo):

```
@Composable
fun TarjetaCompacta(
nombre: String,
imagen: Painter
) {
Box(
modifier = Modifier
.fillMaxWidth()
.height(140.dp)
.clip(RoundedCornerShape(16.dp))
) {
Image(
painter = imagen,
contentDescription = nombre,
contentScale = ContentScale.Crop,
modifier = Modifier.matchParentSize()
)
Box(
modifier = Modifier
.matchParentSize()
.background(Color.Black.copy(alpha = 0.35f))
)
Text(
text = nombre,
color = Color.White,
fontWeight = FontWeight.Bold,
fontSize = 18.sp,
modifier = Modifier
.align(Alignment.BottomStart)
.padding(12.dp)
)
}
}
```

##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
### Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 7

## Paso 10. Dentro de MainActivity, en el bloque setContent, mostrar primero la lista de TarjetaLugar

## (Paso 7) y luego dos TarjetaCompacta apiladas con un Spacer entre ellas:

### Código (MainActivity.kt completo):

```
class MainActivity : ComponentActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContent {
TarjetasCampus_ComposeTheme {
Surface(modifier = Modifier.fillMaxSize()) {
PantallaTarjetas()
}
}
}
}
}
@Composable
fun PantallaTarjetas() {
Column(
modifier = Modifier
.fillMaxSize()
.padding(16.dp),
verticalArrangement = Arrangement.spacedBy(16.dp)
) {
// Estilo 1: tarjetas con Row (imagen circular + texto)
ListaTarjetasLugar()
Spacer(modifier = Modifier.height(8.dp))
// Estilo 2: tarjetas apiladas con Box (imagen de fondo + scrim + texto)
TarjetaCompacta(
nombre = "Biblioteca Central",
imagen = painterResource(id = R.drawable.biblioteca)
)
Spacer(modifier = Modifier.height(12.dp))
TarjetaCompacta(
nombre = "Comedor Universitario",
imagen = painterResource(id = R.drawable.comedor)
)
}
}
@Preview(showBackground = true)
@Composable
fun PantallaTarjetasPreview() {
TarjetasCampus_ComposeTheme {
PantallaTarjetas()
}
}
```
## Paso 11. Ejecutar la aplicación en un emulador y comparar visualmente ambos estilos de tarjeta.

## III. EJERCICIOS/PROBLEMAS PROPUESTOS

## Tomando como referencia el ejercicio resuelto por el docente, agrega navegación al detalle e integra

## las tarjetas dentro de la pantalla de edificios del Laboratorio 05:

## 1. Crea un Composable DetalleLugarScreen(nombre: String, descripcion: String, imagen: Painter)

## que muestre la información del lugar a tamaño completo, reutilizando el estilo de TarjetaLugar.


##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
### Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 8

## 2. Envuelve TarjetaLugar y TarjetaCompacta con Modifier.clickable { ... } y, usando el mismo patrón

## de navegación del Laboratorio 05 (navController.navigate(...) o una lambda), lleva al usuario a

## DetalleLugarScreen al pulsar una tarjeta.

## 3. Reemplaza la lista simple de EdificiosScreen (nombre + botón) por una lista de TarjetaLugar o

## TarjetaCompacta con datos de al menos 4 edificios del campus.

## 4. (Reto opcional) Diseña un tercer estilo de tarjeta propio (por ejemplo con una etiqueta o ícono

## adicional) y documenta en el informe qué combinación de Row/Column/Box y Modifier utilizaste.

## Entregable: informe con capturas de los dos estilos de tarjeta, de la navegación al detalle, y una

## breve justificación de qué recursos (Composables y Modifiers) fueron la mejor opción para cada

## estilo.

## IV. CUESTIONARIO

## 1. ¿Lograste resolver completamente el Ejercicio/Problema Resuelto por el Docente (dos estilos de

## tarjeta de lugares: una con Row/Column mostrando imagen y texto, y otra apilada con Box y

## superposición de texto)? Adjunta una captura de pantalla del emulador mostrando el resultado

## como evidencia.

## 2. ¿Qué combinación de Composables (Row, Column, Box) y Modifiers consideras que fue la mejor

## opción para cada uno de los dos estilos de tarjeta implementados? Justifica tu respuesta

## comparando ambos casos.

## 3. ¿Qué diferencia encontraste entre construir estas interfaces con Composables y Modifier, en

## comparación con el enfoque clásico de LinearLayout/ConstraintLayout en XML? Menciona al

## menos una ventaja concreta que hayas notado.

## V. REFERENCIAS Y BIBLIOGRÁFIA RECOMENDADAS:

## [1] Android Developers, "Compose layout basics". [En línea]. Disponible:

## https://developer.android.com/develop/ui/compose/layouts/basics

## [2] Android Developers, "Modifiers in Compose". [En línea]. Disponible:

## https://developer.android.com/develop/ui/compose/modifiers

## [3] "POSICIONA elementos con JETPACK COMPOSE de manera FÁCIL", video. [En línea]. Disponible:

## https://www.youtube.com/watch?v=vTNFb44AHWs

## [4] "Usando Cards de Material Design en Jetpack Compose", video. [En línea]. Disponible:

## https://www.youtube.com/watch?v=iZiXpWRIl3U

## TÉCNICAS E INSTRUMENTOS DE EVALUACIÓN

## TÉCNICAS:

### Ejercicios propuestos

## INSTRUMENTOS:

### Rúbrica de evaluación

## CRITERIOS DE EVALUACIÓN

- Cumplimiento de los objetivos y ejercicios propuestos en la guía.
- Correcta aplicación de los conceptos y herramientas del tema desarrollado.
- Funcionamiento correcto de lo implementado (compilación/ejecución sin errores).
- Informe (formato estudiante) completo, con evidencias y cuestionario resuelto.
- Entrega dentro del plazo establecido.
- Se permite el uso de Inteligencia Artificial (IA) como fuente de consulta o apoyo para resolver los

### ejercicios; sin embargo, el desarrollo completo del laboratorio no puede basarse únicamente en

### respuestas generadas por IA. El estudiante debe evidenciar comprensión propia mediante la

### justificación razonada de sus respuestas, especialmente en el cuestionario.


##### FACULTAD DE INGENIERÍA DE PRODUCCIÓN Y SERVICIOS

##### ESCUELA PROFESIONAL DE INGENIERÍA DE SISTEMAS

```
Formato: Guía de Práctica de Laboratorio / Talleres / Centros de Simulación
```
### Aprobación: 2022/03/01 Código: GUIA-PRLD- 001 Página: 9
