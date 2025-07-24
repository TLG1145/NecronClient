package cn.boop.necron.config.impl;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class NametagsOptionsImpl extends ModConfig {
    public NametagsOptionsImpl() {
        super("Nametags", "necron/nametags.json");
        initialize();
    }

    @Switch(name = "Enabled", description = "Display nametags of players")
    public static boolean nametags = false;
}
