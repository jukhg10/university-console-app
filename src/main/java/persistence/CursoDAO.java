// persistence/CursoDAO.java
package persistence;

import model.Curso;
import model.Programa;
import observer.CursoObserver;
import persistence.adapter.DatabaseAdapter; // Importar la interfaz del adaptador
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private final DatabaseAdapter adapter; // Cambiar ConnectionFactory por DatabaseAdapter
    private final List<CursoObserver> observers = new ArrayList<>();

    // El constructor ahora recibe un DatabaseAdapter
    public CursoDAO(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void addObserver(CursoObserver observer) {
        observers.add(observer);
    }


    private void notifyObservers(Curso curso) {
        for (CursoObserver observer : observers) {
            observer.onCursoModificado(curso);
        }
    }

    private void notifyObserversOnDelete(Curso curso) {
        for (CursoObserver observer : observers) {
            observer.onCursoEliminado(curso);
        }
    }
    private void notifyObserversOnAdd(Curso curso) {
        for (CursoObserver observer : observers) {
            observer.onCursoAgregado(curso);
        }
    }

    public boolean guardar(Curso curso) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                adapter.quote("CURSO"),
                adapter.quote("NOMBRE"),
                adapter.quote("PROGRAMA_ID"),
                adapter.quote("ACTIVO")
        );

        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {

            stmt.setString(1, curso.getNombre());
            if (curso.getPrograma() != null) {
                stmt.setDouble(2, curso.getPrograma().getId());
            } else {
                stmt.setNull(2, java.sql.Types.NUMERIC);
            }
            stmt.setBoolean(3, curso.isActivo());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    curso.setId(generatedKeys.getInt(1));
                    System.out.println("DAO: Curso guardado con ID: " + curso.getId());

                    // 🔔 Notificar observadores del evento "curso agregado"
                    notifyObserversOnAdd(curso);
                    return true;
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean puedeEliminar(int id) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
                adapter.quote("INSCRIPCION"),
                adapter.quote("CURSO_ID")
        );
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void eliminar(int id) {
        if (!puedeEliminar(id)) {
            System.out.println("No se puede eliminar el curso con ID " + id + " porque tiene inscripciones asociadas.");
            return;
        }

        // ✅ Cargar el curso antes de eliminarlo
        Curso cursoAEliminar = cargarPorId(id);
        if (cursoAEliminar == null) {
            System.out.println("DAO: No se encontró el curso con ID " + id + " para eliminar.");
            return;
        }

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                adapter.quote("CURSO"),
                adapter.quote("ID")
        );

        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("DAO: Curso con ID " + id + " eliminado exitosamente.");
                // ✅ Ahora sí, notificar a los observadores
                notifyObserversOnDelete(cursoAEliminar);
            } else {
                System.out.println("DAO: No se encontró un curso con ID " + id + ".");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Curso cargarPorId(int id) {
        String sql = String.format("SELECT c.%s as CURSO_ID, c.%s as CURSO_NOMBRE, c.%s as CURSO_ACTIVO, " +
                        "p.%s as PROG_ID, p.%s as PROG_NOMBRE, p.%s as PROG_DURACION, p.%s as PROG_REGISTRO " +
                        "FROM %s c LEFT JOIN %s p ON c.%s = p.%s WHERE c.%s = ?",
                adapter.quote("ID"), adapter.quote("NOMBRE"), adapter.quote("ACTIVO"),
                adapter.quote("ID"), adapter.quote("NOMBRE"), adapter.quote("DURACION"),
                adapter.quote("REGISTRO_CALIFICADO"),
                adapter.quote("CURSO"), adapter.quote("PROGRAMA"),
                adapter.quote("PROGRAMA_ID"), adapter.quote("ID"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirCurso(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Curso> cargarTodos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = String.format("SELECT c.%s as CURSO_ID, c.%s as CURSO_NOMBRE, c.%s as CURSO_ACTIVO, " +
                        "p.%s as PROG_ID, p.%s as PROG_NOMBRE, p.%s as PROG_DURACION, p.%s as PROG_REGISTRO " +
                        "FROM %s c LEFT JOIN %s p ON c.%s = p.%s",
                adapter.quote("ID"), adapter.quote("NOMBRE"), adapter.quote("ACTIVO"),
                adapter.quote("ID"), adapter.quote("NOMBRE"), adapter.quote("DURACION"),
                adapter.quote("REGISTRO_CALIFICADO"),
                adapter.quote("CURSO"), adapter.quote("PROGRAMA"),
                adapter.quote("PROGRAMA_ID"), adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cursos.add(construirCurso(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }

    private Curso construirCurso(ResultSet rs) throws SQLException {
        Curso curso = new Curso(
                rs.getInt("CURSO_ID"),
                rs.getString("CURSO_NOMBRE"),
                rs.getBoolean("CURSO_ACTIVO")
        );
        double programaId = rs.getDouble("PROG_ID");
        if (!rs.wasNull()) {
            Programa programa = new Programa(
                    programaId,
                    rs.getString("PROG_NOMBRE"),
                    rs.getDouble("PROG_DURACION"),
                    rs.getDate("PROG_REGISTRO"),
                    null
            );
            curso.setPrograma(programa);
        }
        return curso;
    }

    public void actualizar(Curso curso) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
                adapter.quote("CURSO"),
                adapter.quote("NOMBRE"),
                adapter.quote("PROGRAMA_ID"),
                adapter.quote("ACTIVO"),
                adapter.quote("ID")
        );

        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNombre());
            if (curso.getPrograma() != null) {
                stmt.setDouble(2, curso.getPrograma().getId());
            } else {
                stmt.setNull(2, java.sql.Types.NUMERIC);
            }
            stmt.setBoolean(3, curso.isActivo());
            stmt.setInt(4, curso.getId());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se encontró un curso con ID: " + curso.getId());
            }

            notifyObservers(curso);
        }
    }
}