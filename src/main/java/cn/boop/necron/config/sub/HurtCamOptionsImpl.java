package cn.boop.necron.config.sub;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class HurtCamOptionsImpl extends ModConfig {
    public HurtCamOptionsImpl() {
        super("HurtCam", "necron/hurtcam.json");
        initialize();
    }

    @Switch(name = "Enabled", description = "Render your hurt camera")
    public static boolean hurtCam = false;
}
