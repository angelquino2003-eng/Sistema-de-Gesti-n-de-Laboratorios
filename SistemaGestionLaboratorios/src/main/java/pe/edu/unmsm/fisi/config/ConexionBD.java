package pe.edu.unmsm.fisi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static ConexionBD instance;
    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/gestion_laboratorios_V2";
    private final String USER = "root";
    private final String PASS = "¿Kiri289616?"; 

    private ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión exitosa a MySQL.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error crítico al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static synchronized ConexionBD getInstance() {
        if (instance == null) {
            instance = new ConexionBD();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión con el servidor MySQL cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar cerrar la conexión: " + e.getMessage());
        }
    }
}