package pe.edu.unmsm.fisi.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConexionBD {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/gestion_laboratorios";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static ConexionBD instance;
    private final String url;
    private final String user;
    private final String password;

    private ConexionBD() {
        Properties properties = cargarPropiedades();
        this.url = valorConfigurado("DB_URL", "db.url", properties.getProperty("db.url", DEFAULT_URL));
        this.user = valorConfigurado("DB_USER", "db.user", properties.getProperty("db.user", DEFAULT_USER));
        this.password = valorConfigurado("DB_PASSWORD", "db.password", properties.getProperty("db.password", DEFAULT_PASSWORD));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontró el controlador MySQL. Ejecute el proyecto con Maven para cargar mysql-connector-j.", e);
        }
    }

    public static synchronized ConexionBD getInstance() {
        if (instance == null) {
            instance = new ConexionBD();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public boolean probarConexion() {
        try (Connection ignored = getConnection()) {
            return true;
        } catch (SQLException | IllegalStateException e) {
            return false;
        }
    }

    public String getUrl() {
        return url;
    }

    private Properties cargarPropiedades() {
        Properties properties = new Properties();
        Path externalFile = Path.of("db.properties");
        if (Files.isRegularFile(externalFile)) {
            try (InputStream input = Files.newInputStream(externalFile)) {
                properties.load(input);
                return properties;
            } catch (IOException e) {
                System.err.println("No se pudo leer el db.properties externo: " + e.getMessage());
            }
        }

        try (InputStream input = ConexionBD.class.getResourceAsStream("/db.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer db.properties: " + e.getMessage());
        }
        return properties;
    }

    private String valorConfigurado(String envKey, String systemKey, String fallback) {
        String systemValue = System.getProperty(systemKey);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return fallback;
    }
}
