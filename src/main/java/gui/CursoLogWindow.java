// gui/CursoLogWindow.java
package gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gui.CursoLogController;

public class CursoLogWindow {

    private CursoLogController controller;
    private Stage stage;
    private UniversityController universityController;

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CursoLogWindow.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setStage(stage);

        stage = new Stage();
        stage.setTitle("Registro de Cambios en Cursos");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setUniversityController(UniversityController universityController) {
        this.universityController = universityController;
    }

    public void registerObserver() {
        // Registrar el controlador de la ventana como observador en el DAO
        if (universityController != null) {
            universityController.getServicios().getCursoDAO().addObserver(controller);
        }
    }

    public CursoLogController getController() {
        return controller;
    }
}