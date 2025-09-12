package cn.boop.necron.config.impl;

import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cn.boop.necron.config.ClientNotification;
import cn.boop.necron.config.ModConfig;

public class MiningOptionsImpl extends ModConfig {
    public MiningOptionsImpl() {
        super("Mining Helper", "necron/farming.json");
        initialize();

        registerKeyBind(nukerKB, () -> {
            gemNuker = !gemNuker;
            String message = "Gemstone Nuker" + (gemNuker ? " Enabled" : " Disabled");
            ClientNotification.NotificationType type = (gemNuker ? ClientNotification.NotificationType.ENABLED : ClientNotification.NotificationType.DISABLED);
            ClientNotification.sendNotification("Module", message, type, 2000);
        });
    }

    @Switch(name = "Enabled", description = "Useful mining features")
    public static boolean mining = true;
    @Switch(name = "Gemstone Nuker", description = "Auto fuck gemstones", subcategory = "Macro")
    public static boolean gemNuker = false;
    @Dropdown(name = "Gemstone Type", category = "Macro", options = {"Ruby", "Amethyst", "Jade", "Sapphire", "Amber", "Topaz", "Jasper", "Opal", "Aquamarine", "Citrine", "Onyx", "Peridot"}, subcategory = "Macro")
    public static int gemType = 0;
    @KeyBind(name = "Gemstone Keybind", description = "Keybind to toggle Nuker", subcategory = "Macro")
    public static OneKeyBind nukerKB = new OneKeyBind();
}
