package dto;

import model.Estudiante;

public class EstudianteMapper {

    public static Estudiante toEntity(EstudianteDTO dto) {
        // Usamos true como valor predeterminado para activo y 0.0 para promedio
        // ya que no tenemos esa información en el DTO
        Estudiante estudiante = new Estudiante(
                dto.getId(),
                dto.getNombres(),
                dto.getApellidos(),
                dto.getEmail(),
                dto.getCodigo(),
                true, // Valor predeterminado para activo
                0.0  // Valor predeterminado para promedio
        );
        return estudiante;
    }

    public static EstudianteDTO toDTO(Estudiante entity) {
        return new EstudianteDTO(
                entity.getId(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getEmail(),
                entity.getCodigo()
        );
    }
}