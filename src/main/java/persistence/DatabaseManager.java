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

            // --- ESTRUCTURA DE TABLAS (Sin cambios) ---
            stmt.execute("CREATE TABLE IF NOT EXISTS PERSONA (" +
                    "id DOUBLE PRIMARY KEY, " +
                    "nombres VARCHAR(255), " +
                    "apellidos VARCHAR(255), "+
                    "email VARCHAR(255))"
            );
            stmt.execute("CREATE TABLE IF NOT EXISTS PROFESOR (" +
                    "id DOUBLE PRIMARY KEY, " +
                    "tipocontrato VARCHAR(255), " +
                    "FOREIGN KEY (id) REFERENCES PERSONA(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS FACULTAD (" +
                    "id DOUBLE PRIMARY KEY, " +
                    "nombre VARCHAR(255), " +
                    "decano_id DOUBLE NOT NULL, " +
                    "FOREIGN KEY (decano_id) REFERENCES PERSONA(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS PROGRAMA (" +
                    "id DOUBLE PRIMARY KEY, " +
                    "nombre VARCHAR(255), " +
                    "duracion DOUBLE, " +
                    "registro DATE, "+
                    "facultad_id DOUBLE, " +
                    "FOREIGN KEY (facultad_id) REFERENCES FACULTAD(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS CURSO (" +
                    "id INT PRIMARY KEY, " +
                    "nombre VARCHAR(255), " +
                    "programa_id DOUBLE, " +
                    "FOREIGN KEY (programa_id) REFERENCES PROGRAMA(id), "+
                    "activo BOOLEAN "+
                    ")");
            stmt.execute("CREATE TABLE IF NOT EXISTS ESTUDIANTE (" +
                    "id DOUBLE PRIMARY KEY, " +
                    "codigo DOUBLE, "+
                    "programa_id DOUBLE, "+
                    "activo BOOLEAN, " +
                    "promedio DOUBLE, "+
                    "FOREIGN KEY (id) REFERENCES PERSONA(id), "+
                    "FOREIGN KEY (programa_id) REFERENCES PROGRAMA(id))");
            stmt.execute( "CREATE TABLE IF NOT EXISTS INSCRIPCION (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "ano INT, " +
                    "semestre INT, " +
                    "estudiante_id DOUBLE, " +
                    "curso_id INT, " +
                    "FOREIGN KEY (estudiante_id) REFERENCES ESTUDIANTE(id), " +
                    "FOREIGN KEY (curso_id) REFERENCES CURSO(id))");


            // --- INSERCIÓN DE DATOS DE EJEMPLO (Con adiciones) ---
            stmt.execute("MERGE INTO PERSONA KEY(id) VALUES (1, 'Ana', 'Gomez', 'ana@gmail.com')");
            stmt.execute("MERGE INTO PERSONA KEY(id) VALUES (2, 'Luis', 'Mora', 'luis@gmail.com')");
            stmt.execute("MERGE INTO PERSONA KEY(id) VALUES (3, 'Carlos', 'Perez', 'carlos@gmail.com')");
            // AÑADIDO: Personas para los estudiantes de prueba de la GUI
            stmt.execute("MERGE INTO PERSONA KEY(id) VALUES (101, 'Maria', 'Lopez', 'maria@email.com')");
            stmt.execute("MERGE INTO PERSONA KEY(id) VALUES (102, 'Juan', 'Velez', 'juan@email.com')");

            stmt.execute("MERGE INTO PROFESOR KEY(id) VALUES (3, 'Tiempo Completo')");

            stmt.execute("MERGE INTO FACULTAD(id, nombre, decano_id) KEY(id) VALUES (101, 'Ingeniería', 3)");
            stmt.execute("MERGE INTO PROGRAMA(id, nombre,duracion,registro,facultad_id) KEY(id) VALUES (111, 'Ingeniería sistemas', 10, '2020-02-20', 101)");
            
            stmt.execute("MERGE INTO ESTUDIANTE KEY(id) VALUES (1, 102.2, 111, true, 3.4 )");
            // AÑADIDO: Estudiantes de prueba de la GUI
            stmt.execute("MERGE INTO ESTUDIANTE KEY(id) VALUES (101, 202401, 111, true, 4.5)");
            stmt.execute("MERGE INTO ESTUDIANTE KEY(id) VALUES (102, 202402, 111, true, 3.8)");

            stmt.execute("MERGE INTO CURSO(id, nombre,programa_id,activo) KEY(id) VALUES (901, 'Tecnologia avanzada',111,true )");
            // AÑADIDO: Cursos de prueba de la GUI
            stmt.execute("MERGE INTO CURSO(id, nombre,programa_id,activo) KEY(id) VALUES (902, 'Bases de Datos', 111, true )");
            stmt.execute("MERGE INTO CURSO(id, nombre,programa_id,activo) KEY(id) VALUES (903, 'Cálculo Integral', 111, true )");

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
