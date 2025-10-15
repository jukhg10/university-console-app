// gui/GuiApp.java
package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GuiApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindow.fxml"));
        Parent root = loader.load();

        // The MainWindowController now gets the Singleton instance on its own.
        // We no longer need to inject it here.

        primaryStage.setTitle("Gestor Universitario");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // Launch the log window after the main GUI is shown.
        Platform.runLater(() -> {
            try {
                CursoLogWindow logWindow = new CursoLogWindow();
                logWindow.show();
                // The log window will get the controller instance on its own
                // and register itself as an observer.
                logWindow.registerObserver();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}