package dto;

import model.Facultad;

public class FacultadMapper {

    public static Facultad toEntity(FacultadDTO dto) {
        Facultad facultad = new Facultad(
                dto.getId(),
                dto.getNombre()
        );

        // Convertir PersonaDTO a Persona si el decano no es nulo
        if (dto.getDecano() != null) {
            PersonaDTO decanoDTO = dto.getDecano();
            model.Persona decano = new model.Persona(
                    decanoDTO.getId(),
                    decanoDTO.getNombres(),
                    decanoDTO.getApellidos(),
                    decanoDTO.getEmail()
            );
            facultad.setDecano(decano);
        }

        return facultad;
    }

    public static FacultadDTO toDTO(Facultad entity) {
        PersonaDTO decanoDTO = null;
        if (entity.getDecano() != null) {
            decanoDTO = new PersonaDTO(
                    entity.getDecano().getId(),
                    entity.getDecano().getNombres(),
                    entity.getDecano().getApellidos(),
                    entity.getDecano().getEmail()
            );
        }

        return new FacultadDTO(
                entity.getId(),
                entity.getNombre(),
                decanoDTO
        );
    }
}