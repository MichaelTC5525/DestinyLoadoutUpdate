package main;

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

        //Indexes 0-2 contain weapon slots; indexes 3-7 contain armour loadout pieces; final index contains Power Level
        // including artifact bonus
        int[] webpageStats = pageDataParser.obtainStats();

        System.out.println("Statistics successfully obtained, read values are as follows...");

        String[] desc = new String[]{"Kinetic", "Energy", "Heavy",
                                     "Helmet", "Gauntlet", "Chest", "Leg", "ClassItem",
                                     "Total Power"};
        for (int i = 0; i < 9; i++) {
            System.out.println(desc[i] + ": " + webpageStats[i]);
        }

        SQLReader sqlReader = new SQLReader("localhost", "DestinyInfo", "data_editor", "d2infoEQUINOX");

        ResultSet rs;

        try {
            sqlReader.initConnection();
            rs = sqlReader.runQuery("SELECT * FROM dbo.PlayerLoadouts WHERE PSName = '" + accName +
                    "' AND GuardianClass = '" + guardianClass + "';");

            //Step into the row of interest
            rs.next();
            System.out.println(rs.getString("PSName"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            sqlReader.closeConnection();
        }






    }
}
