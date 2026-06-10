package pe.edu.unmsm.fisi;

import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.repository.UsuarioRepository;
import pe.edu.unmsm.fisi.repository.UsuarioRepositoryImpl;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   INICIANDO PRUEBA DE CONEXIÓN A BD    ");
        System.out.println("=========================================");

        // 1. Intentar obtener la conexión física con el servidor MySQL
        System.out.println("Intentando conectar a MySQL...");
        Connection conn = ConexionBD.getInstance().getConnection();
        
        if (conn != null) {
            System.out.println("\n[OK] ¡Conexión establecida con éxito desde Java!");
            
            // 2. Instanciar el repositorio para probar una consulta real (SELECT)
            UsuarioRepository usuarioRepo = new UsuarioRepositoryImpl();
            
            System.out.println("\nLanzando consulta 'SELECT * FROM tbl_usuarios'...");
            List<Usuario> listaUsuarios = usuarioRepo.listarTodos();
            
            System.out.println("-----------------------------------------");
            System.out.println("Usuarios registrados en la base de datos: " + listaUsuarios.size());
            System.out.println("-----------------------------------------");
            
            // Mostrar los usuarios en la consola si es que hay alguno insertado
            for (Usuario u : listaUsuarios) {
                System.out.println("ID: " + u.getIdUsuario() + " | Nombre: " + u.getNombre() + " | Rol: " + u.getRol());
            }
            System.out.println("-----------------------------------------");
            
        } else {
            System.err.println("\n[ERROR] No se pudo conectar. Verifica que:");
            System.err.println("1. Tu servidor MySQL (XAMPP/MySQL Server) esté ENCENDIDO.");
            System.err.println("2. Hayas ejecutado el script SQL para crear la BD 'gestion_laboratorios'.");
            System.err.println("3. El usuario y la contraseña en ConexionBD.java coincidan con tu MySQL.");
        }
        
        // 3. Cerrar el canal de comunicación al finalizar la aplicación
        System.out.println("\nCerrando recursos...");
        ConexionBD.getInstance().cerrarConexion();
        System.out.println("Prueba finalizada.");
    }
}