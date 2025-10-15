package dto;

import java.util.Date;

public class ProgramaDTO {
    private double id;
    private String nombre;
    private double duracion;
    private Date registro;
    private FacultadDTO facultad; // Usamos FacultadDTO para la facultad

    public ProgramaDTO(double id, String nombre, double duracion, Date registro, FacultadDTO facultad) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.registro = registro;
        this.facultad = facultad;
    }

    // Getters
    public double getId() { return id; }
    public String getNombre() { return nombre; }
    public double getDuracion() { return duracion; }
    public Date getRegistro() { return registro; }
    public FacultadDTO getFacultad() { return facultad; }

    // Setters
    public void setId(double id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDuracion(double duracion) { this.duracion = duracion; }
    public void setRegistro(Date registro) { this.registro = registro; }
    public void setFacultad(FacultadDTO facultad) { this.facultad = facultad; }

    @Override
    public String toString() {
        return "ProgramaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", duracion=" + duracion +
                ", registro=" + registro +
                ", facultad=" + facultad +
                '}';
    }
}