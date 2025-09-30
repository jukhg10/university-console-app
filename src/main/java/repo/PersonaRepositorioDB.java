package repo;

import model.Persona;
import persistence.DatabaseManager;
import persistence.Repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PersonaRepositorioDB implements Repositorio<Persona> {
    @Override
    public void guardar(Persona persona) {
        DatabaseManager.guardarPersona(persona);
    }

    @Override
    public void eliminar(Persona persona) {
        DatabaseManager.eliminarPersona(persona.getId());
    }

    @Override
    public List<Persona> listar() {
        return DatabaseManager.cargarPersonas();
    }

    @Override
    public Persona buscarPorId(Object id) {
        String sql = "SELECT * FROM PERSONA WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Persona(
                            rs.getDouble("id"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}