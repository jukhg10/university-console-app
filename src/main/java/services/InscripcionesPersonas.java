package services;

import model.Persona;
import persistence.PersonaDAO;
import java.util.List;

public class InscripcionesPersonas {

    private final PersonaDAO personaDAO;

    public InscripcionesPersonas(PersonaDAO personaDAO) {
        this.personaDAO = personaDAO;
    }

    public List<Persona> getPersonas() {
        return personaDAO.cargarTodas();
    }
}