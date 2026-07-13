package pe.edu.unmsm.fisi.service;

import pe.edu.unmsm.fisi.model.entity.Computadora;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.ReservaComputadora;
import pe.edu.unmsm.fisi.model.entity.ReservaLaboratorio;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.model.enums.EstadoEquipo;
import pe.edu.unmsm.fisi.model.enums.EstadoReserva;

import java.time.LocalDate;
import java.util.List;

import pe.edu.unmsm.fisi.repository.EquipoRepository;
import pe.edu.unmsm.fisi.repository.EquipoRepositoryImpl;
import pe.edu.unmsm.fisi.repository.ReservaRepository;
import pe.edu.unmsm.fisi.repository.ReservaRepositoryImpl;

public class ReservaServiceImpl implements ReservaService {

    private final EquipoRepository equipoRepo;
    private final ReservaRepository reservaRepo;

    public ReservaServiceImpl() {
        this.equipoRepo = new EquipoRepositoryImpl();
        this.reservaRepo = new ReservaRepositoryImpl();
    }

    @Override
    public ReservaComputadora solicitarAsignacionAutomaticaAlumno(Usuario alumno, int idLaboratorio, String requerimientoSoftware) {
        
        System.out.println("Iniciando Algoritmo Voraz (Greedy) para asignación de PC...");
        
        // 1. OBTENER ESPACIO DE BÚSQUEDA
        List<Computadora> computadorasLab = equipoRepo.listarPorLaboratorio(idLaboratorio);
        Computadora pcAsignada = null;

        // 2. LÓGICA VORAZ (Greedy Choice Property)
        for (Computadora pc : computadorasLab) {
            // Validamos contra el Enum
            if (EstadoEquipo.DISPONIBLE.name().equalsIgnoreCase(pc.getEstado())) {
                pcAsignada = pc;
                break; // ¡Atrapada! Optimizamos recursos.
            }
        }

        // 3. VALIDACIÓN DE RECURSOS
        if (pcAsignada == null) {
            System.err.println("Rechazo: No hay computadoras disponibles en el Laboratorio " + idLaboratorio);
            return null;
        }

        System.out.println("PC Óptima encontrada: " + pcAsignada.getCodigoPc());

        // 4. CONSTRUCCIÓN DEL OBJETO POLIMÓRFICO
        ReservaComputadora nuevaReserva = new ReservaComputadora();
        nuevaReserva.setIdUsuario(alumno.getIdUsuario());
        nuevaReserva.setFecha(LocalDate.now());
        nuevaReserva.setHoraInicio(1400); // Simulación
        nuevaReserva.setHoraFin(1600); // Simulación
        nuevaReserva.setEstado(EstadoReserva.PENDIENTE); // PENDIENTE hasta que inicie sesión real
        nuevaReserva.setIdLaboratorio(idLaboratorio);
        nuevaReserva.setIdComputadora(pcAsignada.getIdComputadora());
        nuevaReserva.setRequerimientoSoftware(requerimientoSoftware); 

        // 5. TRANSACCIÓN HACIA LA BASE DE DATOS
        boolean guardadoOk = reservaRepo.save(nuevaReserva);
        if(guardadoOk) {
             // equipoRepo.cambiarEstado(pcAsignada.getIdComputadora(), EstadoEquipo.OCUPADA.name());
             System.out.println("¡Asignación exitosa! La " + pcAsignada.getCodigoPc() + " ha sido reservada para el alumno.");
             return nuevaReserva;
        } else {
             System.err.println("Error al guardar la reserva en la BD.");
             return null;
        }
    }

    @Override
    public boolean procesarReservaDocente(Usuario profesor, int idLaboratorio, int horaInicio, int horaFin, String curso) {
        
        System.out.println("\n--- Iniciando Interval Scheduling para Reserva Docente ---");

        // 1. VALIDACIÓN DE COHERENCIA DE DATOS
        if (horaInicio >= horaFin) {
            System.err.println("Error: La hora de inicio no puede ser mayor o igual a la hora de fin.");
            return false;
        }

        // 2. OBTENER ESPACIO DE BÚSQUEDA
        List<Reserva> reservasLaboratorio = reservaRepo.findReservasPorLaboratorio(idLaboratorio, LocalDate.now());
        
        // 3. LÓGICA MATEMÁTICA: Detección de Colisiones (Overlap)
        boolean choqueDetectado = false;
        for (Reserva existente : reservasLaboratorio) {
            if (horaInicio < existente.getHoraFin() && horaFin > existente.getHoraInicio()) {
                // Validamos que el choque sea solo con otras clases
                if(existente instanceof ReservaLaboratorio) {
                     ReservaLaboratorio rl = (ReservaLaboratorio) existente;
                     System.err.println("¡Choque de horarios detectado! El laboratorio está ocupado de "
                        + existente.getHoraInicio() + " a " + existente.getHoraFin()
                        + " por el curso de " + rl.getCursoAcademico());
                     choqueDetectado = true;
                     break;
                }
            }
        }

        // 4. RESOLUCIÓN DE LA TRANSACCIÓN
        if (choqueDetectado) {
            System.err.println("Transacción rechazada para proteger la integridad del horario.");
            return false; 
        }

        System.out.println("Validación matemática superada: La franja horaria está completamente libre.");

        // 5. CONSTRUCCIÓN DE LA NUEVA RESERVA POLIMÓRFICA
        ReservaLaboratorio nuevaReserva = new ReservaLaboratorio();
        nuevaReserva.setIdUsuario(profesor.getIdUsuario());
        nuevaReserva.setFecha(LocalDate.now());
        nuevaReserva.setHoraInicio(horaInicio);
        nuevaReserva.setHoraFin(horaFin);
        nuevaReserva.setEstado(EstadoReserva.APROBADA); // La reserva del profe se aprueba de inmediato
        nuevaReserva.setIdLaboratorio(idLaboratorio);
        nuevaReserva.setCursoAcademico(curso);

        // 6. PERSISTENCIA
        boolean guardadoOk = reservaRepo.save(nuevaReserva);
        if (guardadoOk) {
            System.out.println("¡Reserva Docente Exitosa! Laboratorio " + idLaboratorio + " asignado para " + curso + " de " + horaInicio + " a " + horaFin);
            return true;
        } else {
            System.out.println("Error: No se pudo guardar la reserva en la base de datos.");
            return false;
        }
    }
}