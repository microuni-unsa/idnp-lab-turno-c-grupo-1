#import "/components/@unsareport/epis-lab/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Persistencia de datos con Room Database en Jetpack Compose",
  lab_number: "09",
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
  == Repositorio de código fuente

  El código fuente de las implementaciones se encuentra disponible en:

  Resuelto: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l9/src/resuelto

  Christian Mestas: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l9/src/christian

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l9/src/yenaro

  == Ejercicio resuelto por el docente: edificios y ambientes con Room

  Se documenta el desarrollo paso a paso de la aplicación PersistenciaRoom Compose destinada a almacenar edificios del campus y sus ambientes en una base de datos local con la biblioteca Room, exponiendo el resultado relacional en una lista perezosa @android-room @video-room-crud.

  Paso 1. Creación del proyecto PersistenciaRoom Compose con nombre de paquete de ejemplo en lenguaje Kotlin, empleando para la verificación visual un emulador con interfaz de programación de aplicaciones, en adelante API, de nivel 34 o superior, según los recursos exigidos por la guía.

  Paso 2. Configuración de Gradle para usar el procesador de símbolos de Kotlin, en adelante KSP, junto con Room. El archivo de módulo aplica el complemento y agrega las dependencias con una sola versión común, como se aprecia a continuación.

  #figure(
    image("img/resuelto/codigo_paso2c_app.png", width: 48%),
    caption: [Dependencias Room y modelo de vista en el módulo en el entorno de desarrollo.],
  ) <fig-resuelto-paso2c>

  El proyecto conserva el complemento de Android para Gradle, en adelante AGP, en versión 9.4.1 y Kotlin en versión 2.2.10, compatibles con KSP 2.3.6 y Room 2.7.2 según la nota de versiones de la guía.

  Paso 4. Definición de las entidades EdificioEntity y AmbienteEntity, donde la segunda referencia a la primera con clave foránea mediante la columna edificioId, cada una con identificador autogenerado, como se aprecia a continuación.

  #figure(
    image("img/resuelto/codigo_paso5_ambiente.png", width: 48%),
    caption: [Entidad AmbienteEntity con clave foránea en el entorno de desarrollo.],
  ) <fig-resuelto-paso5>

  Paso 6. Creación de la interfaz EdificioDao con las operaciones de crear, leer, actualizar y eliminar, en adelante CRUD, y la clase intermedia EdificioConAmbientes con su consulta relacional transaccional que devuelve el edificio con sus ambientes, como se aprecia a continuación.

  #figure(
    image("img/resuelto/codigo_paso7_relation.png", width: 48%),
    caption: [Consulta relacional con edificio y ambientes en el entorno de desarrollo.],
  ) <fig-resuelto-paso7>

  Paso 8. Creación de la clase AppDatabase que reúne ambas entidades en versión 1 y expone el DAO, junto a la función que la instancia una sola vez con el constructor de bases de datos de Room sobre el archivo app.db, como se aprecia a continuación.

  #figure(
    image("img/resuelto/codigo_paso8_database.png", width: 48%),
    caption: [Base de datos con ambas entidades en el entorno de desarrollo.],
  ) <fig-resuelto-paso8>

  El modelo de vista inserta con una corrutina los dos edificios de muestra con sus ambientes si la base está vacía y expone el resultado relacional como flujo de estado observable: Biblioteca Central de categoría Servicios con Sala de lectura y Hemeroteca, y Facultad de Ingeniería de categoría Académico con Laboratorio de cómputo.

  Paso 10. Creación de la función de composición ListaEdificiosConAmbientes, que observa el flujo del modelo y muestra el resultado en una lista perezosa con clave estable por identificador de edificio, agrupando los ambientes bajo el nombre de cada edificio, como se aprecia a continuación.

  #figure(
    image("img/resuelto/codigo_paso10_lista.png", width: 48%),
    caption: [Lista perezosa de edificios con ambientes en el entorno de desarrollo.],
  ) <fig-resuelto-paso10>

  Paso 11. Reescritura de MainActivity para crear la base de datos, el DAO y la factoría, y mostrar la lista dentro del tema de la aplicación sobre una superficie de tamaño completo, conservando la salutación y la vista previa de la plantilla.

  Paso 12. Ejecución de la aplicación en el emulador y verificación de que los edificios aparecen junto con sus ambientes, y de que al cerrar y volver a abrir la aplicación los datos siguen ahí, como se aprecia a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/resuelto/01_app_lista_edificios.png", width: 48%),
      caption: [Lista de edificios con ambientes en el emulador.],
    ) <fig-resuelto-app>],
    [#figure(
      image("img/resuelto/02_app_reapertura_persistencia.png", width: 48%),
      caption: [Lista intacta tras cerrar y reabrir la aplicación.],
    ) <fig-resuelto-reapertura>]
  )

  Paso 13. Verificación de las tablas con la aplicación en ejecución mediante consola del sistema, revisando EdificioEntity con dos filas y AmbienteEntity con tres filas, como se aprecia a continuación.

  #figure(
    image("img/resuelto/03_inspector_tablas_resuelto.png", width: 80%),
    caption: [Tablas EdificioEntity y AmbienteEntity con datos verificados.],
  ) <fig-resuelto-tablas>

  == Ejercicios propuestos: tercera tabla de notas con segunda consulta relacional

  La guía solicita ampliar el modelo resuelto a tres tablas para almacenar anotaciones de texto sobre el estado o uso de cada ambiente, con el diagrama del modelo, la implementación completa en Room, una segunda consulta relacional distinta de la del resuelto y botones para eliminar y actualizar con persistencia verificada.

  === Implementación desarrollada por Christian Mestas

  La solución copia el resuelto y agrega la entidad NotaEntity con identificador autogenerado, clave foránea ambienteId hacia AmbienteEntity y texto de la nota, manteniendo la base en versión 1 con instalación fresca.

  #code-block(
