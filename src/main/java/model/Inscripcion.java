package model;

public class Inscripcion {
    private int id; // ID para la base de datos
    private Curso curso;
    private int ano;
    private int semestre;
    private Estudiante estudiante;

    public Inscripcion(Estudiante estudiante, Curso curso, int ano, int semestre) {
        this.estudiante = estudiante;
        this.curso = curso;
        this.ano = ano;
        this.semestre = semestre;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Curso getCurso() { return curso; }
    public int getAno() { return ano; }
    public int getSemestre() { return semestre; }
    public Estudiante getEstudiante() { return estudiante; }

    @Override
    public String toString() {
        return "Inscripcion [ID=" + id + ", Año=" + ano + ", Sem=" + semestre + ", " + estudiante + ", " + curso + "]";
    }
}