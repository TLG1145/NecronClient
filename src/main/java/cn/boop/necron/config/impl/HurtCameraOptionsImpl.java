package cn.boop.necron.config.impl;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class HurtCameraOptionsImpl extends ModConfig {
    public HurtCameraOptionsImpl() {
        super("Hurt Camera", "necron/hurtcamera.json");
        initialize();
    }

    @Switch(name = "Enabled", description = "Render your hurt camera")
    public static boolean hurtCam = false;
}
