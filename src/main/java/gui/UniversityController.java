package gui;

import dto.CursoDTO;
import dto.CursoMapper;
import dto.CursoProfesorDTO;
import dto.CursoProfesorMapper;
import dto.EstudianteDTO;
import dto.EstudianteMapper;
import dto.FacultadDTO;
import dto.FacultadMapper;
import dto.InscripcionDTO;
import dto.InscripcionMapper;
import dto.PersonaDTO;
import dto.PersonaMapper;
import dto.ProgramaDTO;
import dto.ProgramaMapper;
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
    private static UniversityController instance;
    private final Servicios servicios;
    // private final CursoObserver cursoLogger = new CursoConsoleLogger();

    // Lists that back the TableViews in the UI
    private final ObservableList<PersonaDTO> personasData;
    private final ObservableList<CursoDTO> listaDeCursos; // <-- Cambiado a DTO
    private final ObservableList<InscripcionDTO> inscripcionesData; // <-- Cambiado a DTO
    private final ObservableList<EstudianteDTO> estudiantesData; // <-- Cambiado a DTO
    private final ObservableList<Profesor> profesoresData;
    private final ObservableList<ProgramaDTO> listaDeProgramas; // <-- Cambiado a DTO
    private final ObservableList<FacultadDTO> listaDeFacultades; // <-- Cambiado a DTO
    private final ObservableList<CursoProfesorDTO> asignacionesData;


    private UniversityController() {
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
        // Convertir las entidades a DTO para la UI
        listaDeCursos = FXCollections.observableArrayList(
                servicios.getCursoDAO().cargarTodos().stream()
                        .map(CursoMapper::toDTO)
                        .collect(Collectors.toList())
        );
        // Convertir las entidades a DTO para la UI
        inscripcionesData = FXCollections.observableArrayList(
                servicios.getInscripcionDAO().cargarDatos().stream()
                        .map(InscripcionMapper::toDTO)
                        .collect(Collectors.toList())
        );
        // Convertir las entidades a DTO para la UI
        estudiantesData = FXCollections.observableArrayList(
                servicios.getEstudianteDAO().cargarTodos().stream()
                        .map(EstudianteMapper::toDTO)
                        .collect(Collectors.toList())
        );
        profesoresData = FXCollections.observableArrayList(servicios.getProfesorDAO().cargarTodos());
        // Convertir las entidades a DTO para la UI
        listaDeProgramas = FXCollections.observableArrayList(
                servicios.getProgramaDAO().cargarTodos().stream()
                        .map(ProgramaMapper::toDTO)
                        .collect(Collectors.toList())
        );
        // Convertir las entidades a DTO para la UI
        listaDeFacultades = FXCollections.observableArrayList(
                servicios.getFacultadDAO().cargarTodas().stream()
                        .map(FacultadMapper::toDTO)
                        .collect(Collectors.toList())
        );
        // Convertir las entidades a DTO para la UI
        asignacionesData = FXCollections.observableArrayList(
                servicios.getCursoProfesorDAO().cargarTodos().stream()
                        .map(CursoProfesorMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    public static synchronized UniversityController getInstance() {
        if (instance == null) {
            instance = new UniversityController();
        }
        return instance;
    }

    // --- Getters for the ObservableLists ---
    public ObservableList<PersonaDTO> getPersonasData() { return personasData; }
    public ObservableList<CursoDTO> getListaDeCursos() { return listaDeCursos; } // <-- Cambiado a DTO
    public ObservableList<InscripcionDTO> getInscripcionesData() { return inscripcionesData; } // <-- Cambiado a DTO
    public ObservableList<EstudianteDTO> getEstudiantesData() { return estudiantesData; } // <-- Cambiado a DTO
    public ObservableList<Profesor> getProfesoresData() { return profesoresData; }
    public ObservableList<ProgramaDTO> getListaDeProgramas() { return listaDeProgramas; } // <-- Cambiado a DTO
    public ObservableList<FacultadDTO> getListaDeFacultades() { return listaDeFacultades; } // <-- Cambiado a DTO
    public ObservableList<CursoProfesorDTO> getAsignacionesData() { return asignacionesData; }

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
    public void inscribirEstudiante(InscripcionDTO inscripcionDTO) { // <-- Cambiado a DTO
        Inscripcion inscripcion = InscripcionMapper.toEntity(inscripcionDTO); // <-- Convertir DTO a entidad
        servicios.getInscripcionDAO().guardar(inscripcion); // El DAO actualiza el ID de la entidad
        // Convertimos la entidad actualizada (con el ID) de nuevo a DTO
        InscripcionDTO actualizado = InscripcionMapper.toDTO(inscripcion);
        inscripcionesData.add(actualizado); // <-- Agregamos el DTO actualizado a la UI
    }


    public boolean eliminarInscripcion(InscripcionDTO inscripcionDTO) { // <-- Cambiado a DTO
        if (inscripcionDTO == null) return false;
        try {
            servicios.getInscripcionDAO().eliminar(inscripcionDTO.getId());
            inscripcionesData.remove(inscripcionDTO); // Update the UI con DTO
            System.out.println("UI: Inscripción eliminada exitosamente.");
            return true; // <-- Indicar que fue exitoso
        } catch (Exception e) {
            System.err.println("Error al eliminar inscripción: " + e.getMessage());
            return false; // <-- Indicar que no fue exitoso
        }
    }

    public void agregarEstudiante(EstudianteDTO estudianteDTO) { // <-- Cambiado a DTO
        Estudiante estudiante = EstudianteMapper.toEntity(estudianteDTO); // <-- Convertir DTO a entidad
        servicios.getEstudianteDAO().guardar(estudiante); // El DAO actualiza el ID de la entidad
        // Convertimos la entidad actualizada (con el ID) de nuevo a DTO
        EstudianteDTO actualizado = EstudianteMapper.toDTO(estudiante);
        estudiantesData.add(actualizado); // <-- Agregamos el DTO actualizado a la UI
    }

    public boolean eliminarEstudiante(EstudianteDTO estudianteDTO) { // <-- Cambiado a DTO
        if (estudianteDTO == null) return false;
        try {
            servicios.getEstudianteDAO().eliminar(estudianteDTO.getId());
            estudiantesData.remove(estudianteDTO); // Update the UI con DTO
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

    public boolean agregarCurso(CursoDTO cursoDTO) { // <-- Cambiado a DTO
        Curso curso = CursoMapper.toEntity(cursoDTO); // <-- Convertir DTO a entidad
        boolean exito = servicios.getCursoDAO().guardar(curso); // El DAO actualiza el ID de la entidad
        if (exito) {
            // Convertimos la entidad actualizada (con el ID) de nuevo a DTO
            CursoDTO actualizado = CursoMapper.toDTO(curso);
            listaDeCursos.add(actualizado); // <-- Agregamos el DTO actualizado a la UI
        } else {
            // Aquí puedes mostrar un mensaje de error al usuario
            System.err.println("No se pudo agregar el curso.");
        }
        return exito;
    }
    public boolean eliminarCurso(CursoDTO cursoDTO) { // <-- Cambiado a DTO
        if (cursoDTO == null) return false;
        if (!servicios.getCursoDAO().puedeEliminar(cursoDTO.getId())) {
            System.out.println("No se puede eliminar el curso porque tiene inscripciones asociadas.");
            return false; // <-- Indicar que no fue exitoso
        }
        servicios.getCursoDAO().eliminar(cursoDTO.getId());
        listaDeCursos.remove(cursoDTO); // Update the UI con DTO
        return true; // <-- Indicar que fue exitoso
    }

    public boolean modificarCurso(CursoDTO cursoDTO) { // <-- Cambiado a DTO
        if (cursoDTO == null) return false;
        try {
            // Convertir DTO a entidad para el DAO
            Curso cursoEntidad = CursoMapper.toEntity(cursoDTO);
            // El DAO se encarga de actualizar en la base de datos
            servicios.getCursoDAO().actualizar(cursoEntidad); // <-- Asumiendo que crearás este método en CursoDAO

            // No toques la interfaz aquí, la entidad ya está actualizada
            System.out.println("UI: Curso modificado exitosamente.");
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar curso: " + e.getMessage());
            return false;
        }
    }
    public void recargarInscripciones() { // <-- Método actualizado para recargar DTOs
        inscripcionesData.setAll(
                servicios.getInscripcionDAO().cargarDatos().stream()
                        .map(InscripcionMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }
    public void recargarAsignaciones() {
        // Convertir las entidades a DTO para la UI
        asignacionesData.setAll(
                servicios.getCursoProfesorDAO().cargarTodos().stream()
                        .map(CursoProfesorMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    public void recargarCursos() { // <-- Método para recargar cursos como DTO
        listaDeCursos.setAll(
                servicios.getCursoDAO().cargarTodos().stream()
                        .map(CursoMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    public void agregarPrograma(ProgramaDTO programaDTO) { // <-- Cambiado a DTO
        Programa programa = ProgramaMapper.toEntity(programaDTO); // <-- Convertir DTO a entidad
        servicios.getProgramaDAO().guardar(programa); // El DAO actualiza el ID de la entidad
        // Convertimos la entidad actualizada (con el ID) de nuevo a DTO
        ProgramaDTO actualizado = ProgramaMapper.toDTO(programa);
        listaDeProgramas.add(actualizado); // <-- Agregamos el DTO actualizado a la UI
    }
    public boolean eliminarPrograma(ProgramaDTO programaDTO) { // <-- Cambiado a DTO
        if (programaDTO == null) return false;
        if (!servicios.getProgramaDAO().puedeEliminar(programaDTO.getId())) {
            System.out.println("No se puede eliminar el programa porque tiene cursos asociados.");
            return false; // <-- Indicar que no fue exitoso
        }
        servicios.getProgramaDAO().eliminar(programaDTO.getId());
        listaDeProgramas.remove(programaDTO); // Update the UI con DTO
        return true; // <-- Indicar que fue exitoso
    }

    public void agregarFacultad(FacultadDTO facultadDTO) { // <-- Cambiado a DTO
        Facultad facultad = FacultadMapper.toEntity(facultadDTO); // <-- Convertir DTO a entidad
        servicios.getFacultadDAO().guardar(facultad); // El DAO actualiza el ID de la entidad
        // Convertimos la entidad actualizada (con el ID) de nuevo a DTO
        FacultadDTO actualizado = FacultadMapper.toDTO(facultad);
        listaDeFacultades.add(actualizado); // <-- Agregamos el DTO actualizado a la UI
    }
    public boolean eliminarFacultad(FacultadDTO facultadDTO) { // <-- Cambiado a DTO
        if (facultadDTO == null) return false;
        if (!servicios.getFacultadDAO().puedeEliminar(facultadDTO.getId())) {
            System.out.println("No se puede eliminar la facultad porque tiene programas asociados.");
            return false;
        }
        servicios.getFacultadDAO().eliminar(facultadDTO.getId());
        listaDeFacultades.remove(facultadDTO); // Update the UI con DTO
        return true;
    }


    public void asignarCurso(CursoProfesorDTO asignacionDTO) {
        // Obtener las entidades relacionadas
        Profesor profesor = servicios.getProfesorDAO().cargarPorId(asignacionDTO.getProfesorId());
        Curso curso = servicios.getCursoDAO().cargarPorId(asignacionDTO.getCursoId());

        if (profesor != null && curso != null) {
            // Crear entidad desde DTO
            CursoProfesor asignacion = CursoProfesorMapper.toEntity(asignacionDTO, profesor, curso);
            servicios.getCursoProfesorDAO().guardar(asignacion);

            // Agregar DTO a la lista de la UI
            asignacionesData.add(asignacionDTO); // Update the UI
        } else {
            System.err.println("No se pudo encontrar el profesor o curso para la asignación.");
        }
    }

    public boolean eliminarAsignacion(CursoProfesorDTO asignacionDTO) {
        if (asignacionDTO == null) return false;
        try {
            servicios.getCursoProfesorDAO().eliminar(
                    asignacionDTO.getProfesorId(),
                    asignacionDTO.getCursoId(),
                    asignacionDTO.getAno(),
                    asignacionDTO.getSemestre()
            );
            asignacionesData.remove(asignacionDTO); // Update the UI
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