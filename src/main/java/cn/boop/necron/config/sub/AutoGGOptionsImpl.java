package cn.boop.necron.config.sub;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class AutoGGOptionsImpl extends ModConfig {
    public AutoGGOptionsImpl() {
        super("AutoGG", "necron/autogg.json");
        initialize();
    }

    @Switch(name = "Enabled", description = "Auto send GG after a game")
    public static boolean autoGG = false;
}
