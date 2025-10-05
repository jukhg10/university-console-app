package persistence;

import model.Estudiante;
import persistence.adapter.DatabaseAdapter; // Importar la interfaz del adaptador
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    private final DatabaseAdapter adapter; // Cambiar ConnectionFactory por DatabaseAdapter

    // El constructor ahora recibe un DatabaseAdapter
    public EstudianteDAO(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void guardar(Estudiante estudiante) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                adapter.quote("ESTUDIANTE"),
                adapter.quote("NOMBRES"),
                adapter.quote("APELLIDOS"),
                adapter.quote("EMAIL"),
                adapter.quote("CODIGO")
        );
        // --- CAMBIO CLAVE AQUÍ ---
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) { // Especificar la columna a devolver

            stmt.setString(1, estudiante.getNombres());
            stmt.setString(2, estudiante.getApellidos());
            stmt.setString(3, estudiante.getEmail());
            stmt.setDouble(4, estudiante.getCodigo());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    estudiante.setId(generatedKeys.getDouble(1));
                    System.out.println("DAO: Estudiante guardado con ID: " + estudiante.getId());
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Estudiante> cargarTodos() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = String.format("SELECT %s, %s, %s, %s, %s FROM %s",
                adapter.quote("ID"),
                adapter.quote("NOMBRES"),
                adapter.quote("APELLIDOS"),
                adapter.quote("EMAIL"),
                adapter.quote("CODIGO"),
                adapter.quote("ESTUDIANTE")
        );
        try (Connection conn = adapter.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                estudiantes.add(construirEstudiante(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estudiantes;
    }

    public void eliminar(double id) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                adapter.quote("ESTUDIANTE"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Estudiante construirEstudiante(ResultSet rs) throws SQLException {
        return new Estudiante(
            rs.getDouble("ID"),
            rs.getString("NOMBRES"),
            rs.getString("APELLIDOS"),
            rs.getString("EMAIL"),
            rs.getDouble("CODIGO"),
            true,
            0.0
        );
    }
}