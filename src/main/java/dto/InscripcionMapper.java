package dto;

import model.Inscripcion;

public class InscripcionMapper {

    public static Inscripcion toEntity(InscripcionDTO dto) {
        // Convertir EstudianteDTO a Estudiante si el estudiante no es nulo
        model.Estudiante estudiante = null;
        if (dto.getEstudiante() != null) {
            estudiante = EstudianteMapper.toEntity(dto.getEstudiante());
        }

        // Convertir CursoDTO a Curso si el curso no es nulo
        model.Curso curso = null;
        if (dto.getCurso() != null) {
            curso = CursoMapper.toEntity(dto.getCurso());
        }

        Inscripcion inscripcion = new Inscripcion(
                estudiante,
                curso,
                dto.getAno(),
                dto.getSemestre()
        );
        inscripcion.setId(dto.getId());

        return inscripcion;
    }

    public static InscripcionDTO toDTO(Inscripcion entity) {
        // Convertir Estudiante a EstudianteDTO si el estudiante no es nulo
        EstudianteDTO estudianteDTO = null;
        if (entity.getEstudiante() != null) {
            estudianteDTO = EstudianteMapper.toDTO(entity.getEstudiante());
        }

        // Convertir Curso a CursoDTO si el curso no es nulo
        CursoDTO cursoDTO = null;
        if (entity.getCurso() != null) {
            cursoDTO = CursoMapper.toDTO(entity.getCurso());
        }

        return new InscripcionDTO(
                entity.getId(),
                estudianteDTO,
                cursoDTO, // <-- Pasamos el CursoDTO
                entity.getAno(),
                entity.getSemestre()
        );
    }
}