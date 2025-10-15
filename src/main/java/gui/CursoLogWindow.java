// gui/CursoLogWindow.java
package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CursoLogWindow {

    private CursoLogController controller;
    private Stage stage;

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CursoLogWindow.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        
        stage = new Stage();
        controller.setStage(stage); // Pass the stage to the controller
        stage.setTitle("Registro de Cambios en Cursos");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void registerObserver() {
        // Register the window's controller as an observer in the DAO
        UniversityController universityController = UniversityController.getInstance();
        if (universityController != null) {
            universityController.getServicios().getCursoDAO().addObserver(controller);
        }
    }

    public CursoLogController getController() {
        return controller;
    }
}