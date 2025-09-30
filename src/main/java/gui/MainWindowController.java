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
import persistence.InscripcionDAO;
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
    private ObservableList<Curso> listaDeCursos;
    private List<Estudiante> listaDeEstudiantes;

    // --- Listas Observables para las Tablas ---
    private ObservableList<Inscripcion> inscripcionesData;
    private ObservableList<Profesor> profesoresData;
    private ObservableList<CursoProfesor> asignacionesData;
    private ObservableList<Estudiante> estudiantesData;

    @FXML private Button eliminarPersonaBtn;

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

    // --- Personas ---
    @FXML private Button agregarPersonaBtn;
    @FXML private TableView<Persona> personasTable;
    @FXML private TableColumn<Persona, Double> personaIdCol;
    @FXML private TableColumn<Persona, String> personaNombresCol;
    @FXML private TableColumn<Persona, String> personaApellidosCol;
    @FXML private TableColumn<Persona, String> personaEmailCol;

    // Lista observable para Personas
    //private ObservableList<Persona> personasData;

    // --- Facultades ---
    @FXML private Button agregarFacultadBtn;
    @FXML private TableView<Facultad> facultadesTable;
    @FXML private TableColumn<Facultad, Double> facultadIdCol;
    @FXML private TableColumn<Facultad, String> facultadNombreCol;
    @FXML private TableColumn<Facultad, String> facultadDecanoCol;

    //private ObservableList<Facultad> facultadesData;
    private ObservableList<Facultad> listaDeFacultades;

    // --- Programas ---
