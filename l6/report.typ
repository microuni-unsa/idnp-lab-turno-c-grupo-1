#import "/components/@unsareport/epis-lab/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Tarjetas de lugares con Row, Column, Box y Modifier en Jetpack Compose",
  lab_number: "06",
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

  Christian Mestas: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l6/src/christian

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l6/src/yenaro

  == Ejercicio resuelto por el docente: Tarjetas de lugares con Row, Column, Box y Modifier

  Se documenta el desarrollo paso a paso de la aplicación TarjetasCampus Compose destinada a presentar lugares del campus en dos estilos de tarjeta, combinando los tres Composables básicos de disposición con una cadena de Modifier para la forma, el fondo y la tipografía @android-layout-basics.

  Paso 1. Creación del proyecto TarjetasCampus Compose con el nombre de paquete de ejemplo en lenguaje Kotlin, empleando para la verificación visual un emulador con nivel de interfaz 34 o superior.

  Paso 2. Declaración de las importaciones del ejercicio debajo del paquete y antes de la clase MainActivity, cubriendo imagen, fondo, disposición, formas, superficie, texto, composición, alineación, recorte, color, escala de contenido, recursos, tipografía, vista previa y unidades de densidad junto con el tema de la aplicación.

  Paso 3. Incorporación de dos imágenes de muestra en el directorio de recursos con los edificios Biblioteca y Comedor, admitiendo un fondo de color como reemplazo temporal del componente de imagen durante el desarrollo.

  Paso 4. Definición de la firma del primer estilo mediante la función TarjetaLugar, la cual recibe el nombre, la descripción, la imagen y el color de fondo del lugar, reservando su contenido para los pasos siguientes.

  #figure(
    image("img/resuelto/codigo_paso4_firma.png", width: 75%),
    caption: [Firma del primer estilo de tarjeta en el entorno de desarrollo.],
  ) <fig-resuelto-paso4>

  Paso 5. Envoltura del contenido en un contenedor Surface con esquinas redondeadas que adopta el color de fondo recibido, disponiendo en su interior un Row alineado verticalmente al centro con relleno uniforme @android-modifiers.

  #figure(
    image("img/resuelto/codigo_paso5_surface_row.png", width: 75%),
    caption: [Contenedor Surface con disposición Row en el entorno de desarrollo.],
  ) <fig-resuelto-paso5>

  Paso 6. Completitud del primer estilo con una imagen recortada en forma circular, un espaciador horizontal y una Column con el nombre destacado y la descripción, respetando el orden de la cadena de Modifier donde la dimensión precede al recorte @android-modifiers.

  #grid(
    columns: (3fr, 1fr),
    gutter: 16pt,
    align: top,
    [#figure(
      image("img/resuelto/codigo_paso6_imagen_columna.png", width: 100%),
      caption: [Imagen circular y columna de textos en el entorno de desarrollo.],
    ) <fig-resuelto-paso6-code>],
    [#figure(
      image("img/resuelto/03_resuelto_solo_row.png", width: 100%),
      caption: [Fase Row.],
    ) <fig-resuelto-paso6-screen>]
  )

  Paso 7. Invocación de TarjetaLugar dentro de una Column con separación uniforme entre elementos, registrando al menos dos lugares del campus con sus respectivos colores de fondo en tonos celeste y naranja pálidos.

  #figure(
    image("img/resuelto/codigo_paso7_lista.png", width: 75%),
    caption: [Lista con dos lugares del campus en el entorno de desarrollo.],
  ) <fig-resuelto-paso7>

  Paso 8. Definición del segundo estilo mediante la función TarjetaCompacta, la cual recibe el nombre y la imagen del lugar y declara un Box de ancho completo con altura fija y esquinas redondeadas, alojando una imagen de fondo que ocupa todo el espacio del contenedor.

  #figure(
    image("img/resuelto/codigo_paso8_compacta_box.png", width: 75%),
    caption: [Estructura del estilo apilado con Box en el entorno de desarrollo.],
  ) <fig-resuelto-paso8>

  Paso 9. Superposición dentro del mismo Box de un segundo contenedor semitransparente a modo de velo y de un texto con el nombre alineado en la esquina inferior izquierda, garantizando la legibilidad sobre la imagen y dejando completo el estilo apilado.

  #grid(
    columns: (2fr, 1fr),
    gutter: 12pt,
    align: top,
    [#figure(
      image("img/resuelto/codigo_paso9_scrim_texto.png", width: 100%),
      caption: [Velo semitransparente y texto superpuesto en el entorno de desarrollo.],
    ) <fig-resuelto-paso9-code>],
    [#figure(
      image("img/resuelto/04_resuelto_solo_box.png", width: 100%),
      caption: [Fase Box.],
    ) <fig-resuelto-paso9-screen>]
  )

  Paso 10. Presentación en la actividad principal de la lista del primer estilo seguida de dos tarjetas compactas apiladas con espaciadores intermedios, dentro del tema de la aplicación y sobre una superficie de tamaño completo, junto con su vista previa @video-compose-cards.

  #grid(
    columns: (2fr, 1fr),
    gutter: 12pt,
    align: top,
    [#figure(
      image("img/resuelto/codigo_paso10_main_pantalla.png", width: 100%),
      caption: [Actividad principal y pantalla de tarjetas en el entorno de desarrollo.],
    ) <fig-resuelto-paso10-code>],
    [#figure(
      image("img/resuelto/05_resuelto_final.png", width: 100%),
      caption: [Pantalla final.],
    ) <fig-resuelto-paso10-screen>]
  )

  Paso 11. Ejecución de la aplicación en el emulador y comparación visual de ambos estilos de tarjeta, verificando la franja horizontal con imagen circular frente a la tarjeta apilada con texto superpuesto.

  == Ejercicios propuestos: Navegación al detalle e integración con la pantalla de edificios del laboratorio anterior

  La práctica propuesta solicita dotar de navegación a las tarjetas integrándolas en la pantalla de edificios del laboratorio anterior, envolviendo ambos estilos con respuesta a la pulsación y conduciendo hacia una pantalla de detalle a tamaño completo, además de diseñar un tercer estilo propio como reto opcional.

  === Implementación desarrollada por Christian Mestas

  La solución desarrollada por Christian Mestas estructura los datos mediante la clase Lugar, la cual añade a los campos del ejercicio resuelto una etiqueta de categoría, exponiendo una lista de cuatro lugares del campus conformada por Biblioteca Central, Comedor Universitario, Pabellón de Sistemas y Auditorio Central. La navegación se resuelve con un grafo de dos destinos que reutiliza el patrón del laboratorio anterior, conduciendo hacia el detalle al pulsar una tarjeta y desapilando al retornar @android-layout-basics.

  #figure(
    image("img/christian/christian-principal.png", width: 90%),
    caption: [Lista de edificios con los tres estilos de tarjeta en el entorno de desarrollo.],
  ) <fig-christian-principal>

  En la pantalla de edificios se despliega una lista eficiente que alterna los tres estilos por posición, combinando la tarjeta horizontal, la tarjeta apilada y un tercer estilo propio con icono y pastilla de etiqueta por lugar. Cada tarjeta propaga la pulsación mediante una función lambda que resguarda el lugar elegido y navega hacia el detalle, donde una imagen circular amplia precede al nombre y la descripción junto con un botón de retorno.

  #figure(
    image("img/christian/christian-detalle.png", width: 90%),
    caption: [Pantalla de detalle con imagen circular y retorno en el entorno de desarrollo.],
  ) <fig-christian-detalle>

  Al volver a la lista tras consultar un detalle se conserva la posición y el estado de la selección, verificando una experiencia uniforme entre ambos destinos con el tercer estilo documentado como combinación de Row, icono y Modifier de fondo y relleno.

  === Implementación desarrollada por Yenaro Noa

  La solución desarrollada por Yenaro Noa estructura los datos mediante la clase Lugar, la cual resguarda el nombre, la descripción, la referencia de imagen y el color de fondo de cada sitio, exponiendo una lista de cuatro lugares del campus conformada por Biblioteca Central, Comedor, Pabellón A y Auditorio.

  #figure(
    image("img/yenaro/01_propuesto_datos_lugar.png", width: 75%),
    caption: [Clase de datos y lista de cuatro lugares en el entorno de desarrollo.],
  ) <fig-yenaro-datos>

  En el primer estilo se reutiliza la tarjeta con Surface y Row del ejercicio resuelto, conservando la imagen circular a la izquierda y la columna de textos a la derecha con el mismo orden de Modifier para dimensión y recorte.

  #figure(
    image("img/yenaro/02_propuesto_tarjeta_lugar.png", width: 75%),
    caption: [Tarjeta del primer estilo reutilizada en el entorno de desarrollo.],
  ) <fig-yenaro-tarjeta>

  En el segundo estilo se reutiliza la tarjeta apilada con Box del ejercicio resuelto, manteniendo la imagen de fondo a tamaño del contenedor con su velo semitransparente y el nombre en la esquina inferior izquierda.

  #figure(
    image("img/yenaro/03_propuesto_tarjeta_compacta.png", width: 75%),
    caption: [Tarjeta del estilo apilado reutilizada en el entorno de desarrollo.],
  ) <fig-yenaro-compacta>

  La pantalla de detalle recibe el lugar completo como parámetro y lo presenta a tamaño completo con una imagen amplia de esquinas redondeadas seguida del nombre y la descripción, reutilizando al pie la tarjeta apilada del mismo lugar para reforzar su identidad visual.

  #grid(
    columns: (2fr, 1fr),
    gutter: 12pt,
    align: top,
    [#figure(
      image("img/yenaro/04_propuesto_detalle.png", width: 100%),
      caption: [Pantalla de detalle a tamaño completo en el entorno de desarrollo.],
    ) <fig-yenaro-detalle-code>],
    [#figure(
      image("img/yenaro/emu_03_detalle_biblioteca.png", width: 100%),
      caption: [Detalle.],
    ) <fig-yenaro-detalle-screen>]
  )

  En la pantalla de edificios se despliega una lista eficiente con las cuatro tarjetas apiladas, envolviendo cada una con respuesta a la pulsación que invoca la función lambda de selección suministrando el lugar elegido.

  #code-block(
