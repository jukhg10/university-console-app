package persistence;

import model.Curso;
import model.Estudiante;
import model.Inscripcion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {

    public void guardarInformacion(Inscripcion inscripcion) {
        String sql = "INSERT INTO INSCRIPCION (ano, semestre, estudiante_id, curso_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, inscripcion.getAno());
            pstmt.setInt(2, inscripcion.getSemestre());
            pstmt.setDouble(3, inscripcion.getEstudiante().getId());
            pstmt.setInt(4, inscripcion.getCurso().getId());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inscripcion.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Inscripcion> cargarDatos() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = "SELECT i.id, i.ano, i.semestre, " +
                     "e.id AS est_id, e.nombres AS est_nombres, e.apellidos AS est_apellidos, " +
                     "c.id AS cur_id, c.nombre AS cur_nombre " +
                     "FROM INSCRIPCION i " +
                     "JOIN ESTUDIANTE e ON i.estudiante_id = e.id " +
                     "JOIN CURSO c ON i.curso_id = c.id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estudiante est = new Estudiante(rs.getDouble("est_id"), rs.getString("est_nombres"), rs.getString("est_apellidos"), "", 0, true, 0);
                Curso cur = new Curso(rs.getInt("cur_id"), rs.getString("cur_nombre"), true);
                Inscripcion insc = new Inscripcion(est, cur, rs.getInt("ano"), rs.getInt("semestre"));
                insc.setId(rs.getInt("id"));
                inscripciones.add(insc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscripciones;
    }

    public void eliminar(int inscripcionId) {
        String sql = "DELETE FROM INSCRIPCION WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, inscripcionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}