package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
    }
}