package persistence;

import model.Inscripcion;

import java.util.List;

public interface InscripcionDAOInterface {
    void guardarInscripcion(Inscripcion inscripcion);
    List<Inscripcion> cargarInscripciones();
    void eliminarInscripcion(int id);
}
