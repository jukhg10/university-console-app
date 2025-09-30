package persistence;

import model.Curso;
import model.CursoProfesor;
import model.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoProfesorDAO implements CursoProfesorDAOInterface{

    @Override
    public void guardarAsignacion(CursoProfesor asignacion) {
        String sql = "INSERT INTO ASIGNACION (profesor_id, curso_id, ano, semestre) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, asignacion.getProfesor().getId());
            pstmt.setInt(2, asignacion.getCurso().getId());
            pstmt.setInt(3, asignacion.getAno());
            pstmt.setInt(4, asignacion.getSemestre());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CursoProfesor> cargarAsignaciones() {
        List<CursoProfesor> asignaciones = new ArrayList<>();
        String sql = """
            SELECT a.ano, a.semestre,
                   p.id AS prof_id, p.nombres AS prof_nombres, p.apellidos AS prof_apellidos, p.email AS prof_email, prof.tipocontrato AS prof_tipo,
                   c.id AS cur_id, c.nombre AS cur_nombre
            FROM ASIGNACION a
            JOIN PROFESOR prof ON a.profesor_id = prof.id
            JOIN PERSONA p ON prof.id = p.id
            JOIN CURSO c ON a.curso_id = c.id
        """;
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Profesor prof = new Profesor(
                        rs.getDouble("prof_id"),
                        rs.getString("prof_nombres"),
                        rs.getString("prof_apellidos"),
                        rs.getString("prof_email"),
                        rs.getString("prof_tipo")
                );
                Curso cur = new Curso(rs.getInt("cur_id"), rs.getString("cur_nombre"), true);
                CursoProfesor cp = new CursoProfesor(prof, cur, rs.getInt("ano"), rs.getInt("semestre"));
                asignaciones.add(cp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return asignaciones;
    }

    @Override
    public void eliminarAsignacion(CursoProfesor asignacion) {
        String sql = "DELETE FROM ASIGNACION WHERE profesor_id = ? AND curso_id = ? AND ano = ? AND semestre = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, asignacion.getProfesor().getId());
            pstmt.setInt(2, asignacion.getCurso().getId());
            pstmt.setInt(3, asignacion.getAno());
            pstmt.setInt(4, asignacion.getSemestre());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
