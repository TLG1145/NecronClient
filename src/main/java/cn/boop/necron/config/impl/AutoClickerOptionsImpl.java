package cn.boop.necron.config.impl;

import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class AutoClickerOptionsImpl extends ModConfig {
    public AutoClickerOptionsImpl() {
        super("Auto Clicker", "necron/autoclicker.json");
        initialize();
    }

    @Switch(name = "Enabled", description = "Auto click your mouse")
    public static boolean autoClicker = false;

    @Switch(name = "Left Click", description = "Auto left click", subcategory = "Auto Clicker")
    public static boolean leftClick = true;
    @Number(name = "Left Click CPS", description = "CPS of left click", min = 0, max = 20, subcategory = "Auto Clicker")
    public static int lCPS = 10;
    @Switch(name = "Right Click", description = "Auto right click", subcategory = "Auto Clicker")
    public static boolean rightClick = true;
    @Number(name = "Right Click CPS", description = "CPS of right click", min = 0, max = 20, subcategory = "Auto Clicker")
    public static int rCPS = 10;


}
