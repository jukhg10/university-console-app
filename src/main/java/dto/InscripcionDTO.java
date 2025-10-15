package dto;

import model.Curso;
import model.Estudiante;

public class InscripcionDTO {
    private int id;
    private EstudianteDTO estudiante;
    private CursoDTO curso; // <-- Cambiado de Curso a CursoDTO
    private int ano;
    private int semestre;

    public InscripcionDTO(int id, EstudianteDTO estudiante, CursoDTO curso, int ano, int semestre) { // <-- Cambiado Curso a CursoDTO
        this.id = id;
        this.estudiante = estudiante;
        this.curso = curso;
        this.ano = ano;
        this.semestre = semestre;
    }

    // Getters
    public int getId() { return id; }
    public EstudianteDTO getEstudiante() { return estudiante; }
    public CursoDTO getCurso() { return curso; } // <-- Cambiado de Curso a CursoDTO
    public int getAno() { return ano; }
    public int getSemestre() { return semestre; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setEstudiante(EstudianteDTO estudiante) { this.estudiante = estudiante; }
    public void setCurso(CursoDTO curso) { this.curso = curso; } // <-- Cambiado de Curso a CursoDTO
    public void setAno(int ano) { this.ano = ano; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    @Override
    public String toString() {
        return "InscripcionDTO{" +
                "id=" + id +
                ", estudiante=" + estudiante +
                ", curso=" + curso +
                ", ano=" + ano +
                ", semestre=" + semestre +
                '}';
    }
}