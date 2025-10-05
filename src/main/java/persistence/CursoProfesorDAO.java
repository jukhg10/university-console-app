package persistence;

import model.Curso;
import model.CursoProfesor;
import model.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoProfesorDAO {

    private final ConnectionFactory connectionFactory;

    public CursoProfesorDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(CursoProfesor asignacion) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                connectionFactory.quote("CURSOPROFESOR"),
                connectionFactory.quote("PROFESOR_ID"),
                connectionFactory.quote("CURSO_ID"),
                connectionFactory.quote("ANO"),
                connectionFactory.quote("SEMESTRE")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, asignacion.getProfesor().getId());
            stmt.setInt(2, asignacion.getCurso().getId());
            stmt.setInt(3, asignacion.getAno());
            stmt.setInt(4, asignacion.getSemestre());

            stmt.executeUpdate();
            System.out.println("DAO: Asignación guardada.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CursoProfesor> cargarTodos() {
        List<CursoProfesor> asignaciones = new ArrayList<>();
        String sql = String.format(
                "SELECT cp.%s, cp.%s, cp.%s, cp.%s, " +
                        "p.%s, p.%s, p.%s, p.%s, p.%s, " +
                        "c.%s, c.%s, c.%s, c.%s " +
                        "FROM %s cp " +
                        "JOIN %s p ON cp.%s = p.%s " +
                        "JOIN %s c ON cp.%s = c.%s",
                connectionFactory.quote("PROFESOR_ID"),
                connectionFactory.quote("CURSO_ID"),
                connectionFactory.quote("ANO"),
                connectionFactory.quote("SEMESTRE"),
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRES"),
                connectionFactory.quote("APELLIDOS"),
                connectionFactory.quote("EMAIL"),
                connectionFactory.quote("TIPO_CONTRATO"),
                connectionFactory.quote("ID"),
                connectionFactory.quote("NOMBRE"),
                connectionFactory.quote("ACTIVO"),
                connectionFactory.quote("PROGRAMA_ID"),
                connectionFactory.quote("CURSOPROFESOR"),
                connectionFactory.quote("PROFESOR"),
                connectionFactory.quote("PROFESOR_ID"),
                connectionFactory.quote("ID"),
                connectionFactory.quote("CURSO"),
                connectionFactory.quote("CURSO_ID"),
                connectionFactory.quote("ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                asignaciones.add(construirAsignacion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asignaciones;
    }

    public void eliminar(double profesorId, int cursoId, int ano, int semestre) {
        String sql = String.format("DELETE FROM %s WHERE %s = ? AND %s = ? AND %s = ? AND %s = ?",
                connectionFactory.quote("CURSOPROFESOR"),
                connectionFactory.quote("PROFESOR_ID"),
                connectionFactory.quote("CURSO_ID"),
                connectionFactory.quote("ANO"),
                connectionFactory.quote("SEMESTRE")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, profesorId);
            stmt.setInt(2, cursoId);
            stmt.setInt(3, ano);
            stmt.setInt(4, semestre);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private CursoProfesor construirAsignacion(ResultSet rs) throws SQLException {
        Profesor profesor = new Profesor(
                rs.getDouble("ID"),
                rs.getString("NOMBRES"),
                rs.getString("APELLIDOS"),
                rs.getString("EMAIL"),
                rs.getString("TIPO_CONTRATO")
        );

        Curso curso = new Curso(
                rs.getInt("ID"),
                rs.getString("NOMBRE"),
                rs.getBoolean("ACTIVO")
        );

        CursoProfesor asignacion = new CursoProfesor(
                profesor,
                curso,
                rs.getInt("ANO"),
                rs.getInt("SEMESTRE")
        );

        return asignacion;
    }
}
