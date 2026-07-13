package pe.edu.unmsm.fisi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Administrador;
import pe.edu.unmsm.fisi.model.entity.Alumno;
import pe.edu.unmsm.fisi.model.entity.Profesor;
import pe.edu.unmsm.fisi.model.entity.Tecnico;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.model.enums.Rol;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    @Override
    public Usuario autenticar(String correo, String password) {
        String sql = "SELECT * FROM tbl_usuarios WHERE correo = ? AND password = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapearUsuario(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo autenticar al usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM tbl_usuarios ORDER BY nombre";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Usuario usuario = mapearUsuario(rs);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar los usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_usuario");
        String nombre = rs.getString("nombre");
        String correo = rs.getString("correo");
        String pass = rs.getString("password");
        Rol rol = Rol.valueOf(rs.getString("rol").trim().toUpperCase());

        return switch (rol) {
            case ALUMNO -> new Alumno(id, nombre, correo, pass,
                    rs.getString("codigo_alumno"), rs.getInt("tiempo_limite"));
            case PROFESOR -> new Profesor(id, nombre, correo, pass,
                    rs.getString("departamento_profesor"));
            case ADMINISTRADOR -> new Administrador(id, nombre, correo, pass);
            case TECNICO -> new Tecnico(id, nombre, correo, pass,
                    rs.getString("especialidad_tecnico"));
        };
    }
}
