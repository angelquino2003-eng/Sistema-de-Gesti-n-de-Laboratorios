package pe.edu.unmsm.fisi.model.entity;

import pe.edu.unmsm.fisi.model.enums.Rol;

public class Tecnico extends Usuario {
    private String especialidad;

    public Tecnico() {
        super();
        this.rol = Rol.TECNICO;
    }

    public Tecnico(int idUsuario, String nombre, String correo, String password, String especialidad) {
        super(idUsuario, nombre, correo, password, Rol.TECNICO);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}