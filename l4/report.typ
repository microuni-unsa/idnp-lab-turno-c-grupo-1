#import "/components/@unsareport/epis-lab/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Monitoreo del estado de la batería con BroadcastReceiver en Jetpack Compose",
  lab_number: "04",
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
#show raw.where(block: false): it => box(inset: (x: 0.5pt))[#it]

#lab-section("SOLUCIÓN Y RESULTADOS")[
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DE EJERCICIOS / PROBLEMAS
  == Repositorio de código fuente

  El código fuente de las implementaciones se encuentra en:

  Christian Mestas: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l4/src/christian

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l4/src/yenaro

  == Ejercicio resuelto por el docente: Porcentaje de batería con BroadcastReceiver

  Se documenta el procedimiento para la construcción de una pantalla que muestra el porcentaje de batería
  usando un BroadcastReceiver registrado con DisposableEffect @android_battery_monitoring
  @youtube_broadcast_compose.

  Paso 1. Crear un proyecto nuevo con la plantilla Empty Activity bajo el nombre BatteryMonitor_Compose
  en lenguaje Kotlin, paquete com.example.batterymonitor_compose, destinado a un emulador o dispositivo
  físico con API 34 o superior. Definir la función composable BatteryScreen con las importaciones de
  BroadcastReceiver, IntentFilter, BatteryManager, DisposableEffect y LocalContext.

  Paso 2. Declarar la variable de estado porcentaje con remember y mutableStateOf para conservar
  el nivel entre recomposiciones, y obtener el contexto con LocalContext.current, necesario para registrar
  el receiver.

  Paso 3. Dentro de DisposableEffect con Unit, crear el objeto BroadcastReceiver y en onReceive
  extraer EXTRA_LEVEL y EXTRA_SCALE del Intent recibido, calculando el porcentaje a partir del nivel y la escala.

  Paso 4. Registrar el receiver con registerReceiver usando IntentFilter
  e imprimir Log.d con etiqueta BatteryScreen, cerrando el efecto con
  onDispose junto a unregisterReceiver y su mensaje de desregistro para evitar fugas de memoria
  @android_broadcasts @android_compose_side_effects.

  Paso 5. Mostrar el valor con Text mostrando el porcentaje de batería y, en MainActivity dentro de
  setContent, reemplazar Greeting por BatteryScreen.

  Paso 6. Ejecutar en el emulador y simular distintos niveles de carga, verificando que el porcentaje en
  pantalla se actualiza con el evento del sistema.

  #figure(
    image("img/resuelto/01_resuelto_monitoreo_bateria.png", width: 65%),
    caption: [Código del receiver, emulador con el porcentaje mostrado y salida de Logcat.],
  ) <fig-resuelto-evidencia>

  Paso 7. Revisar Logcat filtrando por la etiqueta "BatteryScreen" y confirmar los mensajes de registro y
  desregistro del receiver al entrar y salir de la pantalla, como se aprecia en el panel inferior de
  @fig-resuelto-evidencia.

  == Ejercicios propuestos: Actualización manual con PendingIntent y estado de carga

  Tomando como referencia el ejercicio resuelto por el docente, ambas implementaciones parten de una
  pantalla BatteryScreen que muestra el porcentaje de batería recibido del evento del sistema
  ACTION_BATTERY_CHANGED (extras EXTRA_LEVEL y EXTRA_SCALE) mediante un BroadcastReceiver registrado
  con DisposableEffect @android_battery_monitoring @android_broadcasts @android_compose_side_effects, y
  agregan una segunda forma de actualizar el porcentaje, disparada manualmente:

  1. Un botón "Actualizar manualmente" que, en vez de esperar el evento del sistema, dispara un broadcast
  personalizado: un Intent con acción propia envuelto en PendingIntent.getBroadcast y activado con
  PendingIntent.send.
  2. Un segundo BroadcastReceiver que escucha esa acción personalizada y, al recibirla, vuelve a leer el
  estado de la batería y actualiza el mismo estado en pantalla.
  3. Registro y desregistro de este segundo receiver con el mismo patrón DisposableEffect del ejercicio
  resuelto.
  4. (Reto opcional) Indicación en pantalla de si la batería se está cargando o no, usando EXTRA_STATUS
  del Intent original @youtube_broadcast_compose.

  === Implementación desarrollada por Yenaro Noa

  La implementación organiza BatteryScreen en dos estados: porcentaje y enCarga. El receiver del
  sistema calcula el porcentaje y determina la carga comparando EXTRA_STATUS con BATTERY_STATUS_CHARGING
  y BATTERY_STATUS_FULL. El segundo receiver escucha la acción personalizada com.tuapp.ACTUALIZAR_BATERIA
  y relee el nivel desde el Intent fijo (sticky) de ACTION_BATTERY_CHANGED.

  Un hallazgo de la implementación: en API 34 o superior, el receiver de la acción personalizada debe
  registrarse con la variante de tres argumentos
  registerReceiver con el indicador RECEIVER_NOT_EXPORTED; sin este indicador el sistema lanza
  SecurityException al no tratarse de un broadcast exclusivamente del sistema. El receiver de
  ACTION_BATTERY_CHANGED sí admite la variante de dos argumentos por ser un evento del sistema.

  La pantalla presenta el porcentaje con Text, una barra LinearProgressIndicator proporcional al nivel,
  el estado de carga y el botón de actualización manual a ancho completo.

  #grid(
    columns: (1fr, 1fr),
    [#figure(
      image("img/yenaro/01_propuesto_bateria_inicial.png", width: 45%),
      caption: [Pantalla inicial con nivel 100 y estado de no carga.],
    ) <fig-yenaro-inicial>],
    [#figure(
      image("img/yenaro/02_propuesto_nivel_35.png", width: 45%),
      caption: [Actualización automática a 35 ante el evento del sistema.],
    ) <fig-yenaro-nivel>]
  )

  Al presionar el botón se construye un Intent con la acción propia, se envuelve en
  PendingIntent.getBroadcast con FLAG_UPDATE_CURRENT y FLAG_IMMUTABLE, y se activa con send,
  lo que produce la entrada "Actualizacion manual" en el registro del sistema.

  #figure(
    image("img/yenaro/03_propuesto_actualizacion_manual.png", width: 24%),
    caption: [Pantalla tras presionar el botón de actualización manual con PendingIntent.],
  ) <fig-yenaro-manual>

  El reto opcional se verificó forzando el estado de carga (dumpsys battery set status 2), con lo cual
  la pantalla muestra "Estado: Cargando".

  #figure(
    image("img/yenaro/04_propuesto_cargando.png", width: 26%),
    caption: [Reto opcional: indicación de batería en carga mediante EXTRA_STATUS.],
  ) <fig-yenaro-cargando>

  El flujo completo se auditó en Logcat filtrando por la etiqueta "BatteryScreen": registro de ambos
  receivers, nivel inicial y actualización automática a 35, disparo manual y cambio a estado de carga.

  #code-block("l4/img/yenaro/05_logcat_batteryscreen.txt", lang: "text")

  === Implementación desarrollada por Christian Mestas

  La implementación desarrollada por Christian Mestas organiza el código con constantes nombradas (TAG,
  ACTION_ACTUALIZAR_BATERIA, INITIAL_PERCENTAGE, INVALID_BATTERY_VALUE) y dos estados: porcentaje y
  estaCargando. El receiver del sistema calcula el porcentaje con EXTRA_LEVEL y EXTRA_SCALE, y determina
  la carga comparando EXTRA_STATUS con BATTERY_STATUS_CHARGING y BATTERY_STATUS_FULL. La prueba se
  ejecutó en un dispositivo físico (OPPO, modo oscuro), donde la pantalla presenta "Batería: 84%" con el
  estado "Cargando".

  #figure(
    image("img/christian/01_propuesto_codigo_estado_carga.png", width: 65%),
    caption: [Código del receiver con EXTRA_STATUS y dispositivo con el porcentaje y el estado de carga.],
  ) <fig-christian-codigo>

  El segundo receiver escucha la acción propia y vuelve a leer el nivel con BatteryManager; se registra con
  ContextCompat.registerReceiver con RECEIVER_NOT_EXPORTED, variante compatible que evita la
  SecurityException de API 34 o superior. Al presionar el botón se registra "Recarga manual disparada
  mediante PendingIntent": se crea el Intent con la acción propia, se envuelve en
  PendingIntent.getBroadcast con FLAG_UPDATE_CURRENT y FLAG_IMMUTABLE, y se activa con send.

  #figure(
    image("img/christian/02_propuesto_codigo_pending_intent.png", width: 65%),
    caption: [Código del botón manual con Intent propio y PendingIntent, y pantalla con el botón.],
  ) <fig-christian-manual>

  La actualización manual se comprobó en Logcat con las entradas "Recarga manual ejecutada: valor leído =
  84% (Cargando: true)", confirmando la relectura y el reto opcional del estado de carga.

  #figure(
    image("img/christian/03_propuesto_actualizacion_manual_logcat.png", width: 65%),
    caption: [Pantalla tras la actualización manual y Logcat con la recarga ejecutada.],
  ) <fig-christian-logcat>

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. ¿Lograste resolver completamente el Ejercicio Resuelto (pantalla con porcentaje en tiempo real)?
  Adjunta una captura del emulador como evidencia.

  Sí. La pantalla BatteryScreen muestra el porcentaje recibido del evento ACTION_BATTERY_CHANGED y se
  actualiza sin intervención al simular el cambio de nivel, como se observa en la evidencia: nivel inicial
  en @fig-yenaro-inicial y actualización a 35 en @fig-yenaro-nivel, con el registro correspondiente en
  Logcat bajo la etiqueta "BatteryScreen".

  2. ¿Qué diferencia encontraste entre recibir el evento automático (ACTION_BATTERY_CHANGED) y disparar un broadcast manualmente con PendingIntent? ¿En qué situaciones usarías cada uno?

  El evento automático lo emite el sistema cada vez que cambia la batería: la aplicación solo reacciona,
  sin costo de sondeo y sin intervención del usuario, por lo que es el mecanismo adecuado para el monitoreo
  continuo @android_battery_monitoring. El broadcast manual, en cambio, se dispara bajo demanda cuando el
  usuario presiona el botón: el PendingIntent envuelto con la acción propia permite forzar una relectura
  inmediata @android_broadcasts. Se usaría el evento automático para mantener la pantalla siempre
  sincronizada, y el disparo manual para actualización a petición (estilo "tirar para actualizar"), pruebas
  sin esperar el evento del sistema, o reintentos tras una lectura fallida.

  3. Comenta individualmente en el video de referencia
  (https://www.youtube.com/watch?v=AeNnc8yhNTo) qué otras acciones del sistema, además del cambio de
  batería, se pueden escuchar con un BroadcastReceiver. (Captura del comentario; uno por integrante).

  #figure(
    image("img/christian/05_comentario_youtube.jpeg", width: 90%),
    caption: [Comentario de Christian Mestas en el video de referencia.],
  ) <fig-christian-comentario>

  #figure(
    image("img/yenaro/06_comentario_youtube.png", width: 80%),
    caption: [Comentario de Yenaro Noa en el video de referencia.],
  ) <fig-yenaro-comentario>


][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - El BroadcastReceiver permite reaccionar a eventos del sistema como ACTION_BATTERY_CHANGED sin interfaz propia, extrayendo EXTRA_LEVEL y EXTRA_SCALE para calcular el porcentaje de batería.

  - DisposableEffect con su bloque onDispose garantiza el registro y desregistro seguro del receiver según el ciclo de vida del Composable, evitando fugas de memoria.

  - El PendingIntent con una acción personalizada provee una segunda vía de actualización bajo demanda, complementaria al evento automático, verificada con el botón manual y el registro del sistema.

  - El extra EXTRA_STATUS permite informar además si la batería se está cargando, y en API 34 o superior los receivers de acciones propias exigen el indicador RECEIVER_NOT_EXPORTED para registrarse.

]

#lab-section("REFERENCIAS Y BIBLIOGRAFÍA")[
  #show heading: set text(weight: "bold")
  #set heading(numbering: none)
  = REFERENCIAS
  #bibliography("bibliography.bib", style: "ieee", title: none)
]
