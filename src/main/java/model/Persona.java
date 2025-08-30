package model;

public class Persona {
    protected double id;
    protected String nombres;
    protected String apellidos;
    protected String email;

    public Persona(double id, String nombres, String apellidos, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    // Getters y Setters
    public void setId(double id) {this.id = id;}   
    public String getEmail() {return email;}
    public double getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    
    @Override
    public String toString() {
        return "ID=" + id + ", Nombre='" + nombres + " " + apellidos + "'";
    }
}