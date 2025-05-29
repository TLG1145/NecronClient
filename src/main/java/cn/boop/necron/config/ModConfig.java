package cn.boop.necron.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cn.boop.necron.Necron;

public class ModConfig extends Config {
    public ModConfig() {
        super(new Mod(Necron.MODNAME, ModType.UTIL_QOL), "necronclient.json");
        initialize();
    }

    @Switch(name = "Chat Commands", description = "Chat commands in party chat", subcategory = "General")
    public static boolean chatCommands = true;
    @Switch(name = "Color HurtCam", description = "Render your hurt camera", category = "Hypixel")
    public static boolean hurtCam = false;
    @Switch(name = "Custom Title", description = "Custom Window Title", subcategory = "General")
    public static boolean customTitle = false;
    @Switch(name = "Etherwarp", description = "Left click to use Etherwarp ability", category = "Hypixel")
    public static boolean etherwarp = false;
    @Switch(name = "Waypoints", description = "Custom Waypoint", category = "Hypixel")
    public static boolean waypoints = false;
    @Switch(name = "Router", description = "Half-auto Etherwarp route", category = "Hypixel")
    public static boolean router = false;

    public static final ModConfig INSTANCE = new ModConfig();
}
