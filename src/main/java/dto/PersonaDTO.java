package dto;

public class PersonaDTO {
    private double id;
    private String nombres;
    private String apellidos;
    private String email;

    // --- AÑADE ESTE CONSTRUCTOR ---
    public PersonaDTO(double id, String nombres, String apellidos, String email) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    // --- También es buena práctica tener un constructor vacío ---
    public PersonaDTO() {
    }

    // Getters
    public double getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }

    // Setters
    public void setId(double id) { this.id = id; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setEmail(String email) { this.email = email; }

    // --- Opcional: Añadir un método toString() para ver la información fácilmente ---
    @Override
    public String toString() {
        return "PersonaDTO{" +
               "id=" + id +
               ", nombres='" + nombres + '\'' +
               ", apellidos='" + apellidos + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}