// gui/CursoLogController.java
package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Curso;
import observer.CursoObserver;

public class CursoLogController implements CursoObserver {

    @FXML private ListView<String> logListView;

    private final ObservableList<String> logEntries = FXCollections.observableArrayList();
    private Stage stage;

    @FXML
    public void initialize() {
        logListView.setItems(logEntries);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onCursoModificado(Curso curso) {
        String mensaje = String.format("[%s] El curso '%s' (ID: %d) ha sido modificado.",
                java.time.LocalDateTime.now(),
                curso.getNombre(),
                curso.getId()
        );
        logEntries.add(mensaje);
        logListView.scrollTo(logEntries.size() - 1);
    }

    @Override
    public void onCursoEliminado(Curso curso) { // <-- Nuevo método
        String mensaje = String.format("[%s] El curso '%s' (ID: %d) ha sido eliminado.",
                java.time.LocalDateTime.now(),
                curso.getNombre(),
                curso.getId()
        );
        logEntries.add(mensaje);
        logListView.scrollTo(logEntries.size() - 1);
    }

    @FXML
    private void handleCerrar() {
        stage.close();
    }
}