package main.util;

import main.data.IDNumbers;
import main.exception.ProvisioningException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class IDExtractor {

    private String filePath;

    public IDExtractor(String filePath) {
        this.filePath = filePath;
    }

    public IDNumbers extractIDs(String account, String guardianClass) throws IOException {
        List<String> accountEntries = Files.readAllLines(Paths.get(filePath));
        long accId = -1;
        long charId = -1;

        for (String accEntry : accountEntries) {
            try {
                String[] currLineInfo = accEntry.split(";;;;");
                if (currLineInfo[0].equals(account) && currLineInfo[1].equals(guardianClass)) {
                    accId = Long.parseLong(currLineInfo[2]);
                    charId = Long.parseLong(currLineInfo[3]);
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Array access out of bounds. A line of the ID text file may be malformed.");
                e.printStackTrace();
                throw e;
            }
        }

        if (accId == -1 || charId == -1) {
            throw new ProvisioningException("Could not find ID numbers for the specified character. They may not" +
                    " be provisioned in the database to be used in this service.");
        }

        return IDNumbers.builder().accountID(accId).charID(charId).build();
    }

}
