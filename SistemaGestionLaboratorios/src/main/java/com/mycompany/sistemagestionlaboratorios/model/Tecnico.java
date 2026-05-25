package com.mycompany.sistemagestionlaboratorios.model;

public class Tecnico extends Usuario {
    private String especialidad;

    public Tecnico(String id, String nombre, String contrasena, String especialidad) {
        super(id, nombre, contrasena, "TECNICO");
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}