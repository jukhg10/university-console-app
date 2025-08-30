package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.DatabaseManager;

import java.io.IOException;

public class GuiApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Inicializar la base de datos al arrancar
        System.out.println("Iniciando la base de datos...");
        DatabaseManager.initializeDatabase();
        System.out.println("Base de datos lista.");

        // Cargar la vista principal desde el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindow.fxml"));
        Parent root = loader.load();

        // Configurar la escena y el escenario (la ventana)
        primaryStage.setTitle("Gestor Universitario");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}