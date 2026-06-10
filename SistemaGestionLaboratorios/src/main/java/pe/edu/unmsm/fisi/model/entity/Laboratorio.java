package pe.edu.unmsm.fisi.model.entity;

public class Laboratorio {
    private int idLaboratorio;
    private String nombre;
    private int capacidad;

    public Laboratorio() {
    }

    public Laboratorio(int idLaboratorio, String nombre, int capacidad) {
        this.idLaboratorio = idLaboratorio;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public int getIdLaboratorio() { return idLaboratorio; }
    public void setIdLaboratorio(int idLaboratorio) { this.idLaboratorio = idLaboratorio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
}