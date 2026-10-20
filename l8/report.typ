#import "/components/@unsareport/epis-lab/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Listas filtrables con LazyColumn en Jetpack Compose",
  lab_number: "08",
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

  Resuelto: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l8/src/resuelto

  Christian Mestas: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l8/src/l8-christian-codigo

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l8/src/yenaro

  == Ejercicio resuelto por el docente: Lista de edificios con búsqueda en tiempo real

  Se documenta el desarrollo paso a paso de la aplicación ListaFiltrable Compose destinada a mostrar edificios del campus con filtrado en tiempo real, combinando lista perezosa con claves estables y campo de búsqueda con estado @android-lists @video-lazycolumn.

  Paso 1. Creación del proyecto ListaFiltrable Compose con nombre de paquete de ejemplo en lenguaje Kotlin, empleando para la verificación visual un emulador con interfaz de programación de aplicaciones, en adelante API, de nivel 36, superior al mínimo 34 exigido.

  Paso 2. Declaración de las importaciones del ejercicio debajo del paquete y antes de la clase MainActivity, cubriendo disposición, lista perezosa con creación de filas, tarjeta, campo de búsqueda, superficie, texto, composición, estado, alineación, color, tipografía, vista previa y unidades junto con el tema de la aplicación.

  #figure(
    image("img/resuelto/codigo_paso2_imports.png", width: 75%),
    caption: [Importaciones del ejercicio en el entorno de desarrollo.],
  ) <fig-resuelto-paso2>

  Paso 3. Definición del modelo Edificio fuera de cualquier función de composición, con identificador, nombre, categoría y descripción de cada edificio.

  #figure(
    image("img/resuelto/codigo_paso3_dataclass.png", width: 75%),
    caption: [Modelo Edificio en el entorno de desarrollo.],
  ) <fig-resuelto-paso3>

  Paso 4. Creación de la función de composición ListaEdificios con una lista de muestra de seis edificios y una variable de estado para el texto de búsqueda.

  #figure(
    image("img/resuelto/codigo_paso4_estado.png", width: 75%),
    caption: [Lista de muestra y estado de búsqueda en el entorno de desarrollo.],
  ) <fig-resuelto-paso4>

  Paso 5. Declaración encima de la lista de un campo de búsqueda ligado a la variable de texto, con un espaciador intermedio.

  #figure(
    image("img/resuelto/codigo_paso5_search.png", width: 75%),
    caption: [Campo de búsqueda ligado al estado en el entorno de desarrollo.],
  ) <fig-resuelto-paso5>

  Paso 6. Cálculo de la lista filtrada a partir del texto, recalculada en cada recomposición mediante comparación de subcadenas sin distinción entre mayúsculas y minúsculas.

  #code-block(
