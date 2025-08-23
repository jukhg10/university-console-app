package services;

import model.CursoProfesor;
import model.Persona;

import java.util.List;
import java.util.stream.Collectors;

public class InscripcionesPersonas implements Servicios{

    public List<Persona> listado;




    public void inscribir(Persona i) {

    }

    public void eliminar(int idInscripcion) {
    }

    public void actualizar(Persona i) {

    }


    public void guardarInformacion(Persona i) {

    }

    public void cargarDatos() {

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
        return listado.stream().map(Persona::toString).collect(Collectors.toList());
    }

}