"Box(
  modifier = Modifier.clickable {
    onVer(lugar)
  }
) {
  TarjetaCompacta(...)
}",
    lang: "kotlin",
  )

  #grid(
    columns: (4fr, 2fr),
    gutter: 12pt,
    align: top,
    [#figure(
      image("img/yenaro/05_propuesto_edificios_lambda.png", width: 100%),
      caption: [Lista de edificios con navegación por función lambda en el entorno de desarrollo.],
    ) <fig-yenaro-edificios-code>],
    [#figure(
      image("img/yenaro/emu_01_edificios_lista.png", width: 100%),
      caption: [Lista.],
    ) <fig-yenaro-edificios-screen>]
  )

  En la función composable raíz se preserva en memoria el lugar elegido con un estado mutable de valor inicial nulo, mostrando la lista mientras no exista selección y conmutando hacia la pantalla de detalle con su texto de retorno al registrarse una pulsación, siguiendo el mismo patrón de navegación del laboratorio anterior.

  #code-block(
"var elegido by remember { mutableStateOf<Lugar?>(null) }
if (elegido == null) {
  EdificiosScreen(lugares) { elegido = it }
} else {
  DetalleLugarScreen(elegido!!)
}",
    lang: "kotlin",
  )

  #figure(
    image("img/yenaro/07_propuesto_main.png", width: 75%),
    caption: [Actividad principal con conmutación entre lista y detalle en el entorno de desarrollo.],
  ) <fig-yenaro-main>

  Como reto opcional se diseña un tercer estilo propio mediante un Row con la inicial del nombre dentro de un círculo seguido de una columna de textos, aplicando fondo propio por lugar según su color registrado y respuesta a la pulsación, documentando así una combinación distinta de disposición y Modifier @video-compose-layouts.

  #figure(
    image("img/yenaro/06_propuesto_tercer_estilo.png", width: 75%),
    caption: [Tercer estilo propio con inicial y fondo por lugar en el entorno de desarrollo.],
  ) <fig-yenaro-icono>

  Al pulsar cada tarjeta de la lista se conduce hacia el detalle correspondiente al lugar elegido, verificando una navegación coherente entre los cuatro destinos con una experiencia visual uniforme, cuyos detalles restantes se aprecian a continuación.

  #grid(
    columns: (1fr, 1fr),
    gutter: 8pt,
    [#figure(
      image("img/yenaro/emu_05_detalle_comedor.png", width: 65%),
      caption: [Comedor.],
    ) <fig-yenaro-emu-comedor>],
    [#figure(
      image("img/yenaro/emu_06_detalle_pabellon.png", width: 65%),
      caption: [Pabellón A.],
    ) <fig-yenaro-emu-pabellon>]
  )

  #align(center)[#figure(
    image("img/yenaro/emu_07_detalle_auditorio.png", width: 23%),
    caption: [Auditorio.],
  ) <fig-yenaro-emu-auditorio>]

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. *¿Lograste resolver completamente el Ejercicio/Problema Resuelto por el Docente (dos estilos de tarjeta de lugares: una con Row/Column mostrando imagen y texto, y otra apilada con Box y superposición de texto)? Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.*

    Sí, se logró resolver el ejercicio resuelto propuesto por el docente en el proyecto denominado TarjetasCampus Compose. Como evidencia del resultado obtenido, en la @fig-cuestionario-evidencia se aprecia la lista del primer estilo junto con las dos tarjetas apiladas del segundo estilo en el emulador.

    #figure(
      image("img/resuelto/05_resuelto_final.png", width: 20%),
      caption: [Evidencia del resultado en el emulador con los dos estilos de tarjeta.],
    ) <fig-cuestionario-evidencia>

  2. *¿Qué combinación de Composables (Row, Column, Box) y Modifiers consideras que fue la mejor opción para cada uno de los dos estilos de tarjeta implementados? Justifica tu respuesta comparando ambos casos.*

    Para la tarjeta horizontal la mejor opción fue la combinación de Surface con Row y Column, pues el Row distribuye la imagen y los textos en secuencia de lectura mientras la Column apila nombre y descripción en jerarquía, aplicando dimensión antes del recorte circular y relleno uniforme para respirar el contenido @android-layout-basics @android-modifiers. Para la tarjeta apilada la mejor opción fue el Box con imagen a tamaño del contenedor, pues solo la superposición en profundidad permite colocar el velo y el texto sobre la fotografía, anclando el nombre en la esquina inferior izquierda para garantizar su legibilidad @android-layout-basics. La diferencia está en la dimensión del ordenamiento, secuencial frente a estratificado, donde los Modifier de dimensión y recorte modelan la imagen del primer estilo mientras los de fondo y alineación sostienen el texto del segundo.

  3. *¿Qué diferencia encontraste entre construir estas interfaces con Composables y Modifier, en comparación con el enfoque clásico de LinearLayout/ConstraintLayout en XML? Menciona al menos una ventaja concreta que hayas notado.*

    La diferencia está en la unificación del diseño y la lógica en un solo lenguaje, pues los Composables declaran la disposición directamente en Kotlin sin repartir la interfaz entre archivos de marcado y clases de actividad, eliminando la inflación de vistas y la búsqueda de componentes por identificador @video-compose-layouts. Una ventaja concreta que se notó fue el carácter explícito del orden en la cadena de Modifier, donde colocar la dimensión antes del recorte produce una imagen circular nítida y alterar ese orden cambia visiblemente el resultado, mientras que en XML el orden de los atributos no comunica secuencia alguna de aplicación.

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - La combinación de Surface con Row y Column proporciona tarjetas horizontales legibles y predecibles en Jetpack Compose, donde la imagen circular recortada y la columna de textos conservan su jerarquía en cualquier densidad gracias al orden de la cadena de Modifier.

  - Si bien el Row distribuye elementos en secuencia con naturalidad, solo el Box con superposición resuelve la tarjeta apilada, pues el velo semitransparente y el texto anclado dependen de compartir el mismo espacio del contenedor con la imagen de fondo.

  - La aplicación del orden de Modifier con la dimensión antes del recorte y el fondo después del recorte consolida un acabado prolijo, evitando que el color se escape de la forma y garantizando que cada tarjeta respete sus esquinas redondeadas.

  - La envoltura de ambas tarjetas con respuesta a la pulsación y su integración en la lista del laboratorio anterior confirma la extensibilidad del diseño, permitiendo navegar al detalle y sumar un tercer estilo sin alterar los estilos ya verificados.

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
