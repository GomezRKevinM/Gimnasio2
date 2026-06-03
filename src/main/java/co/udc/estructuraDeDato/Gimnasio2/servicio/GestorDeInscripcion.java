package co.udc.estructuraDeDato.Gimnasio2.servicio;

import co.udc.estructuraDeDato.Gimnasio2.modelo.InscripcionClase;

import java.util.*;
import java.util.stream.Collectors;

public class GestorDeInscripcion {

    private List<InscripcionClase> listaGeneral = new ArrayList<>();
    private Queue<InscripcionClase> colaPendientes = new LinkedList<>();
    private Deque<InscripcionClase> pilaHistorial = new ArrayDeque<>();
    private Map<String, InscripcionClase> mapaBusqueda = new HashMap<>();

    public void registrarInscripcion(InscripcionClase inscripcion){
        if(mapaBusqueda.containsKey(inscripcion.getCodigo())){
            throw new IllegalArgumentException("Error: Ya existe una inscripción con el código: " + inscripcion.getCodigo());
        }

        listaGeneral.add(inscripcion);
        colaPendientes.offer(inscripcion);
        mapaBusqueda.put(inscripcion.getCodigo(),inscripcion);
    }

    public void listarTodos(){
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

    public void mostrarPendientes(){
        if(colaPendientes.isEmpty()){
            System.out.println("No hay inscripciones pendientes");
            return;
        }

        System.out.println("Cantidad en cola: " + colaPendientes.size());
        System.out.println("Siguiente en atender: " + colaPendientes.peek());
        System.out.println("--- Lista Completa de Pendientes ---");
        colaPendientes.forEach(System.out::println);
    }

    public void procesarSiguiente(){
        InscripcionClase siguiente = colaPendientes.poll();
        if(siguiente == null){
            throw new IllegalArgumentException("Error: No hay inscripciones pendientes");
        }
        siguiente.setEstado("PROCESADO");
        pilaHistorial.push(siguiente);
        System.out.printf("Se ha procesado con exito a: %s \n", siguiente.getNombreCliente());
    }

    public void mostrarHistorial(){
        if (pilaHistorial.isEmpty()){
            System.out.println("El historial esta vacío");
            return;
        }

        System.out.println("Total procesados: " + pilaHistorial.size());
        System.out.println("Último procesado: " + pilaHistorial.peek());
        System.out.println("--- Historial (LIFO) ---");
        pilaHistorial.forEach(System.out::println);
    }

    public InscripcionClase buscarPorCodigo(String codigo){
        if (codigo.trim().isEmpty()){
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

    public Map<String, Long> generarEstadisticasPorEstado() {
        return listaGeneral.stream()
                .collect(Collectors.groupingBy(InscripcionClase::getEstado, Collectors.counting()));
    }

    public Map<String, List<InscripcionClase>> agruparPorTipoClase() {
        return listaGeneral.stream()
                .collect(Collectors.groupingBy(InscripcionClase::getTipoClase));
    }

    public void cancelarInscripcionPendiente(String codigo) {
        InscripcionClase inscripcion = mapaBusqueda.get(codigo);
        if (inscripcion == null) {
            throw new IllegalArgumentException("Error: No existe una inscripción con ese código.");
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
        colaPendientes.offer(ultimo); // Regresa a la cola
        System.out.println("Se deshizo el procesamiento de: " + ultimo.getNombreCliente() + ". Volvió a pendientes.");
    }

    public void mostrarCantidades() {
        System.out.println("=== Tamaños de las Estructuras ===");
        System.out.println("Lista General: " + listaGeneral.size());
        System.out.println("Cola Pendientes: " + colaPendientes.size());
        System.out.println("Pila Historial: " + pilaHistorial.size());
        System.out.println("Mapa de Búsqueda: " + mapaBusqueda.size());

        System.out.println("\n=== Conteos Específicos con Stream ===");
        long pendientes = listaGeneral.stream().filter(i -> i.getEstado().equals("PENDIENTE")).count();
        long procesados = listaGeneral.stream().filter(i -> i.getEstado().equals("PROCESADO")).count();
        long cancelados = listaGeneral.stream().filter(i -> i.getEstado().equals("CANCELADO")).count();
        System.out.println("Pendientes (Stream): " + pendientes);
        System.out.println("Procesados (Stream): " + procesados);
        System.out.println("Cancelados (Stream): " + cancelados);
    }
}
