package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.client.settings.KeyBinding;

public class PlayerUtils {
    public static void setSneak(boolean sneak) {
         KeyBinding.setKeyBindState(Necron.mc.gameSettings.keyBindSneak.getKeyCode(), sneak);
    }

    public static void rightClick() {
        KeyBinding.setKeyBindState(Necron.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        KeyBinding.onTick(Necron.mc.gameSettings.keyBindUseItem.getKeyCode());
    }
}
