package com.mycompany.sistemagestionlaboratorios.model;

public class Profesor extends Usuario {
    private String departamento;

    public Profesor(String id, String nombre, String contrasena, String departamento) {
        super(id, nombre, contrasena, "PROFESOR");
        this.departamento = departamento;
    }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}