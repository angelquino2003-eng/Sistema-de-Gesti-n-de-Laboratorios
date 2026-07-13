package pe.edu.unmsm.fisi.model.entity;

public class ReservaLaboratorio extends Reserva {
    private String cursoAcademico;

    public ReservaLaboratorio() {
        super();
    }

    public String getCursoAcademico() {
        return cursoAcademico;
    }

    public void setCursoAcademico(String cursoAcademico) {
        this.cursoAcademico = cursoAcademico;
    }
}