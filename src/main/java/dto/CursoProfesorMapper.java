package dto;

import model.Curso;
import model.CursoProfesor;
import model.Profesor;

public class CursoProfesorMapper {
    public static CursoProfesor toEntity(CursoProfesorDTO dto, Profesor profesor, Curso curso) {
        return new CursoProfesor(
                profesor,
                curso,
                dto.getAno(),
                dto.getSemestre()
        );
    }

    public static CursoProfesorDTO toDTO(CursoProfesor entity) {
        return new CursoProfesorDTO(
                entity.getProfesor().getId(),
                entity.getCurso().getId(),
                entity.getAno(),
                entity.getSemestre()
        );
    }
}
