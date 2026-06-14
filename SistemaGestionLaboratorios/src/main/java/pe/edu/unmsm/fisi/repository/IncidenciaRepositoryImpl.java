package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Incidencia;
import java.sql.*;

public class IncidenciaRepositoryImpl implements IncidenciaRepository {

    @Override
    public boolean save(Incidencia incidencia) {
        String sql = "INSERT INTO tbl_incidencias (id_computadora, id_usuario_reporta, descripcion, tipo_incidencia, fecha_reporte, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, incidencia.getIdComputadora());
            stmt.setInt(2, incidencia.getIdUsuarioReporta());
            stmt.setString(3, incidencia.getDescripcion());
            stmt.setString(4, incidencia.getTipoIncidencia());
            stmt.setTimestamp(5, Timestamp.valueOf(incidencia.getFechaReporte()));
            stmt.setString(6, incidencia.getEstado());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar incidencia: " + e.getMessage());
            return false;
        }
    }

    // Cola FIFO: trae la incidencia pendiente más antigua
    @Override
    public Incidencia findSiguienteIncidenciaFIFO() {
        String sql = "SELECT * FROM tbl_incidencias WHERE estado = 'PENDIENTE' ORDER BY fecha_reporte ASC LIMIT 1";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapearIncidencia(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener siguiente incidencia FIFO: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateEstadoIncidencia(int idIncidencia, String nuevoEstado) {
        String sql = "UPDATE tbl_incidencias SET estado = ? WHERE id_incidencia = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idIncidencia);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de incidencia: " + e.getMessage());
            return false;
        }
    }

    private Incidencia mapearIncidencia(ResultSet rs) throws SQLException {
        return new Incidencia(
            rs.getInt("id_incidencia"),
            rs.getInt("id_computadora"),
            rs.getInt("id_usuario_reporta"),
            rs.getString("descripcion"),
            rs.getString("tipo_incidencia"),
            rs.getTimestamp("fecha_reporte").toLocalDateTime(),
            rs.getString("estado")
        );
    }
}
