package pe.edu.unmsm.fisi.model.entity;

import pe.edu.unmsm.fisi.model.enums.Rol;

public class Alumno extends Usuario {
    private String codigoAlumno;
    private int tiempoLimiteMinutos;

    public Alumno() {
        super();
        this.rol = Rol.ALUMNO;
    }

    public Alumno(int idUsuario, String nombre, String correo, String password, String codigoAlumno, int tiempoLimiteMinutos) {
        super(idUsuario, nombre, correo, password, Rol.ALUMNO);
        this.codigoAlumno = codigoAlumno;
        this.tiempoLimiteMinutos = tiempoLimiteMinutos;
    }

    public String getCodigoAlumno() { return codigoAlumno; }
    public void setCodigoAlumno(String codigoAlumno) { this.codigoAlumno = codigoAlumno; }

    public int getTiempoLimiteMinutos() { return tiempoLimiteMinutos; }
    public void setTiempoLimiteMinutos(int tiempoLimiteMinutos) { this.tiempoLimiteMinutos = tiempoLimiteMinutos; }
}