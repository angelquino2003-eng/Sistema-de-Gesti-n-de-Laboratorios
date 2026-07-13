package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Alumno;
import pe.edu.unmsm.fisi.model.entity.Profesor;
import pe.edu.unmsm.fisi.model.entity.Tecnico;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.model.enums.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    @Override
    public Usuario autenticar(String correo, String password) {
        Usuario usuario = null;
        String sql = "SELECT * FROM tbl_usuarios WHERE correo = ? AND password = ?";

        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }
        return usuario;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM tbl_usuarios";

        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    // Método auxiliar para convertir la fila de MySQL en un Objeto Java ._.
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_usuario");
        String nombre = rs.getString("nombre");
        String correo = rs.getString("correo");
        String pass = rs.getString("password");
        Rol rol = Rol.valueOf(rs.getString("rol").toUpperCase());

        // Dependiendo del rol en la BD, instanciamos la clase hija correcta :v
        return switch (rol) {
            case ALUMNO -> new Alumno(id, nombre, correo, pass, rs.getString("codigo_alumno"), rs.getInt("tiempo_limite"));
            case PROFESOR -> new Profesor(id, nombre, correo, pass, rs.getString("departamento_profesor"));
            case TECNICO -> new Tecnico(id, nombre, correo, pass, rs.getString("especialidad_tecnico"));
            default -> null;
        };
    }
}