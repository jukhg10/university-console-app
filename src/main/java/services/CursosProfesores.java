package services;

import model.CursoProfesor;
import persistence.CursoProfesorDAOInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Esta clase ahora gestiona la RELACIÓN entre Cursos y Profesores
public class CursosProfesores implements Servicios {
    private final List<CursoProfesor> asignaciones;

    public CursosProfesores() {
        this.asignaciones = new ArrayList<>();
    }

    // --- MÉTODOS PARA LA LÓGICA DE ASIGNACIÓN ---
    public List<CursoProfesor> getAsignaciones() {
        return new ArrayList<>(asignaciones);
    }

    public void asignarCurso(CursoProfesor nuevaAsignacion) {
        this.asignaciones.add(nuevaAsignacion);
    }

    public void eliminarAsignacion(CursoProfesor asignacion) {
        this.asignaciones.remove(asignacion);
    }

    // --- MÉTODOS REQUERIDOS POR LA INTERFAZ SERVICIOS ---
    @Override
    public String imprimirPosicion(int posicion) {
        if (posicion >= 0 && posicion < asignaciones.size()) {
            return asignaciones.get(posicion).toString();
        }
        return "Posición inválida.";
    }

    @Override
    public int cantidadActual() {
        return asignaciones.size();
    }

    @Override
    public List<String> imprimirLista() {
        return asignaciones.stream()
                .map(CursoProfesor::toString)
                .collect(Collectors.toList());
    }
}