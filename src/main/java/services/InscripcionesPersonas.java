package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Persona;
import persistence.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class InscripcionesPersonas {

    public final ObservableList<Persona> listado;

    public InscripcionesPersonas() {
        this.listado = FXCollections.observableArrayList();
        this.listado.addAll(DatabaseManager.cargarPersonas());
    }

    public void inscribir(Persona p) {
        if (p == null) return;
        if (listado.stream().noneMatch(per -> per.getId() == p.getId())) {
            listado.add(p);
        }
    }

    public void eliminar(double id) {
        listado.removeIf(p -> p.getId() == id);
    }

    public void actualizar(Persona p) {
        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getId() == p.getId()) {
                listado.set(i, p);
                break;
            }
        }
    }
    public void cargarDatos() {
        this.listado.clear();
        this.listado.addAll(DatabaseManager.cargarPersonas());
    }
}