"val edificiosFiltrados = edificios.filter {
  it.nombre.contains(texto, ignoreCase = true)
}",
    lang: "kotlin",
  )

  #figure(
    image("img/resuelto/codigo_paso6_filter.png", width: 75%),
    caption: [Filtrado por nombre en cada recomposición en el entorno de desarrollo.],
  ) <fig-resuelto-paso6>

  Paso 7. Declaración de la lista perezosa usando creación de filas con clave estable por identificador para dibujar cada fila a partir de la lista filtrada.

  #figure(
    image("img/resuelto/codigo_paso7_lazy.png", width: 75%),
    caption: [Lista perezosa con clave estable en el entorno de desarrollo.],
  ) <fig-resuelto-paso7>

  Paso 8. Implementación de la función FilaEdificio, mostrando dentro de una tarjeta el nombre destacado, la categoría en tono gris y la descripción.

  #figure(
    image("img/resuelto/codigo_paso8_fila.png", width: 75%),
    caption: [Fila con tarjeta y tres datos en el entorno de desarrollo.],
  ) <fig-resuelto-paso8>

  Paso 9. Presentación de ListaEdificios dentro del bloque de establecimiento de contenido de MainActivity, sobre una superficie de tamaño completo y dentro del tema de la aplicación, junto con su vista previa.

  #figure(
    image("img/resuelto/codigo_paso9_main.png", width: 75%),
    caption: [Actividad principal y vista previa en el entorno de desarrollo.],
  ) <fig-resuelto-paso9>

  Paso 10. Ejecución de la aplicación en el emulador y verificación de que, al escribir en el campo de búsqueda, la lista se actualiza mostrando solo las coincidencias con dos textos distintos, como se aprecia a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/resuelto/02_filtro_facultad.png", width: 60%),
      caption: [Filtro Facultad.],
    ) <fig-resuelto-filtro-facultad>],
    [#figure(
      image("img/resuelto/03_filtro_biblio.png", width: 60%),
      caption: [Filtro Biblioteca.],
    ) <fig-resuelto-filtro-biblio>]
  )

  Al escribir Facultad solo permanecen las dos facultades, y al escribir Biblioteca solo permanece la biblioteca.

  Paso 11. Prueba del caso sin coincidencias con texto inexistente, confirmando que la lista queda vacía sin errores. La lista completa inicial se muestra junto al caso vacío para contraste, como se aprecia a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/resuelto/01_base_6items.png", width: 70%),
      caption: [Lista completa con los seis edificios en el emulador.],
    ) <fig-resuelto-base>],
    [#figure(
      image("img/resuelto/04_sin_coincidencias.png", width: 70%),
      caption: [Lista vacía sin coincidencias en el emulador.],
    ) <fig-resuelto-vacia>]
  )


  == Ejercicios propuestos: Lista completa de ocho edificios desde archivo con filtro por categoría

  La guía solicita ampliar el resuelto para mostrar ocho edificios reales del campus con cuatro aspectos: carga desde archivo de texto en lugar de lista fija, icono representativo en cada fila, filtro por categoría además de por nombre e integración opcional con tarjetas del laboratorio anterior.

  === Implementación desarrollada por Yenaro Noa

  La solución desarrollada por Yenaro Noa representa ocho edificios como Biblioteca Central, Comedor Universitario, Facultad de Ingeniería, Facultad de Ciencias, Coliseo Universitario, Auditorio Central, Pabellón de Sistemas y Rectorado, cargados desde un archivo de datos en notación de objetos de JavaScript, en adelante JSON, ubicado en la carpeta assets, siguiendo el patrón de persistencia de laboratorios previos y la carga desde recursos del laboratorio de planos. Como aporte propio, toda la pantalla usa un estilo neobrutalista con colores planos brillantes, bordes negros gruesos, sombra dura desplazada sin difuminar y fondo crema.

  #figure(
    image("img/yenaro/01_propuesto_json.png", width: 75%),
    caption: [Archivo de edificios en recursos en el entorno de desarrollo.],
  ) <fig-yenaro-json>

  La lectura del archivo se concentra en una función de carga invocada una sola vez con memoria de composición desde la función de lista, procesando el arreglo con la interfaz estándar sin dependencias adicionales.

  #figure(
    image("img/yenaro/02_propuesto_carga.png", width: 66%),
    caption: [Carga desde recursos con memoria de composición en el entorno de desarrollo.],
  ) <fig-yenaro-carga>

  El filtrado combina ambos criterios con un campo de texto para el nombre y un menú desplegable para la categoría con las opciones Todas, Servicios, Académico, Deportes y Eventos, recalculando la lista en cada recomposición.

  #code-block(
