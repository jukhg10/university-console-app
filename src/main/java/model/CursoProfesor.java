package model;

public class CursoProfesor {

    private Profesor profesor;
    private Curso curso;
    private int ano;
    private int semestre;

    public CursoProfesor(Profesor profesor, Curso curso, int ano, int semestre) {
        this.profesor = profesor;
        this.curso = curso;
        this.ano = ano;
        this.semestre = semestre;
    }

    public Profesor getProfesor() { return profesor; }
    public Curso getCurso() { return curso; }
    public int getAno() { return ano; }
    public int getSemestre() { return semestre; }

    @Override
    public String toString() {
        return "Asignacion [Profesor=" + profesor.getNombres() + ", Curso=" + curso.getNombre() + 
               ", Año=" + ano + ", Semestre=" + semestre + "]";
    }
}