package observer;

import model.Curso;

public class CursoConsoleLogger implements CursoObserver{
    @Override
    public void onCursoModificado(Curso curso) {
        System.out.println(">>> [OBSERVER] El curso '" + curso.getNombre() + "' (ID: " + curso.getId() + ") ha sido modificado.");
    }
}
