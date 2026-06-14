package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.model.entity.Incidencia;

public interface IncidenciaRepository {
    boolean save(Incidencia incidencia);
    Incidencia findSiguienteIncidenciaFIFO();
    boolean updateEstadoIncidencia(int idIncidencia, String nuevoEstado);
}
