package cn.boop.necron.gui;

import cn.boop.necron.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ClientButton extends GuiButton {
    private float hoverAlpha = 0f;
    private static final int CORNER_RADIUS = 6;
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
        RenderUtils.drawRoundedRect(xPosition, yPosition,
            xPosition + width, yPosition + height, CORNER_RADIUS, new Color(0x80111111, true).getRGB());
        //0x60111111
        // 绘制白色遮罩
        if (isHovered && hoverAlpha > 0.01f) {
            int alpha = (int)(hoverAlpha * 0.6f * 255);
            int overlayColor = (alpha << 24) | 0x00FFFFFF; // 修正颜色格式
            RenderUtils.drawRoundedRect(xPosition, yPosition,
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
}
