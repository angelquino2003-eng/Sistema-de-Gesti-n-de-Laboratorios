package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.model.entity.Usuario;
import java.util.List;

public interface UsuarioRepository {
    Usuario autenticar(String correo, String password);
    
    List<Usuario> listarTodos();
}