package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Computadora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipoRepositoryImpl implements EquipoRepository {

    // Cambia el estado de una computadora a OCUPADO o DISPONIBLE
    @Override
    public boolean cambiarEstado(int idComputadora, String nuevoEstado) {
        String sql = "UPDATE tbl_computadoras SET estado = ? WHERE id_computadora = ?";
        Connection conn = ConexionBD.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idComputadora);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado del equipo: " + e.getMessage());
            return false;
        }
    }

    // Busca una computadora por su ID
    @Override
    public Computadora buscarPorId(int idComputadora) {
        String sql = "SELECT * FROM tbl_computadoras WHERE id_computadora = ?";
        Connection conn = ConexionBD.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idComputadora);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearComputadora(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar equipo: " + e.getMessage());
        }
        return null;
    }

    // Lista todas las computadoras de un laboratorio
    @Override
    public List<Computadora> listarPorLaboratorio(int idLaboratorio) {
        List<Computadora> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_computadoras WHERE id_laboratorio = ?";
        Connection conn = ConexionBD.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearComputadora(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar equipos: " + e.getMessage());
        }
        return lista;
    }

    // Convierte una fila de MySQL en un objeto Computadora
    private Computadora mapearComputadora(ResultSet rs) throws SQLException {
        return new Computadora(
            rs.getInt("id_computadora"),
            rs.getString("codigo_pc"),
            rs.getString("estado"),
            rs.getInt("id_laboratorio")
        );
    }
}