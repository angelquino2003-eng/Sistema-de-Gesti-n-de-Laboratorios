package pe.edu.unmsm.fisi.service;

import java.time.LocalDate;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.Usuario;

public interface ReservaService {
    Reserva solicitarAsignacionAutomaticaAlumno(Usuario alumno, int idLaboratorio, String cursoAcademico);

    Reserva solicitarAsignacionAutomaticaAlumno(
            Usuario alumno,
            int idLaboratorio,
            String cursoAcademico,
            LocalDate fecha,
            int horaInicio,
            int horaFin
    );

    boolean procesarReservaDocente(
            Usuario profesor,
            int idLaboratorio,
            int horaInicio,
            int horaFin,
            String curso
    );

    boolean procesarReservaDocente(
            Usuario profesor,
            int idLaboratorio,
            LocalDate fecha,
            int horaInicio,
            int horaFin,
            String curso
    );
}
