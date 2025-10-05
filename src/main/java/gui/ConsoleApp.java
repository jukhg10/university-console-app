package gui;

import dto.PersonaDTO;
import model.Facultad;
import model.Persona;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final UniversityController universityController;
    private final Scanner scanner;

    public ConsoleApp(UniversityController universityController) {
        this.universityController = universityController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        // ... (El resto del código de ConsoleApp que te proporcioné anteriormente)
        // No es necesario pegarlo todo aquí de nuevo, ya lo tienes.
        // Simplemente asegúrate de que esté usando el universityController.
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();
            seleccionarOpcionPrincipal(opcion);
        } while (opcion != 7);
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n=============================================");
        System.out.println("      --- MENÚ PRINCIPAL (CONSOLA) ---   ");
        System.out.println("=============================================");
        System.out.println("1. Gestión de Personas");
        System.out.println("2. Gestión de Facultades");
        System.out.println("3. Gestión de Programas");
        System.out.println("4. Gestión de Cursos");
        System.out.println("5. Gestión de Inscripciones");
        System.out.println("7. Salir de la Consola");
        System.out.print("Seleccione una opción: ");
    }

    private void seleccionarOpcionPrincipal(int opcion) {
        switch (opcion) {
            case 1:
                gestionPersonas();
                break;
            case 2:
                gestionFacultades();
                break;
            case 3:
                 System.out.println("Funcionalidad no implementada en consola.");
                break;
            case 4:
                 System.out.println("Funcionalidad no implementada en consola.");
                break;
            case 5:
                 System.out.println("Funcionalidad no implementada en consola.");
                break;
            case 7:
                System.out.println("Saliendo de la aplicación...");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    private void gestionPersonas() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Personas ---");
            System.out.println("1. Agregar nueva persona");
            System.out.println("2. Ver todas las personas");
            System.out.println("3. Eliminar persona");
            System.out.println("4. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    agregarPersona();
                    break;
                case 2:
                    verPersonas();
                    break;
                case 3:
                    eliminarPersona();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 4);
    }

    private void agregarPersona() {
        try {
            System.out.print("Ingrese ID (número): ");
            double id = scanner.nextDouble(); scanner.nextLine();
            System.out.print("Ingrese Nombres: ");
            String nombres = scanner.nextLine();
            System.out.print("Ingrese Apellidos: ");
            String apellidos = scanner.nextLine();
            System.out.print("Ingrese Email: ");
            String email = scanner.nextLine();

            PersonaDTO nueva = new PersonaDTO(id, nombres, apellidos, email);
            universityController.agregarPersona(nueva);
            System.out.println("¡Persona guardada!");
        } catch (Exception e) {
            handleInputError();
        }
    }

    private void verPersonas() {
        System.out.println("\n--- Listado de Personas ---");
        List<PersonaDTO> lista = universityController.getPersonasData();
        if (lista.isEmpty()) System.out.println("No hay personas registradas.");
        else lista.forEach(System.out::println);
    }

    private void eliminarPersona() {
        try {
            System.out.print("Ingrese el ID de la persona a eliminar: ");
            double id = scanner.nextDouble(); scanner.nextLine();

            PersonaDTO personaParaEliminar = universityController.getPersonasData()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
            
            if (personaParaEliminar != null) {
                universityController.eliminarPersona(personaParaEliminar);
                System.out.println("Persona eliminada (si existía).");
            } else {
                System.out.println("No se encontró una persona con ese ID.");
            }
        } catch (Exception e) {
            handleInputError();
        }
    }

    private void gestionFacultades() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Facultades ---");
            System.out.println("1. Agregar nueva facultad");
            System.out.println("2. Ver todas las facultades");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    agregarFacultad();
                    break;
                case 2:
                    verFacultades();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 3);
    }
    
    private void agregarFacultad() {
        try {
            System.out.print("Ingrese ID de la facultad: ");
            double id = scanner.nextDouble(); scanner.nextLine();
            System.out.print("Ingrese Nombre de la facultad: ");
            String nombre = scanner.nextLine();
            
            Facultad nueva = new Facultad();
            nueva.setId(id);
            nueva.setNombre(nombre);

            universityController.agregarFacultad(nueva);
            System.out.println("¡Facultad guardada!");

        } catch (Exception e) {
            handleInputError();
        }
    }

    private void verFacultades() {
         System.out.println("\n--- Listado de Facultades ---");
         List<Facultad> lista = universityController.getListaDeFacultades();
         if (lista.isEmpty()) System.out.println("No hay facultades registradas.");
         else lista.forEach(f -> System.out.println("ID: " + f.getId() + ", Nombre: " + f.getNombre()));
    }

    private int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            return opcion;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    private void handleInputError() {
        System.out.println("Error: Entrada no válida. Por favor, intente de nuevo.");
    }
}