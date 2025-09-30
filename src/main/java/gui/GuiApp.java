package gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import persistence.DatabaseManager;
import services.CursosInscritos;
import services.InscripcionesPersonas;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class GuiApp extends Application {

    /**
     * The main method now only launches the GUI.
     */

    public static InscripcionesPersonas servicioPersonas;
    public static ObservableList<Facultad> listaDeFacultades;
    public static ObservableList<Programa> listaDeProgramas;
    public static ObservableList<Curso> listaDeCursos;
    public static ObservableList<Inscripcion> inscripcionesData;
    public static ObservableList<Estudiante> listaDeEstudiantes;


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method starts the GUI and then immediately starts the console script on a new thread.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("--- Launching GUI Mode ---");
        DatabaseManager.initializeDatabase();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindow.fxml"));
        Parent root = loader.load();

        MainWindowController controller = loader.getController();
        servicioPersonas = controller.getServicioPersonas();
        listaDeFacultades = controller.getListaDeFacultades();
        listaDeProgramas = controller.getListaDeProgramas();
        inscripcionesData = controller.getInscripcionesData();
        listaDeEstudiantes = controller.getListaDeEstudiantes();

        listaDeCursos = controller.getListaDeCursos();
        primaryStage.setTitle("Gestor Universitario");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        System.out.println("--- Starting background console script... ---");
        Thread consoleThread = new Thread(() -> {
            runConsoleMode();
        });
        consoleThread.setDaemon(true);
        consoleThread.start();
    }

    /**
     * This method contains your console logic. It will now run in the background.
     */
    public static void runConsoleMode() {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            // --- MENÚ PRINCIPAL ---
            System.out.println("\n=============================================");
            System.out.println("        --- MENU PRINCIPAL UNIVERSIDAD ---   ");
            System.out.println("=============================================");
            System.out.println("1. Gestion de Personas");
            System.out.println("2. Gestion de Facultades");
            System.out.println("3. Gestion de Programas");
            System.out.println("4. Gestion de Cursos");
            System.out.println("5. Gestion de Inscripciones");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opcion: ");

            String opcionPrincipal = scanner.nextLine();

            switch (opcionPrincipal) {
                case "1":

                    gestionarPersonas(scanner);
                    break;
                case "2":
                    gestionarFacultades(scanner);

                    break;
                case "3":
                    gestionarProgramas(scanner);
                    break;
                case "4":
                    gestionarCursos(scanner);

                    break;
                case "5":
                    gestionarInscripciones(scanner);
                    break;
                case "6":
                    running = false;
                    System.out.println("Saliendo del meno de consola...");
                    break;
                default:
                    System.out.println("Opcion invalida, intente de nuevo.");
            }
        }
    }

    // --- Método privado para manejar el submenú de Personas ---
    private static void gestionarPersonas(Scanner scanner) {
        boolean enSubMenu = true;
        while (enSubMenu) {
            System.out.println("\n=============================================");
            System.out.println("        --- Gestion de Personas ---          ");
            System.out.println("=============================================");
            System.out.println("1. Inscribir Persona");
            System.out.println("2. Listar Personas");
            System.out.println("3. Volver al Menu Principal");
            System.out.print("Seleccione una opcion: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    inscribirPersonaConsola(scanner);
                    break;
                case "2":
                    listarPersonas();
                    break;
                case "3":
                    enSubMenu = false;
                    System.out.println("Regresando al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invalida, intente de nuevo.");
            }
        }
    }

    private static void gestionarFacultades(Scanner scanner) {
        boolean enSubMenu = true;
        while (enSubMenu) {
            System.out.println("\n=============================================");
            System.out.println("        --- Gestion de Facultades ---        ");
            System.out.println("=============================================");
            System.out.println("1. Agregar Facultad");
            System.out.println("2. Listar Facultades");
            System.out.println("3. Eliminar Facultad");
            System.out.println("4. Volver al Menu Principal");
            System.out.print("Seleccione una opcion: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    agregarFacultadConsola(scanner);
                    break;
                case "2":
                    listarFacultades();
                    break;
                case "3":
                    eliminarFacultadConsola(scanner);
                    break;
                case "4":
                    enSubMenu = false;
                    System.out.println("Regresando al menu principal...");
                    break;
                default:
                    System.out.println("Opcion invaalida, intente de nuevo.");
            }
        }
    }

    private static void inscribirPersonaConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID (numero): ");
            double id = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese Nombres: ");
            String nombres = scanner.nextLine();

            System.out.print("Ingrese Apellidos: ");
            String apellidos = scanner.nextLine();

            System.out.print("Ingrese Email: ");
            String email = scanner.nextLine();

            Persona nueva = new Persona(id, nombres, apellidos, email);

            servicioPersonas.inscribir(nueva);
            DatabaseManager.guardarPersona(nueva);

            System.out.println("Persona inscrita correctamente: " + nueva);
        } catch (NumberFormatException e) {
            System.out.println(" Error: El ID debe ser un numero válido.");
        } catch (Exception e) {
            System.out.println("Error al inscribir persona: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void agregarFacultadConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID de la Facultad (número): ");
            double id = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese Nombre de la Facultad: ");
            String nombre = scanner.nextLine();


            System.out.print("Ingrese ID del Decano ");
            double decanoId = Double.parseDouble(scanner.nextLine());

            Persona decano = null;
            if (decanoId != 0) {

                for (Persona p : servicioPersonas.listado) {
                    if (p.getId() == decanoId) {
                        decano = p;
                        break;
                    }
                }
                if (decano == null) {
                    System.out.println("Advertencia: No se encontró una persona con ese ID. La facultad se creará sin decano.");
                }
            }

            Facultad nueva = new Facultad(id, nombre);
            if (decano != null) {
                nueva.setDecano(decano);
            }


            guardarNuevaFacultadEnDB(nueva);


            listaDeFacultades.add(nueva);

            System.out.println("Facultad agregada correctamente: " + nueva);
        } catch (NumberFormatException e) {
            System.out.println(" Error: El ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("Error al agregar facultad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarFacultades() {
        if (listaDeFacultades.isEmpty()) {
            System.out.println("No hay facultades registradas.");
        } else {
            System.out.println("Facultades registradas:");
            for (Facultad f : listaDeFacultades) {
                String decanoStr = f.getDecano() != null ? f.getDecano().getNombres() + " " + f.getDecano().getApellidos() : "Sin decano";
                System.out.println(" - ID: " + f.getId() + ", Nombre: " + f.getNombre() + ", Decano: " + decanoStr);
            }
        }
    }

    private static void eliminarFacultadConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID de la Facultad a eliminar: ");
            double id = Double.parseDouble(scanner.nextLine());

            // Buscar y eliminar de la lista observable
            Facultad facultadAEliminar = null;
            for (Facultad f : listaDeFacultades) {
                if (f.getId() == id) {
                    facultadAEliminar = f;
                    break;
                }
            }

            if (facultadAEliminar == null) {
                System.out.println("No se encontró una facultad con ese ID.");
                return;
            }

            listaDeFacultades.remove(facultadAEliminar);


            System.out.println("Facultad con ID " + id + " eliminada correctamente.");
        } catch (NumberFormatException e) {
            System.out.println(" Error: El ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar facultad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para guardar facultad en DB (copiado de MainWindowController)
    private static void guardarNuevaFacultadEnDB(Facultad facultad) {
        String sqlFacultad = "MERGE INTO FACULTAD (id, nombre, decano_id) KEY(id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlFacultad)) {

            pstmt.setDouble(1, facultad.getId());
            pstmt.setString(2, facultad.getNombre());
            pstmt.setDouble(3, facultad.getDecano() != null ? facultad.getDecano().getId() : 0);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // --- Método privado para manejar el submenú de Programas ---
    private static void gestionarProgramas(Scanner scanner) {
        boolean enSubMenu = true;
        while (enSubMenu) {
            System.out.println("\n=============================================");
            System.out.println("        --- Gestión de Programas ---         ");
            System.out.println("=============================================");
            System.out.println("1. Agregar Programa");
            System.out.println("2. Listar Programas");
            System.out.println("3. Eliminar Programa");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    agregarProgramaConsola(scanner);
                    break;
                case "2":
                    listarProgramas();
                    break;
                case "3":
                    eliminarProgramaConsola(scanner);
                    break;
                case "4":
                    enSubMenu = false;
                    System.out.println("Regresando al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    private static void agregarProgramaConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID del Programa (número): ");
            double id = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese Nombre del Programa: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese Duración del Programa (número): ");
            double duracion = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese Fecha de Registro (YYYY-MM-DD): ");
            String fechaStr = scanner.nextLine();
            java.util.Date registro = java.sql.Date.valueOf(fechaStr);

            // Para simplificar, asignaremos una facultad existente por ID.
            System.out.print("Ingrese ID de la Facultad (número, 0 si no tiene): ");
            double facultadId = Double.parseDouble(scanner.nextLine());

            Facultad facultad = null;
            if (facultadId != 0) {
                // Buscar facultad en listaDeFacultades
                for (Facultad f : listaDeFacultades) {
                    if (f.getId() == facultadId) {
                        facultad = f;
                        break;
                    }
                }
                if (facultad == null) {
                    System.out.println("Advertencia: No se encontró una facultad con ese ID. El programa se creará sin facultad.");
                }
            }

            Programa nuevo = new Programa(id, nombre, duracion, registro, facultad);

            // Guardar en DB
            guardarNuevoProgramaEnDB(nuevo);

            listaDeProgramas.add(nuevo);

            System.out.println("Programa agregado correctamente: " + nuevo);
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID o la duración deben ser números válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Formato de fecha inválido. Use YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("Error al agregar programa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarProgramas() {
        if (listaDeProgramas.isEmpty()) {
            System.out.println("No hay programas registrados.");
        } else {
            System.out.println("Programas registrados:");
            for (Programa p : listaDeProgramas) {
                String facultadStr = p.getFacultad() != null ? p.getFacultad().getNombre() : "Sin facultad";
                System.out.println(" - ID: " + p.getId() +
                        ", Nombre: " + p.getNombre() +
                        ", Duración: " + p.getDuracion() +
                        ", Fecha: " + p.getRegistro() +
                        ", Facultad: " + facultadStr);
            }
        }
    }

    private static void eliminarProgramaConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID del Programa a eliminar: ");
            double id = Double.parseDouble(scanner.nextLine());

            // Buscar y eliminar de la lista observable
            Programa programaAEliminar = null;
            for (Programa p : listaDeProgramas) {
                if (p.getId() == id) {
                    programaAEliminar = p;
                    break;
                }
            }

            if (programaAEliminar == null) {
                System.out.println("No se encontró un programa con ese ID.");
                return;
            }

            listaDeProgramas.remove(programaAEliminar);


            System.out.println("Programa con ID " + id + " eliminado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar programa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para guardar programa en DB (copiado de MainWindowController)
    private static void guardarNuevoProgramaEnDB(Programa programa) {
        String sqlPrograma = "MERGE INTO PROGRAMA (id, nombre, duracion, registro, facultad_id) KEY(id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlPrograma)) {

            pstmt.setDouble(1, programa.getId());
            pstmt.setString(2, programa.getNombre());
            pstmt.setDouble(3, programa.getDuracion());
            pstmt.setDate(4, new java.sql.Date(programa.getRegistro().getTime()));
            pstmt.setDouble(5, programa.getFacultad() != null ? programa.getFacultad().getId() : 0);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listarPersonas() {
        if (servicioPersonas.listado.isEmpty()) {
            System.out.println("No hay personas registradas.");
        } else {
            System.out.println("Personas registradas:");
            for (Persona p : servicioPersonas.listado) {
                System.out.println(" - " + p);
            }
        }
    }


    // --- Método privado para manejar el submenú de Cursos ---
    private static void gestionarCursos(Scanner scanner) {
        boolean enSubMenu = true;
        while (enSubMenu) {
            System.out.println("\n=============================================");
            System.out.println("        --- Gestión de Cursos ---            ");
            System.out.println("=============================================");
            System.out.println("1. Agregar Curso");
            System.out.println("2. Listar Cursos");
            System.out.println("3. Eliminar Curso");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    agregarCursoConsola(scanner);
                    break;
                case "2":
                    listarCursos();
                    break;
                case "3":
                    eliminarCursoConsola(scanner);
                    break;
                case "4":
                    enSubMenu = false;
                    System.out.println("Regresando al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    private static void agregarCursoConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID del Curso (número entero): ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese Nombre del Curso: ");
            String nombre = scanner.nextLine();

            System.out.print("¿El curso está activo? (s/n): ");
            String activoStr = scanner.nextLine().trim().toLowerCase();
            boolean activo = activoStr.equals("s") || activoStr.equals("si") || activoStr.equals("y") || activoStr.equals("yes");

            // Para simplificar, asignaremos un programa existente por ID.
            System.out.print("Ingrese ID del Programa (número, 0 si no tiene): ");
            double programaId = Double.parseDouble(scanner.nextLine());

            Programa programa = null;
            if (programaId != 0) {
                // Buscar programa en listaDeProgramas
                for (Programa p : listaDeProgramas) {
                    if (p.getId() == programaId) {
                        programa = p;
                        break;
                    }
                }
                if (programa == null) {
                    System.out.println("Advertencia: No se encontró un programa con ese ID. El curso se creará sin programa.");
                }
            }

            Curso nuevo = new Curso(id, nombre, activo);
            if (programa != null) {
                nuevo.setPrograma(programa);
            }

            // Guardar en DB
            guardarNuevoCursoEnDB(nuevo);

            listaDeCursos.add(nuevo);

            System.out.println("Curso agregado correctamente: " + nuevo);
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID del curso debe ser un número entero válido, y el ID del programa un número válido.");
        } catch (Exception e) {
            System.out.println(" Error al agregar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarCursos() {
        if (listaDeCursos.isEmpty()) {
            System.out.println("No hay cursos registrados.");
        } else {
            System.out.println("Cursos registrados:");
            for (Curso c : listaDeCursos) {
                String programaStr = c.getPrograma() != null ? c.getPrograma().getNombre() : "Sin programa";
                System.out.println(" - ID: " + c.getId() +
                        ", Nombre: " + c.getNombre() +
                        ", Activo: " + (c.isActivo() ? "Sí" : "No") +
                        ", Programa: " + programaStr);
            }
        }
    }

    private static void eliminarCursoConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID del Curso a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Buscar y eliminar de la lista observable
            Curso cursoAEliminar = null;
            for (Curso c : listaDeCursos) {
                if (c.getId() == id) {
                    cursoAEliminar = c;
                    break;
                }
            }

            if (cursoAEliminar == null) {
                System.out.println("No se encontró un curso con ese ID.");
                return;
            }

            listaDeCursos.remove(cursoAEliminar);



            System.out.println("Curso con ID " + id + " eliminado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero válido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar curso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para guardar curso en DB (copiado de MainWindowController)
    private static void guardarNuevoCursoEnDB(Curso curso) {
        String sqlCurso = "MERGE INTO CURSO (id, nombre, programa_id, activo) KEY(id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlCurso)) {

            pstmt.setInt(1, curso.getId());
            pstmt.setString(2, curso.getNombre());
            pstmt.setDouble(3, curso.getPrograma() != null ? curso.getPrograma().getId() : 0);
            pstmt.setBoolean(4, curso.isActivo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Método privado para manejar el submenú de Inscripciones ---
    private static void gestionarInscripciones(Scanner scanner) {
        boolean enSubMenu = true;
        while (enSubMenu) {
            System.out.println("\n=============================================");
            System.out.println("        --- Gestión de Inscripciones ---     ");
            System.out.println("=============================================");
            System.out.println("1. Realizar Inscripción");
            System.out.println("2. Listar Inscripciones");
            System.out.println("3. Eliminar Inscripción");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    realizarInscripcionConsola(scanner);
                    break;
                case "2":
                    listarInscripciones();
                    break;
                case "3":
                    eliminarInscripcionConsola(scanner);
                    break;
                case "4":
                    enSubMenu = false;
                    System.out.println("Regresando al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    private static void realizarInscripcionConsola(Scanner scanner) {
        try {

            System.out.print("Ingrese ID del Estudiante: ");
            double estudianteId = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese ID del Curso: ");
            int cursoId = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese Año: ");
            int ano = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese Semestre: ");
            int semestre = Integer.parseInt(scanner.nextLine());

            // Buscar estudiante y curso
            Estudiante estudiante = null;
            for (Estudiante e : listaDeEstudiantes) {
                if (e.getId() == estudianteId) {
                    estudiante = e;
                    break;
                }
            }

            Curso curso = null;
            for (Curso c : listaDeCursos) {
                if (c.getId() == cursoId) {
                    curso = c;
                    break;
                }
            }

            if (estudiante == null) {
                System.out.println("Error: No se encontró un estudiante con ese ID.");
                return;
            }

            if (curso == null) {
                System.out.println("Error: No se encontró un curso con ese ID.");
                return;
            }

            Inscripcion nueva = new Inscripcion(estudiante, curso, ano, semestre);


            guardarNuevaInscripcionEnDB(nueva);


            inscripcionesData.add(nueva);

            System.out.println("Inscripción realizada correctamente: " + nueva);
        } catch (NumberFormatException e) {
            System.out.println("Error: Los IDs, año y semestre deben ser números válidos.");
        } catch (Exception e) {
            System.out.println("Error al realizar inscripción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarInscripciones() {
        if (inscripcionesData.isEmpty()) {
            System.out.println("No hay inscripciones registradas.");
        } else {
            System.out.println("Inscripciones registradas:");
            for (Inscripcion i : inscripcionesData) {
                System.out.println(" - ID: " + i.getId() +
                        ", Estudiante: " + i.getEstudiante().getNombres() + " " + i.getEstudiante().getApellidos() +
                        ", Curso: " + i.getCurso().getNombre() +
                        ", Año: " + i.getAno() +
                        ", Semestre: " + i.getSemestre());
            }
        }
    }

    private static void eliminarInscripcionConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID de la Inscripción a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Buscar y eliminar de la lista observable
            Inscripcion inscripcionAEliminar = null;
            for (Inscripcion i : inscripcionesData) {
                if (i.getId() == id) {
                    inscripcionAEliminar = i;
                    break;
                }
            }

            if (inscripcionAEliminar == null) {
                System.out.println("No se encontró una inscripción con ese ID.");
                return;
            }

            // Eliminar de la lista observable. ¡La GUI se actualizará automáticamente!
            inscripcionesData.remove(inscripcionAEliminar);

            // Opcional: Eliminar de la base de datos
            eliminarInscripcionDeDB(id);

            System.out.println("Inscripción con ID " + id + " eliminada correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número entero válido.");
        } catch (Exception e) {
            System.out.println("Error al eliminar inscripción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para guardar inscripción en DB
    private static void guardarNuevaInscripcionEnDB(Inscripcion inscripcion) {
        String sql = "INSERT INTO INSCRIPCION (ano, semestre, estudiante_id, curso_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, inscripcion.getAno());
            pstmt.setInt(2, inscripcion.getSemestre());
            pstmt.setDouble(3, inscripcion.getEstudiante().getId());
            pstmt.setInt(4, inscripcion.getCurso().getId());

            pstmt.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para eliminar inscripción de DB
    private static void eliminarInscripcionDeDB(int id) {
        String sql = "DELETE FROM INSCRIPCION WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}