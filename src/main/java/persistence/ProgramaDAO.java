// persistence/ProgramaDAO.java
package persistence;

import model.Facultad;
import model.Programa;
import persistence.adapter.DatabaseAdapter; // Importar la interfaz del adaptador

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO {

    private final DatabaseAdapter adapter; // Cambiar ConnectionFactory por DatabaseAdapter

    // El constructor ahora recibe un DatabaseAdapter
    public ProgramaDAO(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void guardar(Programa programa) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                adapter.quote("PROGRAMA"),
                adapter.quote("NOMBRE"),
                adapter.quote("DURACION"),
                adapter.quote("REGISTRO_CALIFICADO"),
                adapter.quote("FACULTAD_ID")
        );

        try (Connection conn = adapter.getConnection();
             // --- CAMBIO CLAVE AQUÍ ---
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) {

            stmt.setString(1, programa.getNombre());
            stmt.setDouble(2, programa.getDuracion());
            stmt.setDate(3, new java.sql.Date(programa.getRegistro().getTime()));
            stmt.setDouble(4, programa.getFacultad().getId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    programa.setId(generatedKeys.getDouble(1));
                    System.out.println("DAO: Programa guardado con ID: " + programa.getId());
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
                adapter.quote("CURSO"),
                adapter.quote("PROGRAMA_ID")
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
            System.out.println("No se puede eliminar el programa con ID " + id + " porque tiene cursos asociados.");
            return;
        }

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                adapter.quote("PROGRAMA"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("DAO: Programa con ID " + id + " eliminado exitosamente.");
            } else {
                System.out.println("DAO: No se encontró un programa con ID " + id + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Programa> cargarTodos() {
        List<Programa> programas = new ArrayList<>();
        String sql = String.format("SELECT p.%s as prog_id, p.%s as prog_nombre, p.%s as prog_duracion, p.%s as prog_registro, f.%s as fac_id, f.%s as fac_nombre FROM %s p JOIN %s f ON p.%s = f.%s",
                adapter.quote("ID"),
                adapter.quote("NOMBRE"),
                adapter.quote("DURACION"),
                adapter.quote("REGISTRO_CALIFICADO"),
                adapter.quote("ID"),
                adapter.quote("NOMBRE"),
                adapter.quote("PROGRAMA"),
                adapter.quote("FACULTAD"),
                adapter.quote("FACULTAD_ID"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                programas.add(construirPrograma(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar los programas: " + e.getMessage());
        }
        return programas;
    }

    private Programa construirPrograma(ResultSet rs) throws SQLException {
        Facultad facultad = new Facultad(rs.getDouble("fac_id"), rs.getString("fac_nombre"));
        return new Programa(
                rs.getDouble("prog_id"),
                rs.getString("prog_nombre"),
                rs.getDouble("prog_duracion"),
                rs.getDate("prog_registro"),
                facultad
        );
    }
}