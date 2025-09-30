package persistence;

import java.util.List;

public interface GestorEntidad<T> {
    void agregar(T entidad);
    void eliminar(T entidad);
    List<T> listar();
    T buscarPorId(Object id);
}