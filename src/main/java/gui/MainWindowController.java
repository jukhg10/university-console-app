package gui;

import dto.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainWindowController {
    private UniversityController universityController;

    // --- FXML Injected Fields ---
    @FXML private Button modificarCursoBtn;
    @FXML private Button eliminarProgramaBtn;
    @FXML private Button eliminarFacultadBtn;
    @FXML private Button eliminarPersonaBtn;
    @FXML private TableView<InscripcionDTO> inscripcionesTable; // <-- Cambiado a DTO
    @FXML private TabPane mainTabPane;
    @FXML private TableColumn<InscripcionDTO, Integer> idCol; // <-- Cambiado a DTO
    @FXML private TableColumn<InscripcionDTO, String> estudianteCol; // <-- Cambiado a DTO
    @FXML private TableColumn<InscripcionDTO, String> cursoCol; // <-- Cambiado a DTO
    @FXML private TableColumn<InscripcionDTO, Integer> anoCol; // <-- Cambiado a DTO
    @FXML private TableColumn<InscripcionDTO, Integer> semestreCol; // <-- Cambiado a DTO
    @FXML private Button eliminarInscripcionBtn;
    @FXML private TableView<EstudianteDTO> estudiantesTable;
    @FXML private TableColumn<EstudianteDTO, Double> estIdCol;
    @FXML private TableColumn<EstudianteDTO, Double> estCodigoCol;
    @FXML private TableColumn<EstudianteDTO, String> estNombresCol;
    @FXML private TableColumn<EstudianteDTO, String> estApellidosCol;
    @FXML private TableColumn<EstudianteDTO, String> estEmailCol;
    @FXML private Button eliminarEstudianteBtn;
    @FXML private TableView<Profesor> profesoresTable;
    @FXML private TableColumn<Profesor, Double> profIdCol;
    @FXML private TableColumn<Profesor, String> profNombresCol;
    @FXML private TableColumn<Profesor, String> profApellidosCol;
    @FXML private TableColumn<Profesor, String> profEmailCol;
    @FXML private TableColumn<Profesor, String> profContratoCol;
    @FXML private Button eliminarProfesorBtn;
    @FXML private TableView<CursoProfesorDTO> asignacionesTable; // <-- Cambiado de CursoProfesor a CursoProfesorDTO
    @FXML
    private TableColumn<CursoProfesorDTO, String> asignacionProfesorColumn;
    @FXML
    private TableColumn<CursoProfesorDTO, String> asignacionCursoColumn;
    @FXML
    private TableColumn<CursoProfesorDTO, Integer> asignacionAnoColumn;
    @FXML
    private TableColumn<CursoProfesorDTO, Integer> asignacionSemestreColumn;
    @FXML
    private TableColumn<CursoProfesorDTO, String> asigProfesorCol;
    @FXML
    private TableColumn<CursoProfesorDTO, String> asigCursoCol;
    @FXML private Button eliminarAsignacionBtn;
    @FXML private TableView<PersonaDTO> personasTable;
    @FXML private TableColumn<PersonaDTO, Double> personaIdCol;
    @FXML private TableColumn<PersonaDTO, String> personaNombresCol;
    @FXML private TableColumn<PersonaDTO, String> personaApellidosCol;
    @FXML private TableColumn<PersonaDTO, String> personaEmailCol;
    @FXML private TableView<FacultadDTO> facultadesTable;
    @FXML private TableColumn<FacultadDTO, Double> facultadIdCol;
    @FXML private TableColumn<FacultadDTO, String> facultadNombreCol;
    @FXML private TableColumn<FacultadDTO, String> facultadDecanoCol;
    @FXML private TableView<ProgramaDTO> programasTable;
    @FXML private TableColumn<ProgramaDTO, Double> programaIdCol;
    @FXML private TableColumn<ProgramaDTO, String> programaNombreCol;
    @FXML private TableColumn<ProgramaDTO, Double> programaDuracionCol;
    @FXML private TableColumn<ProgramaDTO, String> programaFechaCol;
    @FXML private TableColumn<ProgramaDTO, String> programaFacultadCol;
    @FXML private TableView<CursoDTO> cursosTable; // <-- Cambiado a DTO
    @FXML private TableColumn<CursoDTO, Integer> cursoIdCol; // <-- Cambiado a DTO
    @FXML private TableColumn<CursoDTO, String> cursoNombreCol; // <-- Cambiado a DTO
    @FXML private TableColumn<CursoDTO, String> cursoProgramaCol; // <-- Cambiado a DTO
    @FXML private TableColumn<CursoDTO, String> cursoActivoCol; // <-- Cambiado a DTO
    @FXML private Button eliminarCursoBtn;
    @FXML
    private TableColumn<CursoProfesorDTO, Integer> asigAnoCol;  // <-- Esta línea es necesaria
    @FXML
    private TableColumn<CursoProfesorDTO, Integer> asigSemestreCol;

    @FXML
    public void initialize() {
        // Get the singleton instance of the controller
        this.universityController = UniversityController.getInstance();
        configurarTodasLasTablas();
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if ("Inscripciones".equals(newTab.getText())) {
                universityController.recargarInscripciones();
                Platform.runLater(() -> inscripcionesTable.refresh()); // Forzar actualización visual
            } else if ("Asignaciones".equals(newTab.getText())) { // <-- Nueva condición
                universityController.recargarAsignaciones();       // <-- Recarga asignaciones
                Platform.runLater(() -> asignacionesTable.refresh()); // Forzar actualización visual
            } else if ("Cursos".equals(newTab.getText())) { // <-- Nueva condición para recargar cursos
                universityController.recargarCursos();       // <-- Recarga cursos como DTO
                Platform.runLater(() -> cursosTable.refresh()); // Forzar actualización visual
            }
        });
    }

    private void configurarTodasLasTablas() {
        cursosTable.getSelectionModel().selectedItemProperty().addListener((obs, old, nuev) -> eliminarCursoBtn.setDisable(nuev == null));
        // Bind the table data sources to the lists in the UniversityController
        personasTable.setItems(universityController.getPersonasData());
        cursosTable.setItems(universityController.getListaDeCursos()); // <-- Ahora usa DTOs
        programasTable.setItems(universityController.getListaDeProgramas());
        facultadesTable.setItems(universityController.getListaDeFacultades());
        inscripcionesTable.setItems(universityController.getInscripcionesData()); // <-- Ahora usa DTOs
        profesoresTable.setItems(universityController.getProfesoresData());
        estudiantesTable.setItems(universityController.getEstudiantesData());
        asignacionesTable.setItems(universityController.getAsignacionesData());
        facultadesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, nuev) -> eliminarFacultadBtn.setDisable(nuev == null));
        programasTable.getSelectionModel().selectedItemProperty().addListener((obs, old, nuev) -> eliminarProgramaBtn.setDisable(nuev == null));
        cursosTable.getSelectionModel().selectedItemProperty().addListener((obs, old, nuev) -> modificarCursoBtn.setDisable(nuev == null));

        // Configure cell value factories for all tables
        // --- Cursos Table ---
        cursoIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cursoNombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cursoProgramaCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getPrograma() != null ? cellData.getValue().getPrograma().getNombre() : "Sin programa"
                )
        );
        cursoActivoCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isActivo() ? "Sí" : "No")
        );

        // --- Programas Table ---
        programaIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        programaNombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        programaDuracionCol.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        programaFechaCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRegistro() != null ? cellData.getValue().getRegistro().toString() : "")
        );
        programaFacultadCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getFacultad() != null ? cellData.getValue().getFacultad().getNombre() : "Sin facultad"
                )
        );

        // --- Facultades Table ---
        facultadIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        facultadNombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        facultadDecanoCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getDecano() != null ? cellData.getValue().getDecano().getNombres() + " " + cellData.getValue().getDecano().getApellidos() : "Sin decano"
                )
        );

        // --- Inscripciones Table ---
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        estudianteCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getEstudiante() != null ? cellData.getValue().getEstudiante().toString() : "Estudiante desconocido"
        ));
        cursoCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCurso() != null ? cellData.getValue().getCurso().toString() : "Curso desconocido"
        ));
        anoCol.setCellValueFactory(new PropertyValueFactory<>("ano"));
        semestreCol.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        inscripcionesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, anew) -> eliminarInscripcionBtn.setDisable(anew == null));

        // --- Estudiantes Table ---
        estIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        estCodigoCol.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        estNombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        estApellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        estEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        estudiantesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, anew) -> eliminarEstudianteBtn.setDisable(anew == null));

        // --- Profesores Table ---
        profIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        profNombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        profApellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        profEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        profContratoCol.setCellValueFactory(new PropertyValueFactory<>("tipoContrato"));
        profesoresTable.getSelectionModel().selectedItemProperty().addListener((obs, old, anew) -> eliminarProfesorBtn.setDisable(anew == null));

        // --- Asignaciones Table ---
        asigProfesorCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getProfesorId())));
        asigCursoCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCursoId())));
        asigAnoCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getAno()).asObject());
        asigSemestreCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getSemestre()).asObject());
        asignacionesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, anew) -> eliminarAsignacionBtn.setDisable(anew == null));

        // --- Personas Table ---
        personaIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        personaNombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        personaApellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        personaEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        personasTable.getSelectionModel().selectedItemProperty().addListener((obs, old, nuev) -> eliminarPersonaBtn.setDisable(nuev == null));
    }

    // --- Event Handlers ---
    @FXML
    private void handleEliminarPersona() {
        PersonaDTO seleccionada = personasTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar la persona?");
            confirmacion.setContentText("Persona: " + seleccionada.getNombres() + " " + seleccionada.getApellidos() + " (ID: " + seleccionada.getId() + ")");
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = universityController.eliminarPersona(seleccionada);
                if (!exito) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText("No se pudo eliminar la persona.");
                    alert.setContentText("La persona tiene dependencias es decano. Elimine las dependencias primero.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna persona seleccionada");
            alert.setHeaderText("No se ha seleccionado ninguna persona para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleEliminarCurso() {
        CursoDTO seleccionado = cursosTable.getSelectionModel().getSelectedItem(); // <-- Cambiado a DTO
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar el curso?");
            confirmacion.setContentText("Curso: " + seleccionado.getNombre() + " (ID: " + seleccionado.getId() + ")");
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = universityController.eliminarCurso(seleccionado); // <-- Cambiado a DTO
                if (!exito) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText("No se pudo eliminar el curso.");
                    alert.setContentText("El curso tiene inscripciones asociadas. Elimine las inscripciones primero.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún curso seleccionado");
            alert.setHeaderText("No se ha seleccionado ningún curso para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAgregarCurso() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CursoDialog.fxml"));
            DialogPane dialogPane = loader.load();
            // Use direct field access instead of lookup
            // TextField idField = (TextField) dialogPane.lookup("#idField"); <-- Ya no necesitamos el ID
            TextField nombreField = (TextField) dialogPane.lookup("#nombreField");
            ComboBox<ProgramaDTO> programaComboBox = (ComboBox<ProgramaDTO>) dialogPane.lookup("#programaComboBox"); // <-- Cambiado a DTO
            CheckBox activoCheckBox = (CheckBox) dialogPane.lookup("#activoCheckBox");
            programaComboBox.getItems().setAll(universityController.getListaDeProgramas()); // <-- Ahora usa DTOs
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Curso");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // int id = Integer.parseInt(idField.getText().trim()); <-- Ya no leemos el ID
                String nombre = nombreField.getText().trim();
                ProgramaDTO programa = programaComboBox.getValue(); // <-- Cambiado a DTO
                boolean activo = activoCheckBox.isSelected();
                // Creamos el DTO del curso
                CursoDTO nuevo = new CursoDTO(0, nombre, programa, activo); // <-- ID temporal (0), usa DTO
                // Verificar si el curso se agregó correctamente
                boolean exito = universityController.agregarCurso(nuevo); // <-- Cambiado a DTO
                if (!exito) {
                    // Mostrar un mensaje de error al usuario
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al agregar curso");
                    alert.setHeaderText("No se pudo agregar el curso.");
                    alert.setContentText("Ocurrió un error al intentar guardarlo.");
                    alert.showAndWait();
                }
            }
        } catch (NumberFormatException e) {
            // Manejar el caso donde el ID no es un número válido
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de formato");
            alert.setHeaderText("El ID ingresado no es válido.");
            alert.setContentText("Por favor, ingresa un número entero para el ID.");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // gui/MainWindowController.java
    @FXML
    private void handleModificarCurso() {
        CursoDTO seleccionado = cursosTable.getSelectionModel().getSelectedItem(); // <-- Cambiado a DTO
        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CursoDialog.fxml"));
                DialogPane dialogPane = loader.load();
                // Accedemos a los campos del diálogo
                TextField nombreField = (TextField) dialogPane.lookup("#nombreField");
                ComboBox<ProgramaDTO> programaComboBox = (ComboBox<ProgramaDTO>) dialogPane.lookup("#programaComboBox"); // <-- Cambiado a DTO
                CheckBox activoCheckBox = (CheckBox) dialogPane.lookup("#activoCheckBox");
                // Cargamos los datos del curso seleccionado en los campos
                nombreField.setText(seleccionado.getNombre());
                programaComboBox.getItems().setAll(universityController.getListaDeProgramas()); // <-- Ahora usa DTOs
                programaComboBox.setValue(seleccionado.getPrograma()); // <-- Cambiado a DTO
                activoCheckBox.setSelected(seleccionado.isActivo());
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(dialogPane);
                dialog.setTitle("Modificar Curso");
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Actualizamos los datos del DTO del curso con los nuevos valores
                    seleccionado.setNombre(nombreField.getText().trim());
                    seleccionado.setPrograma(programaComboBox.getValue()); // <-- Cambiado a DTO
                    seleccionado.setActivo(activoCheckBox.isSelected());
                    // Llamamos al método de actualización en UniversityController
                    boolean exito = universityController.modificarCurso(seleccionado); // <-- Cambiado a DTO
                    if (exito) {
                        // Forzar actualización visual de la tabla de cursos
                        cursosTable.refresh();
                        // Forzar actualización visual de tablas relacionadas
                        inscripcionesTable.refresh();    // <-- Actualiza la tabla de inscripciones
                        asignacionesTable.refresh();     // <-- Actualiza la tabla de asignaciones
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Modificación exitosa");
                        alert.setHeaderText("El curso ha sido modificado.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error al modificar");
                        alert.setHeaderText("No se pudo modificar el curso.");
                        alert.setContentText("Ocurrió un error al intentar guardar los cambios.");
                        alert.showAndWait();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún curso seleccionado");
            alert.setHeaderText("No se ha seleccionado ningún curso para modificar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAgregarPrograma() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ProgramaDialog.fxml"));
            DialogPane dialogPane = loader.load();
            // TextField idField = (TextField) dialogPane.lookup("#idField"); <-- Ya no necesitamos el ID
            TextField nombreField = (TextField) dialogPane.lookup("#nombreField");
            TextField duracionField = (TextField) dialogPane.lookup("#duracionField");
            DatePicker fechaPicker = (DatePicker) dialogPane.lookup("#fechaPicker");
            ComboBox<FacultadDTO> facultadComboBox = (ComboBox<FacultadDTO>) dialogPane.lookup("#facultadComboBox"); // <-- Cambiado a DTO
            facultadComboBox.getItems().setAll(universityController.getListaDeFacultades()); // <-- Ahora usa DTOs
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Programa");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // double id = Double.parseDouble(idField.getText().trim()); <-- Ya no leemos el ID
                String nombre = nombreField.getText().trim();
                double duracion = Double.parseDouble(duracionField.getText().trim());
                LocalDate localDate = fechaPicker.getValue();
                java.util.Date registro = java.sql.Date.valueOf(localDate);
                FacultadDTO facultad = facultadComboBox.getValue(); // <-- Cambiado a DTO
                // Creamos el DTO del programa
                ProgramaDTO nuevo = new ProgramaDTO(0, nombre, duracion, registro, facultad); // <-- ID temporal (0), usa DTO
                universityController.agregarPrograma(nuevo); // <-- Cambiado a DTO
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarPrograma() {
        ProgramaDTO seleccionado = programasTable.getSelectionModel().getSelectedItem(); // <-- Cambiado a DTO
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar el programa?");
            confirmacion.setContentText("Programa: " + seleccionado.getNombre() + " (ID: " + seleccionado.getId() + ")");
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = universityController.eliminarPrograma(seleccionado); // <-- Cambiado a DTO
                if (!exito) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText("No se pudo eliminar el programa.");
                    alert.setContentText("El programa tiene cursos asociados. Elimine los cursos primero.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún programa seleccionado");
            alert.setHeaderText("No se ha seleccionado ningún programa para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAgregarFacultad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/FacultadDialog.fxml"));
            DialogPane dialogPane = loader.load();
            // TextField idField = (TextField) dialogPane.lookup("#idField"); <-- Ya no necesitamos el ID
            TextField nombreField = (TextField) dialogPane.lookup("#nombreField");
            ComboBox<PersonaDTO> decanoComboBox = (ComboBox<PersonaDTO>) dialogPane.lookup("#decanoComboBox"); // <-- Cambiado a DTO
            List<Persona> decanosDisponibles = universityController.getPersonasYProfesores();
            // Convertir Persona a PersonaDTO para el ComboBox
            List<PersonaDTO> decanosDisponiblesDTO = decanosDisponibles.stream()
                    .map(PersonaMapper::toDTO) // Asumiendo que tienes un mapper para Persona
                    .collect(Collectors.toList());
            decanoComboBox.getItems().setAll(decanosDisponiblesDTO);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Facultad");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // double id = Double.parseDouble(idField.getText().trim()); <-- Ya no leemos el ID
                String nombre = nombreField.getText().trim();
                PersonaDTO decano = decanoComboBox.getValue(); // <-- Cambiado a DTO
                // Creamos el DTO de la facultad
                FacultadDTO nueva = new FacultadDTO(0, nombre, decano); // <-- ID temporal (0), usa DTO
                universityController.agregarFacultad(nueva); // <-- Cambiado a DTO
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarFacultad() {
        FacultadDTO seleccionada = facultadesTable.getSelectionModel().getSelectedItem(); // <-- Cambiado a DTO
        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar la facultad?");
            confirmacion.setContentText("Facultad: " + seleccionada.getNombre() + " (ID: " + seleccionada.getId() + ")");
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = universityController.eliminarFacultad(seleccionada); // <-- Cambiado a DTO
                if (!exito) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText("No se pudo eliminar la facultad.");
                    alert.setContentText("La facultad tiene programas asociados. Elimine los programas primero.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna facultad seleccionada");
            alert.setHeaderText("No se ha seleccionado ninguna facultad para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAgregarPersona() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PersonaDialog.fxml"));
            DialogPane dialogPane = loader.load();
            // TextField idField = (TextField) dialogPane.lookup("#idField");
            TextField nombresField = (TextField) dialogPane.lookup("#nombresField");
            TextField apellidosField = (TextField) dialogPane.lookup("#apellidosField");
            TextField emailField = (TextField) dialogPane.lookup("#emailField");
            // No generamos ID aquí, lo hará la base de datos
            //  idField.setDisable(true); // <-- Deshabilitamos el campo
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Persona");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // No leemos el ID del campo, ya que será generado por la base de datos
                String nombres = nombresField.getText().trim();
                String apellidos = apellidosField.getText().trim();
                String email = emailField.getText().trim();
                PersonaDTO nueva = new PersonaDTO();
                // nueva.setId(...) <-- No lo asignamos aquí
                nueva.setNombres(nombres);
                nueva.setApellidos(apellidos);
                nueva.setEmail(email);
                universityController.agregarPersona(nueva);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInscribir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InscripcionDialog.fxml"));
            DialogPane dialogPane = loader.load();
            ComboBox<EstudianteDTO> estudianteComboBox = (ComboBox<EstudianteDTO>) dialogPane.lookup("#estudianteComboBox");
            ComboBox<CursoDTO> cursoComboBox = (ComboBox<CursoDTO>) dialogPane.lookup("#cursoComboBox"); // <-- Cambiado a CursoDTO
            // Cargar DTOs en el ComboBox
            estudianteComboBox.getItems().setAll(universityController.getEstudiantesData());
            cursoComboBox.getItems().setAll(universityController.getListaDeCursos()); // <-- Ahora usa DTOs
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Realizar Inscripción");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonType.OK.getButtonData()) {
                EstudianteDTO estudianteDTOSeleccionado = estudianteComboBox.getValue(); // Obtenemos el DTO
                CursoDTO cursoDTOSeleccionado = cursoComboBox.getValue(); // <-- Obtenemos el DTO
                TextField anoField = (TextField) dialogPane.lookup("#anoField");
                TextField semestreField = (TextField) dialogPane.lookup("#semestreField");

                if (estudianteDTOSeleccionado != null && cursoDTOSeleccionado != null && !anoField.getText().isEmpty() && !semestreField.getText().isEmpty()) {
                    int ano = Integer.parseInt(anoField.getText());
                    int semestre = Integer.parseInt(semestreField.getText());

                    // --- CORRECCIÓN: Crear un InscripcionDTO usando CursoDTO ---
                    // No necesitas cargar la entidad Curso, solo usamos el DTO
                    InscripcionDTO nuevaInscripcionDTO = new InscripcionDTO(0, estudianteDTOSeleccionado, cursoDTOSeleccionado, ano, semestre); // <-- Usamos CursoDTO

                    // Llamar al método que ahora acepta un DTO
                    universityController.inscribirEstudiante(nuevaInscripcionDTO);
                }
            }
        } catch (NumberFormatException e) {
            // Manejar el caso donde el año o semestre no son números válidos
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de formato");
            alert.setHeaderText("El año o semestre ingresado no es válido.");
            alert.setContentText("Por favor, ingresa números enteros para el año y semestre.");
            alert.showAndWait();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleEliminarInscripcion() {
        InscripcionDTO seleccionada = inscripcionesTable.getSelectionModel().getSelectedItem(); // <-- Ahora usa DTO
        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar la inscripción?");
            // Concatenamos correctamente usando \n para el salto de línea
            confirmacion.setContentText("Estudiante: " + (seleccionada.getEstudiante() != null ?
                    seleccionada.getEstudiante().getNombres() + " " + seleccionada.getEstudiante().getApellidos() : "Desconocido") +
                    "\nCurso: " + (seleccionada.getCurso() != null ?
                    seleccionada.getCurso().getNombre() + " (ID: " + seleccionada.getCurso().getId() + ")" : "Desconocido"));
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = universityController.eliminarInscripcion(seleccionada); // <-- Ahora llama al método que acepta DTO
                if (exito) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Eliminación exitosa");
                    alert.setHeaderText("La inscripción ha sido eliminada.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna inscripción seleccionada");
            alert.setHeaderText("No se ha seleccionado ninguna inscripción para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAgregarEstudiante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/EstudianteDialog.fxml"));
            DialogPane dialogPane = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Añadir Estudiante");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonType.OK.getButtonData()) {
                TextField nombres = (TextField) dialogPane.lookup("#nombresField");
                TextField apellidos = (TextField) dialogPane.lookup("#apellidosField");
                TextField email = (TextField) dialogPane.lookup("#emailField");
                TextField codigo = (TextField) dialogPane.lookup("#codigoField");
                // No generamos ID aquí, lo hará el DAO
                EstudianteDTO nuevoDTO = new EstudianteDTO(0, nombres.getText(), apellidos.getText(), email.getText(), Double.parseDouble(codigo.getText())); // Ajusta el constructor según tu DTO
                universityController.agregarEstudiante(nuevoDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarEstudiante() {
        EstudianteDTO seleccionado = estudiantesTable.getSelectionModel().getSelectedItem(); // <-- Ahora usa DTO
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar al estudiante?");
            confirmacion.setContentText("Estudiante: " + seleccionado.getNombres() + " " + seleccionado.getApellidos() + " (ID: " + seleccionado.getId() + ")");
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = universityController.eliminarEstudiante(seleccionado); // <-- Ahora llama al método que acepta DTO
                if (exito) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Eliminación exitosa");
                    alert.setHeaderText("El estudiante ha sido eliminado.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText("No se pudo eliminar al estudiante.");
                    alert.setContentText("Ocurrió un error al intentar eliminar al estudiante.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún estudiante seleccionado");
            alert.setHeaderText("No se ha seleccionado ningún estudiante para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAgregarProfesor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ProfesorDialog.fxml"));
            DialogPane dialogPane = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Añadir Nuevo Profesor");
            Optional<ButtonType> resultado = dialog.showAndWait();
            if (resultado.isPresent() && resultado.get().getButtonData() == ButtonType.OK.getButtonData()) {
                TextField nombresField = (TextField) dialogPane.lookup("#nombresField");
                TextField apellidosField = (TextField) dialogPane.lookup("#apellidosField");
                TextField emailField = (TextField) dialogPane.lookup("#emailField");
                TextField contratoField = (TextField) dialogPane.lookup("#contratoField");
                // Creamos el profesor con ID temporal (0), el DAO lo actualizará
                Profesor nuevoProfesor = new Profesor(
                        0, // <-- ID temporal
                        nombresField.getText(),
                        apellidosField.getText(),
                        emailField.getText(),
                        contratoField.getText()
                );
                universityController.agregarProfesor(nuevoProfesor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarProfesor() {
        Profesor seleccionado = profesoresTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar el profesor?");
            confirmacion.setContentText("Profesor: " + seleccionado.getNombres() + " " + seleccionado.getApellidos() + " (ID: " + seleccionado.getId() + ")");
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = universityController.eliminarProfesor(seleccionado);
                if (!exito) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText("No se pudo eliminar el profesor.");
                    alert.setContentText("El profesor tiene cursos asignados. Elimine las asignaciones primero.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún profesor seleccionado");
            alert.setHeaderText("No se ha seleccionado ningún profesor para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleAsignarCurso() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AsignacionDialog.fxml"));
            DialogPane dialogPane = loader.load();
            ComboBox<Profesor> profesorComboBox = (ComboBox<Profesor>) dialogPane.lookup("#profesorComboBox");
            ComboBox<CursoDTO> cursoComboBox = (ComboBox<CursoDTO>) dialogPane.lookup("#cursoComboBox"); // <-- Cambiado a DTO
            profesorComboBox.getItems().setAll(universityController.getProfesoresData());
            cursoComboBox.getItems().setAll(universityController.getListaDeCursos()); // <-- Ahora usa DTOs
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Asignar Curso");
            Optional<ButtonType> resultado = dialog.showAndWait();
            if (resultado.isPresent() && resultado.get().getButtonData() == ButtonType.OK.getButtonData()) {
                Profesor profSeleccionado = profesorComboBox.getValue();
                CursoDTO cursoDTOSeleccionado = cursoComboBox.getValue(); // <-- Obtenemos el DTO
                TextField anoField = (TextField) dialogPane.lookup("#anoField");
                TextField semestreField = (TextField) dialogPane.lookup("#semestreField");
                if (profSeleccionado != null && cursoDTOSeleccionado != null && !anoField.getText().isEmpty() && !semestreField.getText().isEmpty()) {
                    int ano = Integer.parseInt(anoField.getText());
                    int semestre = Integer.parseInt(semestreField.getText());
                    // Crear DTO en lugar de entidad
                    CursoProfesorDTO nuevaAsignacionDTO = new CursoProfesorDTO(
                            profSeleccionado.getId(),
                            cursoDTOSeleccionado.getId(), // <-- Usamos el ID del DTO
                            ano,
                            semestre
                    );
                    universityController.asignarCurso(nuevaAsignacionDTO); // <-- Ahora llama al método que acepta DTO
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarAsignacion() {
        CursoProfesorDTO seleccion = asignacionesTable.getSelectionModel().getSelectedItem(); // <-- Ahora usa DTO
        if (seleccion != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar la asignación?");
            // Concatenamos correctamente usando \n para los saltos de línea
            confirmacion.setContentText("Profesor ID: " + seleccion.getProfesorId() +
                    "\nCurso ID: " + seleccion.getCursoId() +
                    "\nAño: " + seleccion.getAno() + ", Semestre: " + seleccion.getSemestre());
            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                boolean exito = universityController.eliminarAsignacion(seleccion); // <-- Ahora llama al método que acepta DTO
                if (exito) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Eliminación exitosa");
                    alert.setHeaderText("La asignación ha sido eliminada.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al eliminar");
                    alert.setHeaderText("No se pudo eliminar la asignación.");
                    alert.setContentText("Ocurrió un error al intentar eliminar la asignación.");
                    alert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ninguna asignación seleccionada");
            alert.setHeaderText("No se ha seleccionado ninguna asignación para eliminar.");
            alert.showAndWait();
        }
    }
}