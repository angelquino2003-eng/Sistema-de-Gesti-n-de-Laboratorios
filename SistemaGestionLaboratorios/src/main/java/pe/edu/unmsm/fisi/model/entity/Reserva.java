package pe.edu.unmsm.fisi.model.entity;

import java.time.LocalDate;

public class Reserva {
    private int idReserva;
    private int idUsuario;
    private String tipoReserva;
    private LocalDate fecha; 
    private int horaInicio;
    private int horaFin;
    private String estado;
    private int idLaboratorio;
    private int idComputadora;
    private String cursoAcademico;

    public Reserva() {
    }

    public Reserva(int idReserva, int idUsuario, String tipoReserva, LocalDate fecha, int horaInicio, int horaFin, String estado, int idLaboratorio, int idComputadora, String cursoAcademico) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.tipoReserva = tipoReserva;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
        this.idLaboratorio = idLaboratorio;
        this.idComputadora = idComputadora;
        this.cursoAcademico = cursoAcademico;
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

    public String getTipoReserva() {
        return tipoReserva;
    }

    public void setTipoReserva(String tipoReserva) {
        this.tipoReserva = tipoReserva;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(int idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public int getIdComputadora() {
        return idComputadora;
    }

    public void setIdComputadora(int idComputadora) {
        this.idComputadora = idComputadora;
    }

    public String getCursoAcademico() {
        return cursoAcademico;
    }

    public void setCursoAcademico(String cursoAcademico) {
        this.cursoAcademico = cursoAcademico;
    }
    
}