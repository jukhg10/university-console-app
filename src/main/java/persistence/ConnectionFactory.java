package persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private final String dbType;
    private final Properties props = new Properties();
    private final String driverProperty;

    public ConnectionFactory(String dbType) {
        this.dbType = dbType.toLowerCase();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
            this.driverProperty = props.getProperty(this.dbType + ".driver");
            if (driverProperty == null) {
                throw new RuntimeException("No se encontró la propiedad del driver para: " + this.dbType);
            }
            Class.forName(driverProperty);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar la configuración o el driver de la BD", e);
        }
    }

    public Connection getConnection() throws SQLException {
        String url = props.getProperty(dbType + ".url");
        String user = props.getProperty(dbType + ".user");
        String password = props.getProperty(dbType + ".password");
        return DriverManager.getConnection(url, user, password);
    }

    public String getInsertSyntax() {
        // --- ESTA ES LA CORRECCIÓN CLAVE ---
        // Ahora devuelve el tipo de base de datos (ej. "mysql") en lugar de "standard".
        // Esto permite que DatabaseManager busque el archivo schema correcto (ej. "schema-mysql.sql").
        return this.dbType;
    }

    public String quote(String identifier) {
        switch (dbType) {
            case "oracle":
                return "\"" + identifier.toUpperCase() + "\"";
            case "mysql":
                return "`" + identifier + "`";
            default: // h2 y otros
                return "\"" + identifier.toUpperCase() + "\"";
        }
    }
}
