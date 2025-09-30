package persistence;

import java.util.List;

public interface Repositorio<T> {
    void guardar(T entidad);
    void eliminar(T entidad);
    List<T> listar();
    T buscarPorId(Object id);
}