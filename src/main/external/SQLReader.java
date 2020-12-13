package main.external;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public ResultSet runQuery(String query) throws SQLException {
        Statement stmt;

        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Error occurred when creating SQL query");
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return stmt.executeQuery(query);
    }

    public void initConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost;database=DestinyInfo;user=data_editor;password=d2infoEQUINOX";
            connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException | ClassNotFoundException e) {
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
