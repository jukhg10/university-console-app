package gui;
import dto.PersonaDTO;

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

public class MainWindowController {

    private UniversityController universityController;

    // --- FXML Injected Fields ---
    @FXML private Button eliminarProgramaBtn;
    @FXML private Button eliminarFacultadBtn;
    @FXML private Button eliminarPersonaBtn;
    @FXML private TableView<Inscripcion> inscripcionesTable;
    @FXML private TableColumn<Inscripcion, Integer> idCol;
    @FXML private TableColumn<Inscripcion, String> estudianteCol;
    @FXML private TableColumn<Inscripcion, String> cursoCol;
    @FXML private TableColumn<Inscripcion, Integer> anoCol;
    @FXML private TableColumn<Inscripcion, Integer> semestreCol;
    @FXML private Button eliminarInscripcionBtn;
    @FXML private TableView<Estudiante> estudiantesTable;
    @FXML private TableColumn<Estudiante, Double> estIdCol;
    @FXML private TableColumn<Estudiante, Double> estCodigoCol;
    @FXML private TableColumn<Estudiante, String> estNombresCol;
    @FXML private TableColumn<Estudiante, String> estApellidosCol;
    @FXML private TableColumn<Estudiante, String> estEmailCol;
    @FXML private Button eliminarEstudianteBtn;
    @FXML private TableView<Profesor> profesoresTable;
    @FXML private TableColumn<Profesor, Double> profIdCol;
    @FXML private TableColumn<Profesor, String> profNombresCol;
    @FXML private TableColumn<Profesor, String> profApellidosCol;
    @FXML private TableColumn<Profesor, String> profEmailCol;
    @FXML private TableColumn<Profesor, String> profContratoCol;
    @FXML private Button eliminarProfesorBtn;
    @FXML private TableView<CursoProfesor> asignacionesTable;
    @FXML private TableColumn<CursoProfesor, String> asigProfesorCol;
    @FXML private TableColumn<CursoProfesor, String> asigCursoCol;
    @FXML private TableColumn<CursoProfesor, Integer> asigAnoCol;
    @FXML private TableColumn<CursoProfesor, Integer> asigSemestreCol;
    @FXML private Button eliminarAsignacionBtn;
    @FXML private TableView<PersonaDTO> personasTable;
    @FXML private TableColumn<PersonaDTO, Double> personaIdCol;
    @FXML private TableColumn<PersonaDTO, String> personaNombresCol;
    @FXML private TableColumn<PersonaDTO, String> personaApellidosCol;
    @FXML private TableColumn<PersonaDTO, String> personaEmailCol;
    @FXML private TableView<Facultad> facultadesTable;
    @FXML private TableColumn<Facultad, Double> facultadIdCol;
    @FXML private TableColumn<Facultad, String> facultadNombreCol;
    @FXML private TableColumn<Facultad, String> facultadDecanoCol;
    @FXML private TableView<Programa> programasTable;
    @FXML private TableColumn<Programa, Double> programaIdCol;
    @FXML private TableColumn<Programa, String> programaNombreCol;
    @FXML private TableColumn<Programa, Double> programaDuracionCol;
    @FXML private TableColumn<Programa, String> programaFechaCol;
    @FXML private TableColumn<Programa, String> programaFacultadCol;
    @FXML private TableView<Curso> cursosTable;
    @FXML private TableColumn<Curso, Integer> cursoIdCol;
    @FXML private TableColumn<Curso, String> cursoNombreCol;
    @FXML private TableColumn<Curso, String> cursoProgramaCol;
    @FXML private TableColumn<Curso, String> cursoActivoCol;
    @FXML private Button eliminarCursoBtn;

    public void setUniversityController(UniversityController universityController) {
        this.universityController = universityController;
        configurarTodasLasTablas();
    }

    @FXML
    public void initialize() {
        // Initialization logic is now handled in setUniversityController
        // to ensure the controller is available.
    }

