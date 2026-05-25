package com.mycompany.sistemagestionlaboratorios.model;

public abstract class Usuario {
    private String id;
    private String nombre;
    private String contrasena;
    private String rol;

    public Usuario(String id, String nombre, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public boolean iniciarSesion(String user, String pass) {
        return this.id.equals(user) && this.contrasena.equals(pass);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}