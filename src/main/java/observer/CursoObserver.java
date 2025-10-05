package observer;

import model.Curso;

public interface CursoObserver {
    void onCursoModificado(Curso curso);
}
