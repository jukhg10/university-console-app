// gui/GuiApp.java
package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.CursoLogController;

import java.io.IOException;

public class GuiApp extends Application {

    // Se usa un campo estático para "recibir" el controlador desde la clase Main.
    private static UniversityController universityController;

    public static void setUniversityController(UniversityController controller) {
        universityController = controller;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindow.fxml"));
        Parent root = loader.load();

        MainWindowController mainWindowController = loader.getController();

        // Se inyecta el controlador que fue recibido desde Main.
        mainWindowController.setUniversityController(universityController);

        primaryStage.setTitle("Gestor Universitario");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // 5. Lanzar la ventana de logs DESPUÉS de que la GUI principal se haya mostrado
        Platform.runLater(() -> {
            try {
                CursoLogWindow logWindow = new CursoLogWindow();
                logWindow.show();
                // Registrar el controlador de la ventana como observador en el CursoDAO
                // Asumiendo que el controlador tiene acceso al servicio (esto es lo ideal)
                // O pasarle el DAO o el controlador principal para acceder al DAO
                // Por simplicidad, vamos a pasar UniversityController a la ventana
                logWindow.setUniversityController(universityController);
                logWindow.registerObserver(); // <-- Método que registra el observador en el DAO
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}