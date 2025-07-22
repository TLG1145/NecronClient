package cn.boop.necron.mixin;

import cn.boop.necron.Necron;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin({FMLHandshakeMessage.ModList.class})
public abstract class MixinModList {

    @Shadow(remap = false)
    private Map<String, String> modTags;

    @Inject(method = {"<init>(Ljava/util/List;)V"}, at = @At("RETURN"), remap = false)
    private void removeMod(List<ModContainer> modList, CallbackInfo ci) {
        if (!Necron.mc.isSingleplayer()) {
            this.modTags.remove(Necron.MODID);
        }
    }
}
