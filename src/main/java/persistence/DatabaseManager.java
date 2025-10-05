package persistence;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseManager {

    public static void initializeDatabase(ConnectionFactory connectionFactory) {
        String scriptFile = "schema-" + connectionFactory.getInsertSyntax() + ".sql";

        try (Connection conn = connectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             InputStream inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream(scriptFile);
             Scanner scanner = new Scanner(inputStream).useDelimiter(";")) {

            System.out.println("Executing SQL script: " + scriptFile);
            while (scanner.hasNext()) {
                String sqlStatement = scanner.next().trim();
                if (!sqlStatement.isEmpty()) {
                    try {
                        stmt.execute(sqlStatement);
                    } catch (SQLException e) {
                        // --- ESTA ES LA CORRECCIÓN PARA H2 ---
                        int errorCode = e.getErrorCode();
                        String upperSql = sqlStatement.toUpperCase();

                        boolean isOracleDropError = (errorCode == 942 && upperSql.contains("DROP"));
                        boolean isMySqlCreateError = (errorCode == 1050 && upperSql.contains("CREATE"));
                        boolean isMySqlDropError = (errorCode == 1051 && upperSql.contains("DROP"));
                        // Añadimos el error de H2: 42101 - Table already exists
                        boolean isH2CreateError = (e.getErrorCode() == 42101 && upperSql.contains("CREATE"));

                        if (isOracleDropError || isMySqlCreateError || isMySqlDropError || isH2CreateError) {
                            System.out.println("Ignoring benign SQL error: " + e.getMessage());
                        } else {
                            throw e; // Lanzar cualquier otro error SQL que sí sea importante
                        }
                    }
                }
            }
            System.out.println("Database schema initialized successfully.");

        } catch (Exception e) {
            System.err.println("Failed to initialize database schema from " + scriptFile);
            e.printStackTrace();
        }
    }
}