// src/main/java/persistence/adapter/DatabaseAdapterFactory.java
package persistence.adapter;

import persistence.ConnectionFactory;

public class DatabaseAdapterFactory {
    public static DatabaseAdapter createAdapter(String dbType) {
        ConnectionFactory connectionFactory = new ConnectionFactory(dbType);
        switch (dbType.toLowerCase()) {
            case "mysql":
                return new MySqlAdapter(connectionFactory);
            case "oracle":
                return new OracleAdapter(connectionFactory);
            case "h2":
            default:
                return new H2Adapter(connectionFactory);
        }
    }
}