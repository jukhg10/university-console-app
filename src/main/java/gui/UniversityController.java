package gui;

import dto.PersonaDTO;
import dto.PersonaMapper;
import fabricas.FabricaServiciosInterna;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import observer.CursoObserver;
import services.Servicios;

import java.util.List;
import java.util.stream.Collectors;

public class UniversityController {
    private final Servicios servicios;
   // private final CursoObserver cursoLogger = new CursoConsoleLogger();

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


        // Registrar el broadcaster en el DAO
     //   servicios.getCursoDAO().addObserver(CursoLogBroadcaster.getInstance());

        // Registrar también el logger en consola si deseas mantenerlo
      //  CursoLogBroadcaster.getInstance().addObserver(new CursoConsoleLogger());
        // --- FIX: Load ALL data from the database upon initialization ---
        personasData = FXCollections.observableArrayList(
                servicios.getPersonaDAO().cargarTodas().stream()
                        .map(PersonaMapper::toDTO)
                        .collect(Collectors.toList())
        );
        // Cargar los cursos desde el DAO (el DAO ya está observado)
        listaDeCursos = FXCollections.observableArrayList(servicios.getCursoDAO().cargarTodos());
        inscripcionesData = FXCollections.observableArrayList(servicios.getInscripcionDAO().cargarDatos());
        estudiantesData = FXCollections.observableArrayList(servicios.getEstudianteDAO().cargarTodos());
        profesoresData = FXCollections.observableArrayList(servicios.getProfesorDAO().cargarTodos());
        listaDeProgramas = FXCollections.observableArrayList(servicios.getProgramaDAO().cargarTodos());
        listaDeFacultades = FXCollections.observableArrayList(servicios.getFacultadDAO().cargarTodas());
        asignacionesData = FXCollections.observableArrayList(servicios.getCursoProfesorDAO().cargarTodos());
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

    // Dentro de UniversityController.java
    public Servicios getServicios() {
        return servicios;
    }
    // --- FIX: Fully Implemented Data-Modification Methods ---
    public void agregarPersona(PersonaDTO personaDTO) {
        Persona persona = PersonaMapper.toEntity(personaDTO);
        servicios.getPersonaDAO().guardar(persona); // <-- El DAO ahora asigna el ID generado

        // Convertimos la entidad actualizada (con el ID) de nuevo a DTO
        PersonaDTO actualizado = PersonaMapper.toDTO(persona);

        personasData.add(actualizado); // <-- Agregamos el DTO actualizado a la UI
    }

    public boolean eliminarPersona(PersonaDTO personaDTO) {
        if (personaDTO == null) return false;
        if (!servicios.getPersonaDAO().puedeEliminar(personaDTO.getId())) {
            System.out.println("No se puede eliminar la persona porque tiene dependencias es decano.");
            return false; // <-- Indicar que no fue exitoso
        }
        servicios.getPersonaDAO().eliminar(personaDTO.getId());
        personasData.remove(personaDTO); // Update the UI
        return true; // <-- Indicar que fue exitoso
    }
    public void inscribirEstudiante(Inscripcion inscripcion) {
        servicios.getInscripcionDAO().guardar(inscripcion);
        inscripcionesData.add(inscripcion); // Update the UI
    }

    public boolean eliminarInscripcion(Inscripcion inscripcion) {
        if (inscripcion == null) return false;
        servicios.getInscripcionDAO().eliminar(inscripcion.getId());
        inscripcionesData.remove(inscripcion); // Update the UI
        System.out.println("UI: Inscripción eliminada exitosamente.");
        return true; // <-- Indicar que fue exitoso
    }

    public void agregarEstudiante(Estudiante estudiante) {
        servicios.getEstudianteDAO().guardar(estudiante);
        estudiantesData.add(estudiante); // Update the UI
    }

    public boolean eliminarEstudiante(Estudiante estudiante) {
        if (estudiante == null) return false;
        try {
            servicios.getEstudianteDAO().eliminar(estudiante.getId());
            estudiantesData.remove(estudiante); // Update the UI
            System.out.println("Estudiante eliminado exitosamente.");
            return true; // <-- Indicar que fue exitoso
        } catch (Exception e) {
            System.err.println("Error al eliminar estudiante: " + e.getMessage());
            return false; // <-- Indicar que no fue exitoso
        }
    }


    public void agregarProfesor(Profesor profesor) {
        servicios.getProfesorDAO().guardar(profesor); // <-- El DAO ahora asigna el ID generado
        profesoresData.add(profesor);
    }

