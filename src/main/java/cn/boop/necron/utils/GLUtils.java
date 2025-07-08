package cn.boop.necron.utils;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtils {
    public static boolean lightingEnabled, depthTestEnabled;

    public static void backupAndSetupRender() {
        depthTestEnabled = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        lightingEnabled = GL11.glGetBoolean(GL11.GL_LIGHTING);

        GlStateManager.depthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    public static void restorePreviousRenderState() {
        GlStateManager.depthMask(true);
        if (depthTestEnabled) GlStateManager.enableDepth();
        else GlStateManager.disableDepth();
        if (lightingEnabled) GlStateManager.enableLighting();
         else GlStateManager.disableLighting();

        GlStateManager.disableBlend();
    }
}
