package dto;

import model.Profesor;

public class ProfesorMapper {
    public static Profesor toEntity(ProfesorDTO dto) {
        return new Profesor(
                dto.getId(),
                dto.getNombres(),
                dto.getApellidos(),
                dto.getEmail(),
                dto.getTipoContrato()
        );
    }

    public static ProfesorDTO toDTO(Profesor entity) {
        return new ProfesorDTO(
                entity.getId(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getEmail(),
                entity.getTipoContrato()
        );
    }
}
