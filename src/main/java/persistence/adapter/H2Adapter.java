// src/main/java/persistence/adapter/H2Adapter.java
package persistence.adapter;

import persistence.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class H2Adapter implements DatabaseAdapter {
    private final ConnectionFactory connectionFactory;

    public H2Adapter(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionFactory.getConnection();
    }

    @Override
    public String getInsertSyntax() {
        return "h2";
    }

    @Override
    public String quote(String identifier) {
        return "\"" + identifier.toUpperCase() + "\"";
    }
}