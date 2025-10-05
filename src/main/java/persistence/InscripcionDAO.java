package persistence;

import model.Curso;
import model.Estudiante;
import model.Inscripcion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// persistence/InscripcionDAO.java
public class InscripcionDAO {
    private final ConnectionFactory connectionFactory;

    public InscripcionDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void guardar(Inscripcion inscripcion) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                connectionFactory.quote("INSCRIPCION"),
                // connectionFactory.quote("ID"), <-- No incluyas el ID
                connectionFactory.quote("ANO"),
                connectionFactory.quote("SEMESTRE"),
                connectionFactory.quote("ESTUDIANTE_ID"),
                connectionFactory.quote("CURSO_ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { // <-- Agregamos esta opción

            // No establecemos el ID aquí
            stmt.setInt(1, inscripcion.getAno());
            stmt.setInt(2, inscripcion.getSemestre());
            stmt.setDouble(3, inscripcion.getEstudiante().getId());
            stmt.setInt(4, inscripcion.getCurso().getId());
            stmt.executeUpdate();

            // Recuperar el ID generado por la base de datos
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inscripcion.setId(generatedKeys.getInt(1)); // <-- Asignamos el ID generado como int
                    System.out.println("DAO: Inscripción guardada con ID: " + inscripcion.getId());
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar la inscripción: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Inscripcion> cargarDatos() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = String.format("SELECT i.%s as insc_id, i.%s as insc_ano, i.%s as insc_sem, " +
                        "e.%s as est_id, e.%s as est_nombres, e.%s as est_apellidos, e.%s as est_email, e.%s as est_codigo, " +
                        "c.%s as cur_id, c.%s as cur_nombre, c.%s as cur_activo " +
                        "FROM %s i " +
                        "JOIN %s e ON i.%s = e.%s " +
                        "JOIN %s c ON i.%s = c.%s",
                connectionFactory.quote("ID"), connectionFactory.quote("ANO"), connectionFactory.quote("SEMESTRE"),
                connectionFactory.quote("ID"), connectionFactory.quote("NOMBRES"), connectionFactory.quote("APELLIDOS"), connectionFactory.quote("EMAIL"), connectionFactory.quote("CODIGO"),
                connectionFactory.quote("ID"), connectionFactory.quote("NOMBRE"), connectionFactory.quote("ACTIVO"),
                connectionFactory.quote("INSCRIPCION"),
                connectionFactory.quote("ESTUDIANTE"), connectionFactory.quote("ESTUDIANTE_ID"), connectionFactory.quote("ID"),
                connectionFactory.quote("CURSO"), connectionFactory.quote("CURSO_ID"), connectionFactory.quote("ID")
        );

        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                inscripciones.add(construirInscripcion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscripciones;
    }

    public void eliminar(int id) {
        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                connectionFactory.quote("INSCRIPCION"),
                connectionFactory.quote("ID")
        );
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Inscripcion construirInscripcion(ResultSet rs) throws SQLException {
        Estudiante estudiante = new Estudiante(
                rs.getDouble("est_id"),
                rs.getString("est_nombres"),
                rs.getString("est_apellidos"),
                rs.getString("est_email"),
                rs.getDouble("est_codigo"),
                true, 0.0
        );

        Curso curso = new Curso(
                rs.getInt("cur_id"),
                rs.getString("cur_nombre"),
                rs.getBoolean("cur_activo")
        );

        Inscripcion inscripcion = new Inscripcion(
                estudiante,
                curso,
                rs.getInt("insc_ano"),
                rs.getInt("insc_sem")
        );
        inscripcion.setId(rs.getInt("insc_id")); // <-- Recuperamos el ID como int

        return inscripcion;
    }
}