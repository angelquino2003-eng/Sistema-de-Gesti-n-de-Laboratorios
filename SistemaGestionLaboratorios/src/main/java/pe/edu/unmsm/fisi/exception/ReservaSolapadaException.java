package pe.edu.unmsm.fisi.exception;

/**
 * Excepción lanzada cuando se detecta un cruce de horarios en el laboratorio.
 */
public class ReservaSolapadaException extends RuntimeException {
    public ReservaSolapadaException(String mensaje) {
        super(mensaje);
    }
}