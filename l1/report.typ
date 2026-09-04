#import "/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Introducción al Desarrollo de Aplicaciones Móviles",
  lab_number: "01",
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

#lab-section("SOLUCCIÓN Y RESULTADOS")[
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DE EJERCICIOS / PROBLEMAS

  == Ejercicio 1: Investiga y explica con tus propias palabras: ¿Qué es una aplicación móvil? ¿Qué se entiende por nuevas plataformas de desarrollo? ¿Por qué es importante el desarrollo móvil en la actualidad?

  Una aplicación móvil es un tipo de software desarrollado específicamente para ejecutarse en dispositivos portátiles @sommerville2016. A diferencia del software de escritorio tradicional, una aplicación móvil se diseña optimizando el consumo de energía, la memoria RAM restringida, el procesamiento en arquitecturas heterogéneas y la interacción táctil en pantallas de tamaño reducido @phillips2022. Asimismo, aprovecha de forma nativa los sensores y periféricos del dispositivo @android_architecture_2024.

  El concepto de nuevas plataformas de desarrollo comprende el conjunto moderno de frameworks, entornos de ejecución y lenguajes de programación para facilitar la creación de aplicaciones multiplataforma a partir de una única base de código @biornhansen2020. Ejemplos destacados en la industria actual son Flutter, React Native, Kotlin Multiplatform y entornos de desarrollo Low-Code/No-Code. Estas plataformas emplean tecnologías optimizadas para generar ejecutables nativos.

  Los dispositivos portátiles se han convertido en el principal canal de acceso a internet a nivel mundial, representando más del 58% del tráfico web global y superando los 6.8 mil millones de usuarios activos @statista2024 @itu2024. Estos transformaron sectores como la banca digital, el comercio electrónico, la telemedicina, la educación virtual y los servicios gubernamentales @pressman2020. Las aplicaciones móviles ofrecen a las organizaciones la capacidad de interactuar en tiempo real con sus usuarios mediante notificaciones, servicios basados en localización y experiencias personalizadas @android_architecture_2024.

  == Ejercicio 2: Elabora un cuadro comparativo entre aplicación móvil, aplicación web y aplicación de escritorio, considerando al menos: dispositivo donde se ejecuta, conexión a internet, instalación, acceso a recursos del equipo y ejemplos.

  En la @tabla-comparativa se detallan las diferencias entre aplicaciones móviles, aplicaciones web y aplicaciones de escritorio según sus entornos de ejecución, instalación y capacidades de integración con el hardware @sommerville2016 @pressman2020.

  #figure(
    table(
      columns: (auto, auto, auto, auto),
      align: (left + horizon),
      stroke: 0.5pt + rgb("#808080"),
      table.header(
        table.cell(fill: rgb("#E5E7EB"))[*Criterio*],
        table.cell(fill: rgb("#E5E7EB"))[*Aplicación Móvil*],
        table.cell(fill: rgb("#E5E7EB"))[*Aplicación Web*],
        table.cell(fill: rgb("#E5E7EB"))[*Aplicación de Escritorio*],
      ),
      [*Dispositivo de ejecución*],
      [Teléfonos inteligentes, tabletas, smartwatches y dispositivos portátiles.],
      [Navegadores web en cualquier dispositivo.],
      [Computadoras de escritorio, portátiles y servidores de trabajo.],

      [*Conexión a internet*],
      [Opción offline con persistencia local, sincronización al recuperar red.],
      [Requerida de forma continua.],
      [Funciona principalmente offline, conexión opcional según el servicio.],

      [*Instalación y distribución*],
      [Descarga e instalación previa desde tiendas oficiales.],
      [Sin instalación local, acceso directo vía URL desde navegador web.],
      [Instalación local binaria.],

      [*Acceso a recursos del equipo*],
      [Acceso total a sensores (GPS, cámara, giroscopio, biometría) y notificaciones.],
      [Acceso restringido por el navegador mediante APIs estandarizadas.],
      [Acceso directo al sistema de archivos, GPU, periféricos y memoria local.],

      [*Ejemplos representativos*],
      [WhatsApp, Spotify, Google Maps, Duolingo, Yape.],
      [Google Docs, Canva, Figma Web, Trello Web.],
      [Visual Studio Code, AutoCAD, Adobe Photoshop, GIMP.],
    ),
    caption: [Cuadro comparativo entre aplicación móvil, aplicación web y aplicación de escritorio.],
  ) <tabla-comparativa>

  == Ejercicio 3: Explica cuáles son las partes básicas de una aplicación móvil (interfaz, lógica, eventos, navegación, datos), siguiendo el mismo procedimiento del ejercicio resuelto por el docente. Acompaña tu explicación con un esquema o diagrama propio.

  Para explicar la estructura básica de una aplicación móvil y sus cinco elementos fundamentales, se aplica el procedimiento metódico de 7 pasos propuesto en la guía práctica, tomando como objeto de estudio la aplicación Spotify @sommerville2016 @android_architecture_2024:

  *Paso 1. Identificar el tipo de aplicación.* Spotify es una aplicación híbrida multiplataforma: reutiliza un núcleo de interfaz y lógica común para la gestión de catálogos y playlists, pero se distribuye e instala de forma nativa en Android e iOS, ofreciendo acceso directo al hardware del dispositivo para la decodificación de audio, notificaciones y conexión a periféricos multimedia @biornhansen2020.

  *Paso 2. Describir su interfaz de usuario.* Está compuesta por la barra de navegación inferior (Inicio, Búsqueda, Tu Biblioteca, Premium), la cuadrícula de acceso rápido, secciones recomendadas, filtros de contenido y el mini-reproductor flotante con controles de reproducción @apple_hig_2024.

  *Paso 3. Describir su lógica de funcionamiento.* Se encarga de decodificar y procesar los flujos de audio en streaming, gestionar el búfer de reproducción continua, calcular recomendaciones personalizadas y coordinar la transmisión de sonido en tiempo real.

  *Paso 4. Describir el manejo de eventos.* La aplicación responde a toques táctiles en tarjetas de contenido para reproducir temas, gestos en el mini-reproductor para cambiar de canción, selecciones de navegación y eventos del sistema como la desconexión de audífonos.

  *Paso 5. Describir la navegación entre pantallas.* El usuario pasa de la pantalla de Inicio o Búsqueda hacia el detalle de una playlist o álbum, y desde allí al reproductor a pantalla completa, a la vista de letras o a los ajustes de calidad.

  *Paso 6. Describir el almacenamiento de datos.* Las canciones descargadas para modo offline, la memoria caché e imágenes se guardan localmente en el dispositivo, mientras que las listas guardadas, canciones marcadas y el historial se sincronizan en la nube.

  *Paso 7. Organizar el análisis en un esquema que resuma los cinco elementos identificados.* En la @fig-mobile-arch se ilustra el esquema de arquitectura y el flujo de comunicación entre los componentes fundamentales de la aplicación.

  #figure(
    image("img/mobile_architecture.svg", width: 50%),
    caption: [Esquema de arquitectura y flujo de comunicación entre los componentes básicos de una aplicación móvil aplicada al análisis de Spotify.],
  ) <fig-mobile-arch>

  == Ejercicio 4: Análisis de Aplicaciones Móviles de Uso Frecuente

  En la @tabla-apps-frecuentes se presenta el análisis detallado de seis aplicaciones móviles populares de uso frecuente junto a sus respectivas capturas de interfaz, detallando su finalidad, perfil de usuario, funciones principales y los componentes visuales de interfaz que implementan @apple_hig_2024.
  #[
    #show figure.where(kind: table): set block(breakable: true)
    #set table.cell(breakable: false)
    #figure(
      table(
        columns: (1fr, 1fr, 1fr, 1.4fr, 1.4fr),
        align: (
          left + horizon,
          left + horizon,
          left + horizon,
          left + horizon,
          left + horizon,
        ),
        stroke: 0.5pt + rgb("#808080"),
        table.header(
          table.cell(fill: rgb("#E5E7EB"))[*Aplicación*],
          table.cell(fill: rgb("#E5E7EB"))[*Finalidad*],
          table.cell(fill: rgb("#E5E7EB"))[*Tipo de Usuario*],
          table.cell(fill: rgb("#E5E7EB"))[*Funciones Principales*],
          table.cell(fill: rgb("#E5E7EB"))[*Elementos de Interfaz*],
        ),
        [
          *WhatsApp*\
          #v(3pt)
          #align(center)[#image("img/whatsapp.jpeg", width: 70%)]
        ],
        [Mensajería instantánea y comunicación VoIP.],
        [Público general con teléfono inteligente.],
        [Envío de mensajes multimedia, llamadas de voz/video, estados, grupos.],
        [`RecyclerView`, `BottomNavigationView`, `FloatingActionButton`, `Toolbar`, ventanas de diálogo.],

        [
          *Spotify*\
          #v(3pt)
          #align(center)[#image("img/spotify.jpeg", width: 70%)]
        ],
        [Reproducción de música y podcasts en streaming.],
        [Amantes de la música y oyentes de podcasts.],
        [Búsqueda de canciones, reproducción en segundo plano, playlists, descargas.],
        [`CardView`, reproductor flotante, barras de progreso, listas de desplazamiento.],

        [
          *Google Maps*\
          #v(3pt)
          #align(center)[#image("img/maps.jpeg", width: 70%)]
        ],
        [Navegación GPS y ubicación geoespacial.],
        [Conductores, peatones, viajeros y turistas.],
        [Rutas en tiempo real, cálculo de tráfico, localización de sitios de interés.],
        [`MapView` interactivo, barra de búsqueda flotante, botones de capas, paneles desplegables.],

        [
          *Duolingo*\
          #v(3pt)
          #align(center)[#image("img/duolingo.jpeg", width: 70%)]
        ],
        [Aprendizaje gamificado de idiomas.],
        [Estudiantes, niños, jóvenes y adultos autodidactas.],
        [Lecciones interactivas, rachas diarias, ejercicios de voz y lectura.],
        [Barras de progreso, animaciones Lottie, botones de opción múltiple, diálogos modales.],

        [
          *Yape*\
          #v(3pt)
          #align(center)[#image("img/yape.jpeg", width: 70%)]
        ],
        [Micropagos digitales y transferencias inmediatas.],
        [Usuarios bancarizados, comerciantes y público general.],
        [Transferencia por número de celular, pago con código QR, recargas de servicios.],
        [Teclado numérico personalizado, `ImageView` para código QR, tarjetas de saldo, alertas toast.],

        [
          *Notion*\
          #v(3pt)
          #align(center)[#image("img/notion.jpeg", width: 70%)]
        ],
        [Gestión de notas, tareas y productividad personal.],
        [Estudiantes, profesionales y equipos de trabajo.],
        [Creación de notas enriquecidas, listas de tareas, tableros Kanban, sincronización.],
        [`NavigationDrawer` lateral, listas de verificación, editores de texto enriquecido, botones flotantes.],
      ),
      caption: [Análisis detallado de seis aplicaciones móviles de uso frecuente con sus respectivas capturas de interfaz.],
    ) <tabla-apps-frecuentes>
  ]

  == Ejercicio 5: Plantea una idea sencilla de aplicación móvil que ayude a resolver una necesidad real, indicando: nombre de la app, problema que busca resolver, usuario al que está dirigida, funciones básicas y posible estructura de pantallas. Este ejercicio servirá como base para el trabajo de las siguientes unidades.

  A continuación se presenta la propuesta conceptual de la aplicación móvil MiUnib, diseñada como solución tecnológica para la gestión y monitoreo del transporte escolar:

  - *Nombre de la aplicación:* MiUnib

  - *Problema que busca resolver:* La falta de visibilidad e incertidumbre de los padres sobre el traslado diario de sus hijos, sumada a la ausencia de una herramienta de supervisión centralizada para las instituciones educativas que permita auditar rutas, vehículos y asistencia en tiempo real.

  - *Usuarios a los que está dirigida:*
    - *Escuela-Admin (Director / Dueño):* Gestión del colegio: creación y edición de rutas con paradas, inventario de movilidades, asignación de conductores y alumnos, auditoría de reportes/historiales y administración de usuarios Staff.
    - *Escuela-Staff (Personal del colegio):* Asignación de alumnos a rutas y registro manual de asistencia con permisos de edición limitada, además de acceso en solo lectura a movilidades y reportes.
    - *Conductor / Movilidad:* Control de trayecto, visualización de paradas ordenadas y registro de subida y bajada de estudiantes mediante escaneo QR.
    - *Padre / Madre:* Visualización del recorrido en tiempo real, recepción de notificaciones automáticas, confirmación de conductor, historial de viajes y calificación del servicio.
    - *Hijo / Estudiante:* Identificación rápida mediante un código QR personal único para el marcado de abordaje y descenso de la movilidad.

  - *Funciones básicas:*
    - *Rastreo GPS en tiempo real:* Seguimiento continuo de la unidad vehicular durante la ruta activa con intervalos de actualización.
    - *Registro de abordaje y asistencia vía QR:* Escaneo de código QR único por alumno al subir y bajar del vehículo.
    - *Notificaciones push e inteligencia de geocercas:* Alertas automáticas para avisar el inicio de ruta, proximidad a la vivienda, recogida confirmada y llegada/salida de la institución educativa.
    - *Gestión de rutas, movilidades y usuarios:* Panel administrativo para la georreferenciación de paradas, asignación de unidades vehiculares y vinculación familiar padre-estudiante-conductor.

  - *Posible estructura de pantallas:* En la @fig-miunib-screens se detalla el diagrama de navegación de la solución, organizado por vistas estratégicas según el rol de cada usuario:
    - *Super-Admin:* Dashboard de colegios activos, editor de marca e historial de suscripciones SaaS.
    - *Escuela-Admin / Staff:* Panel con mapa de unidades activas, gestor de rutas/paradas, registro de movilidades y asignación de alumnos.
    - *Conductor:* Vista de ruta del día, mapa interactivo de paradas secuenciales y escáner QR de abordaje.
    - *Padre / Madre:* Mapa en tiempo real con tiempo estimado de llegada, centro de notificaciones, historial de viajes y panel de confirmación/calificación.

  #figure(
    image("img/miunib_screens.svg", width: 75%),
    caption: [Estructura de pantallas y flujo de navegación según el rol de usuario en MiUnib.],
  ) <fig-miunib-screens>

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. *¿Cuál es la diferencia principal entre una aplicación nativa, una aplicación web y una aplicación híbrida? Menciona en qué situación elegirías cada una.*

    La diferencia principal reside en su arquitectura de ejecución y nivel de acoplamiento con el sistema operativo. La app nativa interactúa directamente con las APIs del SO y la GPU, entregando el máximo rendimiento y control del hardware a costo de mantener códigos separados. La app híbrida utiliza un único código base ejecutado sobre una capa de abstracción o puente, equilibrando rendimiento y velocidad de desarrollo multiplataforma. Por último, la app web corre dentro del motor de un navegador, garantizando portabilidad inmediata sin instalación, pero con acceso restringido al hardware y rendimiento limitado.

  2. *De los cinco elementos que conforman la estructura básica de una aplicación móvil (interfaz, lógica, eventos, navegación, datos), ¿cuál consideras más crítico para que la app funcione correctamente? Justifica tu respuesta.*

    El elemento más crítico es la lógica de funcionamiento, ya que constituye el motor que define las reglas de negocio en el procesamiento de los datos; mientras que la interfaz y la navegación actúan como canales de presentación y flujo, y los eventos funcionan como simples detonadores, es la lógica la que da sentido a las acciones del usuario, sin esta la app no cumpliría ninguna funcionalidad útil.
][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - El desarrollo móvil demanda un pensamiento desacoplado (interfaz, lógica, eventos, navegación y datos) no como preferencia de diseño, sino como una necesidad impuesta por las restricciones físicas de hardware, memoria y conectividad variable.

  - La selección entre paradigmas nativos, híbridos o web busca el balance de compromisos técnicos entre rendimiento, acceso al hardware y costos de desarrollo y mantenimiento. La adopción de una solución móvil se justifica cuando el valor depende de la integración profunda, persistencia y comunicación en tiempo real, pero los frameworks multiplataforma actuales ofrecen una alternativa viable.

  - La descomposición de una aplicación antes de iniciar la codificación previene defectos arquitectónicos y sobrecarga de estado. La importancia de correlacionar eventos con modelos de datos sincronizados permite afrontar desarrollos iterativos de mayor envergadura.
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
