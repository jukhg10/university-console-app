package dto;

import model.Programa;

public class ProgramaMapper {

    public static Programa toEntity(ProgramaDTO dto) {
        // Convertir FacultadDTO a Facultad si la facultad no es nula
        model.Facultad facultad = null;
        if (dto.getFacultad() != null) {
            facultad = FacultadMapper.toEntity(dto.getFacultad());
        }

        return new Programa(
                dto.getId(),
                dto.getNombre(),
                dto.getDuracion(),
                dto.getRegistro(),
                facultad
        );
    }

    public static ProgramaDTO toDTO(Programa entity) {
        // Convertir Facultad a FacultadDTO si la facultad no es nula
        FacultadDTO facultadDTO = null;
        if (entity.getFacultad() != null) {
            facultadDTO = FacultadMapper.toDTO(entity.getFacultad());
        }

        return new ProgramaDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getDuracion(),
                entity.getRegistro(),
                facultadDTO
        );
    }
}