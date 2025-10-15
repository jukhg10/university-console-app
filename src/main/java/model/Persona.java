package model;

public class Persona {
    private double id;
    private String nombres;
    private String apellidos;
    private String email;

    // --- AÑADIR CONSTRUCTOR VACÍO ---
    public Persona() {
    }
    
    // Constructor con parámetros (existente)
    public Persona(double id, String nombres, String apellidos, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    // Getters y Setters
    public double getId() { return id; }
    public void setId(double id) { this.id = id; }
    public String getNombres() { return nombres; }
    
    // --- AÑADIR SETTER FALTANTE ---
    public void setNombres(String nombres) { this.nombres = nombres; }
    
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Persona [ID=" + id + ", Nombre='" + nombres + " " + apellidos + "']";
    }
}