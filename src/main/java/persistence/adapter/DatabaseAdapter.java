// src/main/java/persistence/adapter/DatabaseAdapter.java
package persistence.adapter;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseAdapter {
    Connection getConnection() throws SQLException;
    String getInsertSyntax();
    String quote(String identifier);
}