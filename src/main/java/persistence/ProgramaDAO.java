package persistence;

import model.Facultad;
import model.Programa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO {

    private final ConnectionFactory connectionFactory;

    public ProgramaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    // ... (El método guardar está bien, no necesita cambios)
    public void guardar(Programa programa) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
                connectionFactory.quote("PROGRAMA"),
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRE"),
                connectionFactory.quote("DURACION"),
                connectionFactory.quote("REGISTRO_CALIFICADO"),
                connectionFactory.quote("FACULTAD_ID")
        );

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, programa.getId());
            stmt.setString(2, programa.getNombre());
            stmt.setDouble(3, programa.getDuracion());
            stmt.setDate(4, new java.sql.Date(programa.getRegistro().getTime()));
            stmt.setDouble(5, programa.getFacultad().getId());
            stmt.executeUpdate();
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
        // --- LA CORRECCIÓN ESTÁ AQUÍ ---
        // El constructor de Facultad solo acepta ID y Nombre.
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