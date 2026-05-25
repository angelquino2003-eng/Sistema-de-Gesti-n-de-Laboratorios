package com.mycompany.sistemagestionlaboratorios.repository;

import com.mycompany.sistemagestionlaboratorios.model.*;
import java.util.ArrayList;

public class MemoriaDataStore {
    private ArrayList<Usuario> usuariosRegistrados;

    public MemoriaDataStore() {
        usuariosRegistrados = new ArrayList<>();
        cargarUsuariosPorDefecto();
    }

    private void cargarUsuariosPorDefecto() {
        usuariosRegistrados.add(new Administrador("admin", "Administrador Principal", "admin123"));
        usuariosRegistrados.add(new Alumno("2026001", "Jean Carlo", "alumno123", "Ing. de Software"));
        usuariosRegistrados.add(new Profesor("prof01", "Juan Tapia", "profe123", "Ciencias de la Computación"));
        usuariosRegistrados.add(new Tecnico("tec01", "Soporte TI", "tec123", "Hardware"));
    }

    public Usuario autenticarUsuario(String id, String contrasena) {
        for (Usuario u : usuariosRegistrados) {
            if (u.iniciarSesion(id, contrasena)) {
                return u; 
            }
        }
        return null; 
    }
}