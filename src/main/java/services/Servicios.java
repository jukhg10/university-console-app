package services;

import persistence.*;

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

    // FIX: A single, clean constructor
    public Servicios(ConnectionFactory connectionFactory) {
        // Initialize all DAOs using the provided factory
        this.personaDAO = new PersonaDAO(connectionFactory);
        this.cursoDAO = new CursoDAO(connectionFactory);
        this.inscripcionDAO = new InscripcionDAO(connectionFactory);
        this.estudianteDAO = new EstudianteDAO(connectionFactory);
        this.profesorDAO = new ProfesorDAO(connectionFactory);
        this.facultadDAO = new FacultadDAO(connectionFactory);
        this.programaDAO = new ProgramaDAO(connectionFactory);
        
        // Initialize other services, passing the DAOs they depend on
        this.cursosInscritos = new CursosInscritos(inscripcionDAO);
        this.cursosProfesores = new CursosProfesores(); // Doesn't depend on any DAO yet
        this.inscripcionesPersonas = new InscripcionesPersonas(personaDAO);

        this.cursoProfesorDAO = new CursoProfesorDAO(connectionFactory);
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