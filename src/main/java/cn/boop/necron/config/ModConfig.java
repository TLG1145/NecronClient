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

    @Switch(name = "Chat Commands", description = "Party commands in Hypixel", category = "Hypixel", subcategory = "Chat")
    public static boolean chatCommands = true;
    @Switch(name = "Color HurtCam", description = "Render your hurt camera", category = "Client")
    public static boolean hurtCam = false;
    @Switch(name = "Custom Title", description = "Custom window title", category = "Client")
    public static boolean customTitle = false;
    @Switch(name = "Etherwarp", description = "Left click to use Etherwarp ability", category = "SkyBlock", subcategory = "QoL")
    public static boolean etherwarp = false;
    @Switch(name = "Waypoints", description = "Custom waypoint", category = "SkyBlock", subcategory = "QoL")
    public static boolean waypoints = false;
    @Switch(name = "Router", description = "Etherwarp route (WIP)", category = "SkyBlock", subcategory = "Router")
    public static boolean router = false;
    @Switch(name = "AutoGG", description = "Auto send GG after a game", category = "Hypixel", subcategory = "Chat")
    public static boolean autoGG = false;
    @Switch(name = "Blaze Dagger", description = "Auto swap blaze daggers' mode", category = "SkyBlock", subcategory = "Slayer")
    public static boolean blazeDagger = false;
    @Switch(name = "Nametags", description = "Display nametags of players", category = "Hypixel", subcategory = "Nametags")
    public static boolean nametags = false;
    @Switch(name = "Loop Mode", description = "If you are doing gemstone mining, enable this", category = "SkyBlock", subcategory = "Router")
    public static boolean isLoop = false;

    public static final ModConfig INSTANCE = new ModConfig();
}
