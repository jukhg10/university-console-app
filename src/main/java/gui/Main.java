// gui/Main.java
package gui;

import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        // 1. Se crea una única instancia del controlador para compartirla.
        UniversityController universityController = new UniversityController();

        // 2. Se pasa el controlador a la GuiApp antes de que JavaFX la inicie.
        GuiApp.setUniversityController(universityController);

        // 3. Se crea y se inicia la aplicación de consola en un nuevo hilo.
        Thread consoleThread = new Thread(() -> {
            ConsoleApp consoleApp = new ConsoleApp(universityController);
            consoleApp.start();
        });
        consoleThread.setDaemon(true);
        consoleThread.start();

        // 4. Lanzar la ventana de logs DESPUÉS de que JavaFX esté listo
        Application.launch(GuiApp.class, args);

        // Este código se ejecuta DESPUÉS de que la GUI se cierre
        System.out.println("La aplicación ha terminado.");
    }
}