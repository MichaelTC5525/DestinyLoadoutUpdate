package main;

import main.data.LoadoutStatistics;
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

        int[] loadoutStats = pageDataParser.obtainStats();


        LoadoutStatistics loadoutStatistics = LoadoutStatistics.builder()
                .maxKinetic(loadoutStats[0])
                .maxEnergy(loadoutStats[1])
                .maxHeavy(loadoutStats[2])
                .maxHelmet(loadoutStats[3])
                .maxGauntlet(loadoutStats[4])
                .maxChest(loadoutStats[5])
                .maxLeg(loadoutStats[6])
                .maxClassItem(loadoutStats[7])
                .currPowerLvl(loadoutStats[8])
                .build();

        System.out.println("Statistics successfully obtained, read values are as follows...");

        String[] desc = new String[]{"Kinetic", "Energy", "Heavy",
                                     "Helmet", "Gauntlet", "Chest", "Leg", "ClassItem",
                                     "Total Power"};
        for (int i = 0; i < 9; i++) {
            System.out.println(desc[i] + ": " + loadoutStats[i]);
        }

        SQLReader sqlReader = new SQLReader("localhost", "DestinyInfo", true);

        ResultSet rs = null;

        try {
            sqlReader.initConnection();
            rs = sqlReader.runQuery("SELECT * FROM dbo.PlayerLoadouts");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            sqlReader.closeConnection();
        }

        System.out.println(rs.getInt("MaxWpn1"));




    }
}
