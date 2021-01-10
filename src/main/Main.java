package main;

import main.data.IDNumbers;
import main.data.LoadoutStatistics;

import main.external.SQLReader;
import main.external.WebpageReader;
import main.util.ArgReader;
import main.util.IDExtractor;
import main.util.PageDataParser;

import main.exception.TableRowNotFoundException;

import java.io.File;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//To add a new player to the provisioned list of accounts, be sure to add them to a desired text file
// When running the program, be sure to specify the location of the text file RELATIVE to the .jar executable
// Include the fileName.txt as part of the file-path parameter
public class Main {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        System.out.println("Program starting. Fetching command line parameters...");

        ArgReader argReader = new ArgReader(args);

        System.out.println("Arguments read. Displaying parameters...");

        String accName = argReader.getAccountName();
        String guardianClass = argReader.getGuardianClass();

        //Create file object so we can use getCanonicalPath() method
        File file = new File(argReader.getIdFilePath());
        //Allows us to specify a desired file RELATIVE to the executable .jar
        String resolvedFilePath = file.getCanonicalPath();

        System.out.println("AccountName: " + accName);
        System.out.println("GuardianClass: " + guardianClass);
        System.out.println("ID File Path: " + resolvedFilePath);

        System.out.println("Parsing given file path to determine account and character IDs...");

        IDExtractor idExtractor = new IDExtractor(resolvedFilePath);
        IDNumbers idNums = idExtractor.extractIDs(accName, guardianClass);

        System.out.println("IDs for specified character found. Submitting information to webpage reader...");

        WebpageReader webpageReader = new WebpageReader(idNums.getAccountID(), idNums.getCharID());

        String pageData = webpageReader.readPage();

        System.out.println("Webpage successfully read. Beginning parsing process to extract loadout values...");

        PageDataParser pageDataParser = new PageDataParser(pageData);

        //Indexes 0-2 contain weapon slots; indexes 3-7 contain armour loadout pieces
        int[] webpageStats = pageDataParser.obtainStats();

        //Determine current artifact level by what is present; this does not change regardless of whether
        // max loadout is equipped
        int currArtifactLvl = pageDataParser.obtainPowerLevel() - floorAverageOfArray(webpageStats);

        System.out.println("Statistics successfully obtained from webpage, continuing to SQL database connection...");

        String[] inputList = new String[5];
        Scanner input = new Scanner(System.in);

        System.out.println("Requesting details for SQL database connection.");
        System.out.println("Enter the hostname for the database: ");
        inputList[0] = input.nextLine();

        System.out.println("Enter the instanceName to connect with. If there is none, submit as blank: ");
        inputList[1] = input.nextLine();

        System.out.println("Enter the database name to connect with: ");
        inputList[2] = input.nextLine();

        System.out.println("Enter an authorized username for this connection: ");
        inputList[3] = input.nextLine();

        System.out.println("Enter the password for this username: ");
        inputList[4] = input.nextLine();

        System.out.println("Information obtained. Opening database connection...");

        SQLReader sqlReader;
        if (inputList[1].isEmpty()) {
            sqlReader = new SQLReader(inputList[0], inputList[2], inputList[3], inputList[4]);
        } else {
            sqlReader = new SQLReader(inputList[0], inputList[1], inputList[2], inputList[3], inputList[4]);
        }

        ResultSet rs;

        int[] databaseStats = new int[8];

