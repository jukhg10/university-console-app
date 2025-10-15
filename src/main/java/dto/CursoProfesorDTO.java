package dto;

public class CursoProfesorDTO {

    private double profesorId;
    private int cursoId;
    private int ano;
    private int semestre;

    public CursoProfesorDTO(double profesorId, int cursoId, int ano, int semestre) {
        this.profesorId = profesorId;
        this.cursoId = cursoId;
        this.ano = ano;
        this.semestre = semestre;
    }

    public double getProfesorId() { return profesorId; }
    public void setProfesorId(double profesorId) { this.profesorId = profesorId; }
    public int getCursoId() { return cursoId; }
    public void setCursoId(int cursoId) { this.cursoId = cursoId; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    @Override
    public String toString() {
        return "CursoProfesorDTO{" +
                "profesorId=" + profesorId +
                ", cursoId=" + cursoId +
                ", ano=" + ano +
                ", semestre=" + semestre +
                '}';
    }
}
