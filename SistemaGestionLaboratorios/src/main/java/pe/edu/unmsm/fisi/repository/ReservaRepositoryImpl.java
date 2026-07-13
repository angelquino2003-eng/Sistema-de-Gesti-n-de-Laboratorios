package pe.edu.unmsm.fisi.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Reserva;

public class ReservaRepositoryImpl implements ReservaRepository {

    @Override
    public boolean save(Reserva reserva) {
        String sql = "INSERT INTO tbl_reservas (id_usuario, tipo_reserva, fecha, hora_inicio, hora_fin, estado, id_laboratorio, id_computadora, curso_academico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, reserva.getIdUsuario());
                stmt.setString(2, reserva.getTipoReserva());
                stmt.setDate(3, Date.valueOf(reserva.getFecha()));
                stmt.setInt(4, reserva.getHoraInicio());
                stmt.setInt(5, reserva.getHoraFin());
                stmt.setString(6, reserva.getEstado());
                stmt.setInt(7, reserva.getIdLaboratorio());
                if (reserva.getIdComputadora() == 0) {
                    stmt.setNull(8, Types.INTEGER);
                } else {
                    stmt.setInt(8, reserva.getIdComputadora());
                }
                stmt.setString(9, reserva.getCursoAcademico());
                boolean ok = stmt.executeUpdate() > 0;
                conn.commit();
                return ok;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo guardar la reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reserva> findReservasPorLaboratorio(int idLaboratorio, LocalDate fecha) {
        String sql = "SELECT * FROM tbl_reservas WHERE id_laboratorio = ? AND fecha = ? AND estado IN ('ACTIVA','APROBADA') ORDER BY hora_inicio";
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLaboratorio);
            stmt.setDate(2, Date.valueOf(fecha));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearReserva(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron consultar las reservas: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Reserva> findReservasActivasPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM tbl_reservas WHERE id_usuario = ? AND estado IN ('ACTIVA','APROBADA') ORDER BY fecha DESC, hora_inicio";
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearReserva(rs));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron consultar las reservas del usuario: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Reserva> findTodas() {
        String sql = "SELECT * FROM tbl_reservas ORDER BY fecha DESC, hora_inicio";
        List<Reserva> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearReserva(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron listar las reservas: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public boolean actualizarEstado(int idReserva, String estado) {
        String sql = "UPDATE tbl_reservas SET estado = ? WHERE id_reserva = ?";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado);
            stmt.setInt(2, idReserva);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo actualizar la reserva: " + e.getMessage(), e);
        }
    }

    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        int idComputadora = rs.getInt("id_computadora");
        if (rs.wasNull()) {
            idComputadora = 0;
        }
        return new Reserva(
                rs.getInt("id_reserva"),
                rs.getInt("id_usuario"),
                rs.getString("tipo_reserva"),
                rs.getDate("fecha").toLocalDate(),
                rs.getInt("hora_inicio"),
                rs.getInt("hora_fin"),
                rs.getString("estado"),
                rs.getInt("id_laboratorio"),
                idComputadora,
                rs.getString("curso_academico")
        );
    }
}
