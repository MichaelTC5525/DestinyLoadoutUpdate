package main.util;

import lombok.Getter;

import main.exception.BadOrderException;
import main.exception.InvalidCommandLineException;

import java.util.Arrays;
import java.util.List;

@Getter
public class ArgReader {

    private String accountName;
    private String guardianClass;
    private String idFilePath;

    public ArgReader(String[] args) {
        boolean valid = checkValidParams(Arrays.asList(args));
        if (!valid) {
            throw new InvalidCommandLineException("Parameters given are not valid; check given options");
        }

        try {
            for (int i = 0; i < args.length; i++) {
                switch(args[i]) {
                    case "-acc":
                    case "--accountName":
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
                    case "-id":
                    case "--idFilePath":
                        idFilePath = args[i+1];
                        break;

                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new BadOrderException("Parameters are in a bad order; is the PSName value right after the option, " +
                    "and is the path to the accounts file given?");
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
                (!(args.contains("-id")) && !(args.contains("--idFilePath"))) ||
                args.size() != 5
                );
    }

}
