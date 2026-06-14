package pe.edu.unmsm.fisi.repository;

import pe.edu.unmsm.fisi.model.entity.Computadora;
import java.util.List;

public interface EquipoRepository {
    boolean cambiarEstado(int idComputadora, String nuevoEstado);
    Computadora buscarPorId(int idComputadora);
    List<Computadora> listarPorLaboratorio(int idLaboratorio);
}