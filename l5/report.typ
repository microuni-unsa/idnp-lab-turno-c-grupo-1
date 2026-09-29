#import "/components/@unsareport/epis-lab/lib.typ": code-block, lab-section, table-border-width, unsa-report

#show: unsa-report.with(
  course_name: "Introducción al Desarrollo de Nuevas Plataformas",
  lab_title: "Pantallas múltiples con NavigationBar y comunicación entre Composables en Jetpack Compose",
  lab_number: "05",
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

  Christian Mestas: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l5/src/christian

  Yenaro Noa: https://github.com/microuni-unsa/idnp-lab-turno-c-grupo-1/tree/main/l5/src/yenaro

  == Ejercicio resuelto por el docente: Navegación inferior con NavigationBar y comunicación por función lambda

  Se documenta el desarrollo paso a paso de la aplicación CampusNavigator Compose destinada a gestionar tres pantallas denominadas Home, Edificios y Mapa mediante una barra de navegación inferior, comunicando la selección efectuada en la lista hacia la pantalla principal mediante una función lambda @android-navigation-bar.

  Paso 1. Definición de la jerarquía de navegación mediante la clase sellada Screen, la cual establece las rutas textuales y las etiquetas visibles para las pantallas Home, Edificios y Mapa, junto con la incorporación de las dependencias de Navigation Compose y la biblioteca de íconos extendidos en el archivo de construcción del módulo app @android-navigation-compose.

  Paso 2. Configuración del contenedor Scaffold en la función composable MainScreen, integrando en su parámetro de barra inferior el componente NavigationBar. Dentro de este contenedor se itera la colección de destinos de Screen para crear un NavigationBarItem por cada opción, asociando los íconos del sistema Home, List y Place. El estado seleccionado se calcula dinámicamente comparando la ruta activa obtenida con currentBackStackEntryAsState frente a la ruta registrada en cada ítem, mientras que el evento onClick ejecuta la transición mediante el controlador NavController.

  #figure(
    image("img/resuelto/01_resuelto_home_ide.png", width: 75%),
    caption: [Configuración del contenedor Scaffold, NavigationBar y estado inicial de HomeScreen en el entorno de desarrollo.],
  ) <fig-resuelto-home-ide>

  Paso 3. Declaración del contenedor NavHost alojado dentro del área de relleno entregada por Scaffold, fijando la pantalla Home como destino de arranque. El grafo asocia la ruta home a la función HomeScreen, la ruta edificios a la función EdificiosScreen y la ruta mapa a la función MapaScreen.

  Paso 4. En la pantalla HomeScreen se reciben los datos del edificio consultado a través de un parámetro de tipo cadena de caracteres, presentando un saludo inicial y el valor recibido en pantalla. Al iniciar la aplicación, dicho valor exhibe el texto Ninguno.

  Paso 5. En la pantalla EdificiosScreen se despliega una lista eficiente mediante LazyColumn con cuatro opciones de la universidad: Biblioteca Central, Pabellón A, Pabellón B y Auditorio. Cada fila dispone del nombre y un botón de acción que, al ser accionado, invoca la función lambda onEdificioSeleccionado suministrando el nombre del edificio elegido.

  #figure(
    image("img/resuelto/02_resuelto_edificios_ide.png", width: 75%),
    caption: [Implementación de HomeScreen y EdificiosScreen con la lista de cuatro edificaciones en el entorno de desarrollo.],
  ) <fig-resuelto-edificios-ide>

  Paso 6. En la pantalla MapaScreen se dispone un diseño centrado con un mensaje informativo de marcador de posición señalando que el mapa de ubicaciones se encuentra pendiente de integrar al sistema.

  #figure(
    image("img/resuelto/03_resuelto_mapa_ide.png", width: 75%),
    caption: [Implementación de MapaScreen con marcador de posición en el entorno de desarrollo.],
  ) <fig-resuelto-mapa-ide>

  Paso 7. La función composable raíz MainScreen preserva en memoria la variable de estado mutable para el edificio seleccionado. Al registrar la ruta de Edificios en el grafo NavHost, se proporciona una expresión lambda que sobrescribe dicha variable de estado ante cada selección, desencadenando la recomposición inmediata de HomeScreen para reflejar la última consulta realizada por el usuario en el emulador.

  #grid(
    columns: (1fr, 1fr, 1fr, 1fr),
    gutter: 6pt,
    [#figure(
      image("img/resuelto/01_resuelto_home_inicial.png", width: 75%),
      caption: [Home inicial.],
    ) <fig-resuelto-home>],
    [#figure(
      image("img/resuelto/02_resuelto_edificios_lista.png", width: 75%),
      caption: [Edificios.],
    ) <fig-resuelto-edificios>],
    [#figure(
      image("img/resuelto/03_resuelto_home_actualizado_lambda.png", width: 75%),
      caption: [Home actualizado.],
    ) <fig-resuelto-actualizado>],
    [#figure(
      image("img/resuelto/04_resuelto_mapa.png", width: 75%),
      caption: [Mapa.],
    ) <fig-resuelto-mapa>]
  )

  == Ejercicios propuestos: Comunicación mediante ViewModel compartido y cuarta pestaña

  La práctica propuesta solicita modernizar la arquitectura de la aplicación sustituyendo la propagación del estado por función lambda por un ViewModel compartido, además de incorporar una cuarta pestaña de navegación con su respectivo destino visual.

  === Implementación desarrollada por Christian Mestas

  La solución desarrollada por Christian Mestas implementa la clase SeleccionViewModel, la cual extiende de la clase arquitectónica ViewModel de Android @android-viewmodel. Dicha clase resguarda internamente un flujo mutable StateFlow inicializado con el valor Ninguno y expone una versión de solo lectura hacia la interfaz de usuario, garantizando un flujo unidireccional de datos y una clara separación de responsabilidades. La modificación del estado se encapsula dentro del método seleccionarEdificio @video-compose-mvvm.

  En la función composable raíz se obtiene una instancia única del modelo mediante la función viewModel. Dicha instancia se suministra de forma directa tanto a EdificiosScreen como a HomeScreen. Al eliminar el intermediario de la lambda en el Composable raíz, EdificiosScreen delega la actualización directamente en el ViewModel al presionar el botón Ver, mientras que HomeScreen se suscribe al flujo reactivo empleando collectAsState.

  #figure(
    image("img/christian/01_propuesto_home_inicial.png", width: 75%),
    caption: [Pantalla Home observando el valor inicial del ViewModel compartido e inspección en el entorno de desarrollo.],
  ) <fig-christian-home-inicial>

  #figure(
    image("img/christian/02_propuesto_edificios.png", width: 75%),
    caption: [Selección de un edificio específico en EdificiosScreen delegada directamente sobre el ViewModel compartido.],
  ) <fig-christian-edificios>

  Al retornar a la pestaña Home desde la barra de navegación, la vista presenta el nombre del edificio seleccionado sin requerir parámetros en la ruta ni variables de estado dispersas en la actividad, verificando una experiencia visual idéntica y una arquitectura notablemente más robusta.

  #figure(
    image("img/christian/03_propuesto_home_actualizado.png", width: 75%),
    caption: [Pantalla Home reflejando la actualización del edificio elegido a través del ViewModel compartido en el dispositivo físico.],
  ) <fig-christian-home-actualizado>

  Asimismo, se amplió la clase sellada Screen para admitir la ruta perfil y la etiqueta Perfil, asignando el ícono Person de la biblioteca de materiales. La pantalla PerfilScreen presenta una tarjeta con la información del estudiante Christian Raul Mestas Zegarra, su escuela profesional y los datos académicos del curso, completando el conjunto de cuatro pestañas solicitadas.

  #figure(
    image("img/christian/04_propuesto_perfil.png", width: 75%),
    caption: [Cuarta pestaña Perfil con los datos académicos del estudiante desplegada en el dispositivo físico.],
  ) <fig-christian-perfil>

  === Implementación desarrollada por Yenaro Noa

  La implementación desarrollada por Yenaro Noa estructura igualmente la gestión del estado compartido mediante la clase SeleccionViewModel, empleando en su diseño una propiedad reactiva mutableStateOf con modificador de acceso de escritura privado. Este enfoque expone una lectura directa hacia la interfaz visual y restringe la modificación de datos a la función seleccionar, simplificando la sincronización reactiva en la composición.

  #code-block(
