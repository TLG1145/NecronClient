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
        addDependency("Router", "Waypoints");
    }

    @Switch(name = "Chat Commands", description = "Party commands in Hypixel", subcategory = "General")
    public static boolean chatCommands = true;
    @Switch(name = "Color HurtCam", description = "Render your hurt camera", subcategory = "General")
    public static boolean hurtCam = false;
    @Switch(name = "Custom Title", description = "Custom window title", subcategory = "General")
    public static boolean customTitle = false;
    @Switch(name = "Etherwarp", description = "Left click to use Etherwarp ability", category = "SkyBlock")
    public static boolean etherwarp = false;
    @Switch(name = "Waypoints", description = "Custom waypoint", category = "SkyBlock")
    public static boolean waypoints = false;
    @Switch(name = "Router", description = "Etherwarp route", category = "SkyBlock")
    public static boolean router = false;
    @Switch(name = "AutoGG", description = "Auto send GG after a game", category = "Hypixel")
    public static boolean autoGG = false;
    @Switch(name = "Auto Clicker", description = "Automatically click your mouse", category = "SkyBlock")
    public static boolean autoClicker = false;
    @Switch(name = "Blaze Dagger", description = "Auto swap BS daggers mode", category = "SkyBlock")
    public static boolean blazeDagger = false;

    public static final ModConfig INSTANCE = new ModConfig();
}
