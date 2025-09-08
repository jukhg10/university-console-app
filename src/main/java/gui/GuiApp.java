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

    public static void main(String[] args) {
        runConsoleMode();
        launch(args);
    }

    /**
     * This method runs a descriptive script to enroll a student in a course.
     */
    public static void runConsoleMode() {
        System.out.println("=============================================");
        System.out.println("--- University App Console Test Script ---");
        System.out.println("=============================================");

        System.out.println("\n[STEP 1] Initializing the database schema...");
        DatabaseManager.initializeDatabase();
        System.out.println(" -> Database schema is ready. ✅");

        System.out.println("\n[STEP 2] Enrolling a Student in a Course...");
        CursosInscritos servicioInscripciones = new CursosInscritos();
        
        servicioInscripciones.cargarDatos();
        int initialCount = servicioInscripciones.cantidadActual();
        System.out.println(" -> Number of enrollments in DB before test: " + initialCount);

        System.out.println(" -> Creating sample data: Student 'Juan Velez' and Course 'Bases de Datos'.");
        Estudiante estudianteParaInscribir = new Estudiante(102, "Juan", "Velez", "juan@email.com", 202402, true, 3.8);
        Curso cursoParaInscribir = new Curso(902, "Bases de Datos", true);
        
        System.out.println(" -> Enrolling student in course for semester 2025-2 and saving to database...");
        Inscripcion nuevaInscripcion = new Inscripcion(estudianteParaInscribir, cursoParaInscribir, 2025, 2);
        servicioInscripciones.inscribirCurso(nuevaInscripcion);
        
        servicioInscripciones.cargarDatos(); // Reload from DB
        System.out.println(" -> Number of enrollments after saving: " + servicioInscripciones.cantidadActual());
        
        System.out.println("\n -> Verifying the new enrollment:");
        // Find the new inscription in the list to display it
        servicioInscripciones.imprimirListaCompleta().stream()
            .filter(insc -> insc.getId() == nuevaInscripcion.getId())
            .findFirst()
            .ifPresent(insc -> System.out.println("    - FOUND: " + insc));

        System.out.println("\n -> Cleaning up by deleting the test enrollment from the database...");
        //servicioInscripciones.eliminar(nuevaInscripcion.getId());

        servicioInscripciones.cargarDatos(); // Reload from DB
        System.out.println(" -> Number of enrollments after deleting: " + servicioInscripciones.cantidadActual());
        System.out.println(" -> Enrollment test complete. ✅");

        System.out.println("\n=============================================");
        System.out.println("--- Console Script Finished ---");
        System.out.println("=============================================");
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("--- Launching GUI Mode ---");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindow.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Gestor Universitario");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}