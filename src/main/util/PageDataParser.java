package main.util;

public class PageDataParser {

    private String data;

    public PageDataParser(String data) {
        this.data = data;
    }

    public int[] obtainStats() {
        //Parse through html data to find loadout numbers; data must contain 8 stat levels and 1 overall power level

        //Based on manual checks, we notice that these patterns / sequences appear near the values we want to extract
        String identifySeq = "</div></div></div></a>";
        String powerLvlIdentifySeq = "<spanclass=\"power-symbol\">&#x2727;</span>";

        int[] stats = new int[9];

        //Populate integer array with loadout numbers
        int prevIndexOfStatSeq = 0;
        for (int i = 0; i < 8; i++) {
            int startOfStatSeq = data.indexOf(identifySeq, prevIndexOfStatSeq);

            stats[i] = extractStatLevelAt(startOfStatSeq);
            prevIndexOfStatSeq = startOfStatSeq + identifySeq.length();

        }

        //Populate end of array with overall power level number
        int startOfPowerLvlSeq = data.indexOf(powerLvlIdentifySeq) + powerLvlIdentifySeq.length();
        stats[8] = extractPowerLevelAt(startOfPowerLvlSeq);

        return stats;
    }

    private int extractStatLevelAt(int startOfSeq) {
        int statLvl = 0;

        for (int j = 5; j >= 0; j--) {
            try {
                statLvl = Integer.parseInt(data.substring(startOfSeq - j, startOfSeq));
                break;
            } catch (NumberFormatException ignored) { }
        }
        return statLvl;
    }

    private int extractPowerLevelAt(int startOfSeq) {
        int powerLvl = 0;

        for (int k = 5; k >= 0; k--) {
            try {
                powerLvl = Integer.parseInt(data.substring(startOfSeq, startOfSeq + k));
                break;
            } catch (NumberFormatException ignored) { }
        }

        return powerLvl;
    }

}
