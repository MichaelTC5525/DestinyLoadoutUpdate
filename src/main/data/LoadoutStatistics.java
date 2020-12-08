package main.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoadoutStatistics {

    private int maxKinetic;
    private int maxEnergy;
    private int maxHeavy;
    private int maxHelmet;
    private int maxGauntlet;
    private int maxChest;
    private int maxLeg;
    private int maxClassItem;
    private int currPowerLvl;


}
