// persistence/CursoDAO.java
package persistence;

import model.Curso;
import model.Programa;
import observer.CursoObserver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    private final ConnectionFactory connectionFactory;
    private final List<CursoObserver> observers = new ArrayList<>();

    public CursoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
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

    // Método para notificar eliminaciones
    private void notifyObserversOnDelete(Curso curso) {
        for (CursoObserver observer : observers) {
            observer.onCursoEliminado(curso);
        }
    }

    public boolean guardar(Curso curso) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                connectionFactory.quote("curso"),
                // connectionFactory.quote("id"), <-- No incluimos ID
                connectionFactory.quote("nombre"),
                connectionFactory.quote("programa_id"),
                connectionFactory.quote("activo")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // <-- Agregamos esta opción

            stmt.setString(1, curso.getNombre());
            if (curso.getPrograma() != null) {
                stmt.setDouble(2, curso.getPrograma().getId());
            } else {
                stmt.setNull(2, java.sql.Types.NUMERIC);
            }
            stmt.setBoolean(3, curso.isActivo());
            stmt.executeUpdate();

            // Recuperar el ID generado por la base de datos
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    curso.setId(generatedKeys.getInt(1)); // <-- Asignamos el ID generado
                    System.out.println("DAO: Curso guardado con ID: " + curso.getId());
                    notifyObservers(curso); // <-- Notificar después de guardar
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

    // Método eliminar actualizado para notificar a los observadores con el nuevo método
    public void eliminar(int id) {
        // Primero, cargamos el curso antes de eliminarlo para tener sus datos
        Curso cursoAEliminar = cargarPorId(id);
        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                connectionFactory.quote("curso"),
                connectionFactory.quote("id")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("DAO: Curso con ID " + id + " eliminado exitosamente.");
                // Notificar a los observadores que se eliminó un curso
                if (cursoAEliminar != null) {
                    notifyObserversOnDelete(cursoAEliminar); // <-- Usar el nuevo método de notificación
                }
            } else {
                System.out.println("DAO: No se encontró un curso con ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para cargar un curso por ID
    public Curso cargarPorId(int id) {
        String sql = String.format("SELECT c.%s as curso_id, c.%s as curso_nombre, c.%s as curso_activo, " +
                        "p.%s as prog_id, p.%s as prog_nombre, p.%s as prog_duracion, p.%s as prog_registro " +
                        "FROM %s c LEFT JOIN %s p ON c.%s = p.%s WHERE c.%s = ?",
                connectionFactory.quote("id"), connectionFactory.quote("nombre"), connectionFactory.quote("activo"),
                connectionFactory.quote("id"), connectionFactory.quote("nombre"), connectionFactory.quote("duracion"),
                connectionFactory.quote("registro_calificado"),
                connectionFactory.quote("curso"), connectionFactory.quote("programa"),
                connectionFactory.quote("programa_id"), connectionFactory.quote("id"),
                connectionFactory.quote("id")
        );
        try (Connection conn = connectionFactory.getConnection();
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
                connectionFactory.quote("id"), connectionFactory.quote("nombre"), connectionFactory.quote("activo"),
                connectionFactory.quote("id"), connectionFactory.quote("nombre"), connectionFactory.quote("duracion"),
                connectionFactory.quote("registro_calificado"),
                connectionFactory.quote("curso"), connectionFactory.quote("programa"),
                connectionFactory.quote("programa_id"), connectionFactory.quote("id")
        );
        try (Connection conn = connectionFactory.getConnection();
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
}