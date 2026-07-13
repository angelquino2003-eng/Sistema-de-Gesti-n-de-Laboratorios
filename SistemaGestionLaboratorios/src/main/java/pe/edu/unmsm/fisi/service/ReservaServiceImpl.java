package pe.edu.unmsm.fisi.service;

import java.time.LocalDate;
import java.util.List;

import pe.edu.unmsm.fisi.model.entity.Computadora;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.ReservaComputadora;
import pe.edu.unmsm.fisi.model.entity.ReservaLaboratorio;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.model.enums.EstadoEquipo;
import pe.edu.unmsm.fisi.model.enums.EstadoReserva;
import pe.edu.unmsm.fisi.model.enums.Rol;
import pe.edu.unmsm.fisi.repository.EquipoRepository;
import pe.edu.unmsm.fisi.repository.EquipoRepositoryImpl;
import pe.edu.unmsm.fisi.repository.ReservaRepository;
import pe.edu.unmsm.fisi.repository.ReservaRepositoryImpl;
import pe.edu.unmsm.fisi.exception.EntradaInvalidaException;
import pe.edu.unmsm.fisi.exception.ReservaSolapadaException;

public class ReservaServiceImpl implements ReservaService {

    private final EquipoRepository equipoRepo;
    private final ReservaRepository reservaRepo;

    public ReservaServiceImpl() {
        this.equipoRepo = new EquipoRepositoryImpl();
        this.reservaRepo = new ReservaRepositoryImpl();
    }

    @Override
    public ReservaComputadora solicitarAsignacionAutomaticaAlumno(Usuario alumno, int idLaboratorio, String requerimientoSoftware, int horaInicio, int horaFin) {
        validarUsuario(alumno, Rol.ALUMNO);

        // 1. OBTENER ESPACIO DE BÚSQUEDA
        List<Computadora> computadorasLab = equipoRepo.listarPorLaboratorio(idLaboratorio);

        // 2. LÓGICA VORAZ (Greedy Choice Property)
        Computadora pcAsignada = computadorasLab.stream()
                .filter(pc -> EstadoEquipo.DISPONIBLE.name().equalsIgnoreCase(pc.getEstado()))
                .findFirst()
                .orElseThrow(() -> new EntradaInvalidaException("No hay computadoras disponibles en el Laboratorio " + idLaboratorio));

        // 3. CONSTRUCCIÓN DEL OBJETO POLIMÓRFICO
        ReservaComputadora nuevaReserva = new ReservaComputadora();
        nuevaReserva.setIdUsuario(alumno.getIdUsuario());
        nuevaReserva.setFecha(LocalDate.now());
        nuevaReserva.setHoraInicio(horaInicio);
        nuevaReserva.setHoraFin(horaFin);
        nuevaReserva.setHoraInicio(1400); // Hora de simulación
        nuevaReserva.setHoraFin(1600);    // Hora de simulación
        nuevaReserva.setEstado(EstadoReserva.PENDIENTE);
        nuevaReserva.setIdLaboratorio(idLaboratorio);
        nuevaReserva.setIdComputadora(pcAsignada.getIdComputadora());
        nuevaReserva.setRequerimientoSoftware(requerimientoSoftware); 

        // 4. TRANSACCIÓN HACIA LA BASE DE DATOS
        if (reservaRepo.save(nuevaReserva)) {
             // Bloqueamos el equipo inmediatamente para evitar condiciones de carrera
             equipoRepo.cambiarEstado(pcAsignada.getIdComputadora(), EstadoEquipo.OCUPADO.name());
             return nuevaReserva;
        } else {
             throw new EntradaInvalidaException("Error crítico al guardar la reserva en la base de datos.");
        }
    }

    @Override
    public boolean procesarReservaDocente(Usuario profesor, int idLaboratorio, LocalDate fecha, int horaInicio, int horaFin, String curso) {
        validarUsuario(profesor, Rol.PROFESOR);
        validarDatosReserva(idLaboratorio, curso, fecha, horaInicio, horaFin);

        // 1. ESPACIO DE BÚSQUEDA DE HORARIOS
        List<Reserva> reservasLaboratorio = reservaRepo.findReservasPorLaboratorio(idLaboratorio, fecha);
        
        // 2. LÓGICA MATEMÁTICA: Interval Scheduling
        for (Reserva existente : reservasLaboratorio) {
            if (haySolapamiento(horaInicio, horaFin, existente.getHoraInicio(), existente.getHoraFin())) {
                
                String nombreCurso = "Clase programada";
                if (existente instanceof ReservaLaboratorio) {
                    nombreCurso = ((ReservaLaboratorio) existente).getCursoAcademico();
                }
                
                throw new ReservaSolapadaException(
                        "Choque de horario detectado con el curso '" + nombreCurso + 
                        "' de " + formatearHora(existente.getHoraInicio()) + 
                        " a " + formatearHora(existente.getHoraFin())
                );
            }
        }

        // 3. CONSTRUCCIÓN DE LA RESERVA
        ReservaLaboratorio nuevaReserva = new ReservaLaboratorio();
        nuevaReserva.setIdUsuario(profesor.getIdUsuario());
        nuevaReserva.setFecha(fecha);
        nuevaReserva.setHoraInicio(horaInicio);
        nuevaReserva.setHoraFin(horaFin);
        nuevaReserva.setEstado(EstadoReserva.APROBADA); // Docente tiene aprobación inmediata
        nuevaReserva.setIdLaboratorio(idLaboratorio);
        nuevaReserva.setCursoAcademico(curso);

        // 4. PERSISTENCIA
        if (!reservaRepo.save(nuevaReserva)) {
            throw new EntradaInvalidaException("No se pudo guardar la reserva docente.");
        }
        return true;
    }

    // --- MÉTODOS AUXILIARES DE VALIDACIÓN (Rescatados de Rodrigo) ---

    private void validarUsuario(Usuario usuario, Rol rolEsperado) {
        if (usuario == null) {
            throw new EntradaInvalidaException("No existe una sesión activa.");
        }
        if (usuario.getRol() != rolEsperado) {
            throw new EntradaInvalidaException("El usuario no tiene el rol necesario para esta operación.");
        }
    }

    private void validarDatosReserva(int idLaboratorio, String curso, LocalDate fecha, int inicio, int fin) {
        if (idLaboratorio <= 0) {
            throw new EntradaInvalidaException("El laboratorio seleccionado no es válido.");
        }
        if (curso == null || curso.isBlank()) {
            throw new EntradaInvalidaException("Debe indicar el curso o motivo de la reserva.");
        }
        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            throw new EntradaInvalidaException("La fecha no puede ser anterior a hoy.");
        }
        if (!esHoraValida(inicio) || !esHoraValida(fin) || inicio >= fin) {
            throw new EntradaInvalidaException("El rango horario ingresado no es válido (Ej: 1400 a 1600).");
        }
    }

    private boolean esHoraValida(int hora) {
        int horas = hora / 100;
        int minutos = hora % 100;
        return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59;
    }

    // Aplicación matemática para exclusión mutua de intervalos
    private boolean haySolapamiento(int inicioA, int finA, int inicioB, int finB) {
        return inicioA < finB && finA > inicioB;
    }

    private String formatearHora(int hora) {
        return String.format("%02d:%02d", hora / 100, hora % 100);
    }
}