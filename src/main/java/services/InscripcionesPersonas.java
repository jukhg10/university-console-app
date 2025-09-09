package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Persona;
import persistence.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class InscripcionesPersonas {

    // ✅ CAMBIO 1: Cambiamos el tipo de List a ObservableList
    public ObservableList<Persona> listado;

    public InscripcionesPersonas() {
        // ✅ CAMBIO 2: Inicializamos como ObservableList
        this.listado = FXCollections.observableArrayList();
        this.listado.addAll(DatabaseManager.cargarPersonas());
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
            // ✅ Al agregar a la ObservableList, cualquier TableView vinculada se actualiza automáticamente.
            listado.add(p);
        }
    }

    public void eliminar(double idInscripcion) {
        // ✅ Al eliminar de la ObservableList, cualquier TableView vinculada se actualiza automáticamente.
        listado.removeIf(p -> p.getId() == idInscripcion);
    }

    public void actualizar(Persona p) {
        if (p == null) return;
        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getId() == p.getId()) {
                // ✅ Al actualizar en la ObservableList, la GUI se actualiza automáticamente.
                listado.set(i, p);
                break;
            }
        }
    }

    public void guardarInformacion(Persona p) {
        if (p == null) return;
        for (int i = 0; i < listado.size(); i++) {
            if (listado.get(i).getId() == p.getId()) {
                // ✅ Actualización automática
                listado.set(i, p);
                return;
            }
        }
        // Si no existe, se agrega
        // ✅ Agregado automático
        listado.add(p);
    }

    public void cargarDatos() {
        if (listado == null) {
            // ✅ Aunque esto no debería ocurrir con la inicialización actual,
            // lo dejamos por compatibilidad.
            listado = FXCollections.observableArrayList();
        }
        // Aquí iría la lógica para cargar desde la base de datos, si la tienes.
        // Por ejemplo:
        // listado.setAll(DatabaseManager.cargarTodasLasPersonas());
    }
}
