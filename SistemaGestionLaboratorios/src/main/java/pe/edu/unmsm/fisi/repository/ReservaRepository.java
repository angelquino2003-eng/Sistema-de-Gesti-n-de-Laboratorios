package pe.edu.unmsm.fisi.repository;

import java.time.LocalDate;
import java.util.List;
import pe.edu.unmsm.fisi.model.entity.Reserva;

public interface ReservaRepository {
    boolean save(Reserva reserva);
    List<Reserva> findReservasPorLaboratorio(int idLaboratorio, LocalDate fecha);
    List<Reserva> findReservasActivasPorUsuario(int idUsuario);
    List<Reserva> findTodas();
    boolean actualizarEstado(int idReserva, String estado);
}
