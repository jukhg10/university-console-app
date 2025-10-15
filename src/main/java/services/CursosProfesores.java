package services;

import model.Persona;
import java.util.Collections;
import java.util.List;

public class CursosProfesores {

    // FIX: Add the public, no-argument constructor
    public CursosProfesores() {
        // This is where you might initialize a CursoProfesorDAO in the future
    }

    /**
     * Retrieves a list of all professors.
     * NOTE: This is a placeholder. A full implementation would require a CursoProfesorDAO.
     * @return A list of Persona objects representing professors.
     */
    public List<Persona> getProfesores() {
        // The logic to load professors from their assignments would go here.
        // For now, we return an empty list to prevent errors.
        return Collections.emptyList();
    }
}