package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.ReservaComputadora;
import pe.edu.unmsm.fisi.model.entity.ReservaLaboratorio;
import pe.edu.unmsm.fisi.model.enums.EstadoReserva;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaRepositoryImpl implements ReservaRepository {

    @Override
    public boolean save(Reserva reserva) {
        // Se agregaron las 10 columnas exactas de tu diagrama V2
        String sql = "INSERT INTO tbl_reservas (id_usuario, tipo_reserva, fecha, hora_inicio, hora_fin, estado, id_laboratorio, id_computadora, curso_academico, hora_ingreso_real) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = ConexionBD.getInstance().getConnection();
            conn.setAutoCommit(false); // Inicia la transacción

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // 1. Datos comunes a cualquier reserva
                stmt.setInt(1, reserva.getIdUsuario());
                stmt.setDate(3, Date.valueOf(reserva.getFecha()));
                stmt.setInt(4, reserva.getHoraInicio());
                stmt.setInt(5, reserva.getHoraFin());
                stmt.setString(6, reserva.getEstado().name()); // .name() convierte el Enum a String
                stmt.setInt(7, reserva.getIdLaboratorio());
                
                // 2. Polimorfismo: ¿Es reserva de PC o de Laboratorio?
                if (reserva instanceof ReservaComputadora) {
                    ReservaComputadora rc = (ReservaComputadora) reserva;
                    stmt.setString(2, "COMPUTADORA");
                    stmt.setInt(8, rc.getIdComputadora());
                    stmt.setNull(9, java.sql.Types.VARCHAR);
                } else if (reserva instanceof ReservaLaboratorio) {
                    ReservaLaboratorio rl = (ReservaLaboratorio) reserva;
                    stmt.setString(2, "LABORATORIO");
                    stmt.setNull(8, java.sql.Types.INTEGER);
                    stmt.setString(9, rl.getCursoAcademico());
                }

                // 3. Al crear, la hora de ingreso real nace en NULL
                stmt.setNull(10, java.sql.Types.TIMESTAMP);

                int filas = stmt.executeUpdate();
                conn.commit(); // Confirma la transacción
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar reserva, revirtiendo: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Reserva> findReservasPorLaboratorio(int idLaboratorio, LocalDate fecha) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM tbl_reservas WHERE id_laboratorio = ? AND fecha = ?";
        Connection conn = ConexionBD.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        // Ahora buscamos contra el Enum en texto
        String sql = "SELECT * FROM tbl_reservas WHERE id_usuario = ? AND estado = 'APROBADA'";
        Connection conn = ConexionBD.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    @Override
    public boolean registrarIngresoReal(int idReserva) {
        // Este UPDATE es el equivalente a hacer un INSERT en la tabla que eliminaron
        String sql = "UPDATE tbl_reservas SET hora_ingreso_real = NOW() WHERE id_reserva = ?";
        Connection conn = ConexionBD.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar el ingreso real: " + e.getMessage());
            return false;
        }
    }

    // El corazón del polimorfismo al leer datos
    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo_reserva");
        Reserva reserva;

        // 1. Instanciamos al hijo correspondiente
        if ("COMPUTADORA".equalsIgnoreCase(tipo)) {
            ReservaComputadora rc = new ReservaComputadora();
            rc.setIdComputadora(rs.getInt("id_computadora"));
            reserva = rc;
        } else {
            ReservaLaboratorio rl = new ReservaLaboratorio();
            rl.setCursoAcademico(rs.getString("curso_academico"));
            reserva = rl;
        }

        // 2. Llenamos los datos genéricos del padre
        reserva.setIdReserva(rs.getInt("id_reserva"));
        reserva.setIdUsuario(rs.getInt("id_usuario"));
        reserva.setFecha(rs.getDate("fecha").toLocalDate());
        reserva.setHoraInicio(rs.getInt("hora_inicio"));
        reserva.setHoraFin(rs.getInt("hora_fin"));
        reserva.setEstado(EstadoReserva.valueOf(rs.getString("estado"))); // String a Enum
        reserva.setIdLaboratorio(rs.getInt("id_laboratorio"));

        // 3. Manejamos la hora de ingreso (que podría ser nula)
        Timestamp ingreso = rs.getTimestamp("hora_ingreso_real");
        if (ingreso != null) {
            reserva.setHoraIngresoReal(ingreso.toLocalDateTime());
        }

        return reserva;
    }
}