package observer;

import model.Curso;

public interface CursoObserver {
    void onCursoModificado(Curso curso);
    void onCursoEliminado(Curso curso);
    void onCursoAgregado(Curso curso);
}
