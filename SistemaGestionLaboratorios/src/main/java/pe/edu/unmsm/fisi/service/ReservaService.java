package pe.edu.unmsm.fisi.service;

import java.time.LocalDate;
import pe.edu.unmsm.fisi.model.entity.ReservaComputadora;
import pe.edu.unmsm.fisi.model.entity.Usuario;

public interface ReservaService {
    
    // Algoritmo Voraz (Greedy) para asignación inmediata y óptima de PC a estudiantes.
    // Ocurre en tiempo real, por lo que no requiere parámetros de fecha/hora externos.
    ReservaComputadora solicitarAsignacionAutomaticaAlumno(Usuario alumno, int idLaboratorio, String requerimientoSoftware);

    // Interval Scheduling para prevenir colisiones matemáticas en reservas de docentes.
    // Se añade LocalDate 'fecha' obligatorio para evaluar conflictos en un día específico.
    boolean procesarReservaDocente(Usuario profesor, int idLaboratorio, LocalDate fecha, int horaInicio, int horaFin, String curso);
}