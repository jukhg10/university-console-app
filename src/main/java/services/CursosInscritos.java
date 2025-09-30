package services;

import model.Inscripcion;
import persistence.InscripcionDAO;
import persistence.InscripcionDAOInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CursosInscritos implements Servicios {
    private List<Inscripcion> lista;
    private final InscripcionDAOInterface dao;

    public CursosInscritos(InscripcionDAOInterface dao) {
        this.dao = dao;
        this.lista = dao.cargarInscripciones();
    }

    public void inscribir(Inscripcion inscripcion) {
        dao.guardarInscripcion(inscripcion);
        this.lista.add(inscripcion);
    }

    public void eliminar(int id) {
        dao.eliminarInscripcion(id);
        this.lista.removeIf(i -> i.getId() == id);
    }

    public void cargarDatos() {
        this.lista = dao.cargarInscripciones();
    }

    public List<Inscripcion> obtenerLista() {
        return new ArrayList<>(this.lista);
    }

    @Override
    public String imprimirPosicion(int posicion) {
        if (posicion >= 0 && posicion < lista.size()) {
            return "Posición " + posicion + ": " + lista.get(posicion).toString();
        }
        return "Posición inválida.";
    }

    @Override
    public int cantidadActual() {
        return lista.size();
    }

    @Override
    public List<String> imprimirLista() {
        return lista.stream().map(Inscripcion::toString).collect(Collectors.toList());
    }
}