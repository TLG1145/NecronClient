package cn.boop.necron.utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class GLUtils {
    private static final Map<Integer, Boolean> glCapMap = new HashMap<Integer, Boolean>();
    public static boolean lightingEnabled, depthTestEnabled;

    public static void setGLCap(int cap, boolean flag) {
        glCapMap.put(cap, GL11.glGetBoolean(cap));
        if (flag) {
            GL11.glEnable(cap);
        } else {
            GL11.glDisable(cap);
        }
    }

    private static void revertGLCap(int cap) {
        Boolean origCap = glCapMap.get(cap);
        if (origCap != null) {
            if (origCap.booleanValue()) {
                GL11.glEnable(cap);
            } else {
                GL11.glDisable(cap);
            }
        }
    }

    public static void glEnable(int cap) {
        GLUtils.setGLCap(cap, true);
    }

    public static void glDisable(int cap) {
        GLUtils.setGLCap(cap, false);
    }

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
