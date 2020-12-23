package main.external;

import lombok.Getter;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Getter
public class SQLReader {

    private String connectionString = "jdbc:sqlserver://localhost:1433;database=DestinyInfo;" +
                                        "user=data_editor;password=d2infoEQUINOX;";

    private Statement stmt;
    private Connection connection;

    public SQLReader() { }

    public SQLReader(String connectionString) {
        this.connectionString = connectionString;
    }

    public SQLReader(String hostname, String databaseName, String user, String password) {
        connectionString = "jdbc:sqlserver://" + hostname + ":1433;database=" + databaseName + ";user=" + user +
                ";password=" + password + ";" ;
    }

    public ResultSet runExtractQuery(String query) throws SQLException {
        return stmt.executeQuery(query);
    }

    public void runQuery(String query) throws SQLException {
        stmt.execute(query);
    }

    public void initConnection() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(connectionString);
            stmt = connection.createStatement();
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
