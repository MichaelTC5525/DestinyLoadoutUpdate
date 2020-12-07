package main;

import main.util.ArgReader;
import main.util.PageDataParser;
import main.util.WebpageReader;

import java.io.IOException;

//To add a new player to the provisioned list of accounts, be sure to add them to the accountName list in ArgReader,
// and create a block in the switch statement in WebpageReader with their appropriate ID numbers
public class Main {

    public static void main(String[] args) throws IOException {
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


    }
}
