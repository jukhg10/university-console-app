// persistence/PersonaDAO.java
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
                connectionFactory.quote("PERSONA"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, persona.getNombres());
            stmt.setString(2, persona.getApellidos());
            stmt.setString(3, persona.getEmail());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    persona.setId(generatedKeys.getDouble(1));
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

    // Nuevo método para verificar si una persona puede eliminarse (si no tiene dependencias)
    public boolean puedeEliminar(double id) {
        // Verificar si es decano de alguna facultad
        String sqlFacultad = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
                connectionFactory.quote("FACULTAD"),
                connectionFactory.quote("DECANO_ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlFacultad)) {
            stmt.setDouble(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        return false; // <-- No puede eliminar si es decano
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // No hay otras dependencias para persona
        return true; // <-- Puede eliminar si no es decano
    }

    // Método eliminar actualizado para verificar dependencias antes de eliminar
    public void eliminar(double id) {
        if (!puedeEliminar(id)) {
            System.out.println("No se puede eliminar la persona con ID " + id + " porque es decano de una facultad.");
            return; // <-- No eliminar si tiene dependencias
        }

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                connectionFactory.quote("PERSONA"),
                connectionFactory.quote("ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("DAO: Persona con ID " + id + " eliminada exitosamente.");
            } else {
                System.out.println("DAO: No se encontró una persona con ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Persona> cargarTodas() {
        List<Persona> personas = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", connectionFactory.quote("PERSONA"));
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

    public Persona cargarPorId(double id) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?",
                connectionFactory.quote("PERSONA"),
                connectionFactory.quote("ID")
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
        double id = rs.getDouble("ID");
        String nombres = rs.getString("NOMBRES");
        String apellidos = rs.getString("APELLIDOS");
        String email = rs.getString("EMAIL");

        return new Persona(id, nombres, apellidos, email);
    }
}