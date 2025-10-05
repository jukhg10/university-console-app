// persistence/ProgramaDAO.java
package persistence;

import model.Facultad;
import model.Programa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO {

    private final ConnectionFactory connectionFactory;

    public ProgramaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(Programa programa) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                connectionFactory.quote("PROGRAMA"),
                connectionFactory.quote("NOMBRE"),
                connectionFactory.quote("DURACION"),
                connectionFactory.quote("REGISTRO_CALIFICADO"),
                connectionFactory.quote("FACULTAD_ID")
        );

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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

    // Nuevo método para eliminar un programa por ID
    public void eliminar(double id) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                connectionFactory.quote("PROGRAMA"),
                connectionFactory.quote("ID")
        );
        try (Connection conn = connectionFactory.getConnection();
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
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRE"),
                connectionFactory.quote("DURACION"),
                connectionFactory.quote("REGISTRO_CALIFICADO"),
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRE"),
                connectionFactory.quote("PROGRAMA"),
                connectionFactory.quote("FACULTAD"),
                connectionFactory.quote("FACULTAD_ID"),
                connectionFactory.quote("ID")
        );
        try (Connection conn = connectionFactory.getConnection();
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