"class SeleccionViewModel : ViewModel() {
  var seleccionado by mutableStateOf(\"Ninguno\")
    private set

  fun seleccionar(nombre: String) {
    seleccionado = nombre
  }
}",
    lang: "kotlin",
  )

  En la función composable MainScreen se recibe la instancia del modelo como parámetro predeterminado resuelto por la función viewModel, abasteciendo a HomeScreen y a EdificiosScreen. Al accionar el botón Ver en la lista de edificaciones, EdificiosScreen invoca el método del ViewModel sin recurrir a funciones lambda intermediarias, mientras que HomeScreen lee directamente la propiedad reactiva en su interpolación textual.

  #code-block(
"// Lectura directa en HomeScreen
Text(text = \"Último edificio consultado: ${vm.seleccionado}\")

// Actualización directa en EdificiosScreen
Button(onClick = { vm.seleccionar(nombre) }) {
  Text(\"Ver\")
}",
    lang: "kotlin",
  )

  La interfaz incorpora las cuatro pestañas solicitadas en la clase sellada Screen mediante los elementos Home, Edificios, Mapa y Perfil, garantizando que el resaltado visual y la recomposición del contenido respondan de manera coherente al flujo de navegación inferior.

  #grid(
    columns: (1fr, 1fr, 1fr, 1fr, 1fr),
    gutter: 5pt,
    [#figure(
      image("img/yenaro/01_propuesto_home_vm.png", width: 80%),
      caption: [Home inicial.],
    ) <fig-yenaro-home>],
    [#figure(
      image("img/yenaro/02_propuesto_edificios_vm.png", width: 80%),
      caption: [Edificios.],
    ) <fig-yenaro-edificios>],
    [#figure(
      image("img/yenaro/03_propuesto_home_actualizado_vm.png", width: 80%),
      caption: [Actualizado.],
    ) <fig-yenaro-actualizado>],
    [#figure(
      image("img/yenaro/04_propuesto_mapa.png", width: 80%),
      caption: [Mapa.],
    ) <fig-yenaro-mapa>],
    [#figure(
      image("img/yenaro/05_propuesto_perfil.png", width: 80%),
      caption: [Perfil.],
    ) <fig-yenaro-perfil>]
  )

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = SOLUCIÓN DEL CUESTIONARIO

  1. *¿Lograste resolver completamente el Ejercicio o Problema Resuelto por el Docente, relativo a las pantallas Home, Edificios y Mapa navegables mediante NavigationBar, donde el edificio seleccionado en Edificios se refleja en Home? Adjunta una captura de pantalla del emulador mostrando el resultado como evidencia.*

    Sí, se logró resolver el ejercicio resuelto propuesto por el docente en el proyecto denominado CampusNavigator Compose. Como evidencia del resultado obtenido, en la @fig-cuestionario-evidencia se aprecia la pantalla Home reflejando la actualización del edificio consultado tras navegar desde la lista de edificaciones.

    #figure(
      image("img/resuelto/03_resuelto_home_actualizado_lambda.png", width: 26%),
      caption: [Evidencia del resultado en el emulador con el edificio seleccionado reflejado en la pantalla Home.],
    ) <fig-cuestionario-evidencia>

  2. *En tu propia implementación, ¿qué diferencia encontraste entre comunicar Composables mediante una función lambda y hacerlo mediante un ViewModel compartido? ¿Cuál de las dos alternativas consideras una mejor práctica? Explica brevemente los motivos.*

    La diferencia está en el nivel de acoplamiento, la propagación de eventos y la supervivencia del estado frente a recomposiciones en la jerarquía. La comunicación mediante una función lambda delega la custodia del estado en un Composable antecesor común, obligando al contenedor padre a conocer los tipos de datos intercambiados y a redistribuir callbacks a través de parámetros. Esto genera un encadenamiento de funciones en componentes intermedios y provoca la recomposición innecesaria del nivel superior cada vez que el estado cambia. Un ViewModel compartido sitúa el estado en un objeto de negocio independiente del árbol visual, permitiendo que las pantallas productoras y consumidoras interactúen directamente con flujos reactivos StateFlow sin intermediación obligatoria del contenedor padre.

    Se considera que el uso de un ViewModel compartido es una mejor práctica para comunicar pantallas autónomas dentro de un grafo de navegación, debido a que respeta el principio de responsabilidad única, aísla la lógica de negocio de la capa de presentación y reduce drásticamente el acoplamiento entre pantallas no subordinadas. La comunicación mediante funciones lambda se reserva como mejor práctica únicamente para componentes visuales atómicos, formularios locales o elementos subordinados directos que no trascienden el ámbito de su pantalla contenedora.

  3. *En forma individual, explica qué ventaja adicional ofrece un ViewModel frente a otras formas de mantener el estado al navegar entre pantallas.*

    Respuesta de Christian Mestas:

    Una ventaja que ofrece el ViewModel frente a otras formas de mantener el estado se encuentra en su desacoplamiento del ciclo de vida de los composables. En Jetpack Compose, al navegar hacia un nuevo destino mediante NavController, las pantallas que abandonan la vista son removidas de la composición y destruyen sus estados locales recordados con remember. Si se intentara almacenar la información en variables locales de pantalla, dicha información desaparecería a menos que se guarde artificialmente en la actividad. El ViewModel, al estar vinculado a un almacén de modelos con alcance en la actividad o en el grafo de navegación, retiene el estado intacto en memoria durante toda la sesión del flujo, sobreviviendo incluso a recomposiciones y a cambios de configuración como la rotación de pantalla.

    Respuesta de Yenaro Noa:

    El ViewModel permite centralizar el estado mutable directamente fuera del ciclo de vida efímero de las funciones composables que componen el grafo de navegación. Al emplear un ViewModel compartido en la raíz, las pantallas leen y actualizan el estado de manera directa sin requerir el paso de argumentos en las rutas ni acoplarse con la actividad. Frente a técnicas como rememberSaveable o la serialización en la pila de navegación, el ViewModel evita conversiones complejas de datos y preserva la coherencia del estado durante toda la ejecución de la pantalla principal, simplificando la mantenibilidad del proyecto.

][
  #show heading: set text(weight: "bold")
  #set par(justify: true)
  = CONCLUSIONES

  - La combinación de Scaffold con NavigationBar y NavHost proporciona una arquitectura desacoplada y predecible para aplicaciones con múltiples pantallas en Jetpack Compose, donde la función currentBackStackEntryAsState garantiza una sincronización determinista entre la ruta activa y el resaltado del ítem correspondiente en la barra inferior.

  - Si bien la comunicación de eventos mediante funciones lambda resulta simple para componentes con dependencia jerárquica directa, produce acoplamiento y sobrecarga de recomposiciones cuando se utiliza para comunicar destinos hermanos en un grafo de navegación.

  - La adopción de SeleccionViewModel como modelo compartido con flujos StateFlow o variables de estado con setter privado consolida una arquitectura reactiva limpia, permitiendo que pantallas independientes compartan y actualicen información de manera directa sin comprometer el Composable raíz.

  - La inclusión de una cuarta pestaña para el perfil del estudiante confirma la extensibilidad del diseño basado en la clase sellada Screen, manteniendo un control centralizado de rutas, etiquetas e íconos sin alterar la lógica de navegación general.

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