// --- Programas ---
    @FXML private Button agregarProgramaBtn;
    @FXML private TableView<Programa> programasTable;
    @FXML private TableColumn<Programa, Double> programaIdCol;
    @FXML private TableColumn<Programa, String> programaNombreCol;
    @FXML private TableColumn<Programa, Double> programaDuracionCol;
    @FXML private TableColumn<Programa, String> programaFechaCol;
    @FXML private TableColumn<Programa, String> programaFacultadCol;
    private ObservableList<Programa> listaDeProgramas;
    //private List<Programa> listaDeProgramas;

    // --- Cursos ---
    @FXML private Button agregarCursoBtn;
    @FXML private TableView<Curso> cursosTable;
    @FXML private TableColumn<Curso, Integer> cursoIdCol;
    @FXML private TableColumn<Curso, String> cursoNombreCol;
    @FXML private TableColumn<Curso, String> cursoProgramaCol;
    @FXML private TableColumn<Curso, String> cursoActivoCol;

    //private ObservableList<Curso> cursosData;

    @FXML
    public void initialize() {
        listaDeCursos = FXCollections.observableArrayList();
        //cursosData = FXCollections.observableArrayList();
        servicioInscripciones = new CursosInscritos(new InscripcionDAO());
        servicioCursosProfesores = new CursosProfesores();
        servicioPersonas = new InscripcionesPersonas();

        this.listaDeProfesores = crearProfesoresDePrueba();
        this.listaDeCursos = FXCollections.observableArrayList(crearCursosDePrueba());
        this.listaDeEstudiantes = crearEstudiantesDePrueba();
        inscripcionesData = FXCollections.observableArrayList();
        profesoresData = FXCollections.observableArrayList();
        asignacionesData = FXCollections.observableArrayList();
        estudiantesData = FXCollections.observableArrayList();
        //personasData = FXCollections.observableArrayList(); //
        listaDeFacultades = FXCollections.observableArrayList();
        // facultadesData = FXCollections.observableArrayList();
        listaDeProgramas = FXCollections.observableArrayList();
        //programasData = FXCollections.observableArrayList();

        configurarTodasLasTablas();
        cargarTodosLosDatos();
    }

    private void configurarTodasLasTablas() {
        personasTable.setItems(servicioPersonas.listado);
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
        cursosTable.setItems(listaDeCursos);
        programaIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        programaNombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        programaDuracionCol.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        programaFechaCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRegistro().toString())
        );
        programaFacultadCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getFacultad() != null ? cellData.getValue().getFacultad().getNombre() : "Sin facultad"
                )
        );
        programasTable.setItems(listaDeProgramas);
        facultadIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        facultadNombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        facultadDecanoCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getDecano() != null ? cellData.getValue().getDecano().getNombres() + " " + cellData.getValue().getDecano().getApellidos() : "Sin decano"
                )
        );
        facultadesTable.setItems(listaDeFacultades);

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

        // Personas
        personaIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        personaNombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        personaApellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        personaEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        personasTable.setItems(servicioPersonas.listado);
        personasTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, nuev) -> eliminarPersonaBtn.setDisable(nuev == null)
        );
    }

    private void cargarTodosLosDatos() {
        servicioInscripciones.cargarDatos();
        inscripcionesData.setAll(servicioInscripciones.obtenerLista());
        inscripcionesTable.setItems(inscripcionesData);
        profesoresData.setAll(this.listaDeProfesores);
        profesoresTable.setItems(profesoresData);
        estudiantesData.setAll(this.listaDeEstudiantes);
        estudiantesTable.setItems(estudiantesData);
        asignacionesData.setAll(servicioCursosProfesores.getAsignaciones());
        asignacionesTable.setItems(asignacionesData);
    }

    private List<Persona> getPersonasYProfesores() {
        List<Persona> disponibles = new ArrayList<>();
        for (Persona p : servicioPersonas.listado) {
            // Verifica si el ID de esta persona NO está en la lista de estudiantes
            boolean esEstudiante = listaDeEstudiantes.stream()
                    .anyMatch(estudiante -> estudiante.getId() == p.getId());
            if (!esEstudiante) {
                disponibles.add(p);
            }
        }
        // Añadir profesores (son Personas, pero como Profesor hereda de Persona)
        for (Profesor p : listaDeProfesores) {
            // Evitar duplicados por ID
            boolean existe = disponibles.stream().anyMatch(per -> per.getId() == p.getId());
            if (!existe) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }

    @FXML
    private void handleEliminarPersona() {
        Persona seleccionada = personasTable.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            //  Eliminar de la base de datos
            DatabaseManager.eliminarPersona(seleccionada.getId());

            //  Usar el método eliminar de InscripcionesPersonas para actualizar la vista
            servicioPersonas.eliminar(seleccionada.getId());

            System.out.println("Persona eliminada (de la base de datos y memoria): ID=" + seleccionada.getId() + ", " + seleccionada.getNombres() + " " + seleccionada.getApellidos());
        }
    }
    @FXML
    private void handleAgregarCurso() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/CursoDialog.fxml"));
            DialogPane dialogPane = loader.load();
            TextField idField = (TextField) dialogPane.lookup("#idField");
            TextField nombreField = (TextField) dialogPane.lookup("#nombreField");
            ComboBox<Programa> programaComboBox = (ComboBox<Programa>) dialogPane.lookup("#programaComboBox");
            CheckBox activoCheckBox = (CheckBox) dialogPane.lookup("#activoCheckBox");

            programaComboBox.getItems().setAll(listaDeProgramas);

            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);
            okButton.setText("Agregar Curso");

            Runnable validar = () -> {
                boolean valido = idField.getText().trim().length() > 0 &&
                        idField.getText().matches("\\d+") &&
                        nombreField.getText().trim().length() > 0 &&
                        programaComboBox.getValue() != null;
                okButton.setDisable(!valido);
            };

            idField.textProperty().addListener((obs, old, nuev) -> validar.run());
            nombreField.textProperty().addListener((obs, old, nuev) -> validar.run());
            programaComboBox.valueProperty().addListener((obs, old, nuev) -> validar.run());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Curso");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int id = Integer.parseInt(idField.getText().trim());
                String nombre = nombreField.getText().trim();
                Programa programa = programaComboBox.getValue();
                boolean activo = activoCheckBox.isSelected();

                Curso nuevo = new Curso(id, nombre, activo);
                nuevo.setPrograma(programa);

                // guardarNuevoCursoEnDB(nuevo); // ✅ Eliminado

                // Agrega directamente a la ObservableList. La tabla se actualiza sola.
                listaDeCursos.add(nuevo);

                System.out.println("Curso agregado (en memoria): " + nuevo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Ignorada
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAgregarPrograma() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ProgramaDialog.fxml"));
            DialogPane dialogPane = loader.load();

            TextField idField = (TextField) dialogPane.lookup("#idField");
            TextField nombreField = (TextField) dialogPane.lookup("#nombreField");
            TextField duracionField = (TextField) dialogPane.lookup("#duracionField");
            DatePicker fechaPicker = (DatePicker) dialogPane.lookup("#fechaPicker");
            ComboBox<Facultad> facultadComboBox = (ComboBox<Facultad>) dialogPane.lookup("#facultadComboBox");

            facultadComboBox.getItems().setAll(listaDeFacultades);

            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);
            okButton.setText("Agregar Programa");

            Runnable validar = () -> {
                boolean valido = idField.getText().trim().length() > 0 &&
                        idField.getText().matches("\\d*\\.?\\d+") &&
                        nombreField.getText().trim().length() > 0 &&
                        duracionField.getText().trim().length() > 0 &&
                        duracionField.getText().matches("\\d*\\.?\\d+") &&
                        fechaPicker.getValue() != null &&
                        facultadComboBox.getValue() != null;
                okButton.setDisable(!valido);
            };

            idField.textProperty().addListener((obs, old, nuev) -> validar.run());
            nombreField.textProperty().addListener((obs, old, nuev) -> validar.run());
            duracionField.textProperty().addListener((obs, old, nuev) -> validar.run());
            fechaPicker.valueProperty().addListener((obs, old, nuev) -> validar.run());
            facultadComboBox.valueProperty().addListener((obs, old, nuev) -> validar.run());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Programa");

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                double id = Double.parseDouble(idField.getText().trim());
                String nombre = nombreField.getText().trim();
                double duracion = Double.parseDouble(duracionField.getText().trim());
                java.util.Date registro = java.sql.Date.valueOf(fechaPicker.getValue());
                Facultad facultad = facultadComboBox.getValue();

                Programa nuevo = new Programa(id, nombre, duracion, registro, facultad);

                // guardarNuevoProgramaEnDB(nuevo); // ✅ Eliminado

                //  Agrega directamente a la ObservableList. La tabla se actualiza sola.
                listaDeProgramas.add(nuevo);

                System.out.println("Programa agregado (en memoria): " + nuevo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAgregarFacultad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/FacultadDialog.fxml"));
            DialogPane dialogPane = loader.load();

            TextField idField = (TextField) dialogPane.lookup("#idField");
            TextField nombreField = (TextField) dialogPane.lookup("#nombreField");
            ComboBox<Persona> decanoComboBox = (ComboBox<Persona>) dialogPane.lookup("#decanoComboBox");

            // Llenar ComboBox con personas y profesores (sin estudiantes)
            List<Persona> decanosDisponibles = getPersonasYProfesores();
            decanoComboBox.getItems().setAll(decanosDisponibles);

            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);

            // Validación en tiempo real
            Runnable validar = () -> {
                boolean valido = idField.getText().trim().length() > 0 &&
                        idField.getText().matches("\\d*\\.?\\d+") &&
                        nombreField.getText().trim().length() > 0 &&
                        decanoComboBox.getValue() != null;
                okButton.setDisable(!valido);
            };

            idField.textProperty().addListener((obs, old, nuev) -> validar.run());
            nombreField.textProperty().addListener((obs, old, nuev) -> validar.run());
            decanoComboBox.valueProperty().addListener((obs, old, nuev) -> validar.run());

            okButton.setText("Agregar Facultad");

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Facultad");

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                double id = Double.parseDouble(idField.getText().trim());
                String nombre = nombreField.getText().trim();
                Persona decano = decanoComboBox.getValue();

                Facultad nueva = new Facultad(id, nombre);
                nueva.setDecano(decano);

                // guardarNuevaFacultadEnDB(nueva); // ✅ Eliminado

                //  Agrega directamente a la ObservableList. La tabla se actualiza sola.
                listaDeFacultades.add(nueva);

                System.out.println("Facultad agregada (en memoria): " + nueva);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Ignorada gracias a validación previa
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAgregarPersona() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PersonaDialog.fxml"));
            DialogPane dialogPane = loader.load();

            TextField idField = (TextField) dialogPane.lookup("#idField");
            TextField nombresField = (TextField) dialogPane.lookup("#nombresField");
            TextField apellidosField = (TextField) dialogPane.lookup("#apellidosField");
            TextField emailField = (TextField) dialogPane.lookup("#emailField");

            ComboBox<Persona> decanoComboBox = (ComboBox<Persona>) dialogPane.lookup("#decanoComboBox");
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.setDisable(true);

            Runnable validar = () -> {
                boolean valido = idField.getText().trim().length() > 0 &&
                        idField.getText().matches("\\d*\\.?\\d+") &&
                        nombresField.getText().trim().length() > 0 &&
                        apellidosField.getText().trim().length() > 0 &&
                        emailField.getText().trim().length() > 0;
                okButton.setDisable(!valido);
            };

            idField.textProperty().addListener((obs, old, nuev) -> validar.run());
            nombresField.textProperty().addListener((obs, old, nuev) -> validar.run());
            apellidosField.textProperty().addListener((obs, old, nuev) -> validar.run());
            emailField.textProperty().addListener((obs, old, nuev) -> validar.run());

            okButton.setText("Agregar Persona");

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Agregar Persona");

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                double id = Double.parseDouble(idField.getText().trim());
                String nombres = nombresField.getText().trim();
                String apellidos = apellidosField.getText().trim();
                String email = emailField.getText().trim();

                // ... creación de 'nueva' ...

                Persona nueva = new Persona(id, nombres, apellidos, email);
                // DatabaseManager.guardarPersona(nueva); // ✅ Eliminado
                servicioPersonas.inscribir(nueva); // ← Esto actualiza la ObservableList del servicio

                System.out.println("Persona agregada (en memoria): " + nueva);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Ignorada
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                    servicioInscripciones.inscribir(nueva);

                    inscripcionesData.add(nueva);

                    System.out.println(" Inscripción realizada (en memoria): " + nueva);
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

            inscripcionesData.remove(sel);


            System.out.println(" Inscripción eliminada (en memoria): ID=" + sel.getId());
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
                TextField idField = (TextField) dialogPane.lookup("#idField");
                TextField nombres = (TextField) dialogPane.lookup("#nombresField");
                TextField apellidos = (TextField) dialogPane.lookup("#apellidosField");
                TextField email = (TextField) dialogPane.lookup("#emailField");
                TextField codigo = (TextField) dialogPane.lookup("#codigoField");

                // Obtener el ID ingresado por el usuario
                double nuevoId = Double.parseDouble(idField.getText().trim());

                // Verificar si el ID ya existe
                List<Double> idsExistentes = DatabaseManager.cargarPersonas().stream()
                        .map(Persona::getId)
                        .collect(java.util.stream.Collectors.toList());

                if (idsExistentes.contains(nuevoId)) {
                    System.out.println("ERROR: El ID ingresado ya existe en la base de datos.");
                    return; // Salir sin hacer nada
                }

                // Crear la Persona
                Persona nuevaPersona = new Persona(nuevoId, nombres.getText(), apellidos.getText(), email.getText());

                // Crear el Estudiante (hereda de Persona, por lo tanto, usa el mismo ID)
                Estudiante nuevo = new Estudiante(nuevoId, nombres.getText(), apellidos.getText(), email.getText(), Double.parseDouble(codigo.getText()), true, 0.0);

                // Guardar el Estudiante en la base de datos (esto también inserta en PERSONA)
                guardarNuevoEstudianteEnDB(nuevo);

                // Agregar a la lista local
                this.listaDeEstudiantes.add(nuevo);

                // ✅ Agregar también como Persona para que aparezca en la tabla de personas
                servicioPersonas.inscribir(nuevaPersona);

                cargarTodosLosDatos();
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: El ID debe ser un número válido.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @FXML
    private void handleEliminarEstudiante() {

        Estudiante sel = estudiantesTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            this.listaDeEstudiantes.remove(sel);

            servicioPersonas.eliminar(sel.getId());

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

                // ✅ Agregar también como Persona para que aparezca en la tabla de personas
                Persona nuevaPersona = new Persona(nuevoProfesor.getId(), nombresField.getText(), apellidosField.getText(), emailField.getText());
                servicioPersonas.inscribir(nuevaPersona);

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

            // ✅ Eliminar también de la lista de personas
            servicioPersonas.eliminar(seleccionado.getId());

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

    //  Getter para que la consola pueda acceder al mismo servicio
    public InscripcionesPersonas getServicioPersonas() {
        return this.servicioPersonas;
    }

    public ObservableList<Facultad> getListaDeFacultades() {
        return this.listaDeFacultades;
    }

    //  Getter para que la consola pueda acceder a la lista observable de programas
    public ObservableList<Programa> getListaDeProgramas() {
        return this.listaDeProgramas;
    }

    //  Getter para que la consola pueda acceder a la lista observable de cursos
    public ObservableList<Curso> getListaDeCursos() {
        return this.listaDeCursos;
    }

    public ObservableList<Inscripcion> getInscripcionesData() {
        return this.inscripcionesData;
    }
    public ObservableList<Estudiante> getListaDeEstudiantes() {
        return this.estudiantesData;
    }

}