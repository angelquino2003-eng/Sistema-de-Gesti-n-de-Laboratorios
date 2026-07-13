package pe.edu.unmsm.fisi.service;

import java.time.LocalDate;
import java.util.List;
import pe.edu.unmsm.fisi.model.entity.Computadora;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.model.enums.Rol;
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
    public Reserva solicitarAsignacionAutomaticaAlumno(Usuario alumno, int idLaboratorio, String cursoAcademico) {
        return solicitarAsignacionAutomaticaAlumno(
                alumno,
                idLaboratorio,
                cursoAcademico,
                LocalDate.now(),
                1400,
                1600
        );
    }

    @Override
    public Reserva solicitarAsignacionAutomaticaAlumno(
            Usuario alumno,
            int idLaboratorio,
            String cursoAcademico,
            LocalDate fecha,
            int horaInicio,
            int horaFin
    ) {
        validarUsuario(alumno, Rol.ALUMNO);
        validarDatosReserva(idLaboratorio, cursoAcademico, fecha, horaInicio, horaFin);
        if (!LocalDate.now().equals(fecha)) {
            throw new IllegalArgumentException("La asignación inmediata de una PC solo puede realizarse para la fecha actual.");
        }

        List<Reserva> reservasLaboratorio = reservaRepo.findReservasPorLaboratorio(idLaboratorio, fecha);
        for (Reserva reserva : reservasLaboratorio) {
            if ("PROFESOR".equalsIgnoreCase(reserva.getTipoReserva())
                    && haySolapamiento(horaInicio, horaFin, reserva.getHoraInicio(), reserva.getHoraFin())) {
                throw new IllegalStateException(
                        "El laboratorio está reservado por el curso " + reserva.getCursoAcademico()
                        + " de " + formatearHora(reserva.getHoraInicio())
                        + " a " + formatearHora(reserva.getHoraFin()) + "."
                );
            }
        }

        for (Reserva reserva : reservaRepo.findReservasActivasPorUsuario(alumno.getIdUsuario())) {
            if (fecha.equals(reserva.getFecha())
                    && haySolapamiento(horaInicio, horaFin, reserva.getHoraInicio(), reserva.getHoraFin())) {
                throw new IllegalStateException("El alumno ya tiene una reserva que se cruza con el horario solicitado.");
            }
        }

        Computadora pcAsignada = equipoRepo.listarPorLaboratorio(idLaboratorio)
                .stream()
                .filter(pc -> "DISPONIBLE".equalsIgnoreCase(pc.getEstado()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay computadoras disponibles en el laboratorio seleccionado."));

        Reserva reserva = construirReserva(
                alumno.getIdUsuario(), "ALUMNO", fecha, horaInicio, horaFin,
                idLaboratorio, pcAsignada.getIdComputadora(), cursoAcademico
        );

        if (!reservaRepo.save(reserva)) {
            throw new IllegalStateException("La reserva no pudo registrarse.");
        }
        if (!equipoRepo.cambiarEstado(pcAsignada.getIdComputadora(), "OCUPADA")) {
            throw new IllegalStateException("La reserva se registró, pero no se pudo actualizar el estado de la computadora.");
        }
        return reserva;
    }

    @Override
    public boolean procesarReservaDocente(
            Usuario profesor,
            int idLaboratorio,
            int horaInicio,
            int horaFin,
            String curso
    ) {
        return procesarReservaDocente(
                profesor,
                idLaboratorio,
                LocalDate.now(),
                horaInicio,
                horaFin,
                curso
        );
    }

    @Override
    public boolean procesarReservaDocente(
            Usuario profesor,
            int idLaboratorio,
            LocalDate fecha,
            int horaInicio,
            int horaFin,
            String curso
    ) {
        validarUsuario(profesor, Rol.PROFESOR);
        validarDatosReserva(idLaboratorio, curso, fecha, horaInicio, horaFin);

        for (Reserva existente : reservaRepo.findReservasPorLaboratorio(idLaboratorio, fecha)) {
            if (haySolapamiento(horaInicio, horaFin, existente.getHoraInicio(), existente.getHoraFin())) {
                throw new IllegalStateException(
                        "Choque de horario con la reserva de " + formatearHora(existente.getHoraInicio())
                        + " a " + formatearHora(existente.getHoraFin())
                        + " para " + existente.getCursoAcademico() + "."
                );
            }
        }

        Reserva reserva = construirReserva(
                profesor.getIdUsuario(), "PROFESOR", fecha, horaInicio, horaFin,
                idLaboratorio, 0, curso
        );
        if (!reservaRepo.save(reserva)) {
            throw new IllegalStateException("No se pudo guardar la reserva docente.");
        }
        return true;
    }

    private Reserva construirReserva(
            int idUsuario,
            String tipo,
            LocalDate fecha,
            int inicio,
            int fin,
            int idLaboratorio,
            int idComputadora,
            String curso
    ) {
        Reserva reserva = new Reserva();
        reserva.setIdUsuario(idUsuario);
        reserva.setTipoReserva(tipo);
        reserva.setFecha(fecha);
        reserva.setHoraInicio(inicio);
        reserva.setHoraFin(fin);
        reserva.setEstado("ACTIVA");
        reserva.setIdLaboratorio(idLaboratorio);
        reserva.setIdComputadora(idComputadora);
        reserva.setCursoAcademico(curso.trim());
        return reserva;
    }

    private void validarUsuario(Usuario usuario, Rol rolEsperado) {
        if (usuario == null) {
            throw new IllegalArgumentException("No existe una sesión activa.");
        }
        if (usuario.getRol() != rolEsperado) {
            throw new IllegalArgumentException("El usuario no tiene el rol necesario para esta operación.");
        }
    }

    private void validarDatosReserva(int idLaboratorio, String curso, LocalDate fecha, int inicio, int fin) {
        if (idLaboratorio <= 0) {
            throw new IllegalArgumentException("El laboratorio debe ser mayor que cero.");
        }
        if (curso == null || curso.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el curso o motivo de la reserva.");
        }
        if (fecha == null || fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser anterior a hoy.");
        }
        if (!esHoraValida(inicio) || !esHoraValida(fin) || inicio >= fin) {
            throw new IllegalArgumentException("El rango horario no es válido.");
        }
    }

    private boolean esHoraValida(int hora) {
        int horas = hora / 100;
        int minutos = hora % 100;
        return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59;
    }

    private boolean haySolapamiento(int inicioA, int finA, int inicioB, int finB) {
        return inicioA < finB && finA > inicioB;
    }

    private String formatearHora(int hora) {
        return String.format("%02d:%02d", hora / 100, hora % 100);
    }
}
