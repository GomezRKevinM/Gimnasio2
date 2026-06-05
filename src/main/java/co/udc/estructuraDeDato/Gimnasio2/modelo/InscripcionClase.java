package co.udc.estructuraDeDato.Gimnasio2.modelo;

import java.util.Objects;

public class InscripcionClase {
    private String codigo;
    private String nombreCliente;
    private String tipoClase;
    private String prioridad;
    private String estado;

    public InscripcionClase( String nombreCliente, String tipoClase, String prioridad, String estado) {
        this.nombreCliente = nombreCliente;
        this.tipoClase = tipoClase;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getTipoClase() { return tipoClase; }
    public void setTipoClase(String tipoClase) { this.tipoClase = tipoClase; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // equals y hashCode basados ÚNICAMENTE en el identificador (código)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InscripcionClase inscripcionClase = (InscripcionClase) o;
        return Objects.equals(codigo, inscripcionClase.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        String plantilla = "{ codigo: %s , cliente: %s  , clase: %s , prioridad: %s , estado: %s }";


        return String.format(plantilla,
                this.codigo,
                this.nombreCliente,
                this.tipoClase,
                this.prioridad,
                this.estado
                );
    }
}
