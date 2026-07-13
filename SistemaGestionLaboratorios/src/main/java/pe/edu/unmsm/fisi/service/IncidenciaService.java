package pe.edu.unmsm.fisi.service;

import java.util.List;
import pe.edu.unmsm.fisi.model.entity.Incidencia;
import pe.edu.unmsm.fisi.model.entity.Usuario;

public interface IncidenciaService {
    Incidencia registrar(Usuario usuario, int idComputadora, String tipo, String descripcion);
    Incidencia siguienteFIFO();
    List<Incidencia> listarPendientes();
    List<Incidencia> listarTodas();
    boolean actualizarEstado(int idIncidencia, String estado);
}