"@Entity(
  foreignKeys = [
    ForeignKey(
      entity = AmbienteEntity::class,
      parentColumns = [\"id\"],
      childColumns = [\"ambienteId\"]
    )
  ]
)
data class NotaEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val ambienteId: Int,
  val texto: String
)",
    lang: "kotlin",
  )

  El modelo de datos muestra tres tablas donde Edificio se relaciona uno a muchos con Ambiente mediante edificioId, y Ambiente se relaciona uno a muchos con Nota mediante ambienteId.

  El DAO extendido conserva cada firma del resuelto salvo la inserción de ambiente, que ahora devuelve el identificador generado para sembrar las notas con identificadores reales, y agrega inserción, actualización y borrado para las tres tablas, con dos consultas de borrado por padre y dos funciones transaccionales que borran hijos antes que padres. La segunda consulta relacional agrupa notas bajo cada ambiente y es distinta de la del resuelto porque invierte el nivel de agrupación.

  #code-block(
"data class AmbienteConNotas(
  @Embedded val ambiente: AmbienteEntity,
  @Relation(parentColumn = \"id\", entityColumn = \"ambienteId\")
  val notas: List<NotaEntity>
)

@Transaction
@Query(\"SELECT * FROM AmbienteEntity\")
suspend fun obtenerAmbientesConNotas(): List<AmbienteConNotas>",
    lang: "kotlin",
  )

  El modelo de vista siembra cuatro notas fijas tras insertar los ambientes: El proyector no funciona y Se reserva los martes en Sala de lectura, Ventana rota en el ala norte en Hemeroteca y Faltan teclados en el módulo 3 en Laboratorio de cómputo. Además expone eliminar y renombrar para edificio y ambiente, agregar ambiente con nombre escrito y agregar nota con texto escrito, donde renombrar añade el sufijo de edición mediante copia y actualización, con recarga de ambos flujos tras cada cambio.

  La interfaz conserva la lista del resuelto y agrega por cada edificio un campo Nuevo ambiente con botón Añadir y por cada ambiente los dos botones de texto Eliminar y Renombrar más un campo Nueva nota con botón Añadir, seguida de la sección Notas por ambiente que observa el segundo flujo relacional, como se aprecia a continuación.

  #figure(
    image("img/christian/01_lista_tres_tablas.png", width: 80%),
    caption: [Lista con edificios, ambientes y botones en el entorno de desarrollo.],
  ) <fig-christian-lista>

  #figure(
    image("img/christian/02_consulta_ambiente_notas.png", width: 80%),
    caption: [Segunda consulta con notas por ambiente en el entorno de desarrollo.],
  ) <fig-christian-notas>

  La creación de ambientes y notas desde la interfaz se verifica en las siguientes capturas mediante el inspector de bases de datos en el entorno de desarrollo, donde el campo Nuevo ambiente agrega ambientes a AmbienteEntity y el campo Nueva nota agrega las notas correspondientes en NotaEntity, ambas persistidas en la base de datos.

  #figure(
    image("img/christian/03_crear_ambiente.png", width: 80%),
    caption: [Creación de ambientes verificada en Database Inspector en el entorno de desarrollo.],
  ) <fig-christian-crear-ambiente>

  #figure(
    image("img/christian/04_crear_nota.png", width: 80%),
    caption: [Creación de notas verificada en Database Inspector en el entorno de desarrollo.],
  ) <fig-christian-crear-nota>

  Los cambios de estilo se limitan a espaciados, grosores de fuente y colores del tema, sin navegación ni pantallas nuevas.

  === Implementación desarrollada por Yenaro Noa

  La solución desarrollada por Yenaro Noa representa Biblioteca Central de categoría Servicios con Sala de lectura y Hemeroteca, y Facultad de Ingeniería de categoría Académico con Laboratorio de cómputo, con las mismas tres tablas y claves foráneas que la solución de Christian pero con borrado en cascada declarado en cada clave y un índice sobre cada columna hija, lo cual permite eliminar un edificio o un ambiente con una sola llamada sin consultas de borrado manuales previas.

  #figure(
    image("img/yenaro/01_propuesto_entidades.png", width: 48%),
    caption: [Tres entidades con claves foráneas en cascada en el entorno de desarrollo.],
  ) <fig-yenaro-entidades>

  El diagrama del modelo de datos muestra Edificio uno a muchos con Ambiente mediante edificioId y Ambiente uno a muchos con Nota mediante ambienteId, con borrado en cascada en ambos niveles, como se aprecia a continuación.

  #figure(
    image("img/yenaro/00_diagrama_tablas.png", width: 48%),
    caption: [Diagrama de las tres tablas con borrado en cascada.],
  ) <fig-yenaro-diagrama>

  El DAO ofrece inserción, actualización y borrado para las tres tablas y ambas consultas relacionales con la misma forma que el resuelto extendido, como se aprecia a continuación.

  #figure(
    image("img/yenaro/04_propuesto_dao_consultas.png", width: 48%),
    caption: [Consultas relacionales para edificios y ambientes en el entorno de desarrollo.],
  ) <fig-yenaro-consultas>


  El modelo de vista siembra El proyector no funciona y Se reserva los martes en Sala de lectura, Aforo máximo con 40 personas en Hemeroteca y 30 PCs operativas en Laboratorio de cómputo, con borrado directo apoyado en la cascada y agregado de nota con numeración automática según el total existente, como se aprecia a continuación.

  #figure(
    image("img/yenaro/05_propuesto_viewmodel.png", width: 48%),
    caption: [Modelo de vista con siembra y operaciones en el entorno de desarrollo.],
  ) <fig-yenaro-viewmodel>

  Como aporte propio, toda la pantalla usa un estilo neobrutalista con colores planos brillantes según categoría sobre fondo crema, bordes negros gruesos con sombra dura desplazada y botones blancos con borde negro, como se aprecia a continuación.


  #figure(
    image("img/yenaro/09_propuesto_neo.png", width: 48%),
    caption: [Tarjeta y paleta neobrutalista en el entorno de desarrollo.],
  ) <fig-yenaro-neo>

  La lista agrupa cada edificio en una tarjeta con nombre, categoría en mayúsculas y sus ambientes con botones Editar, Borrar y "+ Nota", seguida de la sección Ambientes y notas con el conteo de notas y botón Quitar por nota, como se aprecia a continuación.


  #figure(
    image("img/yenaro/11_propuesto_ui_edificios.png", width: 48%),
    caption: [Tarjetas de edificios con ambientes en el entorno de desarrollo.],
  ) <fig-yenaro-ui-edificios>

  #figure(
    image("img/yenaro/13_propuesto_ui_notas.png", width: 48%),
    caption: [Sección de ambientes con notas en el entorno de desarrollo.],
  ) <fig-yenaro-ui-notas>

  El cambio de nombre usa un diálogo con campo Nuevo nombre y botones Guardar y Cancelar, como se aprecia a continuación.

  #figure(
    image("img/yenaro/14_propuesto_dialogo.png", width: 48%),
    caption: [Diálogo para renombrar ambiente en el entorno de desarrollo.],
  ) <fig-yenaro-dialogo>

  La lista completa con los dos edificios y sus ambientes, con estilo neobrutalista, y el diálogo de cambio de nombre al pulsar Editar en Sala de lectura se muestran a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/yenaro/01_yenaro_base.png", width: 48%),
      caption: [Lista completa con edificios y ambientes en el emulador.],
    ) <fig-yenaro-base>],
    [#figure(
      image("img/yenaro/02_yenaro_dialogo.png", width: 48%),
      caption: [Diálogo abierto para renombrar ambiente en el emulador.],
    ) <fig-yenaro-dialogo-emulador>]
  )

  Tras guardar Sala Norte el ambiente muestra el nombre nuevo en ambas secciones, y tras borrar Hemeroteca con sus notas mediante la cascada quedan Biblioteca Central con Sala Norte y Facultad de Ingeniería con Laboratorio de cómputo, como se aprecia a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/yenaro/03_yenaro_actualizado.png", width: 48%),
      caption: [Ambiente renombrado a Sala Norte en ambas secciones en el emulador.],
    ) <fig-yenaro-actualizado>],
    [#figure(
      image("img/yenaro/04_yenaro_eliminado.png", width: 48%),
      caption: [Lista tras eliminar Hemeroteca con sus notas en cascada.],
    ) <fig-yenaro-eliminado>]
  )

  Al pulsar "+ Nota" en Sala Norte se agrega Nota 3 numerada automáticamente, como se aprecia a continuación.

  #figure(
    image("img/yenaro/05_yenaro_nota_nueva.png", width: 22%),
    caption: [Nota nueva numerada en ambas secciones en el emulador.],
  ) <fig-yenaro-nota-nueva>

  La verificación de tablas con la aplicación en ejecución mediante consola del sistema muestra EdificioEntity con dos filas, AmbienteEntity con dos filas y NotaEntity con cuatro filas, como se aprecia a continuación.

  #figure(
    image("img/yenaro/06_yenaro_tablas.png", width: 80%),
    caption: [Tres tablas pobladas verificadas en consola del sistema.],
  ) <fig-yenaro-tablas>

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. Lograste resolver completamente el ejercicio resuelto por el docente con datos de edificios y ambientes almacenados con Room, que persisten al cerrar y volver a abrir la aplicación. Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.

    Sí, se resolvió el ejercicio en el proyecto PersistenciaRoom Compose con los dos edificios y tres ambientes de la guía. Como evidencia, en la @fig-resuelto-app se aprecia la lista con Biblioteca Central y Facultad de Ingeniería junto a sus ambientes, en la @fig-resuelto-reapertura la misma lista tras cerrar y reabrir, y en la @fig-resuelto-tablas las tablas pobladas verificadas, lo cual confirma la persistencia tras reinicio.

    #figure(
      image("img/resuelto/01_app_lista_edificios.png", width: 22%),
      caption: [Evidencia del resultado en el emulador con edificios y ambientes.],
    ) <fig-cuestionario-evidencia>

  2. Qué otra alternativa de almacenamiento podría emplearse en una aplicación con necesidades similares. Justifica tu respuesta comparando ventajas y desventajas frente a Room.

    Para necesidades similares existen el almacén de preferencias clave valor, en adelante DataStore, y el uso directo de SQLite con ayudante de apertura y sentencias manuales. DataStore ofrece escritura asíncrona sencilla y es ideal para ajustes o sesión, pero no modela tablas ni relaciones y carece de consultas relacionales, por lo que no cubre el vínculo edificio ambiente nota @android-room. El uso directo de SQLite da control total sobre el esquema y evita una dependencia adicional, pero exige escribir SQL a mano, convertir cursores fila por fila y mantener migraciones manualmente, con errores que solo aparecen en ejecución. Room verifica tablas, columnas y consultas en tiempo de compilación, genera el código repetitivo mediante KSP, integra flujos observables y expresa relaciones con anotaciones, a cambio de una configuración inicial mayor y una biblioteca adicional. Por ello Room es la opción adecuada cuando hay entidades relacionadas y consultas entre tablas, mientras que DataStore basta para preferencias simples @video-room-crud.

  3. En dónde almacena Room los datos de la aplicación. Justifica tu respuesta y explica qué implica para la privacidad de los datos del usuario.

    Room guarda el archivo app.db en el almacenamiento interno privado de la aplicación, dentro del directorio de bases de datos del paquete, gestionado por el sistema con SQLite @android-room. El aislamiento implica que ninguna otra aplicación puede leer ese archivo sin permisos de superusuario, que los datos se eliminan al desinstalar y que el acceso externo solo ocurre si el usuario habilita copias de seguridad o exportación. Para la privacidad esto significa resguardo por defecto frente a otras aplicaciones, pero también responsabilidad de cifrar datos sensibles y de configurar reglas de respaldo, porque una copia en la nube expone el contenido fuera del dispositivo.

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - La configuración con KSP y Room en una sola versión común para ejecución, extensión de Kotlin y compilador permite que la verificación de entidades, claves foráneas y consultas ocurra en tiempo de compilación en lugar de fallar en ejecución.
  - El DAO con inserción, actualización, borrado y dos consultas relacionales separa la escritura del modelo de su lectura agrupada, de modo que la interfaz observa flujos y se recompone sin gestionar cursores.
  - La tercera tabla de notas con su clave foránea y la consulta que agrupa notas bajo ambiente extiende el resuelto sin romperlo, con borrado ordenado manual en la versión de Christian y borrado en cascada en la versión de Yenaro, y los botones de eliminar y renombrar confirman que cada cambio persiste tras reinicio.
  - El archivo de base de datos en almacenamiento interno privado explica la persistencia observada y fundamenta el análisis de privacidad del cuestionario.

]

#lab-section("REFERENCIAS Y BIBLIOGRAFÍA")[
  #show heading: set text(weight: "bold")
  #set heading(numbering: none)
  = REFERENCIAS
  #bibliography("bibliography.bib", style: "ieee", title: none)
]

// #lab-section("RETROALIMENTACIÓN GENERAL")[
//   #block(height: 6em, breakable: false)
// ]
