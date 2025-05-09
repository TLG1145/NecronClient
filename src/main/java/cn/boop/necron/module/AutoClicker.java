package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.PlayerUtils;
import cn.boop.necron.utils.event.MouseAimHandler;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoClicker {
    private static double nextLeftClick = .0;
    private static long lastLeftClickTime = 0L;
    private static final long MIN_CLICK_INTERVAL = 60;

    @SubscribeEvent
    public void simulateLeftClick(TickEvent.ClientTickEvent event) {
        if (Necron.mc.thePlayer == null || Necron.mc.objectMouseOver == null) return;
        if (Necron.mc.currentScreen != null) return;
        long nowMillis = System.currentTimeMillis();
        if (ModConfig.autoClicker && nowMillis >= nextLeftClick && MouseAimHandler.isLeftMouseDown()) {
            if (Necron.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                return;
            }
            if (nowMillis - lastLeftClickTime < MIN_CLICK_INTERVAL) {
                return;
            }
            nextLeftClick = nowMillis + (((double) 1000 / ModConfig.lcps) + ((Math.random() - .5) * 100.0));
            lastLeftClickTime = nowMillis;
            PlayerUtils.leftClick();
        }
    }
}
