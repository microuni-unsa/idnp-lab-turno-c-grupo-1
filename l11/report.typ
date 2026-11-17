#import "/components/@unsareport/epis-lab/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Reproductor de audio-guía con Foreground Service en Jetpack Compose",
  lab_number: "11",
  instructor_name: "Roxana Evelyn Limache Calatayud",
  members: (
    "Mestas Zegarra Christian Raul",
    "Noa Camino Yenaro Joel",
  ),
  custom_variables: (
    course_abbr: "IDNP",
    members_short: "Mestas, Noa",
  ),
)

#set image(width: 78%)
#set list(indent: 2pt)
#set heading(numbering: "1.1.")

#lab-section("SOLUCIÓN Y RESULTADOS")[
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DE EJERCICIOS / PROBLEMAS
  == Repositorio de código fuente y videos de evidencia

  El código fuente de las implementaciones se encuentra disponible en:

  Resuelto: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l11/src/resuelto

  Christian Mestas: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l11/src/christian

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l11/src/yenaro

  \

  Los videos cortos de la pantalla del emulador donde se escucha el audio se encuentran en:

  Video Christian Mestas: https://drive.google.com/file/d/1uDHpHIKREPjmCmpjANX2Ry5Ec2q0dU8q/view?usp=sharing

  Video Yenaro Noa: https://drive.google.com/file/d/1LXjyGh1OE6rhuEvVKa05qcy-fQw-1hYp/view?usp=sharing

  == Ejercicio resuelto por el docente: audio-guía con Foreground Service

  Se documenta el desarrollo paso a paso de la aplicación AudioGuiaResuelto, un reproductor de audio-guía sobre un edificio del campus que se ejecuta como servicio en primer plano, con notificación persistente e información de estado según la visibilidad de la aplicación @android-foreground @video-foreground-task.

  Paso 1. Creación del proyecto AudioGuiaResuelto con nombre de paquete com.example.audioguia_resuelto en lenguaje Kotlin, empleando para la verificación visual un emulador con interfaz de programación de aplicaciones, en adelante API, de nivel 34 o superior, según los recursos exigidos por la guía.

  Paso 2. Declaración en el manifiesto de los permisos FOREGROUND_SERVICE, FOREGROUND_SERVICE_MEDIA_PLAYBACK y POST_NOTIFICATIONS, junto con el servicio AudioGuiaService de tipo mediaPlayback, y agregado de la dependencia lifecycle-process para observar el ciclo de vida de la aplicación.

  Pasos 4 a 8. Creación de AudioGuiaService con las acciones reproducir, pausar y detener, el canal de notificaciones Audio-guía de importancia baja, la notificación persistente con el título Biblioteca Central y el avance simulado con una corrutina que cuenta segundos.

  Pasos 9 a 12. Creación del composable PantallaAudioGuia con los estados de reproducción y de segundo plano, el observador sobre ProcessLifecycleOwner que marca ON_STOP y ON_START, y los tres botones que envían cada acción al servicio, mostrado dentro de MainActivity.

  Paso 13. Ejecución en el emulador: al pulsar Reproducir la pantalla muestra el estado de reproducción, como se aprecia a continuación.

  #figure(
    image("img/resuelto/screenshot_2026-11-17_09-37-34.png", width: 80%),
    caption: [Pantalla en reproducción tras pulsar Reproducir en el emulador.],
  ) <fig-resuelto-app>

  Al pasar la aplicación a segundo plano con el botón Home aparece la notificación persistente con el título del edificio, como se aprecia a continuación.

  #figure(
    image("img/resuelto/screenshot_2026-11-17_09-37-49.png", width: 80%),
    caption: [Notificación persistente del servicio en segundo plano.],
  ) <fig-resuelto-notificacion>

  == Ejercicios propuestos: edificios múltiples, acciones en la notificación, Room y audio real

  La guía solicita mejorar el reproductor con un botón Siguiente edificio que rote la lista de edificios del Laboratorio 08 y muestre el nombre en la notificación, botones Pausar y Detener dentro de la notificación, persistencia con Room del último edificio reproducido con botón Continuar al reabrir, y audio real con MediaPlayer desde la carpeta res/raw en lugar del avance simulado.

  === Implementación desarrollada por Christian Mestas

  La solución conserva la estructura del resuelto y agrega la acción siguiente al servicio, con una lista de cinco edificios: Biblioteca Central, Comedor Universitario, Pabellón de Ingeniería de Sistemas, Coliseo Universitario y Auditorio Central. Cada edificio se asocia a su archivo de narración en una tabla fija dentro del servicio.

  #code-block(
