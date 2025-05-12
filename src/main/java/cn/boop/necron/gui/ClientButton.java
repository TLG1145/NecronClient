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
    private static final int CORNER_RADIUS = 4;
    private static final float HOVER_IN_SPEED = 0.4f;
    private static final float HOVER_OUT_SPEED = 0.15f;

    public ClientButton(int id, int x, int y, int widthIn, int heightIn ,String text) {
        super(id, x, y, widthIn, heightIn, text);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) return;
        boolean isHovered = mouseX >= this.xPosition &&
                          mouseY >= this.yPosition &&
                          mouseX < this.xPosition + this.width &&
                          mouseY < this.yPosition + this.height;

        hoverAlpha += (isHovered ? HOVER_IN_SPEED : -HOVER_OUT_SPEED);
        hoverAlpha = MathHelper.clamp_float(hoverAlpha, 0.0F, 0.5F);
        if (!isHovered && hoverAlpha < 0.005F) hoverAlpha = 0.0F;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        int baseColor = new Color(0x33AAAAAA, true).getRGB(); // 基础背景色
        int borderColor = new Color(0x73969696, true).getRGB(); // 边框颜色

        RenderUtils.drawRoundedRect(xPosition, yPosition,
            xPosition + width, yPosition + height, CORNER_RADIUS, baseColor);
        RenderUtils.drawBorderedRoundedRect(xPosition, yPosition,
                width, height, CORNER_RADIUS, 1.5f, // 边框宽度
                isHovered ? new Color(0x88AAAAAA, true).getRGB() : borderColor // 悬停时边框变亮
        );

        if (isHovered && hoverAlpha > 0.01f) {
            int alpha = (int)(hoverAlpha * 0.6f * 255);
            int overlayColor = (alpha << 24) | 0x00FFFFFF; // 修正颜色格式
            RenderUtils.drawRoundedRect(xPosition, yPosition,
                    xPosition + width, yPosition + height,
                    CORNER_RADIUS, overlayColor);
        }

        int textAlpha = (int)((0.7f + hoverAlpha*0.3f)*255);
        this.drawCenteredString(mc.fontRendererObj, this.displayString,
            this.xPosition + this.width/2,
            this.yPosition + (this.height - 8)/2,
         (textAlpha << 24) | 0x00FFFFFF);
    }
}
