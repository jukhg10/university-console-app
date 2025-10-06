package dto;

import model.Persona;
import java.util.List;
import java.util.stream.Collectors;

public class PersonaMapper {

    /**
     * Convierte una entidad Persona a un PersonaDTO.
     */
    public static PersonaDTO toDTO(Persona persona) {
        PersonaDTO dto = new PersonaDTO();
        dto.setId(persona.getId());
        dto.setNombres(persona.getNombres());
        dto.setApellidos(persona.getApellidos());
        dto.setEmail(persona.getEmail());
        return dto;
    }

    /**
     * Convierte un PersonaDTO a una entidad Persona.
     */
    public static Persona toEntity(PersonaDTO dto) {
        return new Persona(dto.getId(), dto.getNombres(), dto.getApellidos(), dto.getEmail());
    }

    /**
     * Convierte una lista de entidades Persona a una lista de PersonaDTO.
     */
    public static List<PersonaDTO> toDTOList(List<Persona> personas) {
        return personas.stream().map(PersonaMapper::toDTO).collect(Collectors.toList());
    }
}