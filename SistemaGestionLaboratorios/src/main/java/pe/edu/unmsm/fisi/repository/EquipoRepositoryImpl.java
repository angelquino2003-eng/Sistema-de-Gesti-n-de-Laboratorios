package pe.edu.unmsm.fisi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Computadora;

public class EquipoRepositoryImpl implements EquipoRepository {

    @Override
    public boolean cambiarEstado(int idComputadora, String nuevoEstado) {
        String sql = "UPDATE tbl_computadoras SET estado = ? WHERE id_computadora = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idComputadora);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo cambiar el estado del equipo: " + e.getMessage(), e);
        }
    }

    @Override
    public Computadora buscarPorId(int idComputadora) {
        String sql = "SELECT * FROM tbl_computadoras WHERE id_computadora = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idComputadora);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapearComputadora(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo buscar el equipo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Computadora> listarPorLaboratorio(int idLaboratorio) {
        List<Computadora> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_computadoras WHERE id_laboratorio = ? ORDER BY codigo_pc";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearComputadora(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar los equipos: " + e.getMessage(), e);
        }
        return lista;
    }

    private Computadora mapearComputadora(ResultSet rs) throws SQLException {
        return new Computadora(
                rs.getInt("id_computadora"),
                rs.getString("codigo_pc"),
                rs.getString("estado"),
                rs.getInt("id_laboratorio")
        );
    }
}
