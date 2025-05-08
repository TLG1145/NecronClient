package cn.boop.necron.gui;

import cn.boop.necron.config.ModConfig;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;

public final class MainMenu extends GuiScreen {
    private static final ResourceLocation BACKGROUND_TEXTURE =
            new ResourceLocation("necron", "gui/bg1.png");
    private static final ResourceLocation MOD_ICON =
            new ResourceLocation("necron", "gui/icon.png");
    private float mouseXOffset, mouseYOffset;
    private static final float MAX_OFFSET = 0.02f;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float centerX = this.width / 2.0f;
        float centerY = this.height / 2.0f;
        mouseXOffset = (mouseX - centerX) / centerX * MAX_OFFSET;
        mouseYOffset = (mouseY - centerY) / centerY * MAX_OFFSET;

        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        drawBackgroundQuad();

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(MOD_ICON);
        int xPos = (this.width - 256) / 2;
        int yPos = (int)((this.height / 2 ) - 125);
        drawModalRectWithCustomSizedTexture(
                xPos, yPos,
                0, 0,
                256, 128,
                256, 128
        );
        GlStateManager.popMatrix();

        // 绘制按钮和文字
        super.drawScreen(mouseX, mouseY, partialTicks);

        String s1 = "Minecraft 1.8.9";
        String s2 = "Cheaters get banned!";
        this.mc.fontRendererObj.drawStringWithShadow(s1, 2, this.height - 10, 0xFFFFFF);
        this.mc.fontRendererObj.drawStringWithShadow(s2, this.width - fontRendererObj.getStringWidth(s2) - 2, this.height - 10, 0xFFFFFF);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new ClientButton(0, this.width / 2 - 100, this.height / 2 - 28, 200, 20, "Singleplayer"));
        this.buttonList.add(new ClientButton(1, this.width / 2 - 100, this.height / 2 - 4, 200, 20, "Multiplayer"));
        this.buttonList.add(new ClientButton(2, this.width / 2 - 100, this.height / 2 + 20, 200, 20, "Mods"));
        this.buttonList.add(new ClientButton(3, this.width / 2 - 100, this.height / 2 + 60, 98, 20, "Options"));
        this.buttonList.add(new ClientButton(4, this.width / 2 + 2, this.height / 2 + 60, 98, 20, "Quit"));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiModList(this));
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 4: {
                this.mc.shutdown();
                break;
            }
        }
    }

    private void drawBackgroundQuad() {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer worldrenderer = tess.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        float uMin = 0.0f + mouseXOffset;
        float uMax = 1.0f + mouseXOffset;
        float vMin = 1.0f + mouseYOffset;
        float vMax = 0.0f + mouseYOffset;

        worldrenderer.pos(0, this.height, 0.0D).tex(uMin, vMin).endVertex();
        worldrenderer.pos(this.width, this.height, 0.0D).tex(uMax, vMin).endVertex();
        worldrenderer.pos(this.width, 0, 0.0D).tex(uMax, vMax).endVertex();
        worldrenderer.pos(0, 0, 0.0D).tex(uMin, vMax).endVertex();
        tess.draw();
    }
}
