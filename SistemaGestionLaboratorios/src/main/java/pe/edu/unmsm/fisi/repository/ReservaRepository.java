package pe.edu.unmsm.fisi.repository;

import java.time.LocalDate;
import java.util.List;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.enums.EstadoReserva; // Obligamos a respetar el diccionario de datos

public interface ReservaRepository {
    
    boolean save(Reserva reserva);
    
    List<Reserva> findReservasPorLaboratorio(int idLaboratorio, LocalDate fecha);
    
    List<Reserva> findReservasActivasPorUsuario(int idUsuario);
    
    // Mapea directamente a la actualización del campo 'hora_ingreso_real' en tbl_reservas (EER v2)
    boolean registrarIngresoReal(int idReserva);

    // Requerimientos de Rodrigo adaptados a nuestra arquitectura limpia
    List<Reserva> findTodas();
    
    // Cambiamos su 'String' por el Enum para blindar la base de datos
    boolean actualizarEstado(int idReserva, EstadoReserva estado);
}