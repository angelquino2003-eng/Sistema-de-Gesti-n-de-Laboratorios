package pe.edu.unmsm.fisi.model.entity;

public class ReservaComputadora extends Reserva {
    private int idComputadora;
    private String requerimientoSoftware; // Atributo recuperado de tu diagrama UML

    public ReservaComputadora() {
        super();
    }

    public int getIdComputadora() {
        return idComputadora;
    }

    public void setIdComputadora(int idComputadora) {
        this.idComputadora = idComputadora;
    }

    public String getRequerimientoSoftware() {
        return requerimientoSoftware;
    }

    public void setRequerimientoSoftware(String requerimientoSoftware) {
        this.requerimientoSoftware = requerimientoSoftware;
    }
}