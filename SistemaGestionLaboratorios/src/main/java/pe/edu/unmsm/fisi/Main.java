package pe.edu.unmsm.fisi;

// IMPORTACIONES COMPLETAS (Aquí estaba el problema)
import pe.edu.unmsm.fisi.model.entity.Computadora;
import pe.edu.unmsm.fisi.model.entity.Alumno;     // <-- Agregado
import pe.edu.unmsm.fisi.model.entity.Profesor;   // <-- Agregado
import pe.edu.unmsm.fisi.model.enums.Rol;         // <-- Agregado
import pe.edu.unmsm.fisi.service.ReservaService;
import pe.edu.unmsm.fisi.service.ReservaServiceImpl;
import pe.edu.unmsm.fisi.repository.EquipoRepository;
import pe.edu.unmsm.fisi.repository.EquipoRepositoryImpl;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ReservaService reservaService = new ReservaServiceImpl();
            // Instanciamos el repositorio directo solo para la opción de consultar la DB en vivo
            EquipoRepository equipoRepo = new EquipoRepositoryImpl();
            
            int opcion = 0;
            
            // Entidades "quemadas" para agilizar la demostración en vivo
            // Creamos al alumno vacío y lo llenamos con Setters
            Alumno alumnoDemo = new Alumno();
            alumnoDemo.setIdUsuario(1);
            alumnoDemo.setNombre("Jean Carlo");
            alumnoDemo.setCorreo("jean.carlo@unmsm.edu.pe");
            alumnoDemo.setPassword("123");
            alumnoDemo.setRol(Rol.ALUMNO);
            
            // Creamos al profesor vacío y lo llenamos con Setters
            Profesor profesorDemo = new Profesor();
            profesorDemo.setIdUsuario(2);
            profesorDemo.setNombre("Profesor Tapia");
            profesorDemo.setCorreo("tapia@unmsm.edu.pe");
            profesorDemo.setPassword("123");
            profesorDemo.setRol(Rol.PROFESOR);
            
            int idLaboratorioDemo = 1;
            
            System.out.println("\nInicializando módulos de Arquitectura FISI...");
            System.out.println("Conexión a MySQL establecida.");
            
            do {
                System.out.println("\n=======================================================");
                System.out.println("     SISTEMA INTELIGENTE DE LABORATORIOS - UNMSM       ");
                System.out.println("=======================================================");
                System.out.println(" 1. [Alumno]  Solicitar PC (Algoritmo Voraz)");
                System.out.println(" 2. [Docente] Reservar Laboratorio (Interval Scheduling)");
                System.out.println(" 3. [Admin]   Ver Estado del Laboratorio en Vivo");
                System.out.println(" 4. Salir del Sistema");
                System.out.println("=======================================================");
                System.out.print(">> Ingrese una opción: ");
                
                try {
                    opcion = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    opcion = 0;
                }
                
                System.out.println();
                
                switch (opcion) {
                    case 1 -> {
                        System.out.println("--- MODALIDAD ALUMNO: ASIGNACIÓN AUTOMÁTICA ---");
                        System.out.println("Evaluando disponibilidad con Algoritmo Voraz...");
                        reservaService.solicitarAsignacionAutomaticaAlumno(alumnoDemo, idLaboratorioDemo, "Programación Orientada a Objetos");
                        System.out.println("Presione ENTER para continuar...");
                        scanner.nextLine();
                    }
                    
                    case 2 -> {
                        System.out.println("--- MODALIDAD DOCENTE: GESTIÓN DE HORARIOS ---");
                        System.out.println("1. Simular horario LIBRE en la mañana (08:00 a 10:00)");
                        System.out.println("2. Simular choque con un Alumno en la tarde (14:00 a 16:00)");
                        System.out.print("Elija la simulación: ");
                        String subOpcion = scanner.nextLine();
                        
                        switch (subOpcion) {
                            case "1" -> {
                                System.out.println("\nEjecutando Interval Scheduling para horario 0800-1000...");
                                reservaService.procesarReservaDocente(profesorDemo, idLaboratorioDemo, 800, 1000, "Base de Datos");
                            }
                            case "2" -> {
                                System.out.println("\nEjecutando Interval Scheduling para horario 1400-1600 (Horario del alumno)...");
                                reservaService.procesarReservaDocente(profesorDemo, idLaboratorioDemo, 1400, 1600, "Ingeniería de Software");
                            }
                            default -> System.out.println("Opción inválida.");
                        }
                        System.out.println("Presione ENTER para continuar...");
                        scanner.nextLine();
                    }
                    
                    case 3 -> {
                        System.out.println("--- ESTADO ACTUAL DE LA BASE DE DATOS ---");
                        List<Computadora> pcs = equipoRepo.listarPorLaboratorio(idLaboratorioDemo);
                        System.out.println("Laboratorio ID: " + idLaboratorioDemo);
                        for (Computadora pc : pcs) {
                            String estadoStr = pc.getEstado().equals("DISPONIBLE") ? "[DISPONIBLE]" : "[" + pc.getEstado() + "]";
                            System.out.println(" - " + pc.getCodigoPc() + " : " + estadoStr);
                        }
                        System.out.println("\nPresione ENTER para continuar...");
                        scanner.nextLine();
                    }

                    case 4 -> System.out.println("Apagando sistema... ¡Sprint 2 finalizado con éxito!");
                    
                    default -> System.out.println("Error: Ingrese un número del 1 al 4.");
                }
            } while (opcion != 4);
        }
    }
}