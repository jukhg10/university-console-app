package persistence;

import model.Curso;
import model.CursoProfesor;
import model.Profesor;
import persistence.adapter.DatabaseAdapter; // Importar la interfaz del adaptador

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoProfesorDAO {

    private final DatabaseAdapter adapter; // Cambiar ConnectionFactory por DatabaseAdapter

    // El constructor ahora recibe un DatabaseAdapter
    public CursoProfesorDAO(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void guardar(CursoProfesor asignacion) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                adapter.quote("CURSOPROFESOR"),
                adapter.quote("PROFESOR_ID"),
                adapter.quote("CURSO_ID"),
                adapter.quote("ANO"),
                adapter.quote("SEMESTRE")
        );
        try (Connection conn = adapter.getConnection(); // Usar el adaptador
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
                adapter.quote("PROFESOR_ID"),
                adapter.quote("CURSO_ID"),
                adapter.quote("ANO"),
                adapter.quote("SEMESTRE"),
                adapter.quote("ID"),
                adapter.quote("NOMBRES"),
                adapter.quote("APELLIDOS"),
                adapter.quote("EMAIL"),
                adapter.quote("TIPO_CONTRATO"),
                adapter.quote("ID"),
                adapter.quote("NOMBRE"),
                adapter.quote("ACTIVO"),
                adapter.quote("PROGRAMA_ID"),
                adapter.quote("CURSOPROFESOR"),
                adapter.quote("PROFESOR"),
                adapter.quote("PROFESOR_ID"),
                adapter.quote("ID"),
                adapter.quote("CURSO"),
                adapter.quote("CURSO_ID"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection(); // Usar el adaptador
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
                adapter.quote("CURSOPROFESOR"),
                adapter.quote("PROFESOR_ID"),
                adapter.quote("CURSO_ID"),
                adapter.quote("ANO"),
                adapter.quote("SEMESTRE")
        );
        try (Connection conn = adapter.getConnection(); // Usar el adaptador
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

        return new CursoProfesor(
                profesor,
                curso,
                rs.getInt("ANO"),
                rs.getInt("SEMESTRE")
        );
    }
}