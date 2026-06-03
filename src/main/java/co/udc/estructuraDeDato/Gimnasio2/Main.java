package co.udc.estructuraDeDato.Gimnasio2;

import co.udc.estructuraDeDato.Gimnasio2.modelo.InscripcionClase;
import co.udc.estructuraDeDato.Gimnasio2.servicio.GestorDeInscripcion;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    void main(){
        GestorDeInscripcion gestor = new GestorDeInscripcion();
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        // Datos iniciales de prueba para agilizar tu sustentación en video
        try {
            gestor.registrarInscripcion(new InscripcionClase("101", "Kevin Gomez", "Crossfit", "Alta", "PENDIENTE"));
            gestor.registrarInscripcion(new InscripcionClase("102", "Manuel Rojas", "Yoga", "Media", "PENDIENTE"));
            gestor.registrarInscripcion(new InscripcionClase("103", "Maria Perez", "Spinning", "Baja", "PENDIENTE"));
        } catch (Exception ignored) {}

        do {
            System.out.println("\n================ MENU GESTIÓN GIMNASIO ================");
            System.out.println("1. Registrar inscripción");
            System.out.println("2. Ver todos los elementos registrados (List)");
            System.out.println("3. Ver elementos pendientes (Queue)");
            System.out.println("4. Procesar siguiente elemento (FIFO -> LIFO)");
            System.out.println("5. Ver historial de elementos procesados (Deque)");
            System.out.println("6. Buscar elemento por identificador usando Map");
            System.out.println("7. Buscar elemento por Cliente usando Stream");
            System.out.println("8. Filtrar elementos por clase usando Stream");
            System.out.println("9. Ordenar elementos usando Stream");
            System.out.println("10. Ver estadísticas usando Stream y Map");
            System.out.println("11. Ver agrupamientos usando Stream y Map");
            System.out.println("12. Cancelar elemento pendiente");
            System.out.println("13. Deshacer último procesamiento (LIFO)");
            System.out.println("14. Ver cantidad de elementos");
            System.out.println("15. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese código de inscripción: ");
                        String cod = scanner.nextLine();
                        System.out.print("Ingrese nombre del cliente: ");
                        String nom = scanner.nextLine();
                        System.out.print("Ingrese tipo de clase (Yoga/Crossfit/Spinning): ");
                        String tipo = scanner.nextLine();
                        System.out.print("Ingrese prioridad (Alta/Media/Baja): ");
                        String prio = scanner.nextLine();

                        InscripcionClase nueva = new InscripcionClase(cod, nom, tipo, prio, "PENDIENTE");
                        gestor.registrarInscripcion(nueva);
                        System.out.println("¡Inscripción registrada con éxito!");
                        break;

                    case 2:
                        System.out.println("\n--- Todas las Inscripciones ---");
                        gestor.listarTodos();
                        break;

                    case 3:
                        System.out.println("\n--- Cola de Pendientes ---");
                        gestor.mostrarPendientes();
                        break;

                    case 4:
                        gestor.procesarSiguiente();
                        break;

                    case 5:
                        gestor.mostrarHistorial();
                        break;

                    case 6:
                        System.out.print("Ingrese código a buscar: ");
                        String bCod = scanner.nextLine();
                        InscripcionClase porMap = gestor.buscarPorCodigo(bCod);
                        if (porMap != null) {
                            System.out.println("Encontrado (Map): " + porMap);
                        } else {
                            System.out.println("No se encontró ningún elemento con ese código.");
                        }
                        break;

                    case 7:
                        System.out.print("Ingrese el nombre del cliente a buscar: ");
                        String bNom = scanner.nextLine();
                        Optional<InscripcionClase> porStream = gestor.buscarPorClienteStream(bNom);
                        porStream.ifPresentOrElse(
                                System.out::println,
                                () -> System.out.println("No se encontró ningún cliente con ese nombre.")
                        );
                        break;

                    case 8:
                        System.out.print("Ingrese tipo de clase para filtrar: ");
                        String fTipo = scanner.nextLine();
                        List<InscripcionClase> filtrados = gestor.filtrarPorTipoClase(fTipo);
                        if (filtrados.isEmpty()) System.out.println("No hay coincidencias.");
                        else filtrados.forEach(System.out::println);
                        break;

                    case 9:
                        System.out.println("1. Ordenar por Nombre (Ascendente)");
                        System.out.println("2. Ordenar por Código (Descendente)");
                        System.out.print("Seleccione criterio: ");
                        int crit = Integer.parseInt(scanner.nextLine());
                        if (crit == 1) {
                            gestor.ordenarPorNombre().forEach(System.out::println);
                        } else {
                            gestor.ordenarPorCodigoDesc().forEach(System.out::println);
                        }
                        break;

                    case 10:
                        System.out.println("\n--- Estadísticas de Cantidad por Estado ---");
                        Map<String, Long> estadisticas = gestor.generarEstadisticasPorEstado();
                        estadisticas.forEach((est, cant) -> System.out.println("Estado: " + est + " | Cantidad: " + cant));
                        break;

                    case 11:
                        System.out.println("\n--- Inscripciones Agrupadas por Tipo de Clase ---");
                        Map<String, List<InscripcionClase>> agrupado = gestor.agruparPorTipoClase();
                        agrupado.forEach((clase, lista) -> {
                            System.out.println("\nClase: " + clase.toUpperCase());
                            lista.forEach(i -> System.out.println("   -> " + i));
                        });
                        break;

                    case 12:
                        System.out.print("Ingrese código de inscripción a CANCELAR: ");
                        String cCod = scanner.nextLine();
                        gestor.cancelarInscripcionPendiente(cCod);
                        System.out.println("Inscripción cancelada correctamente.");
                        break;

                    case 13:
                        gestor.deshacerUltimoProcesamiento();
                        break;

                    case 14:
                        gestor.mostrarCantidades();
                        break;

                    case 15:
                        System.out.println("Saliendo del sistema de gestión. ¡Hasta luego!");
                        break;

                    default:
                        System.out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor introduzca un número válido.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (opcion != 15);

        scanner.close();
    }

}
