package pe.edu.unmsm.fisi.service;

import pe.edu.unmsm.fisi.model.entity.ReservaComputadora;
import pe.edu.unmsm.fisi.model.entity.Usuario;

public interface ReservaService {
    
    // Algoritmo Voraz (Greedy) para asignación rápida a estudiantes [cite: 74]
    ReservaComputadora solicitarAsignacionAutomaticaAlumno(Usuario alumno, int idLaboratorio, String requerimientoSoftware);

    // Interval Scheduling para validación de horarios de docentes [cite: 75]
    boolean procesarReservaDocente(Usuario profesor, int idLaboratorio, int horaInicio, int horaFin, String curso);
}