package pe.edu.unmsm.fisi.model.entity;

import pe.edu.unmsm.fisi.model.enums.Rol;

public class Profesor extends Usuario {
    private String departamentoAcademico;

    public Profesor() {
        super();
        this.rol = Rol.PROFESOR;
    }

    public Profesor(int idUsuario, String nombre, String correo, String password, String departamentoAcademico) {
        super(idUsuario, nombre, correo, password, Rol.PROFESOR);
        this.departamentoAcademico = departamentoAcademico;
    }

    public String getDepartamentoAcademico() { return departamentoAcademico; }
    public void setDepartamentoAcademico(String departamentoAcademico) { this.departamentoAcademico = departamentoAcademico; }
}