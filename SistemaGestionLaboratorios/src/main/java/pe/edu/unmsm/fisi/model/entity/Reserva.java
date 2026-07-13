package pe.edu.unmsm.fisi.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import pe.edu.unmsm.fisi.model.enums.EstadoReserva;

public abstract class Reserva {
    private int idReserva;
    private int idUsuario;
    private LocalDate fecha; 
    private int horaInicio;
    private int horaFin;
    private EstadoReserva estado;
    private int idLaboratorio; // Ambas reservas necesitan saber en qué lab están
    private LocalDateTime horaIngresoReal; // Reemplazo de SesionActiva

    public Reserva() {
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public int getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(int idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public LocalDateTime getHoraIngresoReal() {
        return horaIngresoReal;
    }

    public void setHoraIngresoReal(LocalDateTime horaIngresoReal) {
        this.horaIngresoReal = horaIngresoReal;
    }
}