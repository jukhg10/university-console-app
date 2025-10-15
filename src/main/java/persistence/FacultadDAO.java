// persistence/FacultadDAO.java
package persistence;

import model.Facultad;
import model.Persona;
import persistence.adapter.DatabaseAdapter; // Importar la interfaz del adaptador

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacultadDAO {

    private final DatabaseAdapter adapter; // Cambiar ConnectionFactory por DatabaseAdapter

    // El constructor ahora recibe un DatabaseAdapter
    public FacultadDAO(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void guardar(Facultad facultad) {
        String sql = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
                adapter.quote("FACULTAD"),
                adapter.quote("NOMBRE"),
                adapter.quote("DECANO_ID")
        );
        // --- CAMBIO CLAVE AQUÍ ---
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) { // Especificar la columna a devolver

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

    public boolean puedeEliminar(double id) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?",
                adapter.quote("PROGRAMA"),
                adapter.quote("FACULTAD_ID")
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
            System.out.println("No se puede eliminar la facultad con ID " + id + " porque tiene programas asociados.");
            return;
        }

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                adapter.quote("FACULTAD"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
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
                adapter.quote("ID"),
                adapter.quote("NOMBRE"),
                adapter.quote("ID"),
                adapter.quote("NOMBRES"),
                adapter.quote("APELLIDOS"),
                adapter.quote("EMAIL"),
                adapter.quote("FACULTAD"),
                adapter.quote("PERSONA"),
                adapter.quote("DECANO_ID"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
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

        // Corrección importante aquí: Oracle devuelve los nombres de columna en mayúsculas
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