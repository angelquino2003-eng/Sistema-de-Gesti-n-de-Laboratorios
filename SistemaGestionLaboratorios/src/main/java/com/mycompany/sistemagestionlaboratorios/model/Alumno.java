package com.mycompany.sistemagestionlaboratorios.model;

public class Alumno extends Usuario {
    private String escuela;

    public Alumno(String id, String nombre, String contrasena, String escuela) {
        super(id, nombre, contrasena, "ALUMNO");
        this.escuela = escuela;
    }

    public String getEscuela() { return escuela; }
    public void setEscuela(String escuela) { this.escuela = escuela; }
}