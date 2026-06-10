package pe.edu.unmsm.fisi.service;

import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.repository.UsuarioRepository;
import pe.edu.unmsm.fisi.repository.UsuarioRepositoryImpl;

public class AutenticacionServiceImpl implements AutenticacionService {

    // El servicio necesita comunicarse con el repositorio de Adrián
    private final UsuarioRepository usuarioRepository;

    public AutenticacionServiceImpl() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    @Override
    public Usuario login(String correo, String password) {
        
        // 1. REGLAS DE NEGOCIO (Validaciones previas)
        if (correo == null || correo.trim().isEmpty()) {
            System.err.println("Error de validación: El correo no puede estar vacío.");
            return null; // Rechazado automáticamente
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("Error de validación: La contraseña no puede estar vacía.");
            return null; // Rechazado automáticamente
        }

        // 2. COMUNICACIÓN CON LA CAPA DE DATOS
        // Si pasa los filtros de seguridad, enviamos la petición a MySQL
        Usuario usuarioAutenticado = usuarioRepository.autenticar(correo, password);

        // 3. RESPUESTA AL CLIENTE (Swing)
        if (usuarioAutenticado == null) {
            System.err.println("Aviso: Credenciales incorrectas o usuario no encontrado.");
        } else {
            System.out.println("¡Éxito! Bienvenido al sistema, " + usuarioAutenticado.getNombre());
        }

        return usuarioAutenticado;
    }
}