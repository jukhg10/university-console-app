package services;

import model.Inscripcion;
import persistence.InscripcionDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CursosInscritos implements Servicios {
    private List<Inscripcion> listado;
    private final InscripcionDAO dao;

    public CursosInscritos() {
        this.listado = new ArrayList<>();
        this.dao = new InscripcionDAO();
    }
    
    // Métodos de gestión
    public void inscribirCurso(Inscripcion i) {
        dao.guardarInformacion(i); // Guarda en DB y obtiene el ID
        this.listado.add(i);
    }
    
    public void eliminar(int idInscripcion) {
        dao.eliminar(idInscripcion);
        listado.removeIf(insc -> insc.getId() == idInscripcion);
    }
    
    public void cargarDatos() {
        this.listado = dao.cargarDatos();
    }
    
    // Métodos de la interfaz Servicios
    @Override
    public String imprimirPosicion(int posicion) {
        if (posicion >= 0 && posicion < listado.size()) {
            return listado.get(posicion).toString();
        }
        return "Posición inválida.";
    }

    @Override
    public int cantidadActual() {
        return listado.size();
    }

    @Override
    public List<String> imprimirLista() {
        return listado.stream().map(Inscripcion::toString).collect(Collectors.toList());
    }
}