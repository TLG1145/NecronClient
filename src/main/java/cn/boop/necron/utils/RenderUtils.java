package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtils {
    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void drawRoundedRect(float x0, float y0, float x1, float y1, float radius, int color) {
        if (x0 == x1 || y0 == y1) {
            return;
        }
        float f2 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f3 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(color & 0xFF) / 255.0f;
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(5);
        GL11.glVertex2f(x0 + radius, y0);
        GL11.glVertex2f(x0 + radius, y1);
        GL11.glVertex2f(x1 - radius, y0);
        GL11.glVertex2f(x1 - radius, y1);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x0, y0 + radius);
        GL11.glVertex2f(x0 + radius, y0 + radius);
        GL11.glVertex2f(x0, y1 - radius);
        GL11.glVertex2f(x0 + radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(x1, y0 + radius);
        GL11.glVertex2f(x1 - radius, y0 + radius);
        GL11.glVertex2f(x1, y1 - radius);
        GL11.glVertex2f(x1 - radius, y1 - radius);
        GL11.glEnd();
        GL11.glBegin(6);
        float f6 = x1 - radius;
        float f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);
        int j;
        for (j = 0; j <= 18; ++j) {
            float f8 = (float)j * 5.0f;
            GL11.glVertex2f((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f8))), (float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f8))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y0 + radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= 18; ++j) {
            float f9 = (float)j * 5.0f;
            GL11.glVertex2f((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f9))), (float)((double)f7 - (double)radius * Math.sin(Math.toRadians(f9))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x0 + radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= 18; ++j) {
            float f10 = (float)j * 5.0f;
            GL11.glVertex2f((float)((double)f6 - (double)radius * Math.cos(Math.toRadians(f10))), (float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f10))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f6 = x1 - radius;
        f7 = y1 - radius;
        GL11.glVertex2f(f6, f7);
        for (j = 0; j <= 18; ++j) {
            float f11 = (float)j * 5.0f;
            GL11.glVertex2f((float)((double)f6 + (double)radius * Math.cos(Math.toRadians(f11))), (float)((double)f7 + (double)radius * Math.sin(Math.toRadians(f11))));
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    public static void drawBorderedRect(float x, float y, float width, float height,
                                        float lineWidth, int borderColor) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(lineWidth);

        glColor(borderColor);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawBorderedRoundedRect(float x, float y, float width, float height,
                                               float radius, float borderWidth, int borderColor) {
        if (width <= 0 || height <= 0 || radius <= 0) return;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(borderWidth);
        glColor(borderColor);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i <= 9; i++) {
            float angle = (float) (Math.PI * 1.5 + (Math.PI * i / 18));
            GL11.glVertex2f(
                    (float)(x + width - radius + Math.cos(angle) * radius),
                    (float)(y + radius + Math.sin(angle) * radius)
            );
        }

        for (int i = 0; i <= 9; i++) {
            float angle = (float) (Math.PI * 0.0 + (Math.PI * i / 18));
            GL11.glVertex2f(
                    (float)(x + width - radius + Math.cos(angle) * radius),
                    (float)(y + height - radius + Math.sin(angle) * radius)
            );
        }

        for (int i = 0; i <= 9; i++) {
            float angle = (float) (Math.PI * 0.5 + (Math.PI * i / 18));
            GL11.glVertex2f(
                    (float)(x + radius + Math.cos(angle) * radius),
                    (float)(y + height - radius + Math.sin(angle) * radius)
            );
        }

        for (int i = 0; i <= 9; i++) {
            float angle = (float) (Math.PI + (Math.PI * i / 18));
            GL11.glVertex2f(
                    (float)(x + radius + Math.cos(angle) * radius),
                    (float)(y + radius + Math.sin(angle) * radius)
            );
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawOutlinedBlockESP(double x, double y, double z, Color color, float lineWidth, float partialTicks) {
        Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
        if (viewer == null) return;

        double viewerX = viewer.prevPosX + (viewer.posX - viewer.prevPosX) * partialTicks;
        double viewerY = viewer.prevPosY + (viewer.posY - viewer.prevPosY) * partialTicks;
        double viewerZ = viewer.prevPosZ + (viewer.posZ - viewer.prevPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        //GlStateManager.depthMask(false);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        RenderUtils.drawOutlinedBoundingBox(new AxisAlignedBB(
                x - viewerX, y - viewerY, z - viewerZ,
                x + 1 - viewerX, y + 1 - viewerY, z + 1 - viewerZ
        ));
        //GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void draw3DLine(double x, double y, double z, double x1, double y1, double z1, float red, float green, float blue, float alpha, float lineWidth) {
        double viewerX = Necron.mc.getRenderManager().viewerPosX;
        double viewerY = Necron.mc.getRenderManager().viewerPosY;
        double viewerZ = Necron.mc.getRenderManager().viewerPosZ;
        x -= viewerX;
        x1 -= viewerX;
        y -= viewerY;
        y1 -= viewerY;
        z -= viewerZ;
        z1 -= viewerZ;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST); // 关闭深度测试，实现穿墙效果
        GL11.glDisable(GL11.GL_TEXTURE_2D); // 禁用纹理
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x1, y1, z1);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D); // 恢复纹理
        GL11.glEnable(GL11.GL_DEPTH_TEST); // 恢复深度测试
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
