# Sistema de Gestión de Inscripciones de Gimnasio

Este proyecto es una aplicación de consola en Java diseñada para resolver el problema real de administrar el flujo de inscripciones a clases de un gimnasio (Yoga, Crossfit, Spinning, etc.), cumpliendo con los lineamientos arquitectónicos y técnicos de la asignatura Estructuras de Datos (Unidad III).

La solución implementa una arquitectura desacoplada en tres paquetes principales (`modelo`, `servicio`, y la clase raíz `Main`) y hace uso exclusivo de las estructuras de datos nativas del **SDK de Java**, eliminando por completo el manejo manual de nodos y punteros, e integrando capacidades avanzadas de programación funcional mediante la API **Java Stream**.

---

## Estructuras de Datos Utilizadas y Justificación

El sistema organiza el flujo de las inscripciones vinculando de forma coordinada cuatro colecciones clave del lenguaje Java para garantizar un procesamiento ordenado, trazabilidad e indexación eficiente:

1. **`List<InscripcionClase>` (Registro General)**
    * **Implementación:** `ArrayList<InscripcionClase>`.
    * **Propósito:** Actúa como la fuente centralizada de datos (un almacenamiento en memoria) que conserva el histórico absoluto de todos los elementos registrados en el gimnasio, sin importar si su estado actual es *Pendiente*, *Procesado* o *Cancelado*. Es la base principal sobre la cual se ejecutan las consultas funcionales complejas con Streams.

2. **`Queue<InscripcionClase>` (Cola de Pendientes)**
    * **Implementación:** `LinkedList<InscripcionClase>`.
    * **Propósito:** Modela una estructura de sala de espera lineal bajo el estricto principio **FIFO (First-In, First-Out)**. Asegura que los clientes que solicitaron su inscripción primero sean atendidos en ese mismo orden cronológico de llegada.

3. **`Deque<InscripcionClase>` (Historial de Procesados como Pila)**
    * **Implementación:** `ArrayDeque<InscripcionClase>`.
    * **Propósito:** Funciona bajo la lógica **LIFO (Last-In, First-Out)** para modelar el historial de operaciones completadas. Almacena las inscripciones cuyo estado cambia a *Procesado*, permitiendo de manera eficiente la operación de "Deshacer" (retirar el último elemento procesado de la cima de la pila y reincorporarlo a la cola de pendientes).

4. **`Map<String, InscripcionClase>` (Índice de Búsqueda Rápida)**
    * **Implementación:** `HashMap<String, InscripcionClase>`.
    * **Propósito:** Utiliza el `codigo` único de la inscripción como llave (`Key`) para indexar instantáneamente los objetos. Esto reduce el costo computacional de búsqueda a un tiempo constante **O(1)** y actúa como mecanismo de control para evitar duplicados en el instante del registro.

5. **`Java Stream API` (Procesamiento Funcional)**
    * **Propósito:** Se emplea para efectuar operaciones declarativas sobre la lista general, abstrayendo ciclos iterativos complejos. Permite realizar búsquedas por atributos secundarios (como el nombre del cliente), filtrados por tipo de clase, ordenamientos de doble criterio, agrupamientos categorizados (`Collectors.groupingBy`) y conteos estadísticos de estados en tiempo real.

---

## Tabla Comparativa: Estructuras Personalizadas vs. Colecciones SDK de Java

contrastando los enfoques aplicados entre la actividad previa (Unidad 2 gimnasio) y esta solución basada en las APIs estables de nivel industrial de Java:

| Aspecto Técnico | Estructuras Personalizadas (Actividad Anterior) | Colecciones del SDK de Java (Actividad Actual) |
| :--- | :--- | :--- |
| **Implementación Física** | El estudiante debe escribir manualmente la lógica interna de almacenamiento (arrays dinámicos o enlaces). | Java provee la estructura completamente optimizada de forma nativa en el SDK. |
| **Manipulación de Nodos** | Se crean y manipulan punteros o referencias de memoria explícitas (`nodo.siguiente`, `nodo.anterior`). | No se manipulan directamente; el manejo de memoria interna está encapsulado por el lenguaje. |
| **Estandarización de Métodos**| Se inventan métodos propios (`insertarAlInicio()`, `eliminarNodo()`, `encolar()`). | Se consumen métodos e interfaces estándar unificados del Framework (`add()`, `poll()`, `push()`, `get()`). |
| **Riesgo de Errores** | **Mayor:** Altamente propenso a excepciones de puntero nulo (`NullPointerException`), desbordamientos o ciclos infinitos. | **Menor:** Probado rigurosamente por la industria mundial; libre de errores de punteros manuales. |
| **Control Interno** | **Mayor:** Permite un control milimétrico sobre el diseño exacto de la celda de memoria y comportamiento. | **Menor:** Se opera sobre una abstracción; el control interno fino es delegado a la máquina virtual (JVM). |
| **Productividad** | **Menor:** Se invierte tiempo considerable depurando la infraestructura de la estructura de datos en lugar del problema. | **Mayor:** Permite centrarse de forma inmediata en la lógica de negocio del gimnasio acelerando los tiempos de entrega. |
| **Mantenibilidad** | Depende exclusivamente de la documentación del estudiante y de la legibilidad de su código artesanal. | **Alta:** Cualquier desarrollador de Java comprende el código al basarse en las APIs y contratos oficiales del lenguaje. |
| **Propósito Didáctico** | Comprender de manera profunda los fundamentos algorítmicos y cómo funcionan internamente las estructuras. | Aprender a resolver problemas reales del entorno empresarial aplicando código eficiente, limpio y robusto. |
