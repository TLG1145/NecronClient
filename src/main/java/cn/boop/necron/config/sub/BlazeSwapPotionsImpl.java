package cn.boop.necron.config.sub;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class BlazeSwapPotionsImpl extends ModConfig {
    public BlazeSwapPotionsImpl() {
        super("Blaze Swap", "necron/blazeswap.json");
        initialize();
    }

    @Switch(name = "Enabled", description = "Auto swap blaze daggers' mode")
    public static boolean blazeSwap = false;
}
