package main.external;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLReader {

    private String connectionString = "Server=localhost;Database=master;Trusted_Connection=True;";
    private Connection connection;

    public SQLReader() { }

    public SQLReader(String connectionString) {
        this.connectionString = connectionString;
    }

    public SQLReader(String hostname, String databaseName, boolean isTrustedConnection) {
        if (isTrustedConnection) {
            connectionString = "Server=" + hostname + ";Database=" + databaseName + ";Trusted_Connection=True;";
        } else {
            connectionString = "Server=" + hostname + ";Database=" + databaseName + ";Trusted_Connection=False;";
        }
    }

    public void initConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.out.println("Connection failed; perhaps connection string is invalid");
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void closeConnection() throws SQLException {
        if (!connection.isClosed()) {
            connection.close();
        }
    }

}
