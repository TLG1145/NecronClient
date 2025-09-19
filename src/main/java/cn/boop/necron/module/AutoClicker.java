package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.PlayerUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static cn.boop.necron.config.impl.AutoClickerOptionsImpl.*;

public class AutoClicker {
    private double nextLeftClick = 0.0;
    private double nextRightClick = 0.0;
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!autoClicker) return;
        if (Necron.mc.currentScreen != null) return;
        long nowMillis = System.currentTimeMillis();

        if (leftClick && Necron.mc.gameSettings.keyBindAttack.isKeyDown() && nowMillis >= nextLeftClick) {
            nextLeftClick = nowMillis + ((1000D / lCPS) + ((Math.random() - 0.5) * 60.0));
            PlayerUtils.leftClick();
        }

        if (rightClick && Necron.mc.gameSettings.keyBindUseItem.isKeyDown() && nowMillis >= nextRightClick) {
            nextRightClick = nowMillis + ((1000D / rCPS) + ((Math.random() - 0.5) * 60.0));
            PlayerUtils.rightClick();
        }
    }

    public static boolean canAim() {
        return autoClicker && Necron.mc.gameSettings.keyBindAttack.isKeyDown();
    }
}
