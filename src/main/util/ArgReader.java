package main.util;

import lombok.Getter;

import main.exception.BadOrderException;
import main.exception.InvalidCommandLineException;
import main.exception.ProvisioningException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ArgReader {

    private final List<String> PROVISIONED_ACCOUNT_NAMES = new ArrayList<>(
                    Arrays.asList("MGamer5525_Pown", "xoPraedyth", "avareverava", "MechaMosura", "US_Sinister1",
                                  "bobbyguti", "AshleyJacobson", "TheEndOfAllThing")
    );

    private String accountName;
    private String guardianClass;

    public ArgReader(String[] args) {
        boolean valid = checkValidParams(Arrays.asList(args));
        if (!valid) {
            throw new InvalidCommandLineException("Parameters given are not valid; check given options");
        }

        try {
            for (int i = 0; i < args.length; i++) {
                switch(args[i]) {
                    case "-acc":
                    case "--acountName":
                        accountName = args[i+1];
                        break;
                    case "-T":
                        guardianClass = "Titan";
                        break;
                    case "-H":
                        guardianClass = "Hunter";
                        break;
                    case "-W":
                        guardianClass = "Warlock";
                        break;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new BadOrderException("Parameters are in a bad order; is the PSName value right after the option?");
        }

        if (!(PROVISIONED_ACCOUNT_NAMES.contains(accountName))) {
            throw new ProvisioningException("AccountName: " + accountName + " is not on the list of provisioned " +
                    "users for this service");
        }
    }

    private boolean checkValidParams(List<String> args) {
        return !(
                (!(args.contains("-acc")) && !(args.contains("--accountName"))) ||
                (!(args.contains("-T")) && !(args.contains("-H")) && !(args.contains("-W"))) ||
                (args.contains("-T") && args.contains("-H") && args.contains("-W")) ||
                (args.contains("-T") && args.contains("-H")) ||
                (args.contains("-T") && args.contains("-W")) ||
                (args.contains("-H") && args.contains("-W")) ||
                args.size() != 3
                );
    }

}
