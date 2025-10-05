package persistence;

import model.Curso;
import model.Programa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {
    
    private final ConnectionFactory connectionFactory;

    public CursoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(Curso curso) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                connectionFactory.quote("curso"),
                connectionFactory.quote("id"),
                connectionFactory.quote("nombre"),
                connectionFactory.quote("programa_id"),
                connectionFactory.quote("activo")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, curso.getId());
            stmt.setString(2, curso.getNombre());
            if (curso.getPrograma() != null) {
                stmt.setDouble(3, curso.getPrograma().getId());
            } else {
                stmt.setNull(3, Types.NUMERIC);
            }
            stmt.setBoolean(4, curso.isActivo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     public List<Curso> cargarTodos() {
    List<Curso> cursos = new ArrayList<>();
    // FIX: Intentemos con "registro_calificado" de nuevo.
    String sql = String.format("SELECT c.%s as curso_id, c.%s as curso_nombre, c.%s as curso_activo, " +
                 "p.%s as prog_id, p.%s as prog_nombre, p.%s as prog_duracion, p.%s as prog_registro " +
                 "FROM %s c LEFT JOIN %s p ON c.%s = p.%s",
                 connectionFactory.quote("id"), connectionFactory.quote("nombre"), connectionFactory.quote("activo"),
                 connectionFactory.quote("id"), connectionFactory.quote("nombre"), connectionFactory.quote("duracion"),
                 connectionFactory.quote("registro_calificado"), // <- CAMBIO AQUÍ
                 connectionFactory.quote("curso"), connectionFactory.quote("programa"),
                 connectionFactory.quote("programa_id"), connectionFactory.quote("id")
    );

    try (Connection conn = connectionFactory.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            cursos.add(construirCurso(rs));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cursos;
}

    private Curso construirCurso(ResultSet rs) throws SQLException {
        // DEFINITIVE FIX: Use ONLY the lowercase aliases to retrieve data.
        Curso curso = new Curso(
            rs.getInt("curso_id"),
            rs.getString("curso_nombre"),
            rs.getBoolean("curso_activo")
        );

        double programaId = rs.getDouble("prog_id");
        if (!rs.wasNull()) {
            Programa programa = new Programa(
                programaId,
                rs.getString("prog_nombre"),
                rs.getDouble("prog_duracion"),
                rs.getDate("prog_registro"),
                null 
            );
            curso.setPrograma(programa);
        }
        return curso;
    }
}