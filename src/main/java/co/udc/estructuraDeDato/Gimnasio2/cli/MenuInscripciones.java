package co.udc.estructuraDeDato.Gimnasio2.cli;

import co.udc.estructuraDeDato.Gimnasio2.modelo.InscripcionClase;
import co.udc.estructuraDeDato.Gimnasio2.servicio.GestorDeInscripcion;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class MenuInscripciones {

    private static final int OPCION_SALIR = 15;

    private final GestorDeInscripcion gestor;
    private final Scanner scanner;

    public MenuInscripciones() {
        this.gestor = new GestorDeInscripcion();
        this.scanner = new Scanner(System.in);
    }

    public void ejecutar() {
        cargarDatosIniciales();

        int opcion = 0;
        do {
            mostrarMenu();

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor introduzca un numero valido.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (opcion != OPCION_SALIR);

        scanner.close();
    }

    private void cargarDatosIniciales() {
        try {
            gestor.registrarInscripcion(new InscripcionClase( "Antonio Banderas", "Crossfit", "Alta", "PENDIENTE"));
            gestor.registrarInscripcion(new InscripcionClase("Jeronimo Martinez", "Yoga", "Media", "PENDIENTE"));
            gestor.registrarInscripcion(new InscripcionClase( "Maria Perez", "Spinning", "Baja", "PENDIENTE"));
        } catch (Exception ignored) {
        }
    }

    private void mostrarMenu() {
        System.out.println("\n================ MENU GESTION GIMNASIO ================");
        System.out.println("1. Registrar inscripcion");
        System.out.println("2. Ver todos los elementos registrados (List)");
        System.out.println("3. Ver elementos pendientes (Queue)");
        System.out.println("4. Procesar siguiente elemento (FIFO -> LIFO)");
        System.out.println("5. Ver historial de elementos procesados (Deque)");
        System.out.println("6. Buscar elemento por identificador usando Map");
        System.out.println("7. Buscar elemento por Cliente usando Stream");
        System.out.println("8. Filtrar elementos por clase usando Stream");
        System.out.println("9. Ordenar elementos usando Stream");
        System.out.println("10. Ver estadisticas usando Stream y Map");
        System.out.println("11. Ver agrupamientos usando Stream y Map");
        System.out.println("12. Cancelar elemento pendiente");
        System.out.println("13. Deshacer ultimo procesamiento (LIFO)");
        System.out.println("14. Ver cantidad de elementos");
        System.out.println("15. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> registrarInscripcion();
            case 2 -> listarInscripciones();
            case 3 -> listarPendientes();
            case 4 -> gestor.procesarSiguiente();
            case 5 -> gestor.mostrarHistorial();
            case 6 -> buscarPorCodigo();
            case 7 -> buscarPorCliente();
            case 8 -> filtrarPorTipoClase();
            case 9 -> ordenarInscripciones();
            case 10 -> mostrarEstadisticas();
            case 11 -> mostrarAgrupamientos();
            case 12 -> cancelarPendiente();
            case 13 -> gestor.deshacerUltimoProcesamiento();
            case 14 -> gestor.mostrarCantidades();
            case OPCION_SALIR -> System.out.println("Saliendo del sistema de gestion. Hasta luego!");
            default -> System.out.println("Opcion invalida. Intente de nuevo.");
        }
    }

    private void registrarInscripcion() {
        System.out.print("Ingrese nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese tipo de clase (Yoga/Crossfit/Spinning): ");
        String tipo = scanner.nextLine();
        System.out.print("Ingrese prioridad (Alta/Media/Baja): ");
        String prioridad = scanner.nextLine();

        InscripcionClase nueva = new InscripcionClase( nombre, tipo, prioridad, "PENDIENTE");

        gestor.registrarInscripcion(nueva);
        nueva = gestor.obtenerUltimoRegistro();
        System.out.println("Inscripcion registrada con exito: "+"\n" + nueva.toString());
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void listarInscripciones() {
        System.out.println("\n--- Todas las Inscripciones ---");
        gestor.listarTodos();
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void listarPendientes() {
        System.out.println("\n--- Cola de Pendientes ---");
        gestor.mostrarPendientes();
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void buscarPorCodigo() {
        System.out.print("Ingrese codigo a buscar: ");
        String codigo = scanner.nextLine();
        InscripcionClase inscripcion = gestor.buscarPorCodigo(codigo);

        if (inscripcion != null) {
            System.out.println("Encontrado (Map): " + inscripcion);
            System.out.println("Presione cualquier tecla para continuar...");
            scanner.nextLine();
            return;
        }

        System.out.println("No se encontro ningun elemento con ese codigo.");
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void buscarPorCliente() {
        System.out.print("Ingrese el nombre del cliente a buscar: ");
        String nombre = scanner.nextLine();
        Optional<InscripcionClase> inscripcion = gestor.buscarPorClienteStream(nombre);

        inscripcion.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No se encontro ningun cliente con ese nombre.")
        );
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void filtrarPorTipoClase() {
        System.out.print("Ingrese tipo de clase para filtrar: ");
        String tipo = scanner.nextLine();
        List<InscripcionClase> filtrados = gestor.filtrarPorTipoClase(tipo);

        if (filtrados.isEmpty()) {
            System.out.println("No hay coincidencias.");
            return;
        }

        filtrados.forEach(System.out::println);
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void ordenarInscripciones() {
        System.out.println("1. Ordenar por Nombre (Ascendente)");
        System.out.println("2. Ordenar por Codigo (Descendente)");
        System.out.print("Seleccione criterio: ");
        int criterio = Integer.parseInt(scanner.nextLine());

        if (criterio == 1) {
            gestor.ordenarPorNombre().forEach(System.out::println);
            System.out.println("Presione cualquier tecla para continuar...");
            scanner.nextLine();
            return;
        }

        gestor.ordenarPorCodigoDesc().forEach(System.out::println);
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void mostrarEstadisticas() {
        System.out.println("\n--- Estadisticas de Cantidad por Estado ---");
        Map<String, Long> estadisticas = gestor.generarEstadisticasPorEstado();
        estadisticas.forEach((estado, cantidad) ->
                System.out.println("Estado: " + estado + " | Cantidad: " + cantidad)
        );

        System.out.println("\n--- Transformacion con Stream map() ---");
        gestor.obtenerNombresClientes().forEach(nombre -> System.out.println("Cliente: " + nombre));

        System.out.println("\n--- Validaciones con anyMatch(), allMatch() y noneMatch() ---");
        System.out.println("Existe al menos una inscripcion pendiente: " + gestor.existePendiente());
        System.out.println("Todas las inscripciones tienen codigo: " + gestor.todosTienenCodigo());
        System.out.println("No hay inscripciones canceladas: " + gestor.noHayCancelados());

        System.out.println("\n--- Reconstruccion de indice con Collectors.toMap() ---");
        System.out.println("Elementos en indice reconstruido: " + gestor.reconstruirIndiceConStream().size());

        System.out.println("\n--- Recorrido del Map original ---");
        gestor.mostrarIndiceBusqueda();

        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void mostrarAgrupamientos() {
        System.out.println("\n--- Inscripciones Agrupadas por Tipo de Clase ---");
        Map<String, List<InscripcionClase>> agrupado = gestor.agruparPorTipoClase();
        agrupado.forEach((clase, inscripciones) -> {
            System.out.println("\nClase: " + clase.toUpperCase());
            inscripciones.forEach(inscripcion -> System.out.println("   -> " + inscripcion));
        });
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }

    private void cancelarPendiente() {
        System.out.print("Ingrese codigo de inscripcion a CANCELAR: ");
        String codigo = scanner.nextLine();
        gestor.cancelarInscripcionPendiente(codigo);
        System.out.println("Inscripcion cancelada correctamente.");
        System.out.println("Presione cualquier tecla para continuar...");
        scanner.nextLine();
    }
}
