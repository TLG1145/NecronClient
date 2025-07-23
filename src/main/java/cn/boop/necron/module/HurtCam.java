package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

import static cn.boop.necron.config.sub.HurtCameraOptionsImpl.hurtCam;

public final class HurtCam {
    private static boolean isHurt = false;

    @SubscribeEvent
    public void onHurt(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (Necron.mc.thePlayer != null && hurtCam) {
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
            if (HurtCam.isHurt() && hurtCam && Minecraft.getMinecraft().currentScreen == null) {
                ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
                RenderUtils.drawBorderedRect(0.0f, 0.0f, sc.getScaledWidth(), sc.getScaledHeight(), 10.0f, new Color(255, 0, 0, 25 * Necron.mc.thePlayer.hurtTime).getRGB()
                );
            }
        }
    }

    private static boolean isHurt() {
        return isHurt;
    }
}
