package pe.edu.unmsm.fisi.exception;

/**
 * Excepción lanzada cuando los datos ingresados en la vista no cumplen con 
 * el formato o las reglas de negocio esperadas.
 */
public class EntradaInvalidaException extends RuntimeException {
    public EntradaInvalidaException(String mensaje) {
        super(mensaje);
    }
}