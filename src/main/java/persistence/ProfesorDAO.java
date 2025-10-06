package persistence;

import model.Profesor;
import persistence.adapter.DatabaseAdapter; // Importar la interfaz del adaptador
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

    private final DatabaseAdapter adapter; // Cambiar ConnectionFactory por DatabaseAdapter

    // El constructor ahora recibe un DatabaseAdapter
    public ProfesorDAO(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void guardar(Profesor profesor) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                adapter.quote("PROFESOR"),
                adapter.quote("NOMBRES"),
                adapter.quote("APELLIDOS"),
                adapter.quote("EMAIL"),
                adapter.quote("TIPO_CONTRATO")
        );
        // --- CAMBIO CLAVE AQUÍ ---
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) { // Especificar la columna a devolver

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

    public boolean puedeEliminar(double id) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
                adapter.quote("CURSOPROFESOR"),
                adapter.quote("PROFESOR_ID")
        );
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
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

    public void eliminar(double id) {
        if (!puedeEliminar(id)) {
            System.out.println("No se puede eliminar el profesor con ID " + id + " porque tiene cursos asignados.");
            return;
        }

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                adapter.quote("PROFESOR"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
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

    public Profesor cargarPorId(double id) {
        String sql = String.format("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = ?",
                adapter.quote("ID"),
                adapter.quote("NOMBRES"),
                adapter.quote("APELLIDOS"),
                adapter.quote("EMAIL"),
                adapter.quote("TIPO_CONTRATO"),
                adapter.quote("PROFESOR"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirProfesor(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar el profesor con ID " + id + ": " + e.getMessage());
        }
        return null; // No se encontró el profesor
    }

    public List<Profesor> cargarTodos() {
        List<Profesor> profesores = new ArrayList<>();
        String sql = String.format("SELECT %s, %s, %s, %s, %s FROM %s",
                adapter.quote("ID"),
                adapter.quote("NOMBRES"),
                adapter.quote("APELLIDOS"),
                adapter.quote("EMAIL"),
                adapter.quote("TIPO_CONTRATO"),
                adapter.quote("PROFESOR")
        );
        try (Connection conn = adapter.getConnection();
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