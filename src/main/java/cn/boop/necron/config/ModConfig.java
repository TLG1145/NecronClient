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

    //@SubConfig
    @Switch(name = "Chat Commands", description = "Chat commands in party chat", subcategory = "General")
    public static boolean chatCommands = true;
    @Switch(name = "Nametag", description = "Render Nametags (Not Complete!!!)", category = "Hypixel")
    public static boolean nameTag = false;
    @Switch(name = "Color HurtCam", description = "Test Module", category = "Hypixel")
    public static boolean hurtCam = false;
    @Switch(name = "Custom Title", description = "Custom Window Title", subcategory = "General")
    public static boolean customTitle = false;
    @Switch(name = "Auto Clicker", description = "Auto click your mouse", category = "Hypixel")
    public static boolean autoClicker = false;
    @Slider(name = "LCPS",  description = "Left click speed", category = "Hypixel", min = 1, max = 20)
    public static int lcps = 10;

    public static final ModConfig INSTANCE = new ModConfig();
}
