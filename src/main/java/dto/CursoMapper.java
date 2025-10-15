package dto;

import model.Curso;

public class CursoMapper {

    public static Curso toEntity(CursoDTO dto) {
        Curso curso = new Curso(
                dto.getId(),
                dto.getNombre(),
                dto.isActivo()
        );

        // Convertir ProgramaDTO a Programa si el programa no es nulo
        if (dto.getPrograma() != null) {
            curso.setPrograma(ProgramaMapper.toEntity(dto.getPrograma()));
        }

        return curso;
    }

    public static CursoDTO toDTO(Curso entity) {
        ProgramaDTO programaDTO = null;
        if (entity.getPrograma() != null) {
            programaDTO = ProgramaMapper.toDTO(entity.getPrograma());
        }

        return new CursoDTO(
                entity.getId(),
                entity.getNombre(),
                programaDTO,
                entity.isActivo()
        );
    }
}