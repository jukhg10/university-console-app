package persistence;

import model.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {

    private final ConnectionFactory connectionFactory;

    public ProfesorDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(Profesor profesor) {
        // --- CORRECCIÓN AQUÍ ---
        // Asegurarse de que los nombres de tabla y columna coincidan con el schema
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                connectionFactory.quote("PROFESOR"),
             //   connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL"),
                connectionFactory.quote("TIPO_CONTRATO")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, profesor.getNombres());
            stmt.setString(2, profesor.getApellidos());
            stmt.setString(3, profesor.getEmail());
            stmt.setString(4, profesor.getTipoContrato());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    profesor.setId(generatedKeys.getDouble(1)); // <-- Asignamos el ID generado como int
                    System.out.println("DAO: Inscripción guardada con ID: " + profesor.getId());
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Profesor> cargarTodos() {
        List<Profesor> profesores = new ArrayList<>();
        // --- LA CORRECCIÓN PRINCIPAL ESTÁ AQUÍ ---
        // Se cambió "CONTRATO" por "TIPO_CONTRATO"
        String sql = String.format("SELECT %s, %s, %s, %s, %s FROM %s",
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL"),
                connectionFactory.quote("TIPO_CONTRATO"),
                connectionFactory.quote("PROFESOR")
        );
        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                profesores.add(construirProfesor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar los profesores: " + e.getMessage());
        }
        return profesores;
    }
    
    public void eliminar(double id) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                connectionFactory.quote("PROFESOR"),
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

    private Profesor construirProfesor(ResultSet rs) throws SQLException {
        // --- CORRECCIÓN AQUÍ ---
        // También se actualiza el nombre de la columna al leer el resultado
        return new Profesor(
            rs.getDouble("ID"),
            rs.getString("NOMBRES"),
            rs.getString("APELLIDOS"),
            rs.getString("EMAIL"),
            rs.getString("TIPO_CONTRATO")
        );
    }
}