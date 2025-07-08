package cn.boop.necron.module;

import java.awt.Color;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.GLUtils;
import cn.boop.necron.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public final class Nametags {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!ModConfig.nametags) return;

        Minecraft mc = Necron.mc;
        Entity viewer = mc.getRenderViewEntity();
        if (viewer == null) return;

        float partialTicks = event.partialTicks;
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        for (EntityPlayer entity : mc.theWorld.playerEntities) {
            if (entity == null || entity == mc.thePlayer|| entity.isInvisible()) continue;
            if (shouldFilter(entity)) continue;

            double pX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double pY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
            double pZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;

            double distance = viewer.getDistance(pX, pY, pZ);
            if (entity.isDead || !entity.isEntityAlive()) continue;
            renderNametag(entity, pX, pY, pZ, (float) distance, partialTicks);
        }
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
    }

    private void renderNametag(EntityPlayer entity, double x, double y, double z, float distance, float partialTicks) {
        Entity viewer = Necron.mc.getRenderViewEntity();

        float size = Necron.mc.thePlayer.getDistanceToEntity(entity) / 10.0f;
        if (size < 1.1f) size = 1.1f;
        y += entity.isSneaking() ? 0.5 : 0.7;
        float scale = size * 2f / 100.0f;

        GL11.glPushMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(false);
        GLUtils.backupAndSetupRender();

        double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        GlStateManager.translate(x - viewerX, y - viewerY + entity.getEyeHeight() - 0.1F, z - viewerZ);
        GL11.glRotatef(-Necron.mc.getRenderManager().playerViewY, 0.0f, 2.0f, 0.0f);
        GL11.glRotatef(Necron.mc.getRenderManager().playerViewX, 2.0f, 0.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);

        String lol = entity.getDisplayName().getFormattedText();
        lol += " §7" + (int) distance + "m§r";
        float width = Necron.mc.fontRendererObj.getStringWidth(lol);
        float nw = -width / 2f - 4.6f;
        float width2 = nw - 2.0f * nw;

        RenderUtils.drawRoundedRect(nw, -17.0f, width2, -1.0f, 3.0f, new Color(25, 25, 25, 114).getRGB());
        Necron.mc.fontRendererObj.drawString(lol, (int)(nw + 4.0f), -13, 0xFFFFFF, true);

        GlStateManager.depthMask(true);
        GLUtils.restorePreviousRenderState();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void onRenderName(RenderLivingEvent.Specials.Pre<EntityPlayer> event) {
        if (event.entity instanceof EntityPlayer && event.entity != Necron.mc.thePlayer && ModConfig.nametags) {
            event.setCanceled(true);
        }
    }

    private boolean shouldFilter(EntityPlayer entity) {
        String dgName = entity.getName().toUpperCase();
        return dgName.contains("CRYPT DREADLORD") ||
                dgName.contains("DECOY") ||
                dgName.contains("ZOMBIE COMMANDER") ||
                dgName.contains("SKELETOR PRIME") ||
                dgName.contains("CRYPT SOULEATER") ||
                dgName.contains("DIAMOND GUY") ||
                dgName.contains("KING MIDAS") ||
                dgName.contains("LOST ADVENTURER") ||
                dgName.contains("SHADOW ASSASSIN") ||
                dgName.contains("SKULL") ||
                dgName.contains("REAPER") ||
                dgName.contains("LEECH") ||
                dgName.contains("VADER") ||
                dgName.contains("MR. DEAD") ||
                dgName.contains("FROST") ||
                dgName.contains("REVOKER") ||
                dgName.contains("PUTRID") ||
                dgName.contains("FREAK") ||
                dgName.contains("PSYCHO") ||
                dgName.contains("PARASITE") ||
                dgName.contains("CANNIBAL") ||
                dgName.contains("TEAR") ||
                dgName.contains("FLAMER") ||
                dgName.contains("OOZE") ||
                dgName.contains("WALKER") ||
                dgName.contains("MUTE") ||
                dgName.contains("BONZO") ||
                dgName.contains("SCARF") ||
                dgName.contains("SPIRIT BEAR") ||
                dgName.contains("CRYPT UNDEAD");
    }
}