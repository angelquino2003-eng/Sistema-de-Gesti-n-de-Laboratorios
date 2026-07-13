package pe.edu.unmsm.fisi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import pe.edu.unmsm.fisi.model.entity.Incidencia;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.repository.EquipoRepository;
import pe.edu.unmsm.fisi.repository.EquipoRepositoryImpl;
import pe.edu.unmsm.fisi.repository.IncidenciaRepository;
import pe.edu.unmsm.fisi.repository.IncidenciaRepositoryImpl;

public class IncidenciaServiceImpl implements IncidenciaService {
    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "EN_PROCESO", "RESUELTA", "CANCELADA");

    private final IncidenciaRepository incidenciaRepository;
    private final EquipoRepository equipoRepository;

    public IncidenciaServiceImpl() {
        this.incidenciaRepository = new IncidenciaRepositoryImpl();
        this.equipoRepository = new EquipoRepositoryImpl();
    }

    @Override
    public Incidencia registrar(Usuario usuario, int idComputadora, String tipo, String descripcion) {
        if (usuario == null) {
            throw new IllegalArgumentException("No existe una sesión activa.");
        }
        if (idComputadora <= 0 || equipoRepository.buscarPorId(idComputadora) == null) {
            throw new IllegalArgumentException("Seleccione una computadora válida.");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("Seleccione el tipo de incidencia.");
        }
        if (descripcion == null || descripcion.trim().length() < 10) {
            throw new IllegalArgumentException("La descripción debe tener al menos 10 caracteres.");
        }

        Incidencia incidencia = new Incidencia();
        incidencia.setIdComputadora(idComputadora);
        incidencia.setIdUsuarioReporta(usuario.getIdUsuario());
        incidencia.setTipoIncidencia(tipo.trim());
        incidencia.setDescripcion(descripcion.trim());
        incidencia.setFechaReporte(LocalDateTime.now());
        incidencia.setEstado("PENDIENTE");

        if (!incidenciaRepository.save(incidencia)) {
            throw new IllegalStateException("No se pudo registrar la incidencia.");
        }
        return incidencia;
    }

    @Override
    public Incidencia siguienteFIFO() {
        return incidenciaRepository.findSiguienteIncidenciaFIFO();
    }

    @Override
    public List<Incidencia> listarPendientes() {
        return incidenciaRepository.findPendientes();
    }

    @Override
    public List<Incidencia> listarTodas() {
        return incidenciaRepository.findTodas();
    }

    @Override
    public boolean actualizarEstado(int idIncidencia, String estado) {
        if (idIncidencia <= 0) {
            throw new IllegalArgumentException("Seleccione una incidencia válida.");
        }
        String normalizado = estado == null ? "" : estado.trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(normalizado)) {
            throw new IllegalArgumentException("El estado seleccionado no es válido.");
        }
        return incidenciaRepository.updateEstadoIncidencia(idIncidencia, normalizado);
    }
}
