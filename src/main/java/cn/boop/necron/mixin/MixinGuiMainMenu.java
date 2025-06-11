package cn.boop.necron.mixin;

import cn.boop.necron.gui.MainMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen{

    @Inject(method = "initGui", at = @At("RETURN"))
    public void onInitGui(CallbackInfo ci) {
        this.mc.displayGuiScreen(new MainMenu());
    }
}
