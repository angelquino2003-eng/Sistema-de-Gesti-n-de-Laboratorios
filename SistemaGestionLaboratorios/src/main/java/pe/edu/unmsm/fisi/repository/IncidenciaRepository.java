package pe.edu.unmsm.fisi.repository;

import java.util.List;
import pe.edu.unmsm.fisi.model.entity.Incidencia;

public interface IncidenciaRepository {
    boolean save(Incidencia incidencia);
    Incidencia findSiguienteIncidenciaFIFO();
    List<Incidencia> findPendientes();
    List<Incidencia> findTodas();
    boolean updateEstadoIncidencia(int idIncidencia, String nuevoEstado);
}
