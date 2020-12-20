package main.external;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLReader {

    private String connectionString = "jdbc:sqlserver://localhost:1433;database=DestinyInfo;" +
                                        "user=data_editor;password=d2infoEQUINOX;";
    private Connection connection;

    public SQLReader() { }

    public SQLReader(String connectionString) {
        this.connectionString = connectionString;
    }

    public SQLReader(String hostname, String databaseName, String user, String password) {
        connectionString = "jdbc:sqlserver://" + hostname + ":1433;database=" + databaseName + ";user=" + user +
                ";password=" + password + ";" ;
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
            connection = DriverManager.getConnection(connectionString);
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
