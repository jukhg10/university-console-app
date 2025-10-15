// src/main/java/persistence/adapter/MySqlAdapter.java
package persistence.adapter;

import persistence.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlAdapter implements DatabaseAdapter {
    private final ConnectionFactory connectionFactory;

    public MySqlAdapter(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionFactory.getConnection();
    }

    @Override
    public String getInsertSyntax() {
        return "mysql";
    }

    @Override
    public String quote(String identifier) {
        return "`" + identifier + "`";
    }
}