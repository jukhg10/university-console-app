package services;

import model.Inscripcion;
import model.Persona;
import persistence.InscripcionDAO;
import java.util.List;
import java.util.stream.Collectors;

public class CursosInscritos {

    private final InscripcionDAO inscripcionDAO;

    // FIX: Add the public constructor
    public CursosInscritos(InscripcionDAO inscripcionDAO) {
        this.inscripcionDAO = inscripcionDAO;
    }

    /**
     * Retrieves a distinct list of students who are enrolled in courses.
     * @return A list of Persona objects representing the students.
     */
    public List<Persona> getEstudiantes() {
        // This logic correctly uses the DAO to get the data
        return inscripcionDAO.cargarDatos().stream()
                .map(Inscripcion::getEstudiante)
                .distinct()
                .collect(Collectors.toList());
    }
}