package cn.boop.necron.config.sub;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class ChatCommandsOptionsImpl extends ModConfig {
    public ChatCommandsOptionsImpl() {
        super("Chat Commands", "necron/chatcommands.json");
        initialize();
    }

    @Switch(name = "Enabled", description = "Party commands in Hypixel", category = "Miscellaneous")
    public static boolean chatCommands = true;
}