    private void configurarTodasLasTablas() {
        cursosTable.getSelectionModel().selectedItemProperty().addListener((obs, old, nuev) -> eliminarCursoBtn.setDisable(nuev == null));
        // Bind the table data sources to the lists in the UniversityController
        personasTable.setItems(universityController.getPersonasData());
        cursosTable.setItems(universityController.getListaDeCursos());
        programasTable.setItems(universityController.getListaDeProgramas());
        facultadesTable.setItems(universityController.getListaDeFacultades());
        inscripcionesTable.setItems(universityController.getInscripcionesData());
        profesoresTable.setItems(universityController.getProfesoresData());
        estudiantesTable.setItems(universityController.getEstudiantesData());
        asignacionesTable.setItems(universityController.getAsignacionesData());
        facultadesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, nuev) -> eliminarFacultadBtn.setDisable(nuev == null));
        programasTable.getSelectionModel().selectedItemProperty().addListener((obs, old, nuev) -> eliminarProgramaBtn.setDisable(nuev == null));
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
        estudianteCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstudiante().toString()));
        cursoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurso().toString()));
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
        asigProfesorCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProfesor().toString()));
        asigCursoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurso().toString()));
        asigAnoCol.setCellValueFactory(new PropertyValueFactory<>("ano"));
        asigSemestreCol.setCellValueFactory(new PropertyValueFactory<>("semestre"));
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
            universityController.eliminarPersona(seleccionada);
        }
    }
    @FXML
    private void handleEliminarCurso() {
        Curso seleccionado = cursosTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Opcional: Preguntar al usuario si está seguro
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar el curso?");
            confirmacion.setContentText("Curso: " + seleccionado.getNombre() + " (ID: " + seleccionado.getId() + ")");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                universityController.eliminarCurso(seleccionado);
            }
        } else {
            // Mostrar un mensaje si no hay selección
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
            ComboBox<Programa> programaComboBox = (ComboBox<Programa>) dialogPane.lookup("#programaComboBox");
            CheckBox activoCheckBox = (CheckBox) dialogPane.lookup("#activoCheckBox");

            programaComboBox.getItems().setAll(universityController.getListaDeProgramas());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Curso");

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // int id = Integer.parseInt(idField.getText().trim()); <-- Ya no leemos el ID
                String nombre = nombreField.getText().trim();
                Programa programa = programaComboBox.getValue();
                boolean activo = activoCheckBox.isSelected();

                // Creamos el curso sin ID, lo generará la base de datos
                Curso nuevo = new Curso(0, nombre, activo); // <-- ID temporal (0)
                nuevo.setPrograma(programa);

                // Verificar si el curso se agregó correctamente
                boolean exito = universityController.agregarCurso(nuevo);
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
    @FXML
    private void handleAgregarPrograma() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ProgramaDialog.fxml"));
            DialogPane dialogPane = loader.load();

            // TextField idField = (TextField) dialogPane.lookup("#idField"); <-- Ya no necesitamos el ID
            TextField nombreField = (TextField) dialogPane.lookup("#nombreField");
            TextField duracionField = (TextField) dialogPane.lookup("#duracionField");
            DatePicker fechaPicker = (DatePicker) dialogPane.lookup("#fechaPicker");
            ComboBox<Facultad> facultadComboBox = (ComboBox<Facultad>) dialogPane.lookup("#facultadComboBox");

            facultadComboBox.getItems().setAll(universityController.getListaDeFacultades());

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
                Facultad facultad = facultadComboBox.getValue();

                // Creamos el programa sin ID, lo generará la base de datos
                Programa nuevo = new Programa(0, nombre, duracion, registro, facultad); // <-- ID temporal (0)

                universityController.agregarPrograma(nuevo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarPrograma() {
        Programa seleccionado = programasTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Opcional: Preguntar al usuario si está seguro
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar el programa?");
            confirmacion.setContentText("Programa: " + seleccionado.getNombre() + " (ID: " + seleccionado.getId() + ")");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                universityController.eliminarPrograma(seleccionado);
            }
        } else {
            // Mostrar un mensaje si no hay selección
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
            ComboBox<Persona> decanoComboBox = (ComboBox<Persona>) dialogPane.lookup("#decanoComboBox");

            List<Persona> decanosDisponibles = universityController.getPersonasYProfesores();
            decanoComboBox.getItems().setAll(decanosDisponibles);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Facultad");

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // double id = Double.parseDouble(idField.getText().trim()); <-- Ya no leemos el ID
                String nombre = nombreField.getText().trim();
                Persona decano = decanoComboBox.getValue();

                // Creamos la facultad sin ID, lo generará la base de datos
                Facultad nueva = new Facultad(0, nombre); // <-- ID temporal (0)
                nueva.setDecano(decano);

                universityController.agregarFacultad(nueva);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarFacultad() {
        Facultad seleccionada = facultadesTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            // Opcional: Preguntar al usuario si está seguro
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar la facultad?");
            confirmacion.setContentText("Facultad: " + seleccionada.getNombre() + " (ID: " + seleccionada.getId() + ")");

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                universityController.eliminarFacultad(seleccionada);
            }
        } else {
            // Mostrar un mensaje si no hay selección
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
            ComboBox<Estudiante> estudianteComboBox = (ComboBox<Estudiante>) dialogPane.lookup("#estudianteComboBox");
            ComboBox<Curso> cursoComboBox = (ComboBox<Curso>) dialogPane.lookup("#cursoComboBox");
            estudianteComboBox.getItems().setAll(universityController.getEstudiantesData());
            cursoComboBox.getItems().setAll(universityController.getListaDeCursos());
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Realizar Inscripción");
            
            Optional<ButtonType> result = dialog.showAndWait();
            
            if (result.isPresent() && result.get().getButtonData() == ButtonType.OK.getButtonData()) {
                Estudiante est = estudianteComboBox.getValue();
                Curso cur = cursoComboBox.getValue();
                TextField anoField = (TextField) dialogPane.lookup("#anoField");
                TextField semestreField = (TextField) dialogPane.lookup("#semestreField");
                
                if (est != null && cur != null && !anoField.getText().isEmpty() && !semestreField.getText().isEmpty()) {
                    int ano = Integer.parseInt(anoField.getText());
                    int semestre = Integer.parseInt(semestreField.getText());
                    Inscripcion nueva = new Inscripcion(est, cur, ano, semestre);
                    universityController.inscribirEstudiante(nueva);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarInscripcion() {
        Inscripcion sel = inscripcionesTable.getSelectionModel().getSelectedItem();
        universityController.eliminarInscripcion(sel);
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
                double nuevoId = System.currentTimeMillis() / 1000.0;
                Estudiante nuevo = new Estudiante(nuevoId, nombres.getText(), apellidos.getText(), email.getText(), Double.parseDouble(codigo.getText()), true, 0.0);
                universityController.agregarEstudiante(nuevo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleEliminarEstudiante() {
        Estudiante sel = estudiantesTable.getSelectionModel().getSelectedItem();
        universityController.eliminarEstudiante(sel);
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
        universityController.eliminarProfesor(seleccionado);
    }

    @FXML
    private void handleAsignarCurso() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AsignacionDialog.fxml"));
            DialogPane dialogPane = loader.load();
            ComboBox<Profesor> profesorComboBox = (ComboBox<Profesor>) dialogPane.lookup("#profesorComboBox");
            ComboBox<Curso> cursoComboBox = (ComboBox<Curso>) dialogPane.lookup("#cursoComboBox");
            profesorComboBox.getItems().setAll(universityController.getProfesoresData());
            cursoComboBox.getItems().setAll(universityController.getListaDeCursos());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Asignar Curso");

            Optional<ButtonType> resultado = dialog.showAndWait();

            if (resultado.isPresent() && resultado.get().getButtonData() == ButtonType.OK.getButtonData()) {
                Profesor profSeleccionado = profesorComboBox.getValue();
                Curso cursoSeleccionado = cursoComboBox.getValue();
                TextField anoField = (TextField) dialogPane.lookup("#anoField");
                TextField semestreField = (TextField) dialogPane.lookup("#semestreField");

                if (profSeleccionado != null && cursoSeleccionado != null && !anoField.getText().isEmpty() && !semestreField.getText().isEmpty()) {
                    int ano = Integer.parseInt(anoField.getText());
                    int semestre = Integer.parseInt(semestreField.getText());
                    CursoProfesor nuevaAsignacion = new CursoProfesor(profSeleccionado, cursoSeleccionado, ano, semestre);
                    universityController.asignarCurso(nuevaAsignacion); // <-- Ahora llama al DAO
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarAsignacion() {
        CursoProfesor seleccion = asignacionesTable.getSelectionModel().getSelectedItem();
        universityController.eliminarAsignacion(seleccion); // <-- Ahora llama al DAO
    }
}