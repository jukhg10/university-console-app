package services;

import model.CursoProfesor;
import model.Inscripcion;
import persistence.InscripcionDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CursosProfesores implements Servicios{

    private List<CursoProfesor> listado;




    public void inscribir(CursoProfesor i) {

    }
    public void guardarInformacion(CursoProfesor i) {

    }

    public void cargarDatos() {

    }
    @Override
    public String toString() {
        return "CursosProfesores{" +
                "listado=" + listado +
                '}';
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
        return listado.stream().map(CursoProfesor::toString).collect(Collectors.toList());
    }
}
