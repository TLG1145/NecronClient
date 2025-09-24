package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.client.settings.KeyBinding;

public class PlayerUtils {
    public static void setSneak(boolean sneak) {
         KeyBinding.setKeyBindState(Necron.mc.gameSettings.keyBindSneak.getKeyCode(), sneak);
    }

    public static void setJump(boolean jump) {
        KeyBinding.setKeyBindState(Necron.mc.gameSettings.keyBindJump.getKeyCode(), jump);
    }

    public static void rightClick() {
        KeyBinding.onTick(Necron.mc.gameSettings.keyBindUseItem.getKeyCode());
    }

    public static void leftClick() {
        KeyBinding.onTick(Necron.mc.gameSettings.keyBindAttack.getKeyCode());
    }
}
