package gui;

import dto.PersonaDTO;
import dto.PersonaMapper;
import fabricas.FabricaServiciosInterna;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import observer.CursoConsoleLogger;
import observer.CursoObserver;
import services.Servicios;

import java.util.List;
import java.util.stream.Collectors;

public class UniversityController {
    private final Servicios servicios;
    private final CursoObserver cursoLogger = new CursoConsoleLogger();

    // Lists that back the TableViews in the UI
    private final ObservableList<PersonaDTO> personasData;
    private final ObservableList<Curso> listaDeCursos;
    private final ObservableList<Inscripcion> inscripcionesData;
    private final ObservableList<Estudiante> estudiantesData;
    private final ObservableList<Profesor> profesoresData;
    private final ObservableList<Programa> listaDeProgramas;
    private final ObservableList<Facultad> listaDeFacultades;
    private final ObservableList<CursoProfesor> asignacionesData;

    public UniversityController() {
        this.servicios = FabricaServiciosInterna.getServicios();
        asignacionesData = FXCollections.observableArrayList(servicios.getCursoProfesorDAO().cargarTodos());
        // --- FIX: Load ALL data from the database upon initialization ---
        personasData = FXCollections.observableArrayList(
                servicios.getPersonaDAO().cargarTodas().stream()
                        .map(PersonaMapper::toDTO)
                        .collect(Collectors.toList())
        );
        List<Curso> cursosDesdeBD = servicios.getCursoDAO().cargarTodos();
        for (Curso curso : cursosDesdeBD) {
            curso.addObserver(cursoLogger); // Registrar observador
        }
        listaDeCursos = FXCollections.observableArrayList(cursosDesdeBD);
        inscripcionesData = FXCollections.observableArrayList(servicios.getInscripcionDAO().cargarDatos());
        estudiantesData = FXCollections.observableArrayList(servicios.getEstudianteDAO().cargarTodos());
        profesoresData = FXCollections.observableArrayList(servicios.getProfesorDAO().cargarTodos());
        listaDeProgramas = FXCollections.observableArrayList(servicios.getProgramaDAO().cargarTodos());
        listaDeFacultades = FXCollections.observableArrayList(servicios.getFacultadDAO().cargarTodas());
        // This remains in-memory for now as it doesn't have a dedicated DAO
        //asignacionesData = FXCollections.observableArrayList();
    }

    public void asignarCurso(CursoProfesor asignacion) {
        servicios.getCursoProfesorDAO().guardar(asignacion);
        asignacionesData.add(asignacion); // Update the UI
    }

    public void eliminarAsignacion(CursoProfesor asignacion) {
        if (asignacion == null) return;
        servicios.getCursoProfesorDAO().eliminar(
                asignacion.getProfesor().getId(),
                asignacion.getCurso().getId(),
                asignacion.getAno(),
                asignacion.getSemestre()
        );
        asignacionesData.remove(asignacion); // Update the UI
    }

    // --- Getters for the ObservableLists ---
    public ObservableList<PersonaDTO> getPersonasData() { return personasData; }
    public ObservableList<Curso> getListaDeCursos() { return listaDeCursos; }
    public ObservableList<Inscripcion> getInscripcionesData() { return inscripcionesData; }
    public ObservableList<Estudiante> getEstudiantesData() { return estudiantesData; }
    public ObservableList<Profesor> getProfesoresData() { return profesoresData; }
    public ObservableList<Programa> getListaDeProgramas() { return listaDeProgramas; }
    public ObservableList<Facultad> getListaDeFacultades() { return listaDeFacultades; }
    public ObservableList<CursoProfesor> getAsignacionesData() { return asignacionesData; }

    // --- FIX: Fully Implemented Data-Modification Methods ---
    public void agregarPersona(PersonaDTO personaDTO) {
        Persona persona = PersonaMapper.toEntity(personaDTO);
        servicios.getPersonaDAO().guardar(persona); // <-- El DAO ahora asigna el ID generado

        // Convertimos la entidad actualizada (con el ID) de nuevo a DTO
        PersonaDTO actualizado = PersonaMapper.toDTO(persona);

        personasData.add(actualizado); // <-- Agregamos el DTO actualizado a la UI
    }

    public void eliminarPersona(PersonaDTO personaDTO) {
        servicios.getPersonaDAO().eliminar(personaDTO.getId());
        personasData.remove(personaDTO); // Update the UI
    }

    public void inscribirEstudiante(Inscripcion inscripcion) {
        servicios.getInscripcionDAO().guardar(inscripcion);
        inscripcionesData.add(inscripcion); // Update the UI
    }

    public void eliminarInscripcion(Inscripcion inscripcion) {
        if (inscripcion == null) return;
        servicios.getInscripcionDAO().eliminar(inscripcion.getId());
        inscripcionesData.remove(inscripcion); // Update the UI
    }

    public void agregarEstudiante(Estudiante estudiante) {
        servicios.getEstudianteDAO().guardar(estudiante);
        estudiantesData.add(estudiante); // Update the UI
    }

    public void eliminarEstudiante(Estudiante estudiante) {
        if (estudiante == null) return;
        servicios.getEstudianteDAO().eliminar(estudiante.getId());
        estudiantesData.remove(estudiante); // Update the UI
    }

    public void agregarProfesor(Profesor profesor) {
        servicios.getProfesorDAO().guardar(profesor); // <-- El DAO ahora asigna el ID generado
        profesoresData.add(profesor);
    }

    public void eliminarProfesor(Profesor profesor) {
        if (profesor == null) return;
        servicios.getProfesorDAO().eliminar(profesor.getId());
        profesoresData.remove(profesor); // Update the UI
    }

    public void agregarCurso(Curso curso) {
        curso.addObserver(cursoLogger); // Registrar observador para nuevos cursos
        servicios.getCursoDAO().guardar(curso);
        listaDeCursos.add(curso); // Update the UI
    }

    public void agregarPrograma(Programa programa) {
        servicios.getProgramaDAO().guardar(programa);
        listaDeProgramas.add(programa); // Update the UI
    }

    public void agregarFacultad(Facultad facultad) {
        servicios.getFacultadDAO().guardar(facultad);
        listaDeFacultades.add(facultad); // Update the UI
    }

   /* public void asignarCurso(CursoProfesor asignacion) {
        // This part needs a CursoProfesorDAO to be fully persistent.
        asignacionesData.add(asignacion);
    }

    public void eliminarAsignacion(CursoProfesor asignacion) {
        if (asignacion == null) return;
        // This part needs a CursoProfesorDAO to be fully persistent.
        asignacionesData.remove(asignacion);
    }*/

    public List<Persona> getPersonasYProfesores() {
        // This can be used to populate ComboBoxes, for example, for selecting a Dean.
        return servicios.getPersonaDAO().cargarTodas();
    }
}