    public boolean eliminarProfesor(Profesor profesor) {
        if (profesor == null) return false;
        if (!servicios.getProfesorDAO().puedeEliminar(profesor.getId())) {
            System.out.println("No se puede eliminar el profesor porque tiene cursos asignados.");
            return false; // <-- Indicar que no fue exitoso
        }
        servicios.getProfesorDAO().eliminar(profesor.getId());
        profesoresData.remove(profesor); // Update the UI
        return true; // <-- Indicar que fue exitoso
    }

    public boolean agregarCurso(Curso curso) {
        boolean exito = servicios.getCursoDAO().guardar(curso);
        if (exito) {
            listaDeCursos.add(curso); // Update the UI
        } else {
            // Aquí puedes mostrar un mensaje de error al usuario
            System.err.println("No se pudo agregar el curso.");
        }
        return exito;
    }
    public boolean eliminarCurso(Curso curso) {
        if (curso == null) return false;
        if (!servicios.getCursoDAO().puedeEliminar(curso.getId())) {
            System.out.println("No se puede eliminar el curso porque tiene inscripciones asociadas.");
            return false; // <-- Indicar que no fue exitoso
        }
        servicios.getCursoDAO().eliminar(curso.getId());
        listaDeCursos.remove(curso); // Update the UI
        return true; // <-- Indicar que fue exitoso
    }

    public boolean modificarCurso(Curso curso) {
        if (curso == null) return false;
        try {
            // El DAO se encarga de actualizar en la base de datos
            servicios.getCursoDAO().actualizar(curso); // <-- Asumiendo que crearás este método en CursoDAO

            // No toques la interfaz aquí
            System.out.println("UI: Curso modificado exitosamente.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar curso: " + e.getMessage());
            return false;
        }
    }
    public void recargarInscripciones() {
        inscripcionesData.setAll(servicios.getInscripcionDAO().cargarDatos());
    }
    public void recargarAsignaciones() {
        asignacionesData.setAll(servicios.getCursoProfesorDAO().cargarTodos()); // Asegúrate de que este método exista en CursoProfesorDAO
    }
    public void agregarPrograma(Programa programa) {
        servicios.getProgramaDAO().guardar(programa);
        listaDeProgramas.add(programa); // Update the UI
    }
    public boolean eliminarPrograma(Programa programa) {
        if (programa == null) return false;
        if (!servicios.getProgramaDAO().puedeEliminar(programa.getId())) {
            System.out.println("No se puede eliminar el programa porque tiene cursos asociados.");
            return false; // <-- Indicar que no fue exitoso
        }
        servicios.getProgramaDAO().eliminar(programa.getId());
        listaDeProgramas.remove(programa); // Update the UI
        return true; // <-- Indicar que fue exitoso
    }

    public void agregarFacultad(Facultad facultad) {
        servicios.getFacultadDAO().guardar(facultad);
        listaDeFacultades.add(facultad); // Update the UI
    }
    public boolean eliminarFacultad(Facultad facultad) {
        if (facultad == null) return false;
        if (!servicios.getFacultadDAO().puedeEliminar(facultad.getId())) {
            System.out.println("No se puede eliminar la facultad porque tiene programas asociados.");
            return false;
        }
        servicios.getFacultadDAO().eliminar(facultad.getId());
        listaDeFacultades.remove(facultad); // Update the UI
        return true;
    }


    public void asignarCurso(CursoProfesor asignacion) {
        servicios.getCursoProfesorDAO().guardar(asignacion);
        asignacionesData.add(asignacion); // Update the UI
    }

    public boolean eliminarAsignacion(CursoProfesor asignacion) {
        if (asignacion == null) return false;
        try {
            servicios.getCursoProfesorDAO().eliminar(
                    asignacion.getProfesor().getId(),
                    asignacion.getCurso().getId(),
                    asignacion.getAno(),
                    asignacion.getSemestre()
            );
            asignacionesData.remove(asignacion); // Update the UI
            System.out.println("UI: Asignación eliminada exitosamente.");
            return true; // <-- Indicar que fue exitoso
        } catch (Exception e) {
            System.err.println("Error al eliminar asignación: " + e.getMessage());
            return false; // <-- Indicar que no fue exitoso
        }
    }

    public List<Persona> getPersonasYProfesores() {
        // This can be used to populate ComboBoxes, for example, for selecting a Dean.
        return servicios.getPersonaDAO().cargarTodas();
    }
}