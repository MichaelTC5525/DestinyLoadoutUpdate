package main;

import main.exception.TableRowNotFoundException;
import main.external.SQLReader;
import main.util.ArgReader;
import main.util.PageDataParser;
import main.external.WebpageReader;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

//To add a new player to the provisioned list of accounts, be sure to add them to the accountName list in ArgReader,
// and create a block in the switch statement in WebpageReader with their appropriate ID numbers
public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Program starting. Fetching command line parameters...");

        ArgReader argReader = new ArgReader(args);

        System.out.println("Arguments read. Displaying search parameters.");

        String accName = argReader.getAccountName();
        String guardianClass = argReader.getGuardianClass();

        System.out.println("AccountName: " + accName);
        System.out.println("GuardianClass: " + guardianClass);

        System.out.println("Submitting information to webpage reader...");
        WebpageReader webpageReader = new WebpageReader(accName, guardianClass);

        String pageData = webpageReader.readPage();

        PageDataParser pageDataParser = new PageDataParser(pageData);

        //Indexes 0-2 contain weapon slots; indexes 3-7 contain armour loadout pieces; final index 8 contains Power
        // Level including artifact bonus
        int[] webpageStats = pageDataParser.obtainStats();

        //Determine current artifact level by what is present; this does not change regardless of whether
        // max loadout is equipped
        int currArtifactLvl = webpageStats[webpageStats.length - 1] - averageArray(webpageStats);

        System.out.println("Statistics successfully obtained, webpage values are as follows...");

        String[] desc = new String[]{"Kinetic", "Energy", "Heavy",
                                     "Helmet", "Gauntlet", "Chest", "Leg", "ClassItem"};
        for (int i = 0; i < 8; i++) {
            System.out.println(desc[i] + ": " + webpageStats[i]);
        }
        System.out.println("CurrentArtifactLevel: " + currArtifactLvl);

        SQLReader sqlReader = new SQLReader("localhost", "DestinyInfo", "data_editor", "d2infoEQUINOX");

        ResultSet rs;

        int[] databaseStats = new int[8];

        try {
            sqlReader.initConnection();
            System.out.println("SQL Server Connection established; extracting database table values for specified " +
                                "character...");
            rs = sqlReader.runQuery("SELECT * FROM dbo.PlayerLoadouts WHERE PSName = '" + accName +
                    "' AND GuardianClass = '" + guardianClass + "';");

            //Step into the row of interest; there should only be one row in this set
            if (!rs.next()) {
                throw new TableRowNotFoundException("The character assigned to this account was not found in the " +
                        "table. You may need to initialize values for this character in SSMS.");
            }

            //Populate int array with info from database; specify columns so we are independent of column order in
            // database table
            databaseStats[0] = rs.getInt("MaxWpn1");
            databaseStats[1] = rs.getInt("MaxWpn2");
            databaseStats[2] = rs.getInt("MaxWpn3");
            databaseStats[3] = rs.getInt("MaxHelmet");
            databaseStats[4] = rs.getInt("MaxArm");
            databaseStats[5] = rs.getInt("MaxChest");
            databaseStats[6] = rs.getInt("MaxLeg");
            databaseStats[7] = rs.getInt("MaxClassItem");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            sqlReader.closeConnection();
        }

        System.out.println("Statistics successfully obtained, database values are as follows...");
        for (int i = 0; i < 8; i++) {
            System.out.println(desc[i] + ": " + databaseStats[i]);
        }

    }

    private static int averageArray(int[] stats) {
        int total = 0;
        for (int stat : stats) {
            total += stat;
        }

        //stats.length should never be 0
        return (int) Math.floor(total / stats.length);
    }
}
