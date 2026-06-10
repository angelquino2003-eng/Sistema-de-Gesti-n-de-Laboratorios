package pe.edu.unmsm.fisi.model.entity;

import java.time.LocalDateTime;

public class Incidencia {
    private int idIncidencia;
    private int idComputadora;
    private int idUsuarioReporta;
    private String descripcion;
    private String tipoIncidencia;
    private LocalDateTime fechaReporte; // Para manejar TIMESTAMP (fecha y hora)
    private String estado;

    public Incidencia() {
    }

    public Incidencia(int idIncidencia, int idComputadora, int idUsuarioReporta, String descripcion, String tipoIncidencia, LocalDateTime fechaReporte, String estado) {
        this.idIncidencia = idIncidencia;
        this.idComputadora = idComputadora;
        this.idUsuarioReporta = idUsuarioReporta;
        this.descripcion = descripcion;
        this.tipoIncidencia = tipoIncidencia;
        this.fechaReporte = fechaReporte;
        this.estado = estado;
    }

    public int getIdIncidencia() { return idIncidencia; }
    public void setIdIncidencia(int idIncidencia) { this.idIncidencia = idIncidencia; }

    public int getIdComputadora() { return idComputadora; }
    public void setIdComputadora(int idComputadora) { this.idComputadora = idComputadora; }

    public int getIdUsuarioReporta() { return idUsuarioReporta; }
    public void setIdUsuarioReporta(int idUsuarioReporta) { this.idUsuarioReporta = idUsuarioReporta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipoIncidencia() { return tipoIncidencia; }
    public void setTipoIncidencia(String tipoIncidencia) { this.tipoIncidencia = tipoIncidencia; }

    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(LocalDateTime fechaReporte) { this.fechaReporte = fechaReporte; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}