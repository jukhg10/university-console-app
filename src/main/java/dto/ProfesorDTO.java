package dto;

public class ProfesorDTO extends PersonaDTO{
    private String tipoContrato;

    public ProfesorDTO(double id, String nombres, String apellidos, String email, String tipoContrato) {
        super(id, nombres, apellidos, email);
        this.tipoContrato = tipoContrato;
    }

    // Constructor vacío
    public ProfesorDTO() {
        super();
    }

    // Getter y Setter para tipoContrato
    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }

    @Override
    public String toString() {
        return "ProfesorDTO{" +
                "id=" + getId() +
                ", nombres='" + getNombres() + '\'' +
                ", apellidos='" + getApellidos() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", tipoContrato='" + tipoContrato + '\'' +
                '}';
    }
}
