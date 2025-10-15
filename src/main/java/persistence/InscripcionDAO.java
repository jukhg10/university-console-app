package persistence;

import model.Curso;
import model.Estudiante;
import model.Inscripcion;
import persistence.adapter.DatabaseAdapter; // Importar la interfaz del adaptador
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {
    private final DatabaseAdapter adapter; // Cambiar ConnectionFactory por DatabaseAdapter

    // El constructor ahora recibe un DatabaseAdapter
    public InscripcionDAO(DatabaseAdapter adapter) {
        this.adapter = adapter;
    }

    public void guardar(Inscripcion inscripcion) {
        String sql = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                adapter.quote("INSCRIPCION"),
                adapter.quote("ANO"),
                adapter.quote("SEMESTRE"),
                adapter.quote("ESTUDIANTE_ID"),
                adapter.quote("CURSO_ID")
        );
        // --- CAMBIO CLAVE AQUÍ ---
        try (Connection conn = adapter.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID"})) { // Especificar la columna a devolver

            stmt.setInt(1, inscripcion.getAno());
            stmt.setInt(2, inscripcion.getSemestre());
            stmt.setDouble(3, inscripcion.getEstudiante().getId());
            stmt.setInt(4, inscripcion.getCurso().getId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inscripcion.setId(generatedKeys.getInt(1));
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
                adapter.quote("ID"), adapter.quote("ANO"), adapter.quote("SEMESTRE"),
                adapter.quote("ID"), adapter.quote("NOMBRES"), adapter.quote("APELLIDOS"), adapter.quote("EMAIL"), adapter.quote("CODIGO"),
                adapter.quote("ID"), adapter.quote("NOMBRE"), adapter.quote("ACTIVO"),
                adapter.quote("INSCRIPCION"),
                adapter.quote("ESTUDIANTE"), adapter.quote("ESTUDIANTE_ID"), adapter.quote("ID"),
                adapter.quote("CURSO"), adapter.quote("CURSO_ID"), adapter.quote("ID")
        );

        try (Connection conn = adapter.getConnection();
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
                adapter.quote("INSCRIPCION"),
                adapter.quote("ID")
        );
        try (Connection conn = adapter.getConnection();
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
        inscripcion.setId(rs.getInt("insc_id"));

        return inscripcion;
    }
}