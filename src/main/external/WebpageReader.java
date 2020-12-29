package main.external;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WebpageReader {

    private final String urlStem = "https://www.bungie.net/en/Gear/2/";
    private URL url;

    public WebpageReader(long accountID, long charID) throws MalformedURLException {
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
