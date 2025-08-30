package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import persistence.DatabaseManager;
import services.CursosInscritos;
import services.CursosProfesores;
import services.InscripcionesPersonas;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainWindowController {

    // --- Servicios ---
    private CursosInscritos servicioInscripciones;
    private CursosProfesores servicioCursosProfesores;
    private InscripcionesPersonas servicioPersonas;

    // --- Listas de Datos en Memoria (gestionadas por el controlador) ---
    private List<Profesor> listaDeProfesores;
    private List<Curso> listaDeCursos;
    private List<Estudiante> listaDeEstudiantes;

    // --- Listas Observables para las Tablas ---
    private ObservableList<Inscripcion> inscripcionesData;
    private ObservableList<Profesor> profesoresData;
    private ObservableList<CursoProfesor> asignacionesData;
    private ObservableList<Estudiante> estudiantesData;

    // --- Componentes UI (Declaraciones para todas las pestañas) ---
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


    @FXML
    public void initialize() {
        servicioInscripciones = new CursosInscritos();
        servicioCursosProfesores = new CursosProfesores();
        servicioPersonas = new InscripcionesPersonas();

        this.listaDeProfesores = crearProfesoresDePrueba();
        this.listaDeCursos = crearCursosDePrueba();
        this.listaDeEstudiantes = crearEstudiantesDePrueba();

        inscripcionesData = FXCollections.observableArrayList();
        profesoresData = FXCollections.observableArrayList();
        asignacionesData = FXCollections.observableArrayList();
        estudiantesData = FXCollections.observableArrayList();

        configurarTodasLasTablas();
        cargarTodosLosDatos();
    }

    private void configurarTodasLasTablas() {
        // Inscripciones
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        estudianteCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstudiante().toString()));
        cursoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurso().toString()));
        anoCol.setCellValueFactory(new PropertyValueFactory<>("ano"));
        semestreCol.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        inscripcionesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, anew) -> eliminarInscripcionBtn.setDisable(anew == null));

        // Estudiantes
        estIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        estCodigoCol.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        estNombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        estApellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        estEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        estudiantesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, anew) -> eliminarEstudianteBtn.setDisable(anew == null));

        // Profesores
        profIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        profNombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        profApellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        profEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        profContratoCol.setCellValueFactory(new PropertyValueFactory<>("tipoContrato"));
        profesoresTable.getSelectionModel().selectedItemProperty().addListener((obs, old, anew) -> eliminarProfesorBtn.setDisable(anew == null));

        // Asignaciones
        asigProfesorCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProfesor().toString()));
        asigCursoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurso().toString()));
        asigAnoCol.setCellValueFactory(new PropertyValueFactory<>("ano"));
        asigSemestreCol.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        asignacionesTable.getSelectionModel().selectedItemProperty().addListener((obs, old, anew) -> eliminarAsignacionBtn.setDisable(anew == null));
    }

    private void cargarTodosLosDatos() {
        servicioInscripciones.cargarDatos();
        inscripcionesData.setAll(servicioInscripciones.imprimirListaCompleta());
        inscripcionesTable.setItems(inscripcionesData);

        profesoresData.setAll(this.listaDeProfesores);
        profesoresTable.setItems(profesoresData);

        estudiantesData.setAll(this.listaDeEstudiantes);
        estudiantesTable.setItems(estudiantesData);

        asignacionesData.setAll(servicioCursosProfesores.getAsignaciones());
        asignacionesTable.setItems(asignacionesData);
    }

    // --- Handlers Pestaña Inscripciones ---
    @FXML
    private void handleInscribir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/InscripcionDialog.fxml"));
            DialogPane dialogPane = loader.load();
            ComboBox<Estudiante> estudianteComboBox = (ComboBox<Estudiante>) dialogPane.lookup("#estudianteComboBox");
            ComboBox<Curso> cursoComboBox = (ComboBox<Curso>) dialogPane.lookup("#cursoComboBox");
            estudianteComboBox.getItems().setAll(this.listaDeEstudiantes);
            cursoComboBox.getItems().setAll(this.listaDeCursos);
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
                    servicioInscripciones.inscribirCurso(nueva);
                    cargarTodosLosDatos();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarInscripcion() {
        Inscripcion sel = inscripcionesTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            servicioInscripciones.eliminar(sel.getId());
            cargarTodosLosDatos();
        }
    }

    // --- Handlers Pestaña Estudiantes ---
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
                guardarNuevoEstudianteEnDB(nuevo);
                this.listaDeEstudiantes.add(nuevo);
                cargarTodosLosDatos();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarEstudiante() {
        // Nota: Esta lógica solo elimina de la lista en memoria.
        // Una implementación completa requeriría eliminar de la DB.
        Estudiante sel = estudiantesTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            this.listaDeEstudiantes.remove(sel);
            cargarTodosLosDatos();
        }
    }

    // --- Handlers Pestaña Profesores ---
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
                Profesor nuevoProfesor = new Profesor(System.currentTimeMillis() / 1000.0, nombresField.getText(), apellidosField.getText(), emailField.getText(), contratoField.getText());
                this.listaDeProfesores.add(nuevoProfesor);
                cargarTodosLosDatos();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarProfesor() {
        Profesor seleccionado = profesoresTable.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            this.listaDeProfesores.remove(seleccionado);
            cargarTodosLosDatos();
        }
    }

    // --- Handlers Pestaña Asignaciones ---
    @FXML
    private void handleAsignarCurso() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/AsignacionDialog.fxml"));
            DialogPane dialogPane = loader.load();
            ComboBox<Profesor> profesorComboBox = (ComboBox<Profesor>) dialogPane.lookup("#profesorComboBox");
            ComboBox<Curso> cursoComboBox = (ComboBox<Curso>) dialogPane.lookup("#cursoComboBox");
            profesorComboBox.getItems().setAll(this.listaDeProfesores);
            cursoComboBox.getItems().setAll(this.listaDeCursos);
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
                    servicioCursosProfesores.asignarCurso(nuevaAsignacion);
                    cargarTodosLosDatos();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEliminarAsignacion() {
        CursoProfesor seleccion = asignacionesTable.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            servicioCursosProfesores.eliminarAsignacion(seleccion);
            cargarTodosLosDatos();
        }
    }

    // --- Métodos de Ayuda ---
    private void guardarNuevoEstudianteEnDB(Estudiante estudiante) {
        String sqlPersona = "INSERT INTO PERSONA(id, nombres, apellidos, email) VALUES(?, ?, ?, ?)";
        String sqlEstudiante = "INSERT INTO ESTUDIANTE(id, codigo, programa_id, activo, promedio) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmtPersona = conn.prepareStatement(sqlPersona); PreparedStatement pstmtEstudiante = conn.prepareStatement(sqlEstudiante)) {
                pstmtPersona.setDouble(1, estudiante.getId());
                pstmtPersona.setString(2, estudiante.getNombres());
                pstmtPersona.setString(3, estudiante.getApellidos());
                pstmtPersona.setString(4, estudiante.getEmail());
                pstmtPersona.executeUpdate();
                pstmtEstudiante.setDouble(1, estudiante.getId());
                pstmtEstudiante.setDouble(2, estudiante.getCodigo());
                pstmtEstudiante.setDouble(3, 111); // ID del programa por defecto
                pstmtEstudiante.setBoolean(4, true);
                pstmtEstudiante.setDouble(5, 0.0);
                pstmtEstudiante.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Estudiante> crearEstudiantesDePrueba() {
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(new Estudiante(101, "Maria", "Lopez", "maria@email.com", 202401, true, 4.5));
        estudiantes.add(new Estudiante(102, "Juan", "Velez", "juan@email.com", 202402, true, 3.8));
        return estudiantes;
    }

    private List<Profesor> crearProfesoresDePrueba() {
        List<Profesor> profes = new ArrayList<>();
        profes.add(new Profesor(1, "Lucia", "Pineda", "lucia@email.com", "Catedrático"));
        profes.add(new Profesor(2, "Marcos", "Aurelio", "marcos@email.com", "Tiempo Completo"));
        return profes;
    }

    private List<Curso> crearCursosDePrueba() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso(901, "Tecnologia avanzada", true));
        cursos.add(new Curso(902, "Bases de Datos", true));
        cursos.add(new Curso(903, "Cálculo Integral", true));
        return cursos;
    }
}