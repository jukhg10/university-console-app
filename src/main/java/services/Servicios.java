package services;

import persistence.*;
import persistence.adapter.DatabaseAdapter; // Importa el adaptador

public class Servicios {

    // --- DAOs ---
    private final PersonaDAO personaDAO;
    private final CursoDAO cursoDAO;
    private final InscripcionDAO inscripcionDAO;
    private final EstudianteDAO estudianteDAO;
    private final ProfesorDAO profesorDAO;
    private final FacultadDAO facultadDAO;
    private final ProgramaDAO programaDAO;
    private final CursoProfesorDAO cursoProfesorDAO;
    
    // --- Other Services ---
    private final CursosInscritos cursosInscritos;
    private final CursosProfesores cursosProfesores;
    private final InscripcionesPersonas inscripcionesPersonas;

    // Constructor que acepta DatabaseAdapter
    public Servicios(DatabaseAdapter adapter) {
        // Initialize all DAOs using the provided adapter
        this.personaDAO = new PersonaDAO(adapter);
        this.cursoDAO = new CursoDAO(adapter);
        this.inscripcionDAO = new InscripcionDAO(adapter);
        this.estudianteDAO = new EstudianteDAO(adapter);
        this.profesorDAO = new ProfesorDAO(adapter);
        this.facultadDAO = new FacultadDAO(adapter);
        this.programaDAO = new ProgramaDAO(adapter);
        this.cursoProfesorDAO = new CursoProfesorDAO(adapter);
        
        // Initialize other services, passing the DAOs they depend on
        this.cursosInscritos = new CursosInscritos(inscripcionDAO);
        this.cursosProfesores = new CursosProfesores();
        this.inscripcionesPersonas = new InscripcionesPersonas(personaDAO);
    }

    // --- Getters for all DAOs and Services ---
    public PersonaDAO getPersonaDAO() { return personaDAO; }
    public CursoDAO getCursoDAO() { return cursoDAO; }
    public InscripcionDAO getInscripcionDAO() { return inscripcionDAO; }
    public EstudianteDAO getEstudianteDAO() { return estudianteDAO; }
    public ProfesorDAO getProfesorDAO() { return profesorDAO; }
    public FacultadDAO getFacultadDAO() { return facultadDAO; }
    public ProgramaDAO getProgramaDAO() { return programaDAO; }
    
    public CursosInscritos getCursosInscritos() { return cursosInscritos; }
    public CursosProfesores getCursosProfesores() { return cursosProfesores; }
    public InscripcionesPersonas getInscripcionesPersonas() { return inscripcionesPersonas; }

    public CursoProfesorDAO getCursoProfesorDAO() { return cursoProfesorDAO; }
}