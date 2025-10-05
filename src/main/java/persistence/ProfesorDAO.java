// persistence/ProfesorDAO.java
package persistence;

import model.Profesor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

    private final ConnectionFactory connectionFactory;

    public ProfesorDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(Profesor profesor) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                connectionFactory.quote("PROFESOR"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL"),
                connectionFactory.quote("TIPO_CONTRATO")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, profesor.getNombres());
            stmt.setString(2, profesor.getApellidos());
            stmt.setString(3, profesor.getEmail());
            stmt.setString(4, profesor.getTipoContrato());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    profesor.setId(generatedKeys.getDouble(1));
                    System.out.println("DAO: Profesor guardado con ID: " + profesor.getId());
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Nuevo método para verificar si un profesor puede eliminarse (si no tiene cursos asignados)
    public boolean puedeEliminar(double id) {
        // Verificar si tiene cursos asignados
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
                connectionFactory.quote("CURSOPROFESOR"), // <-- Tabla de asignaciones
                connectionFactory.quote("PROFESOR_ID") // <-- Columna que referencia al profesor
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // Puede eliminar si no tiene cursos asignados
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método eliminar actualizado para verificar dependencias antes de eliminar
    public void eliminar(double id) {
        if (!puedeEliminar(id)) {
            System.out.println("No se puede eliminar el profesor con ID " + id + " porque tiene cursos asignados.");
            return; // <-- No eliminar si tiene dependencias
        }

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                connectionFactory.quote("PROFESOR"),
                connectionFactory.quote("ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("DAO: Profesor con ID " + id + " eliminado exitosamente.");
            } else {
                System.out.println("DAO: No se encontró un profesor con ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Profesor> cargarTodos() {
        List<Profesor> profesores = new ArrayList<>();
        String sql = String.format("SELECT %s, %s, %s, %s, %s FROM %s",
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL"),
                connectionFactory.quote("TIPO_CONTRATO"),
                connectionFactory.quote("PROFESOR")
        );
        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                profesores.add(construirProfesor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar los profesores: " + e.getMessage());
        }
        return profesores;
    }

    private Profesor construirProfesor(ResultSet rs) throws SQLException {
        return new Profesor(
                rs.getDouble("ID"),
                rs.getString("NOMBRES"),
                rs.getString("APELLIDOS"),
                rs.getString("EMAIL"),
                rs.getString("TIPO_CONTRATO")
        );
    }
}