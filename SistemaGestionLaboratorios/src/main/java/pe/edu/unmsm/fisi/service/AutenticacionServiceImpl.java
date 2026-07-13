package pe.edu.unmsm.fisi.service;

import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.repository.UsuarioRepository;
import pe.edu.unmsm.fisi.repository.UsuarioRepositoryImpl;

public class AutenticacionServiceImpl implements AutenticacionService {
    private final UsuarioRepository usuarioRepository;

    public AutenticacionServiceImpl() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    @Override
    public Usuario login(String correo, String password) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        return usuarioRepository.autenticar(correo.trim(), password);
    }
}
