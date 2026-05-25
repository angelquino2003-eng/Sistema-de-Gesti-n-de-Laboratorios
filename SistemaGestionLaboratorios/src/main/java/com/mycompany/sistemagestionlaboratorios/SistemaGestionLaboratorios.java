package com.mycompany.sistemagestionlaboratorios;

import com.mycompany.sistemagestionlaboratorios.model.Usuario;
import com.mycompany.sistemagestionlaboratorios.repository.MemoriaDataStore;
import java.util.Scanner;

public class SistemaGestionLaboratorios {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MemoriaDataStore dataStore = new MemoriaDataStore();
        Usuario usuarioLogueado = null;

        System.out.println("=========================================");
        System.out.println("SISTEMA DE GESTIÓN DE LABORATORIOS - FISI");
        System.out.println("=========================================");

        // Bucle de Login
        while (usuarioLogueado == null) {
            System.out.print("\nIngrese su ID/Usuario: ");
            String id = scanner.nextLine();
            
            System.out.print("Ingrese su Contraseña: ");
            String pass = scanner.nextLine();

            usuarioLogueado = dataStore.autenticarUsuario(id, pass);

            if (usuarioLogueado == null) {
                System.out.println("[ERROR] Credenciales incorrectas. Intente nuevamente.");
            } else {
                System.out.println("\n[EXITO] Bienvenido, " + usuarioLogueado.getNombre() + "!");
                System.out.println("Rol detectado: " + usuarioLogueado.getRol());
            }
        }

        // Iniciamos el bucle infinito del menú pasándole el scanner
        iniciarMenu(usuarioLogueado, scanner);
        
        System.out.println("\nCerrando sesión... ¡Hasta pronto, " + usuarioLogueado.getNombre() + "!");
        scanner.close();
    }
    
    private static void iniciarMenu(Usuario usuario, Scanner scanner) {
        String opcion = "";
        
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            switch (usuario.getRol()) {
                case "ALUMNO":
                    System.out.println("1. Solicitar Computadora (Asignación Voraz)");
                    System.out.println("2. Reportar Fallo de Equipo");
                    break;
                case "PROFESOR":
                    System.out.println("1. Reservar Laboratorio (Interval Scheduling)");
                    System.out.println("2. Ver disponibilidad de aulas");
                    break;
                case "ADMINISTRADOR":
                    System.out.println("1. Visualizar Matriz de Laboratorio");
                    System.out.println("2. Ver estadísticas de uso");
                    break;
                case "TECNICO":
                    System.out.println("1. Atender siguiente incidencia (FIFO)");
                    System.out.println("2. Actualizar estado de computadora");
                    break;
            }
            System.out.println("0. Salir del Sistema");
            System.out.print("Seleccione una opción: ");
            
            // Leemos lo que escribe el usuario
            opcion = scanner.nextLine();
            
            // Lógica de navegación temporal
            if (!opcion.equals("0")) {
                System.out.println("\n[SISTEMA] Accediendo a la opción " + opcion + "...");
                System.out.println("[SISTEMA] (La lógica algorítmica de esta función se implementará en la Fase 2)");
                System.out.println("Presione ENTER para volver al menú...");
                scanner.nextLine(); // Pausa dramática para que el usuario pueda leer
            }
            
        } while (!opcion.equals("0")); // El bucle se repite hasta que presione 0
    }
}