// gui/Main.java
package gui;

import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        // 1. Se crea y se inicia la aplicación de consola en un nuevo hilo.
        Thread consoleThread = new Thread(() -> {
            // The ConsoleApp now gets the controller instance itself.
            ConsoleApp consoleApp = new ConsoleApp();
            consoleApp.start();
        });
        consoleThread.setDaemon(true);
        consoleThread.start();

        // 2. Launch the JavaFX application.
        Application.launch(GuiApp.class, args);

        // This code executes AFTER the GUI is closed.
        System.out.println("La aplicación ha terminado.");
    }
}