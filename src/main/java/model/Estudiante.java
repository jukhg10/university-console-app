package model;

public class Estudiante extends Persona {
    private double codigo;
    private Programa programa;
    private boolean activo;
    

    public Estudiante(double id, String nombres, String apellidos, String email, double codigo, boolean activo, double promedio) {
        super(id, nombres, apellidos, email);
        this.codigo = codigo;
        this.activo = activo;
        
    }

    // Getters y Setters
    public double getCodigo() { return codigo; }
    public Programa getPrograma() { return programa; }
    public void setPrograma(Programa programa) { this.programa = programa; }

    @Override
    public String toString() {
        return "Estudiante [" + super.toString() + ", Codigo=" + codigo + ", Activo=" + activo + "]";
    }
}