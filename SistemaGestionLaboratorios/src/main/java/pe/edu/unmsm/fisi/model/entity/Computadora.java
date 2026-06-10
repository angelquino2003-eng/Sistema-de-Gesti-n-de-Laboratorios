package pe.edu.unmsm.fisi.model.entity;

public class Computadora {
    private int idComputadora;
    private String codigoPc;
    private String estado;
    private int idLaboratorio;

    public Computadora() {
    }

    public Computadora(int idComputadora, String codigoPc, String estado, int idLaboratorio) {
        this.idComputadora = idComputadora;
        this.codigoPc = codigoPc;
        this.estado = estado;
        this.idLaboratorio = idLaboratorio;
    }

    public int getIdComputadora() { return idComputadora; }
    public void setIdComputadora(int idComputadora) { this.idComputadora = idComputadora; }

    public String getCodigoPc() { return codigoPc; }
    public void setCodigoPc(String codigoPc) { this.codigoPc = codigoPc; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdLaboratorio() { return idLaboratorio; }
    public void setIdLaboratorio(int idLaboratorio) { this.idLaboratorio = idLaboratorio; }
}