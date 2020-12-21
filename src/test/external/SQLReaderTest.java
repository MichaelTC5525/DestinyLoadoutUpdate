package test.external;

import main.external.SQLReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

public class SQLReaderTest {

    private SQLReader defaultReader;
    private SQLReader invalidReader;
    private SQLReader customReader;

    @BeforeEach
    public void setup() {
        defaultReader = new SQLReader();
        invalidReader = new SQLReader("jdbc:sqlserver://unknownserver");
        customReader = new SQLReader("somehost", "database1", "newUser", "12345");
    }

    @Test
    public void testConstructors() {
        Assertions.assertEquals("jdbc:sqlserver://localhost:1433;database=DestinyInfo;user=data_editor;password=d2infoEQUINOX;",
                                defaultReader.getConnectionString());
        Assertions.assertEquals("jdbc:sqlserver://unknownserver", invalidReader.getConnectionString());
        Assertions.assertEquals("jdbc:sqlserver://somehost:1433;database=database1;user=newUser;password=12345;",
                                customReader.getConnectionString());

    }

    @Test
    public void testCatchInvalidConnectionString() {
        try {
            invalidReader.initConnection();
            fail();
        } catch (SQLException | ClassNotFoundException ignored) {}
    }

    @Test
    public void testSuccessfulConnectionAndReceiveResultSet() {
        try {
            defaultReader.initConnection();
            ResultSet rs = defaultReader.runQuery("SELECT * FROM dbo.PlayerLoadouts;");
            Assertions.assertNotNull(rs);
            defaultReader.closeConnection();
        } catch (SQLException | ClassNotFoundException e) {
            fail();
        }
    }
}