"private val AUDIOS = mapOf(
  \"Biblioteca Central\" to R.raw.narracion_biblioteca,
  \"Comedor Universitario\" to R.raw.narracion_comedor,
  \"Pabellón de Ingeniería de Sistemas\" to R.raw.narracion_sistemas,
  \"Coliseo Universitario\" to R.raw.narracion_coliseo,
  \"Auditorio Central\" to R.raw.narracion_auditorio
)",
    lang: "kotlin",
  )

  La notificación incluye las acciones Pausar y Detener construidas con addAction y pendientes de tipo servicio, además de la apertura de la aplicación al pulsar el cuerpo. Al reproducir, el servicio crea el MediaPlayer con el audio del edificio actual, publica la notificación en primer plano y guarda el nombre en Room; al pausar actualiza el texto a pausa; al detener libera el reproductor y finaliza el servicio; al avanzar cambia de edificio y reinicia la reproducción con el audio nuevo.

  La persistencia usa una sola tabla ultima_reproduccion con identificador fijo 1 y el nombre del edificio, escrita con Upsert en cada reproducción y leída al abrir la aplicación para ofrecer el botón Continuar solo cuando el edificio guardado difiere del inicial.

  #code-block(
"@Entity(tableName = \"ultima_reproduccion\")
data class UltimaReproduccionEntity(
  @PrimaryKey
  val id: Int = 1,
  val nombreEdificio: String
)",
    lang: "kotlin",
  )

  El audio real consiste en cinco narraciones grabadas por el integrante, una por edificio, de entre 14 y 18 segundos cada una, ubicadas en res/raw con los nombres narracion_biblioteca, narracion_comedor, narracion_sistemas, narracion_coliseo y narracion_auditorio.

  La pantalla organiza los controles en tres secciones con rótulo: Ahora suena con la tarjeta del edificio actual y el estado de reproducción, Reproducción con Reproducir a ancho completo y Pausar junto a Detener, y Edificios con Siguiente edificio y el botón Continuar con el edificio guardado, como se aprecia a continuación.

  #figure(
    image("img/christian/screenshot_2026-11-17_09-55-32.png", width: 80%),
    caption: [Pantalla con tarjeta de edificio y controles en el emulador.],
  ) <fig-christian-app>

  Tras reproducir y pasar a segundo plano, la notificación muestra el edificio actual con las acciones Pausar y Detener, como se aprecia a continuación.

  #figure(
    image("img/christian/screenshot_2026-11-17_09-55-51.png", width: 80%),
    caption: [Notificación con edificio y acciones de control.],
  ) <fig-christian-notificacion>

  Al pulsar Siguiente edificio durante la reproducción, la pantalla y la notificación cambian al edificio siguiente, como se aprecia a continuación.

  #figure(
    image("img/christian/screenshot_2026-11-17_09-55-56.png", width: 80%),
    caption: [Cambio de edificio reflejado en pantalla en el emulador.],
  ) <fig-christian-siguiente>

  #figure(
    image("img/christian/screenshot_2026-11-17_09-56-19.png", width: 80%),
    caption: [Notificación actualizada con el edificio siguiente.],
  ) <fig-christian-notificacion-siguiente>

  === Implementación desarrollada por Yenaro Noa

  La solución de Yenaro Noa cubre los cuatro ejercicios con una lista de ocho edificios del campus, los mismos de su Laboratorio 08: Biblioteca Central, Comedor Universitario, Facultad de Ingeniería de Producción y Servicios, Pabellón de Ingeniería de Sistemas, Coliseo Universitario, Estadio Universitario, Auditorio Central y Facultad de Ciencias Naturales y Formales. El avance al siguiente edificio se resuelve dentro del servicio a partir del edificio actual, como se aprecia a continuación.

  #figure(
    image("img/yenaro/01_propuesto_siguiente.png", width: 48%),
    caption: [Avance circular al siguiente edificio en el entorno de desarrollo.],
  ) <fig-yenaro-siguiente>

  La notificación agrega las acciones Pausar y Detener con pendientes de tipo servicio inmutables, como se aprecia a continuación.

  #figure(
    image("img/yenaro/02_propuesto_actions.png", width: 48%),
    caption: [Acciones Pausar y Detener en la notificación en el entorno de desarrollo.],
  ) <fig-yenaro-actions>

  La persistencia usa la tabla ultimo_edificio con identificador fijo 1, escrita por el servicio en cada reproducción y leída al abrir la aplicación para mostrar el botón Continuar, como se aprecia a continuación.

  #figure(
    image("img/yenaro/03_propuesto_room.png", width: 48%),
    caption: [Entidad y DAO del último edificio en el entorno de desarrollo.],
  ) <fig-yenaro-room>

  El audio real se reproduce con MediaPlayer desde el archivo guia_biblioteca en res/raw, una narración de 29 segundos grabada por el integrante, en modo repetición, con pausa, detención y liberación del reproductor según la acción recibida, como se aprecia a continuación.

  #figure(
    image("img/yenaro/04_propuesto_mediaplayer.png", width: 48%),
    caption: [Reproducción con MediaPlayer en el entorno de desarrollo.],
  ) <fig-yenaro-mediaplayer>

  Como aporte propio, toda la pantalla usa un estilo neobrutalista con tarjeta de color según edificio sobre fondo crema, bordes negros gruesos con sombra dura desplazada y botones blancos con borde negro, como se aprecia a continuación.

  #figure(
    image("img/yenaro/05_propuesto_neo.png", width: 48%),
    caption: [Tarjeta y paleta neobrutalista en el entorno de desarrollo.],
  ) <fig-yenaro-neo>

  La verificación del archivo de audio en res/raw y de la fila persistida en la base guia_db mediante consola del sistema muestra Comedor Universitario guardado, como se aprecia a continuación.

  #figure(
    image("img/yenaro/06_propuesto_raw_db.png", width: 48%),
    caption: [Audio en res/raw y fila persistida verificados en consola del sistema.],
  ) <fig-yenaro-raw-db>

  La pantalla base muestra el edificio actual con el último edificio guardado y el botón Continuar para retomarlo, como se aprecia a continuación.

  #figure(
    image("img/yenaro/01_yenaro_base.png", width: 22%),
    caption: [Pantalla base con último edificio y Continuar en el emulador.],
  ) <fig-yenaro-base>

  Al reproducir, el estado cambia y el aviso de segundo plano aparece cuando la aplicación pierde visibilidad, como se aprecia a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/yenaro/02_yenaro_reproduciendo.png", width: 48%),
      caption: [Estado de reproducción en el emulador.],
    ) <fig-yenaro-reproduciendo>],
    [#figure(
      image("img/yenaro/03_yenaro_siguiente.png", width: 48%),
      caption: [Edificio siguiente con aviso de segundo plano.],
    ) <fig-yenaro-siguiente-app>]
  )

  La notificación persistente en segundo plano muestra el edificio actual con las acciones Pausar y Detener, como se aprecia a continuación.

  #figure(
    image("img/yenaro/04_yenaro_notificacion.png", width: 22%),
    caption: [Notificación con acciones en segundo plano.],
  ) <fig-yenaro-notificacion>

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. *Lograste resolver completamente el ejercicio resuelto por el docente con el reproductor de audio-guía en Foreground Service y notificación visible al pasar la aplicación a segundo plano. Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.*

    Sí, se resolvió el ejercicio en el proyecto AudioGuiaResuelto con las tres acciones del servicio, el canal Audio-guía y el observador de ciclo de vida. Como evidencia, en la @fig-resuelto-app-2 se aprecia la pantalla en reproducción.

    #figure(
      image("img/resuelto/screenshot_2026-11-17_09-37-34.png", width: 60%),
      caption: [Pantalla en reproducción tras pulsar Reproducir en el emulador.],
    ) <fig-resuelto-app-2>

  2. *Por qué es obligatorio mostrar una notificación en un Foreground Service, a diferencia de un started Service como el del Laboratorio 10. Explica qué problema busca evitar esta restricción del sistema operativo.*

    La notificación obligatoria existe para que el usuario sepa que la aplicación sigue consumiendo recursos aunque no esté visible. Un servicio iniciado común puede pasar desapercibido y el sistema puede detenerlo al necesitar memoria, mientras que un servicio en primer plano recibe prioridad similar a una aplicación visible y por eso debe anunciarse. Sin esta restricción, una aplicación podría agotar la batería, usar la red o rastrear la ubicación en secreto; la notificación persistente da transparencia y una vía directa para que el usuario note la tarea y la detenga @android-foreground.

  3. *En forma individual, comenta en el video de referencia qué diferencia encuentras entre la forma de mostrar la notificación en el video y el control de visibilidad basado en ProcessLifecycleOwner implementado en este laboratorio. Colocar la captura del comentario, uno por integrante del grupo.*

    Comentario de Christian Mestas.

    #figure(
      image("img/christian/comentario_video.png", width: 80%),
      caption: [Comentario de Christian en el video de referencia.],
    ) <fig-comentario-christian>

    Comentario de Yenaro Noa.

    #figure(
      image("img/yenaro/comentario_video.png", width: 80%),
      caption: [Comentario de Yenaro en el video de referencia.],
    ) <fig-comentario-yenaro>

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - El servicio con canal de importancia baja y llamada a startForeground en la acción reproducir cumple la obligación del sistema y mantiene la reproducción al pasar a segundo plano, con la notificación como único aviso visible.
  - El observador sobre ProcessLifecycleOwner separa la visibilidad de la aplicación del estado del servicio, de modo que la pantalla informa por sí misma en primer plano y remite a la notificación solo en segundo plano.
  - Las acciones Pausar y Detener dentro de la notificación ofrecen control total sin abrir la aplicación, y el botón Siguiente edificio actualiza pantalla y notificación con el mismo estado en ambas implementaciones.
  - La tabla de un solo registro con Room conserva el último edificio entre sesiones y el botón Continuar lo retoma, mientras MediaPlayer reemplaza la corrutina simulada con audio real desde res/raw.

]

#lab-section("REFERENCIAS Y BIBLIOGRAFÍA")[
  #show heading: set text(weight: "bold")
  #set heading(numbering: none)
  = REFERENCIAS
  #bibliography("bibliography.bib", style: "ieee", title: none)
]