"val edificiosFiltrados = edificios.filter {
  (categoria == Todas || it.categoria == categoria) &&
    it.nombre.contains(texto, ignoreCase = true)
}",
    lang: "kotlin",
  )

  Cada fila abandona la tarjeta del resuelto y usa un contenedor con fondo plano según categoría en tonos amarillo, azul, verde y rosado, borde negro grueso, sombra dura negra desplazada e icono negro en recuadro blanco con borde, con textos en negro y categoría en mayúsculas.

  #figure(
    image("img/yenaro/04_propuesto_fila.png", width: 75%),
    caption: [Fila con icono por categoría en el entorno de desarrollo.],
  ) <fig-yenaro-fila>

  La actividad principal y la vista previa completan la pantalla con iconos de Material según categoría.

  #figure(
    image("img/yenaro/05_propuesto_main.png", width: 75%),
    caption: [Actividad principal y vista previa en el entorno de desarrollo.],
  ) <fig-yenaro-main>

  La lista completa con los ocho edificios y el filtrado por nombre con Facultad se muestran a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/yenaro/01_yenaro_base_8.png", width: 50%),
      caption: [Lista completa con los ocho edificios en el emulador.],
    ) <fig-yenaro-base>],
    [#figure(
      image("img/yenaro/02_yenaro_filtro_nombre.png", width: 50%),
      caption: [Filtro por nombre Facultad en el emulador.],
    ) <fig-yenaro-filtro-nombre>]
  )

  Al escribir Facultad con categoría Todas solo permanecen las dos facultades con sus iconos de escuela.

  Al elegir la categoría Servicios la lista muestra solo Biblioteca Central, Comedor Universitario y Rectorado, verificando el segundo criterio. Con texto inexistente la lista queda vacía sin errores, confirmando el caso sin coincidencias, como se aprecia a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/yenaro/03_yenaro_filtro_categoria.png", width: 50%),
      caption: [Filtro por categoría Servicios en el emulador.],
    ) <fig-yenaro-filtro-categoria>],
    [#figure(
      image("img/yenaro/04_yenaro_sin_resultados.png", width: 50%),
      caption: [Sin coincidencias con doble filtro en el emulador.],
    ) <fig-yenaro-vacia>]
  )

  === Implementación desarrollada por Christian Mestas

  La solución desarrollada por Christian Mestas representa ocho edificios como Biblioteca Central, Comedor Universitario, Facultad de Ingeniería de Producción y Servicios, Facultad de Ciencias Naturales y Formales, Pabellón de Ingeniería de Sistemas, Coliseo Universitario, Estadio Universitario y Auditorio Central, cargados desde el archivo de texto edificios.txt en la carpeta assets con formato de campos separados por barras verticales, siguiendo el mismo patrón de persistencia de laboratorios previos.

  La lectura ignora líneas vacías y descarta registros incompletos, convirtiendo el identificador a número y recortando espacios en cada campo, con una sola carga en memoria de composición.

  El filtrado combina campo de texto para el nombre con menú desplegable para la categoría con iconos en cada opción, mostrando un mensaje centrado de sin resultados cuando la lista filtrada queda vacía en lugar de mostrar una lista vacía.

  Como integración con el laboratorio anterior, cada fila alterna entre dos estilos según la posición par o impar: tarjeta amplia con icono circular y datos completos para posiciones pares y tarjeta compacta con icono de fondo y texto recortado a dos líneas para posiciones impares, ambas con color de fondo según categoría.

  #figure(
    image("img/l8-christian-capturas/01-lista-completa.png", width: 90%),
    caption: [Lista completa en el entorno de desarrollo y en el emulador.],
  ) <fig-christian-base>

  Al escribir el texto co solo permanecen Comedor Universitario y Coliseo Universitario, demostrando el filtrado por nombre, como se aprecia a continuación.

  #figure(
    image("img/l8-christian-capturas/02-filtro-nombre-co.png", width: 90%),
    caption: [Filtro por nombre en el entorno de desarrollo y en el emulador.],
  ) <fig-christian-filtro-nombre>

  Al desplegar el menú de categoría se observan las opciones Todas, Académico, Deportes, Eventos y Servicios con sus iconos, como se aprecia a continuación.

  #figure(
    image("img/l8-christian-capturas/03-menu-categorias.png", width: 90%),
    caption: [Menú de categorías desplegado en el entorno de desarrollo y en el emulador.],
  ) <fig-christian-menu>

  Al elegir la categoría Deportes la lista muestra solo Coliseo Universitario y Estadio Universitario, verificando el segundo criterio, como se aprecia a continuación.

  #figure(
    image("img/l8-christian-capturas/04-filtro-categoria-deportes.png", width: 90%),
    caption: [Filtro por categoría Deportes en el entorno de desarrollo y en el emulador.],
  ) <fig-christian-filtro-categoria>

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. Lograste resolver completamente el ejercicio resuelto por el docente con lista de edificaciones que se filtra en tiempo real al escribir en el campo de búsqueda. Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.

    Sí, se resolvió el ejercicio en el proyecto ListaFiltrable Compose con los seis edificios de la guía. Como evidencia, en la @fig-resuelto-base se aprecia la lista completa, y en @fig-resuelto-filtro-facultad y @fig-resuelto-filtro-biblio el filtrado en acción con dos textos distintos, además del caso vacío en @fig-resuelto-vacia.

    #figure(
      image("img/resuelto/01_base_6items.png", width: 20%),
      caption: [Evidencia del resultado en el emulador con la lista completa.],
    ) <fig-cuestionario-evidencia>

  2. En el video de referencia sobre uso de lista perezosa se aborda el tema de listas en Compose. Qué términos o conceptos nuevos, o que amplían lo visto en la sesión de teoría, se mencionan en el video. Explica brevemente.

    El video introduce creación de filas con variantes por índice y con clave para declarar filas a partir de colecciones, la diferencia práctica entre lista vertical y lista horizontal frente a columna con desplazamiento, y el uso de modificador en la fila con tarjeta con relleno y ancho completo combinado con estado para que la lista se recomponga al cambiar los datos @video-lazycolumn. Respecto a la teoría, amplía el rol de la clave estable para animaciones y conservación de estado por fila.

  3. Por qué es recomendable pasar un parámetro de clave estable a la función de creación de filas de una lista perezosa. Qué podría ocurrir si no se especifica al filtrar o reordenar la lista.

    La clave le da a la composición una identidad estable por fila entre recomposiciones, de modo que al filtrar o reordenar reutiliza los nodos correctos en lugar de recrearlos por posición @android-lists. Sin clave, al escribir en el buscador las filas visibles pueden conservar estado equivocado, perderse animaciones de entrada y salida y ejecutarse recomposiciones innecesarias de toda la lista en vez de solo las filas afectadas.

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - La lista perezosa con clave estable por identificador muestra solo las filas visibles y las recicla al hacer scroll, reemplazando a la vista recicladora con adaptador y contenedor sin código adicional @android-lists.
  - El filtrado en tiempo real se resuelve con una variable de estado ligada al campo de texto y un filtrado recalculado en cada recomposición, donde cada carácter escrito produce una lista nueva y una recomposición mínima de la lista.
  - La carga de ocho edificios desde archivo con una sola lectura en memoria de composición desacopla los datos del dibujo, permitiendo ampliar la lista sin tocar las funciones de composición.
  - El segundo criterio con menú desplegable y la fila con icono y color por categoría extienden el patrón del resuelto sin romperlo, con estilo neobrutalista en la versión de Yenaro y tarjetas alternadas en la versión de Christian, y el caso sin coincidencias confirma que la lista vacía no produce errores.

]

// #lab-section("RETROALIMENTACIÓN GENERAL")[
//   #block(height: 6em, breakable: false)
// ]

#lab-section("REFERENCIAS Y BIBLIOGRAFÍA")[
  #show heading: set text(weight: "bold")
  #set heading(numbering: none)
  = REFERENCIAS
  #bibliography("bibliography.bib", style: "ieee", title: none)
]
