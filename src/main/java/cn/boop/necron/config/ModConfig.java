package cn.boop.necron.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.Switch;
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
    @Switch(name = "Nametag", description = "Render Nametags (Not Complete!!!)", category = "Hypixel")
    public static boolean nameTag = false;
    @Number(name = "Distance", description = "Render Distance of the Nametags", category = "Hypixel", min = 1, max = 200)
    public static int nametagDistance = 128;
    @Switch(name = "ColorHurtCam", description = "Test Module", category = "Hypixel")
    public static boolean hurtCam = false;
    @Switch(name = "Custom Title", description = "Custom Window Title", subcategory = "General")
    public static boolean customTitle = false;



    public static final ModConfig INSTANCE = new ModConfig();
}
