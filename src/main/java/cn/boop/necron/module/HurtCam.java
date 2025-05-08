package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public final class HurtCam {
    private static boolean isHurt = false;

    @SubscribeEvent
    public void onHurt(TickEvent.ClientTickEvent event) {
        if (Necron.mc.thePlayer != null && ModConfig.hurtCam) {
            if (Necron.mc.thePlayer.hurtTime > 0) {
                isHurt = true;
            }
            if (Necron.mc.thePlayer.hurtTime <= 0) {
                isHurt = false;
            }
        }
    }

    @SubscribeEvent
    public void onRenderScreen(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            if (HurtCam.isHurt() && ModConfig.hurtCam && Minecraft.getMinecraft().currentScreen == null) {
                ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
                RenderUtils.drawBorderedRect(
                        0.0f,
                        0.0f,
                        sc.getScaledWidth(),
                        sc.getScaledHeight(),
                        10.0f,
                        new Color(255, 0, 0, 25 * Necron.mc.thePlayer.hurtTime).getRGB()  // 纯红色描边
                );
            }
        }
    }

    private static boolean isHurt() {
        return isHurt;
    }
}
