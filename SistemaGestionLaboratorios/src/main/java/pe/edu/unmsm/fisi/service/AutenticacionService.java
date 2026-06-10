package pe.edu.unmsm.fisi.service;

import pe.edu.unmsm.fisi.model.entity.Usuario;

public interface AutenticacionService {
    // Recibe las credenciales crudas de la vista y devuelve el objeto Usuario si el login es exitoso
    Usuario login(String correo, String password);
}