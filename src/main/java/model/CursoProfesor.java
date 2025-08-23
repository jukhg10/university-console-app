package model;

public class CursoProfesor {
    private Profesor profesor;
    private int ano;
    private int semestre;
    private Curso curso;

    @Override
    public String toString() {
        return "CursoProfesor [Profesor=" + profesor + ", Ano=" + ano + ", Semestre=" + semestre + ", Curso=" + curso + "]";
    }


}
