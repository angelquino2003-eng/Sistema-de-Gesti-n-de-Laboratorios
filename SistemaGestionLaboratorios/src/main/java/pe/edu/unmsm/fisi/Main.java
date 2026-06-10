package pe.edu.unmsm.fisi;

import pe.edu.unmsm.fisi.model.entity.Alumno;
import pe.edu.unmsm.fisi.model.entity.Profesor;
import pe.edu.unmsm.fisi.service.ReservaService;
import pe.edu.unmsm.fisi.service.ReservaServiceImpl;

public class Main {
    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("   TEST DE LÓGICA: ALGORITMOS DE ASIGNACIÓN (FISI)   ");
        System.out.println("=====================================================\n");

        // Instanciamos tu servicio (que por ahora usa las listas simuladas)
        ReservaService reservaService = new ReservaServiceImpl();

        // ----------------------------------------------------------------
        // PRUEBA 1: ALGORITMO VORAZ (Greedy)
        // ----------------------------------------------------------------
        System.out.println(">>> PRUEBA 1: SOLICITUD DE ALUMNO (Algoritmo Voraz) <<<");
        Alumno alumnoPrueba = new Alumno(1, "Jean Carlo", "jean.tecsi@unmsm.edu.pe", "123", "20260001", 120);
        
        // El laboratorio 1 tiene simuladas: PC-01(Ocupada), PC-02(Mantenimiento), PC-03(Disponible), PC-04(Disponible)
        // El algoritmo debería ignorar las dos primeras y atrapar inmediatamente la PC-03.
        reservaService.solicitarAsignacionAutomaticaAlumno(alumnoPrueba, 1, "Arquitectura de Computadoras");


        // ----------------------------------------------------------------
        // PRUEBA 2: INTERVAL SCHEDULING (Choque de Horarios)
        // ----------------------------------------------------------------
        System.out.println("\n>>> PRUEBA 2: RESERVA DOCENTE (Provocando Colisión) <<<");
        Profesor profePrueba = new Profesor(2, "Juan Ricardo Tapia", "jtapia@unmsm.edu.pe", "123", "Ingeniería de Software");
        
        // Intentamos reservar de 09:00 a 11:00. 
        // Recordatorio: Simulamos que ya hay una reserva de 08:00 a 10:00.
        // Matemáticamente esto DEBE dar error porque se cruzan entre las 09:00 y las 10:00.
        reservaService.procesarReservaDocente(profePrueba, 1, 900, 1100, "Sistemas Operativos");


        // ----------------------------------------------------------------
        // PRUEBA 3: INTERVAL SCHEDULING (Horario Libre)
        // ----------------------------------------------------------------
        System.out.println("\n>>> PRUEBA 3: RESERVA DOCENTE (Espacio Libre) <<<");
        // Intentamos reservar de 11:00 a 13:00.
        // Recordatorio: Hay reservas simuladas de 08:00-10:00 y de 14:00-16:00.
        // La franja de 11:00 a 13:00 cae exactamente en el medio, así que DEBE ser exitosa.
        reservaService.procesarReservaDocente(profePrueba, 1, 1100, 1300, "Algorítmica II");
        
        System.out.println("\n=====================================================");
        System.out.println("                 FIN DE LAS PRUEBAS                  ");
        System.out.println("=====================================================");
    }
}