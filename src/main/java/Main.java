import model.Curso;
import model.Estudiante;
import model.Inscripcion;
import persistence.DatabaseManager;
import services.CursosInscritos;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando aplicación...");
        DatabaseManager.initializeDatabase();

        CursosInscritos servicio = new CursosInscritos();
        servicio.cargarDatos();
        System.out.println("Datos cargados. " + servicio.cantidadActual() + " inscripciones existentes.");

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        while (opcion != 4) {
            System.out.println("\n--- MENÚ DE GESTIÓN DE INSCRIPCIONES ---");
            System.out.println("1. Registrar nueva inscripción");
            System.out.println("2. Listar todas las inscripciones");
            System.out.println("3. Eliminar inscripción");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        System.out.println("\n-- Nueva Inscripción --");
                        System.out.print("ID del Estudiante (ej: 101, 102): ");
                        double estId = Double.parseDouble(scanner.nextLine());
                        System.out.print("ID del Curso (ej: 901, 902): ");
                        int curId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Año (ej: 2025): ");
                        int ano = Integer.parseInt(scanner.nextLine());
                        System.out.print("Semestre (ej: 2): ");
                        int semestre = Integer.parseInt(scanner.nextLine());
                        
                        // En una app real, buscaríamos los objetos. Aquí los creamos para el ejemplo.
                        Estudiante est = new Estudiante(estId, "Estudiante", "Temp", "", estId, true, 0);
                        Curso cur = new Curso(curId, "Curso Temp", true);

                        Inscripcion nuevaInscripcion = new Inscripcion(est, cur, ano, semestre);
                        servicio.inscribirCurso(nuevaInscripcion);
                        System.out.println("¡Inscripción registrada con éxito!");
                        break;
                    case 2:
                        System.out.println("\n-- Listado de Inscripciones (" + servicio.cantidadActual() + ") --");
                        if (servicio.cantidadActual() == 0) {
                            System.out.println("No hay inscripciones para mostrar.");
                        } else {
                            servicio.imprimirLista().forEach(System.out::println);
                        }
                        break;
                    case 3:
                        System.out.println("\n-- Eliminar Inscripción --");
                        System.out.print("ID de la inscripción a eliminar: ");
                        int idEliminar = Integer.parseInt(scanner.nextLine());
                        servicio.eliminar(idEliminar);
                        System.out.println("Inscripción eliminada (si existía).");
                        break;
                    case 4:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            }
        }
        scanner.close();
    }
}