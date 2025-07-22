package cn.boop.necron.config.sub;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class RouterOptionsImpl extends ModConfig {
    public RouterOptionsImpl() {
        super("Router", "necron/router.json");
        initialize();
    }

    @Switch(name = "Router", description = "Etherwarp route (WIP)")
    public static boolean router = false;
    @Switch(name = "Loop", description = "Let current waypoints can loop")
    public static boolean isLoop = false;
    @Switch(name = "Dev Message", description = "Display debug message while use Router")
    public static boolean devMsg = false;
}
