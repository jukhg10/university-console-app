// persistence/FacultadDAO.java
package persistence;

import model.Facultad;
import model.Persona;

import java.sql.*;
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
                connectionFactory.quote("NOMBRE"),
                connectionFactory.quote("DECANO_ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, facultad.getNombre());
            if (facultad.getDecano() != null) {
                stmt.setDouble(2, facultad.getDecano().getId());
            } else {
                stmt.setNull(2, java.sql.Types.NUMERIC);
            }
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    facultad.setId(generatedKeys.getDouble(1));
                    System.out.println("DAO: Facultad guardada con ID: " + facultad.getId());
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Nuevo método para eliminar una facultad por ID
    public boolean puedeEliminar(double id) {
        // Verificar si tiene programas asociados
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
                connectionFactory.quote("PROGRAMA"),
                connectionFactory.quote("FACULTAD_ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // Puede eliminar si no tiene programas
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void eliminar(double id) {
        if (!puedeEliminar(id)) {
            System.out.println("No se puede eliminar la facultad con ID " + id + " porque tiene programas asociados.");
            return;
        }

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                connectionFactory.quote("FACULTAD"),
                connectionFactory.quote("ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("DAO: Facultad con ID " + id + " eliminada exitosamente.");
            } else {
                System.out.println("DAO: No se encontró una facultad con ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Facultad> cargarTodas() {
        List<Facultad> facultades = new ArrayList<>();
        String sql = String.format("SELECT f.%s, f.%s, p.%s, p.%s, p.%s, p.%s " +
                        "FROM %s f LEFT JOIN %s p ON f.%s = p.%s",
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRE"),
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL"),
                connectionFactory.quote("FACULTAD"),
                connectionFactory.quote("PERSONA"),
                connectionFactory.quote("DECANO_ID"),
                connectionFactory.quote("ID")
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
        Facultad facultad = new Facultad(
                rs.getDouble("ID"),
                rs.getString("NOMBRE")
        );

        double decanoId = rs.getDouble("ID");
        if (!rs.wasNull()) {
            Persona decano = new Persona(
                    decanoId,
                    rs.getString("NOMBRES"),
                    rs.getString("APELLIDOS"),
                    rs.getString("EMAIL")
            );
            facultad.setDecano(decano);
        }

        return facultad;
    }
}