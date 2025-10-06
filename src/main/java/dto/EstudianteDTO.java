package dto;

public class EstudianteDTO {
    private double id;
    private String nombres;
    private String apellidos;
    private String email;
    private double codigo;

    public EstudianteDTO(double id, String nombres, String apellidos, String email, double codigo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.codigo = codigo;
    }

    // Getters
    public double getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }
    public double getCodigo() { return codigo; }

    // Setters
    public void setId(double id) { this.id = id; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setEmail(String email) { this.email = email; }
    public void setCodigo(double codigo) { this.codigo = codigo; }

    @Override
    public String toString() {
        return "EstudianteDTO{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", codigo=" + codigo +
                '}';
    }
}