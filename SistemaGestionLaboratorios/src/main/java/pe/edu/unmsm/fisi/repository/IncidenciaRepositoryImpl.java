package pe.edu.unmsm.fisi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Incidencia;

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
            throw new IllegalStateException("No se pudo guardar la incidencia: " + e.getMessage(), e);
        }
    }

    @Override
    public Incidencia findSiguienteIncidenciaFIFO() {
        String sql = "SELECT * FROM tbl_incidencias WHERE estado = 'PENDIENTE' ORDER BY fecha_reporte ASC LIMIT 1";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? mapearIncidencia(rs) : null;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo obtener la siguiente incidencia: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Incidencia> findPendientes() {
        return consultar("SELECT * FROM tbl_incidencias WHERE estado IN ('PENDIENTE','EN_PROCESO') ORDER BY fecha_reporte ASC");
    }

    @Override
    public List<Incidencia> findTodas() {
        return consultar("SELECT * FROM tbl_incidencias ORDER BY fecha_reporte DESC");
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
            throw new IllegalStateException("No se pudo actualizar la incidencia: " + e.getMessage(), e);
        }
    }

    private List<Incidencia> consultar(String sql) {
        List<Incidencia> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearIncidencia(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron consultar las incidencias: " + e.getMessage(), e);
        }
        return lista;
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
