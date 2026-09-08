#import "/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Primeros pasos con Android Studio y Jetpack Compose",
  lab_number: "02",
  instructor_name: "Roxana Evelyn Limache Calatayud",
  members: (
    "Mestas Zegarra Christian Raul",
    "Noa Camino Yenaro Joel",
  ),
)

#set image(width: 78%)
#set list(indent: 2pt)
#set heading(numbering: "1.1.")
#show raw.where(block: false): it => box(inset: (x: 0.5pt))[#it]

#lab-section("SOLUCIÓN Y RESULTADOS")[
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DE EJERCICIOS / PROBLEMAS
  == Repositorio de código fuente

  El código fuente de las implementaciones se encuentra en:

  Christian Mestas: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l2/src/christian

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l2/src/yenaro

  == Ejercicio resuelto por el docente: Construcción de Hello World con Jetpack Compose

  Se documenta el procedimiento secuencial para la construcción de una primera aplicación móvil utilizando el entorno Android Studio y el kit de interfaz declarativa Jetpack Compose, siguiendo las instrucciones de la guía de práctica y el tutorial audiovisual de referencia @youtube_hello_world_compose.

  Paso 1. Abrir Android Studio. En la pantalla inicial de bienvenida del entorno de desarrollo integrado (IDE), seleccionar la opción New Project para iniciar el asistente de creación de proyectos.

  Paso 2. En la lista de plantillas disponibles para dispositivos móviles, elegir la plantilla Empty Activity, correspondiente a la categoría Phone and Tablet. Esta plantilla proporciona una base configurada de forma predeterminada con Jetpack Compose.

  Paso 3. Configurar el nombre del proyecto como HelloWorldCompose, establecer el paquete de la aplicación y confirmar que el lenguaje sea Kotlin. Proceder mediante el botón Finish.

  Paso 4. Aguardar a que el sistema complete la descarga de dependencias y la sincronización de Gradle. Se abre de forma automática el archivo `MainActivity.kt` conteniendo el código fuente generado por la plantilla.

  Paso 5. Ejecutar el proyecto haciendo uso del botón Run, utilizando un emulador o un dispositivo físico conectado. Se comprueba la presentación del mensaje Hello Android! en la pantalla del dispositivo.

  Paso 6. Dentro de `MainActivity.kt`, ubicar la función composable denominada `Greeting` anotada con `@Composable`. Esta función recibe un texto mediante un parámetro denominado name y lo presenta en la pantalla empleando un componente `Text`.

  #figure(
    image("img/init_project.png", width: 88%),
    caption: [Ejecución inicial del proyecto HelloWorldCompose mostrando el mensaje predeterminado en pantalla.]) <fig-init-project>

  #figure(
    image("img/christian/01_resuelto_codigo_greeting.png", width: 88%),
    caption: [Inspección y modificación de la función Greeting dentro del entorno Android Studio.],
  ) <fig-codigo-greeting>

  Paso 7. Ubicar la invocación de `Greeting` dentro del bloque `setContent` en el método `onCreate` de la actividad. Reemplazar el texto asignado por el nombre del estudiante, Christian Mestas.

  Paso 8. Volver a ejecutar el proyecto mediante el botón Run y verificar que el dispositivo refleja el saludo personalizado en pantalla dentro del entorno de desarrollo.

  #figure(
    image("img/christian/02_resuelto_saludo_personalizado.png", width: 88%),
    caption: [Compilación y ejecución en Android Studio mostrando el saludo personalizado en el dispositivo conectado.],
  ) <fig-saludo-personalizado>

  == Ejercicios propuestos: Aplicación de registro de lectura de libros

  Ambos integrantes del equipo desarrollaron de forma individual la solución para la aplicación de registro de lectura de libros, cumpliendo con la totalidad de requerimientos solicitados en la guía de práctica: diseño de pantalla composable con tres campos `TextField`, almacenamiento interno privado en modo `MODE_PRIVATE` mediante `openFileOutput`, lectura y registro en consola con `Log.d`, y el cumplimiento del reto opcional de visualización de datos en pantalla mediante el componente `Text`.

  === Implementación desarrollada por Christian Mestas

  La implementación desarrollada por Christian Mestas estructura la interfaz mediante una pantalla composable con campos de texto para el título del libro, autor y número de páginas leídas, acompañada de un botón para ejecutar la persistencia de datos.

  #figure(
    image("img/christian/04_propuesto_formulario_inicial.png", width: 85%),
    caption: [Interfaz de usuario inicial con campos de texto y botón de guardado.],
  ) <fig-christian-form>

  Al presionar el botón Guardar, la aplicación escribe la información ingresada en un archivo de texto plano alojado en el almacenamiento interno en modo `MODE_PRIVATE` @android_storage_internal. Simultáneamente, emite el registro por la consola de depuración mediante el método `Log.d` y cumple el reto opcional al presentar de forma dinámica los datos almacenados directamente en pantalla dentro del área de almacenamiento interno.

  #figure(
    image("img/christian/05_propuesto_guardado_logcat_pantalla.png", width: 85%),
    caption: [Confirmación de guardado, salida en Logcat mediante Log.d y visualización en pantalla.],
  ) <fig-christian-logcat>

  Asimismo, se incorporó control de validación de entradas para notificar al usuario ante campos vacíos o valores no permitidos, garantizando la consistencia de los datos antes de proceder a la persistencia.

  #figure(
    image("img/christian/06_propuesto_validacion_error.png", width: 85%),
    caption: [Manejo y despliegue de mensajes de validación ante campos incompletos.],
  ) <fig-christian-error>

  === Implementación desarrollada por Yenaro Noa

  La implementación desarrollada por Yenaro Noa define una pantalla interactiva denominada `BookScreen` que presenta los tres campos de entrada basados en `TextField` y dos botones de control: Guardar y Ver registro.

  #grid(
    columns: (1fr, 1fr),
    [#figure(
      image("img/yenaro/04_propuesto_formulario_vacio.png", width: 35%),
      caption: [Pantalla BookScreen con campos de texto vacíos y botones de control.],
    ) <fig-yenaro-vacio>],
    [#figure(
      image("img/yenaro/05_propuesto_formulario_completado.png", width: 35%),
      caption: [Formulario con datos del libro completados.],
    ) <fig-yenaro-completado>]
  )

  Al accionar el botón Guardar, los valores son transferidos a un archivo de texto plano en el almacenamiento interno privado utilizando `openFileOutput` en modo `MODE_PRIVATE` @android_storage_internal, presentando una confirmación visual. Al presionar el botón Ver registro, se efectúa la lectura del archivo interno mediante `openFileInput` y se despliega el contenido en pantalla utilizando un componente `Text`, dando cumplimiento al reto opcional.

  #grid(
    columns: (1fr, 1fr),
    [#figure(
      image("img/yenaro/06_propuesto_guardar_confirmacion.png", width: 35%),
      caption: [Confirmación de guardado en almacenamiento interno.],
    ) <fig-yenaro-guardar>],
    [#figure(
      image("img/yenaro/07_propuesto_ver_registro_pantalla.png", width: 35%),
      caption: [Visualización del registro leído en pantalla mediante Text.],
    ) <fig-yenaro-ver>]
  )

  La lectura del archivo se comprueba mediante la consola de depuración haciendo uso de `Log.d` bajo la etiqueta BookRegistry, auditando la persistencia física en el almacenamiento interno privado del dispositivo y validando el código con pruebas unitarias automatizadas.

  #figure(
    image("img/yenaro/08_propuesto_logcat_consola.png", width: 85%),
    caption: [Salida en consola de depuración Logcat mediante Log.d.],
  ) <fig-yenaro-logcat>

  #figure(
    image("img/yenaro/09_propuesto_almacenamiento_interno.png", width: 85%),
    caption: [Inspección del archivo de texto plano generado en el almacenamiento interno privado.],
  ) <fig-yenaro-archivo>

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. ¿Consideras complejo el entorno de Android Studio y el uso de Jetpack Compose para tu primera aplicación? Explica brevemente por qué.

  Android Studio presenta cierta complejidad inicial para principiantes debido a la gestión de dependencias en Gradle, la descarga de componentes del (SDK), la administración de emuladores y el consumo intensivo de memoria (RAM) y procesamiento (CPU) en el entorno de desarrollo integrado (IDE) @phillips2022.

  Sin embargo, Jetpack Compose reduce notablemente la dificultad en el diseño de la interfaz de usuario (UI) frente al esquema tradicional basado en archivos (XML) @android_why_adopt. Su paradigma declarativo en Kotlin puro describe la pantalla mediante funciones composables que se sincronizan de forma reactiva mediante recomposición ante cambios de estado, disminuyendo el código repetitivo y previniendo inconsistencias visuales @android_compose_mental_model. Así, mientras el entorno demanda adaptación técnica, Compose simplifica significativamente la construcción de la aplicación.

  2. ¿Cuál es el propósito de la función `setContent` dentro del `onCreate` de una Activity que usa Jetpack Compose?

  La función `setContent` es una función de extensión de `ComponentActivity` en la biblioteca `androidx.activity.compose` cuyo propósito es vincular la jerarquía declarativa de Jetpack Compose a la ventana de la actividad, estableciendo su vista raíz @android_setcontent_doc.

  En lugar de vincular archivos de diseño en (XML) mediante el método tradicional `setContentView`, `setContent` inicializa internamente un contenedor `ComposeView` que aloja el contexto de composición @android_setcontent_doc. De esta forma, conecta el ciclo de vida de la actividad con las funciones composables definidas en Kotlin, permitiendo renderizar la interfaz de usuario (UI) y ejecutar las recomposiciones reactivas ante modificaciones del estado @android_setcontent_doc @android_compose_mental_model.
][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - Jetpack Compose transforma el desarrollo de aplicaciones móviles al reemplazar el esquema imperativo tradicional de vistas por un modelo declarativo en Kotlin puro, facilitando la creación de interfaces dinámicas y reduciendo el código repetitivo sin depender de archivos de diseño en (XML).

  - La función de extensión `setContent` constituye el puente de enlace indispensable entre el ciclo de vida de `ComponentActivity` y el entorno reactivo de Compose, alojando la jerarquía visual dentro de una vista raíz optimizada para gestionar recomposiciones eficientes.

  - El almacenamiento interno privado accesible mediante `openFileOutput` en modo `MODE_PRIVATE` provee un mecanismo directo y seguro para la persistencia de datos en archivos planos, aislando la información de otras aplicaciones del sistema sin requerir permisos adicionales.

  - La verificación dual a través de mensajes en consola con `Log.d` y la actualización directa en la interfaz mediante el componente `Text` valida de manera íntegra el flujo de captura, almacenamiento y recuperación de datos dentro del dispositivo móvil.
]

#lab-section("RETROALIMENTACIÓN GENERAL")[
  #block(height: 6em, breakable: false)
]

#lab-section("REFERENCIAS Y BIBLIOGRAFÍA")[
  #show heading: set text(weight: "bold")
  #set heading(numbering: none)
  = REFERENCIAS
  #bibliography("bibliography.bib", style: "ieee", title: none)
]
