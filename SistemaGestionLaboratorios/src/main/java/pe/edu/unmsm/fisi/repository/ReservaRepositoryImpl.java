package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaRepositoryImpl implements ReservaRepository {

    @Override
    public boolean save(Reserva reserva) {
        String sql = "INSERT INTO tbl_reservas (id_usuario, tipo_reserva, fecha, hora_inicio, hora_fin, estado, id_laboratorio, id_computadoras, curso_academico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConexionBD.getInstance().getConnection();
            conn.setAutoCommit(false); // Inicia la transacción

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, reserva.getIdUsuario());
                stmt.setString(2, reserva.getTipoReserva());
                stmt.setDate(3, Date.valueOf(reserva.getFecha()));
                stmt.setInt(4, reserva.getHoraInicio());
                stmt.setInt(5, reserva.getHoraFin());
                stmt.setString(6, reserva.getEstado());
                stmt.setInt(7, reserva.getIdLaboratorio());
                stmt.setInt(8, reserva.getIdComputadora());
                stmt.setString(9, reserva.getCursoAcademico());

                int filas = stmt.executeUpdate();
                conn.commit(); // Confirma la transacción
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar reserva, revirtiendo: " + e.getMessage());
            try {
                if (conn != null) conn.rollback(); // Revierte si algo falla
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Restaura el modo normal
            } catch (SQLException e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Reserva> findReservasPorLaboratorio(int idLaboratorio, LocalDate fecha) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_reservas WHERE id_laboratorio = ? AND fecha = ?";
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
            System.err.println("Error al listar reservas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> findReservasActivasPorUsuario(int idUsuario) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_reservas WHERE id_usuario = ? AND estado = 'APROBADA'";
        try (Connection conn = ConexionBD.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearReserva(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reservas del usuario: " + e.getMessage());
        }
        return lista;
    }

    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        return new Reserva(
            rs.getInt("id_reserva"),
            rs.getInt("id_usuario"),
            rs.getString("tipo_reserva"),
            rs.getDate("fecha").toLocalDate(),
            rs.getInt("hora_inicio"),
            rs.getInt("hora_fin"),
            rs.getString("estado"),
            rs.getInt("id_laboratorio"),
            rs.getInt("id_computadoras"),
            rs.getString("curso_academico")
        );
    }
}
