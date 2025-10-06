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

    public void removeObserver(CursoObserver observer) {
        observers.remove(observer);
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

    public boolean guardar(Curso curso) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                adapter.quote("curso"),
                adapter.quote("nombre"),
                adapter.quote("programa_id"),
                adapter.quote("activo")
        );
        // --- CAMBIO CLAVE AQUÍ ---
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) { // Especificar la columna a devolver

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
                    notifyObservers(curso);
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

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                adapter.quote("curso"),
                adapter.quote("id")
        );
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("DAO: Curso con ID " + id + " eliminado exitosamente.");
                Curso cursoAEliminar = cargarPorId(id);
                if (cursoAEliminar != null) {
                    notifyObserversOnDelete(cursoAEliminar);
                }
            } else {
                System.out.println("DAO: No se encontró un curso con ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Curso cargarPorId(int id) {
        String sql = String.format("SELECT c.%s as curso_id, c.%s as curso_nombre, c.%s as curso_activo, " +
                        "p.%s as prog_id, p.%s as prog_nombre, p.%s as prog_duracion, p.%s as prog_registro " +
                        "FROM %s c LEFT JOIN %s p ON c.%s = p.%s WHERE c.%s = ?",
                adapter.quote("id"), adapter.quote("nombre"), adapter.quote("activo"),
                adapter.quote("id"), adapter.quote("nombre"), adapter.quote("duracion"),
                adapter.quote("registro_calificado"),
                adapter.quote("curso"), adapter.quote("programa"),
                adapter.quote("programa_id"), adapter.quote("id"),
                adapter.quote("id")
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
        String sql = String.format("SELECT c.%s as curso_id, c.%s as curso_nombre, c.%s as curso_activo, " +
                        "p.%s as prog_id, p.%s as prog_nombre, p.%s as prog_duracion, p.%s as prog_registro " +
                        "FROM %s c LEFT JOIN %s p ON c.%s = p.%s",
                adapter.quote("id"), adapter.quote("nombre"), adapter.quote("activo"),
                adapter.quote("id"), adapter.quote("nombre"), adapter.quote("duracion"),
                adapter.quote("registro_calificado"),
                adapter.quote("curso"), adapter.quote("programa"),
                adapter.quote("programa_id"), adapter.quote("id")
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
                rs.getInt("curso_id"),
                rs.getString("curso_nombre"),
                rs.getBoolean("curso_activo")
        );
        double programaId = rs.getDouble("prog_id");
        if (!rs.wasNull()) {
            Programa programa = new Programa(
                    programaId,
                    rs.getString("prog_nombre"),
                    rs.getDouble("prog_duracion"),
                    rs.getDate("prog_registro"),
                    null
            );
            curso.setPrograma(programa);
        }
        return curso;
    }

    public void actualizar(Curso curso) throws SQLException {
        String sql = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
                adapter.quote("curso"),
                adapter.quote("nombre"),
                adapter.quote("programa_id"),
                adapter.quote("activo"),
                adapter.quote("id")
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