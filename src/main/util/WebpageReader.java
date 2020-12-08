package main.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WebpageReader {

    private final String urlStem = "https://www.bungie.net/en/Gear/2/";
    private long accountID;
    private long charID;

    private URL url;

    public WebpageReader(String accountName, String guardianClass) throws MalformedURLException {
        //set accountID, charID based on accountName, guardianClass
        //url = urlStem + accountID + "/" + charID

        switch(accountName) {
            case "MGamer5525_Pown":
                accountID = 4611686018438093631L;
                switch(guardianClass) {
                    case "Titan":
                        charID = 2305843009263515973L;
                        break;
                    case "Hunter":
                        charID = 2305843009266872396L;
                        break;
                    case "Warlock":
                        charID = 2305843009266872428L;
                        break;
                }
                break;
            case "xoPraedyth":
                accountID = 4611686018428843576L;
                switch(guardianClass) {
                    case "Titan":
                        charID = 2305843009272418109L;
                        break;
                    case "Hunter":
                        charID = 2305843009265276369L;
                        break;
                    case "Warlock":
                        charID = 2305843009266113213L;
                        break;
                }
                break;
            case "MechaMosura":
                accountID = 4611686018451859929L;
                switch(guardianClass) {
                    case "Titan":
                        charID = 2305843009264443435L;
                        break;
                    case "Hunter":
                        charID = 2305843009310775284L;
                        break;
                    case "Warlock":
                        charID = 2305843009277726871L;
                        break;
                }
                break;
            case "avareverava":
                accountID = 4611686018479675254L;
                switch(guardianClass) {
                    case "Titan":
                        charID = 2305843009393512555L;
                        break;
                    case "Hunter":
                        charID = 2305843009393512556L;
                        break;
                    case "Warlock":
                        charID = 2305843009378410171L;
                        break;
                }
                break;
            case "AshleyJacobson":
                accountID = 4611686018469069968L;
                switch(guardianClass) {
                    case "Titan":
                        charID = 2305843009385504579L;
                        break;
                    case "Hunter":
                        charID = 2305843009267902551L;
                        break;
                    case "Warlock":
                        charID = 2305843009444694489L;
                        break;
                }
                break;
            case "TheEndOfAllThing":
                accountID = 4611686018431677513L;
                switch(guardianClass) {
                    case "Titan":
                        charID = 2305843009263027162L;
                        break;
                    case "Hunter":
                        charID = 2305843009263027164L;
                        break;
                    case "Warlock":
                        charID = 2305843009553894237L;
                        break;
                }
                break;
            case "US_Sinister1":
                accountID = 4611686018465762536L;
                switch(guardianClass) {
                    case "Titan":
                        charID = 2305843009290624828L;
                        break;
                    case "Hunter":
                        charID = 2305843009261820235L;
                        break;
                    case "Warlock":
                        charID = 2305843009357284173L;
                        break;
                }
                break;
            case "bobbyguti":
                accountID = 4611686018463597894L;
                switch(guardianClass) {
                    case "Titan":
                        charID = 2305843009278517685L;
                        break;
                    case "Hunter":
                        charID = 2305843009361605626L;
                        break;
                    case "Warlock":
                        charID = 2305843009723784218L;
                        break;
                }
                break;
        }

        url = new URL(urlStem + accountID + "/" + charID);
    }

    public String readPage() throws IOException {
        System.out.println("URL to read: " + url);
        Scanner sc = new Scanner(url.openStream());
        StringBuffer sb = new StringBuffer();

        while(sc.hasNext()) {
            sb.append(sc.next());
        }

        return sb.toString();
    }
}
