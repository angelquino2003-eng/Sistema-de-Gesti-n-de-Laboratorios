package pe.edu.unmsm.fisi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de Configuración de Base de Datos.
 * Aplica el Patrón de Diseño Singleton para garantizar una única instancia de 
 * conexión activa en todo el ciclo de vida de la aplicación.
 */
public final class ConexionBD {

    private static ConexionBD instance;
    private Connection connection;

    // Credenciales apuntando directamente a la v2
    private final String URL = "jdbc:mysql://localhost:3306/gestion_laboratorios_v2?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "¿Kiri289616?";

    private ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("[DATABASE INFO] Conexión establecida exitosamente con 'gestion_laboratorios_v2'.");
        } catch (ClassNotFoundException e) {
            System.err.println("[DATABASE ERROR] No se encontró el Driver de MySQL JDBC. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[DATABASE ERROR] Error de autenticación o conexión a MySQL. " + e.getMessage());
        }
    }

    public static synchronized ConexionBD getInstance() {
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                instance = new ConexionBD();
            }
        } catch (SQLException e) {
            System.err.println("[DATABASE ERROR] Fallo al verificar el estado de la conexión: " + e.getMessage());
            instance = new ConexionBD(); // Re-intento en caso de fallo
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void cerrarConexion() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DATABASE INFO] Conexión de base de datos cerrada de forma segura.");
                }
            } catch (SQLException e) {
                System.err.println("[DATABASE ERROR] Error al intentar cerrar la conexión JDBC: " + e.getMessage());
            }
        }
    }
}