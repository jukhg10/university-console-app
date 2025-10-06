// gui/ConsoleApp.java
package gui;

import dto.PersonaDTO;
import model.*; // Import all models

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.Date;


public class ConsoleApp {
    private final UniversityController universityController;
    private final Scanner scanner;

    public ConsoleApp() {
        this.universityController = UniversityController.getInstance(); // Get instance from Singleton
        this.scanner = new Scanner(System.in);
    }

    public void start() {
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
                gestionProgramas();
                break;
            case 4:
                gestionCursos();
                break;
            case 5:
                gestionInscripciones();
                break;
            case 7:
                System.out.println("Saliendo de la aplicación...");
                break;
            default:
                System.out.println("Opción no válida.");
        }
    }

    // --- GESTIÓN DE PERSONAS ---
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
            System.out.print("Ingrese Nombres: ");
            String nombres = scanner.nextLine();
            System.out.print("Ingrese Apellidos: ");
            String apellidos = scanner.nextLine();
            System.out.print("Ingrese Email: ");
            String email = scanner.nextLine();

            PersonaDTO nueva = new PersonaDTO();
            nueva.setNombres(nombres);
            nueva.setApellidos(apellidos);
            nueva.setEmail(email);

            universityController.agregarPersona(nueva);
            System.out.println("¡Persona guardada!");
        } catch (Exception e) {
            handleInputError(e);
        }
    }

    private void verPersonas() {
        System.out.println("\n--- Listado de Personas ---");
        List<PersonaDTO> lista = universityController.getPersonasData();
        if (lista.isEmpty()) {
            System.out.println("No hay personas registradas.");
        } else {
            lista.forEach(p -> System.out.println("ID: " + p.getId() + ", Nombre: " + p.getNombres() + " " + p.getApellidos()));
        }
    }

    private void eliminarPersona() {
        try {
            verPersonas();
            System.out.print("\nIngrese el ID de la persona a eliminar: ");
            double id = scanner.nextDouble(); scanner.nextLine();

            PersonaDTO personaParaEliminar = universityController.getPersonasData()
                    .stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (personaParaEliminar != null) {
                if(universityController.eliminarPersona(personaParaEliminar)) {
                    System.out.println("Persona eliminada con éxito.");
                } else {
                    System.out.println("Error: No se pudo eliminar la persona. Verifique que no sea decano de una facultad.");
                }
            } else {
                System.out.println("No se encontró una persona con ese ID.");
            }
        } catch (Exception e) {
            handleInputError(e);
        }
    }

    // --- GESTIÓN DE FACULTADES ---
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
            System.out.print("Ingrese Nombre de la facultad: ");
            String nombre = scanner.nextLine();

            Facultad nueva = new Facultad(0, nombre); // ID es autogenerado

            universityController.agregarFacultad(nueva);
            System.out.println("¡Facultad guardada!");

        } catch (Exception e) {
            handleInputError(e);
        }
    }

    private void verFacultades() {
        System.out.println("\n--- Listado de Facultades ---");
        List<Facultad> lista = universityController.getListaDeFacultades();
        if (lista.isEmpty()) System.out.println("No hay facultades registradas.");
        else lista.forEach(f -> System.out.println("ID: " + f.getId() + ", Nombre: " + f.getNombre()));
    }


    // --- GESTIÓN DE PROGRAMAS (CORREGIDO) ---
    private void gestionProgramas() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Programas ---");
            System.out.println("1. Agregar nuevo programa");
            System.out.println("2. Ver todos los programas");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    agregarPrograma();
                    break;
                case 2:
                    verProgramas();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 3);
    }

    private void agregarPrograma() {
        try {
            System.out.print("Ingrese Nombre del programa: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese la duración en semestres: ");
            double duracion = scanner.nextDouble(); scanner.nextLine();
            System.out.print("Ingrese la fecha de registro (yyyy-MM-dd): ");
            String fechaStr = scanner.nextLine();
            LocalDate localDate = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);
            Date registro = java.sql.Date.valueOf(localDate);

            verFacultades();
            System.out.print("Ingrese el ID de la facultad a la que pertenece: ");
            double facultadId = scanner.nextDouble(); scanner.nextLine();

            Facultad facultadSeleccionada = universityController.getListaDeFacultades()
                    .stream()
                    .filter(f -> f.getId() == facultadId)
                    .findFirst()
                    .orElse(null);

            if (facultadSeleccionada != null) {
                // Usamos el constructor existente, como en la GUI, con ID temporal 0
                Programa nuevo = new Programa(0, nombre, duracion, registro, facultadSeleccionada);
                universityController.agregarPrograma(nuevo);
                System.out.println("¡Programa guardado!");
            } else {
                System.out.println("Error: No se encontró una facultad con ese ID.");
            }
        } catch (Exception e) {
            handleInputError(e);
        }
    }

    private void verProgramas() {
        System.out.println("\n--- Listado de Programas ---");
        List<Programa> lista = universityController.getListaDeProgramas();
        if (lista.isEmpty()) {
            System.out.println("No hay programas registrados.");
        } else {
            lista.forEach(p -> System.out.println(
                    "ID: " + p.getId() +
                    ", Nombre: " + p.getNombre() +
                    ", Facultad: " + (p.getFacultad() != null ? p.getFacultad().getNombre() : "N/A")
            ));
        }
    }

    // --- GESTIÓN DE CURSOS ---
    private void gestionCursos() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Cursos ---");
            System.out.println("1. Agregar nuevo curso");
            System.out.println("2. Ver todos los cursos");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    agregarCurso();
                    break;
                case 2:
                    verCursos();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 3);
    }

    private void agregarCurso() {
        try {
            System.out.print("Ingrese Nombre del curso: ");
            String nombre = scanner.nextLine();

            verProgramas();
            System.out.print("Ingrese el ID del programa al que pertenece el curso: ");
            double programaId = scanner.nextDouble(); scanner.nextLine();

            Programa programaSeleccionado = universityController.getListaDeProgramas()
                .stream()
                .filter(p -> p.getId() == programaId)
                .findFirst()
                .orElse(null);

            if (programaSeleccionado != null) {
                Curso nuevo = new Curso(0, nombre, true); // ID es autogenerado
                nuevo.setPrograma(programaSeleccionado);
                universityController.agregarCurso(nuevo);
                System.out.println("¡Curso guardado!");
            } else {
                System.out.println("Error: No se encontró un programa con ese ID.");
            }

        } catch (Exception e) {
            handleInputError(e);
        }
    }

    private void verCursos() {
        System.out.println("\n--- Listado de Cursos ---");
        List<Curso> lista = universityController.getListaDeCursos();
        if (lista.isEmpty()) {
            System.out.println("No hay cursos registrados.");
        } else {
            lista.forEach(c -> System.out.println(
                    "ID: " + c.getId() +
                    ", Nombre: " + c.getNombre() +
                    ", Programa: " + (c.getPrograma() != null ? c.getPrograma().getNombre() : "N/A")
            ));
        }
    }

    // --- GESTIÓN DE INSCRIPCIONES (CORREGIDO) ---
    private void gestionInscripciones() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Inscripciones ---");
            System.out.println("1. Inscribir estudiante a un curso");
            System.out.println("2. Ver todas las inscripciones");
            System.out.println("3. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    agregarInscripcion();
                    break;
                case 2:
                    verInscripciones();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 3);
    }
        private void verEstudiantes() {
        System.out.println("\n--- Listado de Estudiantes ---");
        List<Estudiante> lista = universityController.getEstudiantesData();
        if (lista.isEmpty()) {
            System.out.println("No hay Estudiantes registrados.");
        } else {
            lista.forEach(e -> System.out.println("ID: " + e.getId() + ", Nombre: " + e.getNombres() + " " + e.getApellidos()));
        }
    }


    private void agregarInscripcion() {
        try {
            verEstudiantes(); // Mostramos los estudiantes disponibles
            System.out.print("\nIngrese el ID del estudiante a inscribir: ");
            double estudianteId = scanner.nextDouble(); scanner.nextLine();

            verCursos();
            System.out.print("\nIngrese el ID del curso al que desea inscribirlo: ");
            int cursoId = scanner.nextInt(); scanner.nextLine();

            System.out.print("Ingrese el año de la inscripción: ");
            int ano = scanner.nextInt(); scanner.nextLine();
            System.out.print("Ingrese el semestre de la inscripción: ");
            int semestre = scanner.nextInt(); scanner.nextLine();

            Estudiante estudiante = universityController.getEstudiantesData().stream()
                .filter(e -> e.getId() == estudianteId).findFirst().orElse(null);
            Curso curso = universityController.getListaDeCursos().stream()
                .filter(c -> c.getId() == cursoId).findFirst().orElse(null);

            if (estudiante != null && curso != null) {
                // Usamos el constructor de la GUI
                Inscripcion nueva = new Inscripcion(estudiante, curso, ano, semestre);
                // Llamamos al método correcto del controlador
                universityController.inscribirEstudiante(nueva);
                System.out.println("¡Inscripción realizada!");
            } else {
                System.out.println("Error: Estudiante o Curso no encontrados.");
            }
            
        } catch (Exception e) {
            handleInputError(e);
        }
    }

    private void verInscripciones() {
        System.out.println("\n--- Listado de Inscripciones ---");
        // Llamamos al método correcto del controlador
        List<Inscripcion> lista = universityController.getInscripcionesData();
        if(lista.isEmpty()) {
            System.out.println("No hay inscripciones registradas.");
        } else {
            lista.forEach(i -> System.out.println(
                "ID: " + i.getId() +
                ", Estudiante: " + (i.getEstudiante() != null ? i.getEstudiante().getNombres() : "N/A") +
                ", Curso: " + (i.getCurso() != null ? i.getCurso().getNombre() : "N/A") +
                ", Año: " + i.getAno() + ", Semestre: " + i.getSemestre()
            ));
        }
    }

    // --- MÉTODOS UTILITARIOS ---
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

    private void handleInputError(Exception e) {
        System.out.println("Error: Entrada no válida o problema ocurrido. Por favor, intente de nuevo.");
        // Opcional: imprimir el error para depuración
        // e.printStackTrace();
    }
}