package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:./data/universityDB";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Se crean tablas dummy para Estudiante y Curso para satisfacer las claves foráneas
            stmt.execute("CREATE TABLE IF NOT EXISTS ESTUDIANTE (id DOUBLE PRIMARY KEY, nombres VARCHAR(255), apellidos VARCHAR(255))");
            stmt.execute("CREATE TABLE IF NOT EXISTS CURSO (id INT PRIMARY KEY, nombre VARCHAR(255))");

            String sqlInscripcion = "CREATE TABLE IF NOT EXISTS INSCRIPCION (" +
                                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                    "ano INT, " +
                                    "semestre INT, " +
                                    "estudiante_id DOUBLE, " +
                                    "curso_id INT, " +
                                    "FOREIGN KEY (estudiante_id) REFERENCES ESTUDIANTE(id), " +
                                    "FOREIGN KEY (curso_id) REFERENCES CURSO(id))";
            stmt.execute(sqlInscripcion);
            
            // Insertar datos de ejemplo si no existen
            stmt.execute("MERGE INTO ESTUDIANTE KEY(id) VALUES (101, 'Ana', 'Gomez')");
            stmt.execute("MERGE INTO ESTUDIANTE KEY(id) VALUES (102, 'Luis', 'Mora')");
            stmt.execute("MERGE INTO CURSO KEY(id) VALUES (901, 'Calculo I')");
            stmt.execute("MERGE INTO CURSO KEY(id) VALUES (902, 'Bases de Datos')");
            
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}