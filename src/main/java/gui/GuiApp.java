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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class GuiApp extends Application {

    /**
     * The main method now only launches the GUI.
     */
    // ✅ MANTÉN esta declaración. Es la variable que usarán los métodos de consola.
    public static InscripcionesPersonas servicioPersonas;
    public static ObservableList<Facultad> listaDeFacultades;
    public static ObservableList<Programa> listaDeProgramas;

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
        servicioPersonas = controller.getServicioPersonas(); // ← Esto asigna la instancia que ya existe en el controlador
        listaDeFacultades = controller.getListaDeFacultades();
        listaDeProgramas = controller.getListaDeProgramas();
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
            System.out.println("        --- MENÚ PRINCIPAL UNIVERSIDAD ---   ");
            System.out.println("=============================================");
            System.out.println("1. Gestión de Personas");
            System.out.println("2. Gestión de Facultades");
            System.out.println("3. Gestión de Programas");
            System.out.println("4. Gestión de Cursos");
            System.out.println("5. Gestión de Inscripciones");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            String opcionPrincipal = scanner.nextLine();

            switch (opcionPrincipal) {
                case "1":
                    // --- SUBMENÚ: PERSONAS ---
                    gestionarPersonas(scanner);
                    break;
                case "2":
                    gestionarFacultades(scanner);
                    // Aquí iría la lógica futura para facultades
                    break;
                case "3":
                    gestionarProgramas(scanner);
                    break;
                case "4":
                    System.out.println("\n--- Módulo de Cursos (Próximamente) ---");
                    // Aquí iría la lógica futura para cursos
                    break;
                case "5":
                    System.out.println("\n--- Módulo de Inscripciones (Próximamente) ---");
                    // Aquí iría la lógica futura para inscripciones
                    break;
                case "6":
                    running = false;
                    System.out.println("Saliendo del menú de consola...");
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    // --- Método privado para manejar el submenú de Personas ---
    private static void gestionarPersonas(Scanner scanner) {
        boolean enSubMenu = true;
        while (enSubMenu) {
            System.out.println("\n=============================================");
            System.out.println("        --- Gestión de Personas ---          ");
            System.out.println("=============================================");
            System.out.println("1. Inscribir Persona");
            System.out.println("2. Listar Personas");
            System.out.println("3. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    inscribirPersonaConsola(scanner);
                    break;
                case "2":
                    listarPersonas();
                    break;
                case "3":
                    enSubMenu = false; // Sale del submenú y vuelve al menú principal
                    System.out.println("Regresando al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    private static void gestionarFacultades(Scanner scanner) {
        boolean enSubMenu = true;
        while (enSubMenu) {
            System.out.println("\n=============================================");
            System.out.println("        --- Gestión de Facultades ---        ");
            System.out.println("=============================================");
            System.out.println("1. Agregar Facultad");
            System.out.println("2. Listar Facultades");
            System.out.println("3. Eliminar Facultad");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

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
                    System.out.println("Regresando al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    private static void inscribirPersonaConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID (número): ");
            double id = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese Nombres: ");
            String nombres = scanner.nextLine();

            System.out.print("Ingrese Apellidos: ");
            String apellidos = scanner.nextLine();

            System.out.print("Ingrese Email: ");
            String email = scanner.nextLine();

            Persona nueva = new Persona(id, nombres, apellidos, email);

            // Guarda en memoria y en la DB
            servicioPersonas.inscribir(nueva);
            DatabaseManager.guardarPersona(nueva); // 👈 Si ya tienes este método implementado

            System.out.println("✅ Persona inscrita correctamente: " + nueva);
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("❌ Error al inscribir persona: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void agregarFacultadConsola(Scanner scanner) {
        try {
            System.out.print("Ingrese ID de la Facultad (número): ");
            double id = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese Nombre de la Facultad: ");
            String nombre = scanner.nextLine();

            // Para simplificar, asignaremos un decano existente por ID.
            System.out.print("Ingrese ID del Decano ");
            double decanoId = Double.parseDouble(scanner.nextLine());

            Persona decano = null;
            if (decanoId != 0) {
                // Buscar decano en servicioPersonas
                for (Persona p : servicioPersonas.listado) {
                    if (p.getId() == decanoId) {
                        decano = p;
                        break;
                    }
                }
                if (decano == null) {
                    System.out.println("❌ Advertencia: No se encontró una persona con ese ID. La facultad se creará sin decano.");
                }
            }

            Facultad nueva = new Facultad(id, nombre);
            if (decano != null) {
                nueva.setDecano(decano);
            }

            // Guardar en DB
            guardarNuevaFacultadEnDB(nueva);

            // ✅ Agregar a la lista observable compartida. ¡La GUI se actualizará automáticamente!
            listaDeFacultades.add(nueva);

            System.out.println("✅ Facultad agregada correctamente: " + nueva);
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("❌ Error al agregar facultad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarFacultades() {
        if (listaDeFacultades.isEmpty()) {
            System.out.println("No hay facultades registradas.");
        } else {
            System.out.println("📋 Facultades registradas:");
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
                System.out.println("❌ No se encontró una facultad con ese ID.");
                return;
            }

            // ✅ Eliminar de la lista observable. ¡La GUI se actualizará automáticamente!
            listaDeFacultades.remove(facultadAEliminar);

            // Opcional: Eliminar de la base de datos (debes implementar este método en DatabaseManager si lo deseas)
            // eliminarFacultadDeDB(id);

            System.out.println("✅ Facultad con ID " + id + " eliminada correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar facultad: " + e.getMessage());
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
                    System.out.println("❌ Advertencia: No se encontró una facultad con ese ID. El programa se creará sin facultad.");
                }
            }

            Programa nuevo = new Programa(id, nombre, duracion, registro, facultad);

            // Guardar en DB
            guardarNuevoProgramaEnDB(nuevo);

            // ✅ Agregar a la lista observable compartida. ¡La GUI se actualizará automáticamente!
            listaDeProgramas.add(nuevo);

            System.out.println("✅ Programa agregado correctamente: " + nuevo);
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El ID o la duración deben ser números válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error: Formato de fecha inválido. Use YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("❌ Error al agregar programa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarProgramas() {
        if (listaDeProgramas.isEmpty()) {
            System.out.println("No hay programas registrados.");
        } else {
            System.out.println("📋 Programas registrados:");
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
                System.out.println("❌ No se encontró un programa con ese ID.");
                return;
            }

            // ✅ Eliminar de la lista observable. ¡La GUI se actualizará automáticamente!
            listaDeProgramas.remove(programaAEliminar);

            // Opcional: Eliminar de la base de datos (debes implementar este método en DatabaseManager si lo deseas)
            // eliminarProgramaDeDB(id);

            System.out.println("✅ Programa con ID " + id + " eliminado correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: El ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar programa: " + e.getMessage());
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
            System.out.println("📋 Personas registradas:");
            for (Persona p : servicioPersonas.listado) {
                System.out.println(" - " + p);
            }
        }
    }


}