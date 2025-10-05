package persistence;

import model.Persona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    private final ConnectionFactory connectionFactory;

    public PersonaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(Persona persona) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                connectionFactory.quote("PERSONA"), // <-- Asegúrate de usar mayúsculas si es necesario
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, persona.getNombres());
            stmt.setString(2, persona.getApellidos());
            stmt.setString(3, persona.getEmail());

            stmt.executeUpdate(); // <-- Ejecutamos antes de obtener el ID generado

            // Recuperar el ID generado por la base de datos
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    persona.setId(generatedKeys.getDouble(1)); // <-- Asignamos el ID generado
                    System.out.println("DAO: Persona guardada con ID: " + persona.getId());
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar persona: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Persona> cargarTodas() {
        List<Persona> personas = new ArrayList<>();
        // FIX: Dynamically build the SQL query
        String sql = String.format("SELECT * FROM %s", connectionFactory.quote("persona"));
        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                personas.add(construirPersona(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personas;
    }

    public void eliminar(double id) {
        // FIX: Dynamically build the SQL query
        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                connectionFactory.quote("persona"),
                connectionFactory.quote("id")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Persona cargarPorId(double id) {
        // FIX: Dynamically build the SQL query
        String sql = String.format("SELECT * FROM %s WHERE %s = ?",
                connectionFactory.quote("persona"),
                connectionFactory.quote("id")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return construirPersona(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Persona construirPersona(ResultSet rs) throws SQLException {
   
    double id = rs.getDouble("id");
    String nombres = rs.getString("nombres");
    String apellidos = rs.getString("apellidos");
    String email = rs.getString("email");
    
    return new Persona(id, nombres, apellidos, email);
}
}