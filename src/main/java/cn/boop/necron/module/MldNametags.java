package cn.boop.necron.module;

import java.awt.Color;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.GLUtils;
import cn.boop.necron.utils.RenderUtils;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public final class MldNametags {
    private float ticks;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderLiving(RenderLivingEvent.Post<EntityPlayer> event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        EntityPlayer entity = (EntityPlayer) event.entity;
        if (PlayerStats.inDungeon) return;
        if (entity != Necron.mc.thePlayer && ModConfig.nameTag) {
            double pX = entity.prevPosX + (entity.posX - entity.prevPosX) * getPartialTicks() - Necron.mc.getRenderManager().viewerPosX;
            double pY = entity.prevPosY + (entity.posY - entity.prevPosY) * getPartialTicks() - Necron.mc.getRenderManager().viewerPosY;
            double pZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * getPartialTicks() - Necron.mc.getRenderManager().viewerPosZ;
            float distance = Necron.mc.thePlayer.getDistanceToEntity(entity);

            float size = Necron.mc.thePlayer.getDistanceToEntity(entity) / 10.0f;
            if (size < 1.1f) size = 1.1f;
            pY += entity.isSneaking() ? 0.5 : 0.7;
            float scale = size * 1.8f / 100.0f;

            GL11.glPushMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            try {
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GLUtils.backupAndSetupRender();
                GL11.glDepthRange(0.0001, 1.0);
                GlStateManager.translate(pX, pY + (entity.isSneaking() ? 1.2 : 1.5), pZ);
                GlStateManager.rotate(-Necron.mc.getRenderManager().playerViewY, 0, 1, 0);
                GlStateManager.rotate(Necron.mc.getRenderManager().playerViewX, 1, 0, 0);
                GlStateManager.scale(-scale, -scale, scale);

                String displayText = entity.getDisplayName().getFormattedText();
                displayText += " §7" + (int) distance + "m";
                float textWidth = Necron.mc.fontRendererObj.getStringWidth(displayText);
                float nw = -textWidth / 2f - 4.6f;
                float width2 = nw - 2.0f * nw;

                RenderUtils.drawRoundedRect(nw, -17.0f, width2, -0.1f, 1.0f, new Color(25, 25, 25, 101).getRGB());
                RenderUtils.drawRoundedRect(nw, -2.0f, width2, -0.1f, 1.0f, new Color(152, 171, 195, 150).getRGB());
                RenderUtils.drawRoundedRect(nw, -2.0f, width2, -0.1f, 1.0f, new Color(47, 164, 228, 200).getRGB());
                Necron.mc.fontRendererObj.drawString(displayText, (int)(nw + 4.0f), -13, 0xFFFFFF);
                this.renderHead(entity);
            } finally {
                GL11.glDepthRange(0.0, 1.0);
                GLUtils.restorePreviousRenderState();
                GlStateManager.disableBlend();
                GlStateManager.popAttrib();
                GlStateManager.popMatrix();
                GL11.glPopMatrix();
            }
        }
    }

    @SubscribeEvent
    public void onRenderName(RenderLivingEvent.Specials.Pre<EntityPlayer> event) {
        if (event.entity instanceof EntityPlayer && event.entity != Necron.mc.thePlayer && ModConfig.nameTag) {
            event.setCanceled(true); // 取消原版名称标签渲染
        }
    }

    public float getPartialTicks() {
        return this.ticks;
    }

    private void renderHead(EntityPlayer player) {
        ResourceLocation texture = ((AbstractClientPlayer)player).getLocationSkin();
        Necron.mc.getTextureManager().bindTexture(texture);
        Gui.drawScaledCustomSizeModalRect((int)((float) -19.0 + 3.0f), (int)((double) (float) -55.0 + 3.5), 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
    }
}