        try {
            sqlReader.initConnection();
            System.out.println("SQL Server Connection established; extracting database table values for specified " +
                                "character...");
            rs = sqlReader.runExtractQuery("SELECT * FROM dbo.PlayerLoadouts WHERE PSName = '" + accName +
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

        System.out.println("Statistics successfully obtained from database, continuing to value comparison...");

        //Create the object that will contain highest values between the two sources
        LoadoutStatistics loadoutStatistics = LoadoutStatistics.builder()
                                                .maxKinetic(Math.max(webpageStats[0], databaseStats[0]))
                                                .maxEnergy(Math.max(webpageStats[1], databaseStats[1]))
                                                .maxHeavy(Math.max(webpageStats[2], databaseStats[2]))
                                                .maxHelmet(Math.max(webpageStats[3], databaseStats[3]))
                                                .maxGauntlet(Math.max(webpageStats[4], databaseStats[4]))
                                                .maxChest(Math.max(webpageStats[5], databaseStats[5]))
                                                .maxLeg(Math.max(webpageStats[6], databaseStats[6]))
                                                .maxClassItem(Math.max(webpageStats[7], databaseStats[7]))
                                                .currArtifactLvl(currArtifactLvl)
                                                .build();

        System.out.println("Comparisons between webpage and database values complete, summary is as follows...");
        System.out.printf("MaxKinetic: Webpage = %d , Database = %d --> Overall = " + loadoutStatistics.getMaxKinetic()
                + "%n", webpageStats[0], databaseStats[0]);
        System.out.printf("MaxEnergy: Webpage = %d , Database = %d --> Overall = " + loadoutStatistics.getMaxEnergy()
                + "%n", webpageStats[1], databaseStats[1]);
        System.out.printf("MaxHeavy: Webpage = %d , Database = %d --> Overall = " + loadoutStatistics.getMaxHeavy()
                + "%n", webpageStats[2], databaseStats[2]);
        System.out.printf("MaxHelmet: Webpage = %d , Database = %d --> Overall = " + loadoutStatistics.getMaxHelmet()
                + "%n", webpageStats[3], databaseStats[3]);
        System.out.printf("MaxArm: Webpage = %d , Database = %d --> Overall = " + loadoutStatistics.getMaxGauntlet()
                + "%n", webpageStats[4], databaseStats[4]);
        System.out.printf("MaxChest: Webpage = %d , Database = %d --> Overall = " + loadoutStatistics.getMaxChest()
                + "%n", webpageStats[5], databaseStats[5]);
        System.out.printf("MaxLeg: Webpage = %d , Database = %d --> Overall = " + loadoutStatistics.getMaxLeg()
                + "%n", webpageStats[6], databaseStats[6]);
        System.out.printf("MaxClassItem: Webpage = %d , Database = %d --> Overall = " + loadoutStatistics.getMaxClassItem()
                + "%n", webpageStats[7], databaseStats[7]);
        System.out.println("CurrentArtifactLevel: " + loadoutStatistics.getCurrArtifactLvl());

        try {
            sqlReader.initConnection();
            System.out.println("New SQL Connection established; updating values in database table...");

            sqlReader.runQuery("UPDATE dbo.PlayerLoadouts " +
                                "SET MaxWpn1 = " + loadoutStatistics.getMaxKinetic() + "," +
                                "MaxWpn2 = " + loadoutStatistics.getMaxEnergy() + "," +
                                "MaxWpn3 = " + loadoutStatistics.getMaxHeavy() + "," +
                                "CurrArtifactLvl = " + loadoutStatistics.getCurrArtifactLvl() + " " +
                                "WHERE PSName = '" + accName + "'");

            sqlReader.runQuery("UPDATE dbo.PlayerLoadouts " +
                                "SET MaxHelmet = " + loadoutStatistics.getMaxHelmet() + "," +
                                "MaxArm = " + loadoutStatistics.getMaxGauntlet() + "," +
                                "MaxChest = " + loadoutStatistics.getMaxChest() + "," +
                                "MaxLeg = " + loadoutStatistics.getMaxLeg() + "," +
                                "MaxClassItem = " + loadoutStatistics.getMaxClassItem() + " " +
                                "WHERE PSName = '" + accName + "' AND GuardianClass = '" + guardianClass + "'");

            System.out.println("Database values have been updated. Confirm these changes in SSMS if necessary.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        } finally {
            sqlReader.closeConnection();
        }

        System.out.println("DestinyLoadoutUpdate process completed for the " + guardianClass + " character belonging" +
                " to account " + accName);
    }

    private static int floorAverageOfArray(int[] stats) {
        int total = 0;
        for (int stat : stats) {
            total += stat;
        }

        //stats.length should never be 0
        return (int) Math.floor(total / stats.length);
    }
}
