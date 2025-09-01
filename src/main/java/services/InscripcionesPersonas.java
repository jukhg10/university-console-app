package services;

import model.Persona;

import java.util.ArrayList;
import java.util.List;

public class InscripcionesPersonas {

    public List<Persona> listado;

    public InscripcionesPersonas() {
        this.listado = new ArrayList<>();
    }


    public void inscribir(Persona p) {
        if (p == null) return;
        boolean existe = false;
        for (Persona persona : listado) {
            if (persona.getId() == p.getId()) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            listado.add(p);
        }
    }

    public void eliminar(double idInscripcion) {
        listado.removeIf(p -> p.getId() == idInscripcion);
    }


    public void actualizar(Persona p) {
        if (p == null) return;
        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getId() == p.getId()) {
                listado.set(i, p);
                break;
            }
        }
    }


    public void guardarInformacion(Persona p) {
        if (p == null) return;
        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getId() == p.getId()) {
                listado.set(i, p);
                return;
            }
        }
        // Si no existe, se agrega
        listado.add(p);
    }


    public void cargarDatos() {
        if (listado == null) {
            listado = new ArrayList<>();
        }

    }




}
