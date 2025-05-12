package cn.boop.necron.module;

import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.event.MouseAimHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AimTest {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        boolean isLeftMouseDown = MouseAimHandler.isLeftMouseDown();
        boolean autoClickerEnabled = ModConfig.autoClicker;

        if (isLeftMouseDown && autoClickerEnabled) {
            RotationUtils.aimAtNearestEntity(6, 0.5F);
        }
    }
}