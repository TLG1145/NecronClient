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

    public static void revertAllCaps() {
        for (Integer cap : glCapMap.keySet()) {
            GLUtils.revertGLCap(cap);
        }
    }

    public static void backupAndSetupRender() {
        depthTestEnabled = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        lightingEnabled = GL11.glGetBoolean(GL11.GL_LIGHTING);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        //System.out.println("Depth Test Enabled (After Disable): " + GL11.glIsEnabled(GL11.GL_DEPTH_TEST));

        if (lightingEnabled) GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    public static void restorePreviousRenderState() {
        if (depthTestEnabled) GL11.glEnable(GL11.GL_DEPTH_TEST);
        if (lightingEnabled) GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }
}
