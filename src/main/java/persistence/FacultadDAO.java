package persistence;

import model.Facultad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FacultadDAO {

    private final ConnectionFactory connectionFactory;

    public FacultadDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(Facultad facultad) {
        String sql = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
                connectionFactory.quote("FACULTAD"),
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRE")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, facultad.getId());
            stmt.setString(2, facultad.getNombre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Facultad> cargarTodas() {
        List<Facultad> facultades = new ArrayList<>();
        // --- LA CORRECCIÓN ESTÁ AQUÍ ---
        // Se eliminó la unión (JOIN) con la tabla PERSONA y la búsqueda de la columna DECANO_ID
        // porque no existen en el esquema de la base de datos que creamos.
        String sql = String.format("SELECT %s, %s FROM %s",
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRE"),
                connectionFactory.quote("FACULTAD")
        );
        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                facultades.add(construirFacultad(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar las facultades: " + e.getMessage());
        }
        return facultades;
    }

    private Facultad construirFacultad(ResultSet rs) throws SQLException {
        // --- CORRECCIÓN AQUÍ ---
        // Se usa el constructor que solo necesita ID y Nombre.
        return new Facultad(
                rs.getDouble("ID"),
                rs.getString("NOMBRE")
        );
    }
}