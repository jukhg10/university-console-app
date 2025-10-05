package persistence;

import model.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    private final ConnectionFactory connectionFactory;

    public EstudianteDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(Estudiante estudiante) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                connectionFactory.quote("ESTUDIANTE"),
               // connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL"),
                connectionFactory.quote("CODIGO")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, estudiante.getNombres());
            stmt.setString(2, estudiante.getApellidos());
            stmt.setString(3, estudiante.getEmail());
            stmt.setDouble(4, estudiante.getCodigo());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    estudiante.setId(generatedKeys.getDouble(1)); // <-- Asignamos el ID generado como int
                    System.out.println("DAO: Inscripción guardada con ID: " + estudiante.getId());
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
        // --- LA CORRECCIÓN ESTÁ AQUÍ ---
        // Se cambió "CORREO_ELECTRONICO" por "EMAIL".
        String sql = String.format("SELECT %s, %s, %s, %s, %s FROM %s",
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL"),
                connectionFactory.quote("CODIGO"),
                connectionFactory.quote("ESTUDIANTE")
        );
        try (Connection conn = connectionFactory.getConnection();
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
                connectionFactory.quote("ESTUDIANTE"),
                connectionFactory.quote("ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Estudiante construirEstudiante(ResultSet rs) throws SQLException {
        // También se corrige aquí para que coincida con el nombre de la columna en la BD
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