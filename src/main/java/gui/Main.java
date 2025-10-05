package gui;

import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        // 1. Se crea una única instancia del controlador para compartirla.
        UniversityController universityController = new UniversityController();

        // 2. Se pasa el controlador a la GuiApp antes de que JavaFX la inicie.
        GuiApp.setUniversityController(universityController);

        // 3. Se crea y se inicia la aplicación de consola en un nuevo hilo.
        //    Esto es crucial para no bloquear la interfaz gráfica.
        Thread consoleThread = new Thread(() -> {
            ConsoleApp consoleApp = new ConsoleApp(universityController);
            consoleApp.start();
        });
        consoleThread.setDaemon(true); // El hilo de la consola terminará si la GUI se cierra.
        consoleThread.start();

        // 4. Se lanza la aplicación de JavaFX (GUI).
        Application.launch(GuiApp.class, args);
    }
}