package pe.edu.unmsm.fisi.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.ReservaComputadora;
import pe.edu.unmsm.fisi.model.entity.ReservaLaboratorio;
import pe.edu.unmsm.fisi.model.enums.EstadoReserva; // Obligatorio para evitar Strings quemados [cite: 185-187]

public class ReservaRepositoryImpl implements ReservaRepository {

    @Override
    public boolean save(Reserva reserva) {
        String sql = "INSERT INTO tbl_reservas (id_usuario, tipo_reserva, fecha, hora_inicio, hora_fin, estado, id_laboratorio, id_computadora, curso_academico, hora_ingreso_real) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Obtenemos la conexión del Singleton sin el try-with-resources para no cerrarla accidentalmente
        Connection conn = ConexionBD.getInstance().getConnection();
        
        try {
            conn.setAutoCommit(false); // Inicia la transacción manual
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, reserva.getIdUsuario());
                stmt.setDate(3, Date.valueOf(reserva.getFecha()));
                stmt.setInt(4, reserva.getHoraInicio());
                stmt.setInt(5, reserva.getHoraFin());
                stmt.setString(6, reserva.getEstado().name()); 
                stmt.setInt(7, reserva.getIdLaboratorio());
                
                // Polimorfismo: Detectamos qué tipo de reserva es [cite: 178-180]
                if (reserva instanceof ReservaComputadora) {
                    ReservaComputadora rc = (ReservaComputadora) reserva;
                    stmt.setString(2, "COMPUTADORA");
                    stmt.setInt(8, rc.getIdComputadora());
                    stmt.setNull(9, Types.VARCHAR);
                } else if (reserva instanceof ReservaLaboratorio) {
                    ReservaLaboratorio rl = (ReservaLaboratorio) reserva;
                    stmt.setString(2, "LABORATORIO");
                    stmt.setNull(8, Types.INTEGER);
                    stmt.setString(9, rl.getCursoAcademico());
                }

                // La hora de ingreso real nace en NULL al programar la reserva
                stmt.setNull(10, Types.TIMESTAMP);

                int filas = stmt.executeUpdate();
                conn.commit(); 
                return filas > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ReservaRepository] Error al guardar reserva: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("[ReservaRepository] Error al hacer rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("[ReservaRepository] Error al restaurar autocommit: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Reserva> findReservasPorLaboratorio(int idLaboratorio, LocalDate fecha) {
        String sql = "SELECT * FROM tbl_reservas WHERE id_laboratorio = ? AND fecha = ? AND estado IN ('ACTIVA','APROBADA') ORDER BY hora_inicio";
        List<Reserva> lista = new ArrayList<>();
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
            System.err.println("[ReservaRepository] Error en findReservasPorLaboratorio: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Reserva> findReservasActivasPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM tbl_reservas WHERE id_usuario = ? AND estado IN ('ACTIVA','APROBADA') ORDER BY fecha DESC, hora_inicio";
        List<Reserva> lista = new ArrayList<>();
        Connection conn = ConexionBD.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearReserva(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[ReservaRepository] Error en findReservasActivasPorUsuario: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean registrarIngresoReal(int idReserva) {
        // Ejecución delegada a MySQL con CURRENT_TIMESTAMP para la v2
        String sql = "UPDATE tbl_reservas SET hora_ingreso_real = CURRENT_TIMESTAMP WHERE id_reserva = ?";
        Connection conn = ConexionBD.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ReservaRepository] Error al registrar ingreso real: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Reserva> findTodas() {
        String sql = "SELECT * FROM tbl_reservas ORDER BY fecha DESC, hora_inicio";
        List<Reserva> lista = new ArrayList<>();
        Connection conn = ConexionBD.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearReserva(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ReservaRepository] Error al listar todas las reservas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizarEstado(int idReserva, EstadoReserva estado) {
        String sql = "UPDATE tbl_reservas SET estado = ? WHERE id_reserva = ?";
        Connection conn = ConexionBD.getInstance().getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estado.name());
            stmt.setInt(2, idReserva);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ReservaRepository] Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }

    // Método central de mapeo para evitar duplicidad de código
    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo_reserva");
        Reserva reserva;

        // Instanciamos a la clase hija correcta
        if ("COMPUTADORA".equalsIgnoreCase(tipo)) {
            ReservaComputadora rc = new ReservaComputadora();
            rc.setIdComputadora(rs.getInt("id_computadora"));
            reserva = rc;
        } else {
            ReservaLaboratorio rl = new ReservaLaboratorio();
            rl.setCursoAcademico(rs.getString("curso_academico"));
            reserva = rl;
        }

        // Llenamos los atributos heredados del padre
        reserva.setIdReserva(rs.getInt("id_reserva"));
        reserva.setIdUsuario(rs.getInt("id_usuario"));
        reserva.setFecha(rs.getDate("fecha").toLocalDate());
        reserva.setHoraInicio(rs.getInt("hora_inicio"));
        reserva.setHoraFin(rs.getInt("hora_fin"));
        reserva.setEstado(EstadoReserva.valueOf(rs.getString("estado")));
        reserva.setIdLaboratorio(rs.getInt("id_laboratorio"));

        // Manejo seguro del TIMESTAMP que podría ser nulo
        Timestamp ingreso = rs.getTimestamp("hora_ingreso_real");
        if (ingreso != null) {
            reserva.setHoraIngresoReal(ingreso.toLocalDateTime());
        }

        return reserva;
    }
}