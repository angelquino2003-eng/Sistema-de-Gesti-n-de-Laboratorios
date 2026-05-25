package com.mycompany.sistemagestionlaboratorios.model;

public class Administrador extends Usuario {
    public Administrador(String id, String nombre, String contrasena) {
        super(id, nombre, contrasena, "ADMINISTRADOR");
    }
}