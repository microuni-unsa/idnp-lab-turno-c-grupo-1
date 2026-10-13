#import "/components/@unsareport/epis-lab/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Plano interactivo de un ambiente con Canvas en Jetpack Compose",
  lab_number: "07",
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

  Resuelto: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l7/src/resuelto

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l7/src/yenaro

  == Ejercicio resuelto por el docente: Plano de un ambiente con Canvas y marcadores interactivos

  Se documenta el desarrollo paso a paso de la aplicación PlanoInteractivo Compose destinada a dibujar el plano de un ambiente con dos salones y marcadores interactivos que muestran información al ser tocados, combinando el Composable Canvas con primitivas DrawScope y detección de toques por Modifier @android-graphics-draw @android-pointer-input @video-canvas-basics.

  Paso 1. Creación del proyecto PlanoInteractivo_Compose con Package name com.example.planointeractivo_compose en lenguaje Kotlin, empleando para la verificación visual un emulador con API 36 (Medium Phone, nivel superior al mínimo 34 exigido).

  Paso 2. Declaración de las importaciones del ejercicio debajo del package y antes de la clase MainActivity, cubriendo Canvas, detectTapGestures, disposición, diálogo, estado, geometría, color, entrada de puntero y vista previa junto con el tema de la aplicación.

  #figure(
    image("img/resuelto/codigo_paso2_imports.png", width: 75%),
    caption: [Importaciones del ejercicio en el entorno de desarrollo.],
  ) <fig-resuelto-paso2>

  Paso 3. Definición de la data class Marcador fuera de cualquier Composable, con centro, radio, título y descripción de cada marcador del plano.

  #figure(
    image("img/resuelto/codigo_paso3_dataclass.png", width: 75%),
    caption: [Data class Marcador en el entorno de desarrollo.],
  ) <fig-resuelto-paso3>

  Paso 4. Creación del Composable PlanoAmbiente con una lista de al menos 2 marcadores de muestra (remember) y una variable de estado para el marcador seleccionado.

  #figure(
    image("img/resuelto/codigo_paso4_estado.png", width: 75%),
    caption: [Lista de marcadores y estado de selección en el entorno de desarrollo.],
  ) <fig-resuelto-paso4>

  Paso 5. Declaración de un Canvas de ancho completo con altura fija de 300.dp, agregando ya el Modifier.pointerInput con detectTapGestures todavía vacío.

  #figure(
    image("img/resuelto/codigo_paso5_canvas.png", width: 75%),
    caption: [Canvas con Modifier.pointerInput en el entorno de desarrollo.],
  ) <fig-resuelto-paso5>

  Paso 6. Dibujo dentro del Canvas de los dos salones con drawRect y de la pared divisoria entre ellos con drawLine.

  #figure(
    image("img/resuelto/codigo_paso6_rects.png", width: 75%),
    caption: [Salones y pared divisoria con drawRect y drawLine en el entorno de desarrollo.],
  ) <fig-resuelto-paso6-code>

  El estado intermedio de este paso se verificó en el emulador antes de agregar los marcadores:

  #figure(
    image("img/resuelto/04_resuelto_paso6_sin_marcadores.png", width: 28%),
    caption: [Paso 6: salones sin marcadores en el emulador.],
  ) <fig-resuelto-paso6-screen>

  Paso 7. Recorrido de la lista de marcadores dibujando cada uno con drawCircle, dejando completo el dibujo del plano.

  #figure(
    image("img/resuelto/codigo_paso7_circulos.png", width: 75%),
    caption: [Marcadores con drawCircle en el entorno de desarrollo.],
  ) <fig-resuelto-paso7>

  Paso 8. Cálculo dentro de detectTapGestures de la distancia entre el toque y el centro de cada marcador, asignando a marcadorSeleccionado cuando la distancia es menor o igual al radio.

  #figure(
    image("img/resuelto/codigo_paso8_tap.png", width: 75%),
    caption: [Detección del toque por distancia al centro en el entorno de desarrollo.],
  ) <fig-resuelto-paso8>

  Paso 9. Presentación fuera del Canvas de un AlertDialog cuando marcadorSeleccionado no sea null, con el título y la descripción del marcador.

  #figure(
    image("img/resuelto/codigo_paso9_dialog.png", width: 75%),
    caption: [AlertDialog de información en el entorno de desarrollo.],
  ) <fig-resuelto-paso9>

  Paso 10. Presentación de PlanoAmbiente dentro del bloque setContent de MainActivity, sobre una Surface de tamaño completo y dentro del tema de la aplicación, junto con su vista previa.

  #figure(
    image("img/resuelto/codigo_paso10_main.png", width: 75%),
    caption: [Actividad principal y vista previa en el entorno de desarrollo.],
  ) <fig-resuelto-paso10>

  Paso 11. Ejecución de la aplicación en el emulador y verificación de que, al tocar cada marcador dibujado sobre el plano, se abre el diálogo correspondiente con su información.

  #figure(
    image("img/resuelto/01_resuelto_plano_base.png", width: 28%),
    caption: [Plano con dos salones y dos marcadores en el emulador.],
  ) <fig-resuelto-base>

  Al tocar el marcador de la izquierda se abre el diálogo de la Sala A, y al tocar el de la derecha el de la Sala B, como se aprecia a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/resuelto/02_resuelto_dialogo_sala_a.png", width: 100%),
      caption: [Diálogo Sala A.],
    ) <fig-resuelto-dialogo-a>],
    [#figure(
      image("img/resuelto/03_resuelto_dialogo_sala_b.png", width: 100%),
      caption: [Diálogo Sala B.],
    ) <fig-resuelto-dialogo-b>]
  )

  == Ejercicios propuestos: Ampliación del plano a una edificación del campus

  La práctica propuesta solicita ampliar el plano resuelto por el docente para representar la visualización interior de al menos una edificación del campus: (1) al menos 4 ambientes con etiquetas visibles, (2) marcadores cargados desde un archivo (.json o .csv en assets/) siguiendo el patrón del Laboratorio 03, (3) estado trasladado a un PlanoViewModel siguiendo el patrón del Laboratorio 05, y (4) plano integrado como nueva pantalla accesible desde MapaScreen (Laboratorio 05) o desde una tarjeta del Laboratorio 06.

  === Implementación desarrollada por Christian Mestas

  // TODO: describir edificación elegida, 4 ambientes, carga desde archivo, ViewModel e integración. Agregar figuras en img/christian/

  === Implementación desarrollada por Yenaro Noa

  La solución desarrollada por Yenaro Noa representa el Pabellón de Sistemas con 4 ambientes (Lab. Cómputo 1, Lab. Cómputo 2, Aula 201 y Patio Central), cada uno con su etiqueta visible dibujada sobre el plano mediante nativeCanvas. A diferencia del resuelto, el dibujo usa coordenadas fraccionales del tamaño del Canvas, por lo que el plano se adapta a cualquier densidad de pantalla.

  Los marcadores no están escritos en el código: se cargan desde `assets/marcadores.json` con coordenadas fraccionales, radio en dp, título y descripción de cada ambiente.

  #figure(
    image("img/yenaro/01_propuesto_json.png", width: 75%),
    caption: [Archivo de marcadores en assets en el entorno de desarrollo.],
  ) <fig-yenaro-json>

  La lectura del archivo se concentra en `cargarMarcadores`, invocada una sola vez desde la actividad, y el ViewModel se construye con esos datos mediante una factoría propia.

  #figure(
    image("img/yenaro/03_propuesto_carga.png", width: 75%),
    caption: [Carga desde assets y factoría del ViewModel en el entorno de desarrollo.],
  ) <fig-yenaro-carga>

  El estado del plano y sus marcadores vive en `PlanoViewModel`, que expone la lista y la selección como StateFlow, en lugar de mantenerlos con remember en el Composable como hace el resuelto.

  #figure(
    image("img/yenaro/02_propuesto_viewmodel.png", width: 75%),
    caption: [PlanoViewModel con StateFlow en el entorno de desarrollo.],
  ) <fig-yenaro-viewmodel>

  El Canvas combina el Modifier.pointerInput con detectTapGestures del resuelto, convirtiendo cada toque a la misma escala fraccional de los marcadores antes de comparar la distancia contra el radio en píxeles.

  #figure(
    image("img/yenaro/04_propuesto_canvas.png", width: 75%),
    caption: [Canvas y detección de toques en el entorno de desarrollo.],
  ) <fig-yenaro-canvas>

  El dibujo agrega muros exteriores, los 4 ambientes con sus colores y las paredes divisorias con drawRect y drawLine.

  #figure(
    image("img/yenaro/05_propuesto_dibujo.png", width: 75%),
    caption: [Muros, ambientes y divisorias en el entorno de desarrollo.],
  ) <fig-yenaro-dibujo>

  El diálogo de información y la actividad principal que conecta la carga del JSON con el ViewModel completan la pantalla, lista para integrarse como destino de navegación desde MapaScreen.

  #figure(
    image("img/yenaro/06_propuesto_dialogo_main.png", width: 75%),
    caption: [Diálogo y actividad principal en el entorno de desarrollo.],
  ) <fig-yenaro-dialogo-main>

  #figure(
    image("img/yenaro/01_yenaro_plano_base.png", width: 45%),
    caption: [Plano del Pabellón de Sistemas con 4 ambientes en el emulador.],
  ) <fig-yenaro-base>

  Al pulsar cada marcador se abre su diálogo con los datos cargados desde el archivo, verificando los 4 ambientes:

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/yenaro/02_yenaro_dialogo_lab1.png", width: 100%),
      caption: [Lab. 1.],
    ) <fig-yenaro-lab1>],
    [#figure(
      image("img/yenaro/03_yenaro_dialogo_lab2.png", width: 100%),
      caption: [Lab. 2.],
    ) <fig-yenaro-lab2>]
  )

  #grid(
    columns: (1fr, 1fr),
    gutter: 12pt,
    [#figure(
      image("img/yenaro/04_yenaro_dialogo_aula.png", width: 100%),
      caption: [Aula 201.],
    ) <fig-yenaro-aula>],
    [#figure(
      image("img/yenaro/05_yenaro_dialogo_patio.png", width: 100%),
      caption: [Patio.],
    ) <fig-yenaro-patio>]
  )

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. *¿Lograste resolver completamente el Ejercicio/Problema Resuelto por el Docente (plano de un ambiente dibujado con Canvas, donde tocar un marcador abre un diálogo con su información)? Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.*

    Sí, se resolvió el ejercicio en el proyecto PlanoInteractivo_Compose. Como evidencia, en la @fig-cuestionario-evidencia se aprecia el plano con los dos salones y sus marcadores, y en las figuras de diálogos el AlertDialog abierto al tocar cada marcador.

    #figure(
      image("img/resuelto/01_resuelto_plano_base.png", width: 20%),
      caption: [Evidencia del resultado en el emulador con los dos salones y marcadores.],
    ) <fig-cuestionario-evidencia>

  2. *En tu propia implementación, ¿cómo calculaste si un toque cayó dentro de un marcador circular? ¿Qué otra estrategia se te ocurre para detectar toques sobre formas no circulares, como un rectángulo o un polígono?*

    En ambas implementaciones se calculó la distancia euclidiana entre el offset del toque entregado por detectTapGestures y el centro del marcador, comparándola con el radio: `(tap - center).getDistance() <= radio` @android-pointer-input. En el resuelto el centro y el radio están en píxeles fijos, mientras que en la versión de Yenaro el centro se deriva de coordenadas fraccionales del tamaño del Canvas y el radio se convierte de dp a píxeles con la densidad, pero la comparación es la misma. Para formas no circulares se propone la prueba de contención del rectángulo (comparar x e y contra los bordes left/right/top/bottom) y, para polígonos arbitrarios, el algoritmo de ray casting (punto en polígono) o el uso de Path con `contains(offset)` sobre la región dibujada.

  3. *En forma individual, comenta en el video de referencia (https://www.youtube.com/watch?v=cCIPYW_N12o) qué otra forma se te ocurre para lograr un dibujo interactivo similar sin depender directamente de Canvas y DrawScope. (Colocar la captura del comentario; uno por integrante del grupo).*

    // TODO: agregar captura del comentario por integrante @video-canvas-basics.

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - El Composable Canvas con DrawScope permite representar el plano de un ambiente con primitivas declarativas (drawRect, drawLine, drawCircle) sin subclases de View, y el Modifier.pointerInput con detectTapGestures cubre la interacción sin onTouchEvent manual @android-graphics-draw @android-pointer-input.
  - La comparación de la distancia del toque contra el radio del marcador resuelve la selección en formas circulares, mientras que las etiquetas con nativeCanvas y la contención por rectángulo extienden el plano a ambientes reales con formas variadas.
  - La carga de marcadores desde `assets/marcadores.json` y el traslado del estado a un PlanoViewModel con StateFlow desacoplan datos y presentación, permitiendo ampliar de 2 a 4 ambientes sin tocar el dibujo.
  - El uso de coordenadas fraccionales del tamaño del Canvas hace que el mismo plano se adapte a distintas densidades, a diferencia de las coordenadas fijas en píxeles del ejercicio resuelto.

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
