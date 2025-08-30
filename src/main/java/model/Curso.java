package model;

public class Curso {
    private int id;
    private String nombre;
    private Programa programa;
    private boolean activo;

    public Curso(int id, String nombre, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setPrograma(Programa programa) { this.programa = programa; }

    @Override
    public String toString() {
        return "Curso [ID=" + id + ", Nombre='" + nombre + "']";
    }
}