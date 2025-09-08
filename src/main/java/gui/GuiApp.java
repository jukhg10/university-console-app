package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Curso;
import model.Estudiante;
import model.Inscripcion;
import model.Persona;
import persistence.DatabaseManager;
import services.CursosInscritos;
import services.InscripcionesPersonas;

import java.io.IOException;

public class GuiApp extends Application {

    /**
     * The main method now only launches the GUI.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method starts the GUI and then immediately starts the console script on a new thread.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // First, set up and show the GUI window as fast as possible.
        System.out.println("--- Launching GUI Mode ---");
        DatabaseManager.initializeDatabase();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Gestor Universitario");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // --- NEW CODE TO RUN CONSOLE AT THE SAME TIME ---
        System.out.println("--- Starting background console script... ---");
        // Create a new thread that will run our console logic.
        Thread consoleThread = new Thread(() -> {
            runConsoleMode();
        });
        
        // This makes sure the console script stops if you close the GUI window.
        consoleThread.setDaemon(true); 
        
        // Start the background thread.
        consoleThread.start();
    }

    /**
     * This method contains your console logic. It will now run in the background.
     */
    public static void runConsoleMode() {
        System.out.println("=============================================");
        System.out.println("--- University App Console Test Script ---");
        System.out.println("=============================================");

        // NOTE: We don't initialize the database here anymore, 'start()' does it.

        System.out.println("\n[STEP 1] Testing Database Persistence (Inscripciones)...");
        CursosInscritos servicioInscripciones = new CursosInscritos();
        servicioInscripciones.cargarDatos();
        System.out.println(" -> Initial inscriptions loaded: " + servicioInscripciones.cantidadActual());

        System.out.println("\n[STEP 2] Testing In-Memory Logic (Personas)...");
        InscripcionesPersonas servicioPersonas = new InscripcionesPersonas();
        servicioPersonas.inscribir(new Persona(1, "Ana", "Gómez", "ana@email.com"));
        servicioPersonas.inscribir(new Persona(2, "Luis", "Mendoza", "luis@email.com"));
        System.out.println(" -> Final list of in-memory personas:");
        for (Persona p : servicioPersonas.listado) {
            System.out.println("    - " + p);
        }

        System.out.println("\n=============================================");
        System.out.println("--- Console Script Finished ---");
        System.out.println("=============================================");
    }
}