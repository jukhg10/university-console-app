package persistence;

import persistence.adapter.DatabaseAdapter; // Importa el adaptador
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseManager {

    // Modifica el método para que acepte DatabaseAdapter
    public static void initializeDatabase(DatabaseAdapter adapter) {
        String scriptFile = "schema-" + adapter.getInsertSyntax() + ".sql";

        try (Connection conn = adapter.getConnection(); // Usa el adaptador
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
                        int errorCode = e.getErrorCode();
                        String upperSql = sqlStatement.toUpperCase();

                        boolean isOracleDropError = (errorCode == 942 && upperSql.contains("DROP"));
                        boolean isMySqlCreateError = (errorCode == 1050 && upperSql.contains("CREATE"));
                        boolean isMySqlDropError = (errorCode == 1051 && upperSql.contains("DROP"));
                        boolean isH2CreateError = (e.getErrorCode() == 42101 && upperSql.contains("CREATE"));

                        if (isOracleDropError || isMySqlCreateError || isMySqlDropError || isH2CreateError) {
                            System.out.println("Ignoring benign SQL error: " + e.getMessage());
                        } else {
                            throw e;
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