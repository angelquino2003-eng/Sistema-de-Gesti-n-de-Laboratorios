package pe.edu.unmsm.fisi.service;

import pe.edu.unmsm.fisi.model.entity.Computadora;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import java.time.LocalDate;
import java.util.List;

// Importamos los repositorios (Adrián creará las implementaciones reales de estas interfaces)
import pe.edu.unmsm.fisi.repository.EquipoRepository;
import pe.edu.unmsm.fisi.repository.EquipoRepositoryImpl;
import pe.edu.unmsm.fisi.repository.ReservaRepository;
import pe.edu.unmsm.fisi.repository.ReservaRepositoryImpl;

public class ReservaServiceImpl implements ReservaService {

    // Dependencias hacia la capa de datos (Adrián)
    private final EquipoRepository equipoRepo;
    private final ReservaRepository reservaRepo;

    public ReservaServiceImpl() {
        // Cuando Adrián termine su parte en la Semana 10 y 11, descomentaremos esto:
        this.equipoRepo = new EquipoRepositoryImpl();
        this.reservaRepo = new ReservaRepositoryImpl();
    }

    @Override
    public Reserva solicitarAsignacionAutomaticaAlumno(Usuario alumno, int idLaboratorio, String cursoAcademico) {
        
        System.out.println("Iniciando Algoritmo Voraz (Greedy) para asignación de PC...");
        
        // 1. OBTENER ESPACIO DE BÚSQUEDA
        // Le pedimos a la BD de Adrián TODAS las PCs de ese laboratorio específico
        List<Computadora> computadorasLab = equipoRepo.listarPorLaboratorio(idLaboratorio);
        
        // SIMULACIÓN (Borrar cuando Adrián termine su repositorio):
        /* List<Computadora> computadorasLab = List.of(
            new Computadora(1, "PC-01", "OCUPADA", idLaboratorio),
            new Computadora(2, "PC-02", "MANTENIMIENTO", idLaboratorio),
            new Computadora(3, "PC-03", "DISPONIBLE", idLaboratorio),
            new Computadora(4, "PC-04", "DISPONIBLE", idLaboratorio)
        ); */

        Computadora pcAsignada = null;

        // 2. LÓGICA VORAZ (Greedy Choice Property)
        // El algoritmo itera buscando la decisión óptima local (la primera PC DISPONIBLE).
        // Apenas la encuentra, rompe el bucle para ahorrar tiempo de procesamiento (O(n) en el peor caso).
        for (Computadora pc : computadorasLab) {
            if ("DISPONIBLE".equalsIgnoreCase(pc.getEstado())) {
                pcAsignada = pc;
                break; // ¡Atrapada! No seguimos buscando, optimizamos recursos.
            }
        }

        // 3. VALIDACIÓN DE RECURSOS
        if (pcAsignada == null) {
            System.err.println("Rechazo: No hay computadoras disponibles en el Laboratorio " + idLaboratorio);
            return null;
        }

        System.out.println("PC Óptima encontrada: " + pcAsignada.getCodigoPc());

        // 4. CONSTRUCCIÓN DEL OBJETO RESERVA
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setIdUsuario(alumno.getIdUsuario());
        nuevaReserva.setTipoReserva("ALUMNO");
        nuevaReserva.setFecha(LocalDate.now());
        // Simulamos que la reserva es para la hora actual (ej. 14:00 a 16:00)
        nuevaReserva.setHoraInicio(1400); 
        nuevaReserva.setHoraFin(1600);
        nuevaReserva.setEstado("ACTIVA");
        nuevaReserva.setIdLaboratorio(idLaboratorio);
        nuevaReserva.setIdComputadora(pcAsignada.getIdComputadora());
        nuevaReserva.setCursoAcademico(cursoAcademico);

        // 5. TRANSSACCIÓN HACIA LA BASE DE DATOS
        // Aquí llamaríamos a los repositorios de Adrián para guardar la reserva y bloquear la PC
        reservaRepo.save(nuevaReserva);
        equipoRepo.cambiarEstado(pcAsignada.getIdComputadora(), "OCUPADA");
        
        System.out.println("¡Asignación exitosa! La " + pcAsignada.getCodigoPc() + " ha sido bloqueada para el alumno.");

        return nuevaReserva;
    }

    @Override
    public boolean procesarReservaDocente(Usuario profesor, int idLaboratorio, int horaInicio, int horaFin, String curso) {
        
        System.out.println("\n--- Iniciando Interval Scheduling para Reserva Docente ---");

        // 1. VALIDACIÓN DE COHERENCIA DE DATOS
        if (horaInicio >= horaFin) {
            System.err.println("Error: La hora de inicio no puede ser mayor o igual a la hora de fin.");
            return false;
        }

        // 2. OBTENER RESERVAS EXISTENTES DEL DÍA (El espacio de búsqueda)
        List<Reserva> reservasLaboratorio = reservaRepo.findReservasPorLaboratorio(idLaboratorio, LocalDate.now());
        
        // SIMULACIÓN: Imaginemos que la base de datos nos dice que el Laboratorio ya tiene estas reservas hoy:
        /* List<Reserva> reservasLaboratorio = List.of(
            // Reserva de 08:00 a 10:00 para Base de Datos
            new Reserva(1, 99, "PROFESOR", LocalDate.now(), 800, 1000, "ACTIVA", idLaboratorio, 0, "Base de Datos"),
            // Reserva de 14:00 a 16:00 para Algorítmica I
            new Reserva(2, 98, "PROFESOR", LocalDate.now(), 1400, 1600, "ACTIVA", idLaboratorio, 0, "Algorítmica I")
        ); */

        // 3. LÓGICA MATEMÁTICA: Detección de Colisiones (Overlap)
        boolean choqueDetectado = false;
        
        for (Reserva existente : reservasLaboratorio) {
            // Aplicamos la fórmula matemática: A < D && B > C
            if (horaInicio < existente.getHoraFin() && horaFin > existente.getHoraInicio()) {
                System.err.println("¡Choque de horarios detectado! El laboratorio está ocupado de " 
                        + existente.getHoraInicio() + " a " + existente.getHoraFin() 
                        + " por el curso de " + existente.getCursoAcademico());
                choqueDetectado = true;
                break; // Detenemos la búsqueda para ahorrar recursos
            }
        }

        // 4. RESOLUCIÓN DE LA TRANSACCIÓN
        if (choqueDetectado) {
            System.err.println("Transacción rechazada para proteger la integridad del horario.");
            return false; 
        }

        System.out.println("Validación matemática superada: La franja horaria está completamente libre.");
        
        // 5. CONSTRUCCIÓN DE LA NUEVA RESERVA
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setIdUsuario(profesor.getIdUsuario());
        nuevaReserva.setTipoReserva("PROFESOR");
        nuevaReserva.setFecha(LocalDate.now());
        nuevaReserva.setHoraInicio(horaInicio);
        nuevaReserva.setHoraFin(horaFin);
        nuevaReserva.setEstado("ACTIVA");
        nuevaReserva.setIdLaboratorio(idLaboratorio);
        // ID Computadora = 0 indica que se reservó el cuarto completo, no una máquina individual
        nuevaReserva.setIdComputadora(0); 
        nuevaReserva.setCursoAcademico(curso);

        // PERSISTENCIA (A la espera de Adrián)
        reservaRepo.save(nuevaReserva);

        System.out.println("¡Reserva Docente Exitosa! Laboratorio " + idLaboratorio + " asignado para " + curso + " de " + horaInicio + " a " + horaFin);
        
        return true;
    }
}