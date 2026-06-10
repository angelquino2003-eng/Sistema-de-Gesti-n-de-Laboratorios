package pe.edu.unmsm.fisi.service;

import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.Usuario;

public interface ReservaService {
    
    // Algoritmo Voraz (Greedy) para asignación rápida a estudiantes
    Reserva solicitarAsignacionAutomaticaAlumno(Usuario alumno, int idLaboratorio, String cursoAcademico);
    
    // Interval Scheduling para validación de horarios de docentes (Fase posterior)
    boolean procesarReservaDocente(Usuario profesor, int idLaboratorio, int horaInicio, int horaFin, String curso);
}