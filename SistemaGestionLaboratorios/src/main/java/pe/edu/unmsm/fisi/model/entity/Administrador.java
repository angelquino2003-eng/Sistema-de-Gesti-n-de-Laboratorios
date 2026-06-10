package pe.edu.unmsm.fisi.model.entity;

import pe.edu.unmsm.fisi.model.enums.Rol;

public class Administrador extends Usuario {
    
    public Administrador() {
        super();
        this.rol = Rol.ADMINISTRADOR;
    }

    public Administrador(int idUsuario, String nombre, String correo, String password) {
        super(idUsuario, nombre, correo, password, Rol.ADMINISTRADOR);
    }
}