package model;

public class Facultad {
    private double id;
    private String nombre;
    private Persona decano;

    // --- AÑADIR CONSTRUCTOR VACÍO ---
    // Esto resuelve el error de compilación en ConsoleApp.
    public Facultad() {
    }

    // Constructor con parámetros (si ya lo tienes, déjalo)
    public Facultad(double id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters (sin cambios)
    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Persona getDecano() {
        return decano;
    }

    public void setDecano(Persona decano) {
        this.decano = decano;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}