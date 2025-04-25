package cn.boop.necron.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import cn.boop.necron.Necron;

public class ModConfig extends Config {
    public ModConfig() {
        super(new Mod(Necron.MODNAME, ModType.UTIL_QOL), "necronclient.json");
        initialize();
    }

    @Switch(name = "Chat Commands", description = "Chat commands in party chat", subcategory = "General", size = OptionSize.SINGLE)
    public static boolean chatCommands = true;
    @Switch(name = "Nametag", description = "Render Nametags", category = "Hypixel", size = OptionSize.SINGLE)
    public static boolean nameTag = false;


    public static final ModConfig INSTANCE = new ModConfig();
}
