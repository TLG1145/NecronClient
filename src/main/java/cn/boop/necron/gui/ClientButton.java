package cn.boop.necron.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ClientButton extends GuiButton {
    private float hoverAlpha = 0f;
    private static final int CORNER_RADIUS = 5;
    private static final float HOVER_IN_SPEED = 0.4f;
    private static final float HOVER_OUT_SPEED = 0.15f;

    public ClientButton(int id, int x, int y, int widthIn, int heightIn ,String text) {
        super(id, x, y, widthIn, heightIn, text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) return;

        // 鼠标悬停检测
        boolean isHovered = mouseX >= this.xPosition &&
                          mouseY >= this.yPosition &&
                          mouseX < this.xPosition + this.width &&
                          mouseY < this.yPosition + this.height;

        // 透明度动画
        hoverAlpha += (isHovered ? HOVER_IN_SPEED : -HOVER_OUT_SPEED);
        hoverAlpha = MathHelper.clamp_float(hoverAlpha, 0.0F, 0.5F);
        if (!isHovered && hoverAlpha < 0.005F) hoverAlpha = 0.0F;

        // 启用抗锯齿
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        // 绘制背景
        drawRoundedRect(xPosition, yPosition,
            xPosition + width, yPosition + height, CORNER_RADIUS, 0x60111111);

        // 绘制白色遮罩
        if (isHovered && hoverAlpha > 0.01f) {
            int alpha = (int)(hoverAlpha * 0.6f * 255);
            int overlayColor = (alpha << 24) | 0x00FFFFFF; // 修正颜色格式
            drawRoundedRect(xPosition, yPosition,
                    xPosition + width, yPosition + height,
                    CORNER_RADIUS, overlayColor);
        }

        // 绘制文字
        int textAlpha = (int)((0.7f + hoverAlpha*0.3f)*255);
        this.drawCenteredString(mc.fontRendererObj, this.displayString,
            this.xPosition + this.width/2,
            this.yPosition + (this.height - 8)/2,
         (textAlpha << 24) | 0x00FFFFFF);
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
        int j = 0;
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
}
