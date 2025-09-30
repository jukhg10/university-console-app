package persistence;

import model.CursoProfesor;

import java.util.List;

public interface CursoProfesorDAOInterface {

    void guardarAsignacion(CursoProfesor asignacion);
    List<CursoProfesor> cargarAsignaciones();
    void eliminarAsignacion(CursoProfesor asignacion);
}
