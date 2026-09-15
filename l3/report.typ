#import "/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Navegación entre pantallas: inicio de sesión y registro con Jetpack Compose",
  lab_number: "03",
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

  Christian Mestas: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l3/src/christian

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l3/src/yenaro

  == Ejercicio resuelto por el docente: Navegación entre inicio de sesión y registro

  Se documenta el procedimiento para la construcción de una navegación entre una pantalla de inicio de sesión y una pantalla de registro @youtube_navigation_compose.

  Paso 1. Crear un proyecto nuevo con la plantilla Empty Activity bajo el nombre NavCompose_LoginRegistro en lenguaje Kotlin, destinado a un emulador o dispositivo físico con nivel de interfaz de programación de aplicaciones (API) 34 o superior.

  Paso 2. Agregar la dependencia `navigation-compose` en su versión 2.8.0 dentro del archivo `build.gradle.kts` del módulo app y sincronizar Gradle, junto con las importaciones de `NavHost`, `composable` y `rememberNavController` en `MainActivity.kt`.

  #figure(
    image("img/resuelto/01_resuelto_dependencia_navigation.png", width: 65%),
    caption: [Declaración de la dependencia de Navigation Compose en el archivo de construcción del módulo app.]) <fig-resuelto-dependencia>

  Paso 3. Definir dos funciones composables denominadas `LoginScreen` y `RegistroScreen` que reciben funciones lambda para comunicar los eventos producidos, junto con una lista compartida en memoria denominada `cuentasRegistradas` que contiene la cuenta de prueba admin con contraseña 1234.

  Paso 4. En `LoginScreen`, disponer dos campos `OutlinedTextField` para el usuario y la contraseña, con transformación visual de ocultamiento en la contraseña, y dos botones denominados Ingresar y Crear cuenta.

  Paso 5. En `RegistroScreen`, disponer los campos de nuevo usuario y nueva contraseña con los botones Aceptar y Cancelar, donde Aceptar agrega el par ingresado a la lista en memoria y notifica el registro exitoso.

  #figure(
    image("img/resuelto/02_resuelto_codigo_registro.png", width: 65%),
    caption: [Definición de la pantalla de registro con campos de texto y botones de aceptación y cancelación.],
  ) <fig-resuelto-registro>

  Paso 6. En `MainActivity`, crear el controlador mediante `rememberNavController` y declarar el contenedor `NavHost` con destino inicial login, registrando las rutas login y registro según el grafo de navegación de la guía.

  #figure(
    image("img/resuelto/03_resuelto_grafo_navhost.png", width: 65%),
    caption: [Grafo de navegación con las rutas de inicio de sesión y registro gestionadas por el controlador.],
  ) <fig-resuelto-navhost>

  Paso 7. Conectar la navegación mediante las funciones lambda: el botón Crear cuenta invoca `navigate` hacia la ruta registro, mientras que Aceptar y Cancelar invocan `popBackStack` para regresar a la pantalla anterior.

  Paso 8. Implementar la validación del ingreso comparando los valores digitados contra la lista en memoria mediante el método `any`. Ante coincidencia se limpia el mensaje de error y se notifica el éxito con un mensaje emergente mediante `Toast`. Ante discrepancia se presenta el mensaje de credenciales incorrectas en pantalla.

  Paso 9. Ejecutar el proyecto y verificar el flujo completo: inicio en `LoginScreen` con la cuenta admin y confirmación de bienvenida, navegación a `RegistroScreen` para registrar al usuario waaa con el teclado en pantalla, e ingreso posterior con las credenciales nuevas confirmado con el mensaje de bienvenida.

  #figure(
    image("img/resuelto/04_resuelto_login_admin_bienvenido.png", width: 65%),
    caption: [Ingreso con la cuenta de prueba admin y mensaje de bienvenida en el emulador.],
  ) <fig-resuelto-login-admin>

  #figure(
    image("img/resuelto/05_resuelto_registro_usuario_teclado.png", width: 65%),
    caption: [Registro del usuario nuevo con el teclado en pantalla sobre el formulario.],
  ) <fig-resuelto-registro-teclado>

  #figure(
    image("img/resuelto/06_resuelto_login_toast_bienvenido.png", width: 65%),
    caption: [Ingreso con las credenciales registradas y confirmación de bienvenida.],
  ) <fig-resuelto-login-toast>

  == Ejercicios propuestos: Persistencia de cuentas y pantalla de bienvenida

  Ambos integrantes del equipo desarrollaron de forma individual la extensión solicitada en la guía de práctica: una tercera pantalla `HomeScreen` que recibe el nombre del usuario como argumento de navegación en la ruta home, persistencia de las cuentas en el archivo `cuentas.txt` del almacenamiento interno privado mediante `openFileOutput` en modo `MODE_APPEND`, validación de campos vacíos en ambos formularios y verificación del archivo generado en el dispositivo.

  === Implementación desarrollada por Christian Mestas

  La implementación desarrollada por Christian Mestas registra cada cuenta como una línea con el usuario y la contraseña separados por coma, escrita con `openFileOutput` en modo `MODE_APPEND`. Ante un registro exitoso se presenta la confirmación mediante `Toast` y se regresa a la pantalla anterior con `popBackStack`, mientras que el botón Cancelar descarta el formulario con la misma operación de retorno.

  #figure(
    image("img/christian/02_propuesto_registro_previo.png", width: 65%),
    caption: [Formulario de registro con los datos del usuario nuevo y vista previa en el entorno de desarrollo integrado (IDE).],
  ) <fig-christian-registro>

  #figure(
    image("img/christian/01_propuesto_registro_teclado.png", width: 65%),
    caption: [Llenado del formulario de registro con el teclado en pantalla sobre el emulador.],
  ) <fig-christian-teclado>

  En `LoginScreen` se valida primero que ningún campo quede vacío y luego se busca coincidencia leyendo el archivo con `openFileInput`, dividiendo cada línea por el delimitador coma. Ante coincidencia se navega a la pantalla de bienvenida con `navigate` pasando el nombre como argumento de la ruta. Ante ausencia de coincidencia se presenta el mensaje de cuenta no encontrada en la interfaz de usuario (UI).

  #figure(
    image("img/christian/04_propuesto_login_credenciales.png", width: 65%),
    caption: [Ingreso de las credenciales registradas en la pantalla de inicio de sesión.],
  ) <fig-christian-login>

  #figure(
    image("img/christian/03_propuesto_login_confirmacion.png", width: 65%),
    caption: [Confirmación de cuenta registrada al regresar a la pantalla de inicio de sesión.],
  ) <fig-christian-confirmacion>
  La validación del reto opcional impide el envío de formularios incompletos en ambas pantallas con el mensaje de campos por completar, como se comprueba al intentar registrar sin contraseña y al intentar ingresar sin usuario.

  #figure(
    image("img/christian/07_propuesto_validacion_registro.png", width: 65%),
    caption: [Mensaje de validación ante el intento de registro con la contraseña vacía.],
  ) <fig-christian-validacion-registro>

  #figure(
    image("img/christian/08_propuesto_validacion_login.png", width: 65%),
    caption: [Mensaje de validación ante el intento de ingreso con el usuario vacío.],
  ) <fig-christian-validacion-login>

  La pantalla `HomeScreen` presenta el mensaje de bienvenida con el nombre recibido como argumento de navegación. El contenido del archivo se comprobó en el explorador de dispositivos del IDE, donde figura la línea registrada con el usuario y la contraseña.

  #figure(
    image("img/christian/05_propuesto_home_bienvenido.png", width: 65%),
    caption: [Pantalla de bienvenida con el nombre del usuario recibido como argumento de navegación.],
  ) <fig-christian-home>

  #figure(
    image("img/christian/06_propuesto_cuentas_txt_explorer.png", width: 65%),
    caption: [Inspección del archivo de cuentas generado en el almacenamiento interno mediante el explorador de dispositivos.],
  ) <fig-christian-cuentas>

  === Implementación desarrollada por Yenaro Noa

  La implementación desarrollada por Yenaro Noa organiza la navegación en tres rutas denominadas login, registro y home con argumento de usuario. El diagrama del grafo resume las transiciones de avance con `navigate`, incluido el avance a la bienvenida tras validar las credenciales, y las transiciones de retorno con `popBackStack`, incluido el cierre de sesión hacia el inicio.

  La pantalla de inicio de sesión presenta los campos de usuario y contraseña con los botones Ingresar y Crear cuenta, mientras que la pantalla de registro presenta los campos de usuario nuevo y contraseña nueva con los botones Aceptar y Cancelar.

  #grid(
    columns: (1fr, 1fr),
    [#figure(
      image("img/yenaro/01_propuesto_login_inicial.png", width: 35%),
      caption: [Pantalla de inicio de sesión con campos vacíos y botones de control.],
    ) <fig-yenaro-login>],
    [#figure(
      image("img/yenaro/02_propuesto_registro.png", width: 35%),
      caption: [Pantalla de registro con campos de usuario nuevo y contraseña nueva.],
    ) <fig-yenaro-registro>]
  )

  Al presionar Aceptar, los valores se escriben en el archivo `cuentas.txt` con `openFileOutput` en modo `MODE_APPEND` y se confirma la creación mediante `Toast`. Al presionar Ingresar, los valores se comparan contra las líneas del archivo leídas con `openFileInput`; ante coincidencia se navega a la bienvenida con el nombre como argumento, y ante discrepancia se presenta el mensaje de cuenta no encontrada.

  #grid(
    columns: (1fr, 1fr),
    [#figure(
      image("img/yenaro/03_propuesto_login_error.png", width: 35%),
      caption: [Mensaje de cuenta no encontrada ante credenciales sin registro.],
    ) <fig-yenaro-error>],
    [#figure(
      image("img/yenaro/04_propuesto_home_bienvenido.png", width: 35%),
      caption: [Pantalla de bienvenida con el nombre del usuario y cierre de sesión.],
    ) <fig-yenaro-home>]
  )

  El reto opcional de validación se cumple en ambos formularios con el mensaje de campos incompletos ante envíos vacíos. La pantalla de bienvenida incluye además el cierre de sesión que regresa al inicio con `popBackStack` dirigido a la ruta login.

  #figure(
    image("img/yenaro/05_propuesto_validacion_vacios.png", width: 15%),
    caption: [Mensaje de validación ante el envío del formulario con campos vacíos.],
  ) <fig-yenaro-validacion>

  La persistencia se verificó listando el directorio de archivos de la aplicación y el contenido del archivo generado, donde figura la cuenta registrada. El flujo de ejecución se auditó adicionalmente en la salida del registro del sistema filtrada por el paquete de la aplicación.

  #figure(
    image("img/yenaro/06_propuesto_cuentas_txt_terminal.png", width: 55%),
    caption: [Verificación del archivo de cuentas generado y su contenido en el dispositivo.],
  ) <fig-yenaro-cuentas>

  #figure(
    image("img/yenaro/07_propuesto_logcat.png", width: 55%),
    caption: [Salida del registro del sistema durante la ejecución del flujo de navegación.],
  ) <fig-yenaro-logcat>

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. ¿Qué ventajas tiene usar Navigation Compose frente al enfoque tradicional de múltiples actividades comunicadas por Intent?

  Navigation Compose concentra la aplicación en una sola actividad que aloja un contenedor `NavHost` con pantallas representadas como funciones composables, donde el objeto `NavController` gestiona la navegación y la pila de destinos @android_navigation_compose. Este esquema evita crear una actividad por pantalla y el paso de datos mediante extras de `Intent` @android_intents_filters, con lo cual se reducen las transiciones del ciclo de vida entre actividades y la necesidad de banderas de lanzamiento para controlar la pila de tareas @android_tasks_backstack. La interfaz compartida como andamiajes y barras superiores se mantiene estable mientras solo cambia el contenido definido por el grafo de navegación @android_navigation_design. Además, las pantallas reciben funciones de navegación como parámetros en lugar del controlador completo, lo cual permite probar cada destino de forma independiente @android_navigation_testing. Las actividades múltiples conservan su lugar para límites reales del sistema como el lanzamiento de aplicaciones externas, comportamientos separados de tarea o ventana, y la migración incremental de aplicaciones existentes.

  2. Además de los argumentos de navegación, ¿qué otro mecanismo podrías usar para compartir datos entre pantallas en una app Compose? Explica brevemente cómo funcionaría.

  Un mecanismo alternativo es un modelo de vista compartido con alcance al grafo de navegación. Cada grafo posee su propio propietario de almacén de modelos, de modo que el modelo obtenido desde la entrada de la pila del grafo padre permanece disponible mientras dicho grafo continúe en la pila @android_viewmodel_scoping. En la práctica, cada pantalla recupera la entrada padre con `getBackStackEntry` usando la ruta del grafo y luego obtiene el modelo con `viewModel` pasando dicha entrada, con lo cual todas las pantallas del mismo flujo comparten la misma instancia. El estado del formulario vive en el modelo y sobrevive a recomposiciones y cambios de configuración, mientras que las pantallas pequeñas reciben únicamente el estado y las funciones de evento que necesitan para mantenerse desacopladas @android_compose_libraries. Los argumentos de navegación quedan reservados para valores pequeños y específicos del destino como identificadores, y el modelo compartido asume el estado temporal de flujos con varias pantallas como registros o asistentes.
][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - Navigation Compose transforma la gestión de pantallas al reemplazar múltiples actividades comunicadas por `Intent` con un grafo de destinos composables gobernado por `NavController`, con operaciones explícitas de avance con `navigate` y de retorno con `popBackStack`.

  - El paso del nombre de usuario como argumento en la ruta home demuestra el mecanismo directo de comunicación entre pantallas, mientras que un modelo de vista con alcance al grafo ofrece la alternativa para estado compartido de mayor volumen sin serializar datos en la ruta.

  - El almacenamiento interno privado accesible mediante `openFileOutput` en modo `MODE_APPEND` provee un mecanismo directo para la persistencia acumulativa de cuentas en un archivo de texto plano, aislando la información de otras aplicaciones del sistema sin requerir permisos adicionales.

  - La verificación en tres niveles mediante mensajes en pantalla, inspección del archivo generado en el dispositivo y salida del registro del sistema valida de manera íntegra el flujo de registro, autenticación contra archivo y navegación hacia la bienvenida.

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
