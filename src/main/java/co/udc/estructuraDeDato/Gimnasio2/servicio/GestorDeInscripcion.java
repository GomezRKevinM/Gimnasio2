package co.udc.estructuraDeDato.Gimnasio2.servicio;

import co.udc.estructuraDeDato.Gimnasio2.modelo.InscripcionClase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

public class GestorDeInscripcion {

    private final List<InscripcionClase> listaGeneral = new ArrayList<>();
    private final Queue<InscripcionClase> colaPendientes = new LinkedList<>();
    private final Deque<InscripcionClase> pilaHistorial = new ArrayDeque<>();
    private final Map<String, InscripcionClase> mapaBusqueda = new HashMap<>();

    public void registrarInscripcion(InscripcionClase inscripcion) {
        inscripcion.setCodigo(generarCodigoUnico());

        if (mapaBusqueda.containsKey(inscripcion.getCodigo())) {
            throw new IllegalArgumentException("Error: Ya existe una inscripcion con el codigo: " + inscripcion.getCodigo());
        }

        listaGeneral.add(inscripcion);
        colaPendientes.offer(inscripcion);
        mapaBusqueda.put(inscripcion.getCodigo(), inscripcion);
    }

    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString();
        } while (mapaBusqueda.containsKey(codigo));
        return codigo;
    }

    public void listarTodos() {
        if (listaGeneral.isEmpty()) {
            System.out.println("No hay inscripciones registradas");
            return;
        }

        String head = "| CODIGO | CLIENTE | CLASE | PRIORIDAD | ESTADO |";
        System.out.println("-".repeat(head.length()));
        System.out.println(head);
        System.out.println("-".repeat(head.length()));
        listaGeneral.forEach(System.out::println);
    }

    public void mostrarPendientes() {
        if (colaPendientes.isEmpty()) {
            System.out.println("No hay inscripciones pendientes");
            return;
        }

        System.out.println("Cantidad en cola: " + colaPendientes.size());
        System.out.println("Siguiente en atender: " + colaPendientes.peek());
        System.out.println("--- Lista Completa de Pendientes ---");
        colaPendientes.forEach(System.out::println);
    }

    public InscripcionClase obtenerUltimoRegistro() {
        if (listaGeneral.isEmpty()) {
            System.out.println("No hay inscripciones registradas");
            return null;
        }
        return listaGeneral.getLast();
    }

    public void procesarSiguiente() {
        InscripcionClase siguiente = colaPendientes.poll();
        if (siguiente == null) {
            throw new IllegalArgumentException("Error: No hay inscripciones pendientes");
        }
        siguiente.setEstado("PROCESADO");
        pilaHistorial.push(siguiente);
        System.out.printf("Se ha procesado con exito a: %s%n", siguiente.getNombreCliente());
    }

    public void mostrarHistorial() {
        if (pilaHistorial.isEmpty()) {
            System.out.println("El historial esta vacio");
            return;
        }

        System.out.println("Total procesados: " + pilaHistorial.size());
        System.out.println("Ultimo procesado: " + pilaHistorial.peek());
        System.out.println("--- Historial (LIFO) ---");
        pilaHistorial.forEach(System.out::println);
    }

    public InscripcionClase buscarPorCodigo(String codigo) {
        if (codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("No se puede buscar con un codigo vacio");
        }

        return mapaBusqueda.get(codigo);
    }

    public Optional<InscripcionClase> buscarPorClienteStream(String nombre) {
        return listaGeneral.stream()
                .filter(i -> i.getNombreCliente().equalsIgnoreCase(nombre))
                .findFirst();
    }

    public List<InscripcionClase> filtrarPorTipoClase(String tipoClase) {
        return listaGeneral.stream()
                .filter(i -> i.getTipoClase().equalsIgnoreCase(tipoClase))
                .toList();
    }

    public List<InscripcionClase> ordenarPorNombre() {
        return listaGeneral.stream()
                .sorted(Comparator.comparing(InscripcionClase::getNombreCliente))
                .toList();
    }

    public List<InscripcionClase> ordenarPorCodigoDesc() {
        return listaGeneral.stream()
                .sorted(Comparator.comparing(InscripcionClase::getCodigo).reversed())
                .toList();
    }

    public List<String> obtenerNombresClientes() {
        return listaGeneral.stream()
                .map(InscripcionClase::getNombreCliente)
                .toList();
    }

    public Map<String, Long> generarEstadisticasPorEstado() {
        return listaGeneral.stream()
                .collect(Collectors.groupingBy(InscripcionClase::getEstado, Collectors.counting()));
    }

    public Map<String, List<InscripcionClase>> agruparPorTipoClase() {
        return listaGeneral.stream()
                .collect(Collectors.groupingBy(InscripcionClase::getTipoClase));
    }

    public Map<String, InscripcionClase> reconstruirIndiceConStream() {
        return listaGeneral.stream()
                .collect(Collectors.toMap(
                        InscripcionClase::getCodigo,
                        inscripcion -> inscripcion,
                        (existente, repetida) -> existente
                ));
    }

    public boolean existePendiente() {
        return listaGeneral.stream()
                .anyMatch(i -> i.getEstado().equalsIgnoreCase("PENDIENTE"));
    }

    public boolean todosTienenCodigo() {
        return listaGeneral.stream()
                .allMatch(i -> i.getCodigo() != null && !i.getCodigo().isBlank());
    }

    public boolean noHayCancelados() {
        return listaGeneral.stream()
                .noneMatch(i -> i.getEstado().equalsIgnoreCase("CANCELADO"));
    }

    public void mostrarIndiceBusqueda() {
        if (mapaBusqueda.isEmpty()) {
            System.out.println("El mapa de busqueda esta vacio");
            return;
        }

        System.out.println("--- Claves del Map (keySet) ---");
        mapaBusqueda.keySet().forEach(System.out::println);

        System.out.println("--- Valores del Map (values) ---");
        mapaBusqueda.values().forEach(System.out::println);

        System.out.println("--- Entradas del Map (entrySet) ---");
        mapaBusqueda.entrySet().forEach(entry ->
                System.out.println(entry.getKey() + " => " + entry.getValue())
        );
    }

    public void cancelarInscripcionPendiente(String codigo) {
        InscripcionClase inscripcion = mapaBusqueda.get(codigo);
        if (inscripcion == null) {
            throw new IllegalArgumentException("Error: No existe una inscripcion con ese codigo.");
        }
        if (!inscripcion.getEstado().equalsIgnoreCase("PENDIENTE")) {
            throw new IllegalStateException("Error: Solo se pueden cancelar inscripciones en estado PENDIENTE.");
        }
        inscripcion.setEstado("CANCELADO");
        colaPendientes.removeIf(i -> i.getCodigo().equals(codigo));
    }

    public void deshacerUltimoProcesamiento() {
        if (pilaHistorial.isEmpty()) {
            throw new IllegalStateException("Error: No hay procesamientos que deshacer en el historial.");
        }
        InscripcionClase ultimo = pilaHistorial.pop();
        ultimo.setEstado("PENDIENTE");
        colaPendientes.offer(ultimo);
        System.out.println("Se deshizo el procesamiento de: " + ultimo.getNombreCliente() + ". Volvio a pendientes.");
    }

    public void mostrarCantidades() {
        System.out.println("=== Tamanos de las Estructuras ===");
        System.out.println("Lista General: " + listaGeneral.size());
        System.out.println("Cola Pendientes: " + colaPendientes.size());
        System.out.println("Pila Historial: " + pilaHistorial.size());
        System.out.println("Mapa de Busqueda: " + mapaBusqueda.size());
        System.out.println("Mapa vacio: " + mapaBusqueda.isEmpty());

        System.out.println("\n=== Conteos Especificos con Stream ===");
        long pendientes = listaGeneral.stream().filter(i -> i.getEstado().equals("PENDIENTE")).count();
        long procesados = listaGeneral.stream().filter(i -> i.getEstado().equals("PROCESADO")).count();
        long cancelados = listaGeneral.stream().filter(i -> i.getEstado().equals("CANCELADO")).count();
        System.out.println("Pendientes (Stream): " + pendientes);
        System.out.println("Procesados (Stream): " + procesados);
        System.out.println("Cancelados (Stream): " + cancelados);
    }
}
