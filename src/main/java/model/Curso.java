package model;

import observer.CursoObserver;

import java.util.ArrayList;
import java.util.List;

public class Curso {
    private int id;
    private String nombre;
    private Programa programa;
    private boolean activo;

    public Curso() {
    }
    
    // Constructor con parámetros (que ya tenías)
    public Curso(int id, String nombre, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }


    // Getters y Setters (sin cambios)
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setPrograma(Programa programa) { this.programa = programa; }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Programa getPrograma() {
        return programa;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Curso [ID=" + id + ", Nombre='" + nombre + "']";
    }
}