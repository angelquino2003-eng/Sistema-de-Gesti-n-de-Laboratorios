package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.model.entity.Reserva;
import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository {
    boolean save(Reserva reserva);
    List<Reserva> findReservasPorLaboratorio(int idLaboratorio, LocalDate fecha);
    List<Reserva> findReservasActivasPorUsuario(int idUsuario);
}