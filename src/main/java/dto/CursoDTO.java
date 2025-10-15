package dto;

public class CursoDTO {
    private int id;
    private String nombre;
    private ProgramaDTO programa; // Usamos ProgramaDTO para el programa
    private boolean activo;

    public CursoDTO(int id, String nombre, ProgramaDTO programa, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.programa = programa;
        this.activo = activo;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public ProgramaDTO getPrograma() { return programa; }
    public boolean isActivo() { return activo; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrograma(ProgramaDTO programa) { this.programa = programa; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "CursoDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", programa=" + programa +
                ", activo=" + activo +
                '}';
    }
}