package dto;

public class FacultadDTO {
    private double id;
    private String nombre;
    private PersonaDTO decano; // Usamos PersonaDTO para el decano

    public FacultadDTO(double id, String nombre, PersonaDTO decano) {
        this.id = id;
        this.nombre = nombre;
        this.decano = decano;
    }

    // Getters
    public double getId() { return id; }
    public String getNombre() { return nombre; }
    public PersonaDTO getDecano() { return decano; }

    // Setters
    public void setId(double id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDecano(PersonaDTO decano) { this.decano = decano; }

    @Override
    public String toString() {
        return "FacultadDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", decano=" + decano +
                '}';
    }
}