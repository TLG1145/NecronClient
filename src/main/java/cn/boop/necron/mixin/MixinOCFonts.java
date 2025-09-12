package cn.boop.necron.mixin;

import cc.polyfrost.oneconfig.renderer.font.Font;
import cc.polyfrost.oneconfig.renderer.font.Fonts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Fonts.class)
public class MixinOCFonts {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Ljava/lang/String;Ljava/lang/String;)Lcc/polyfrost/oneconfig/renderer/font/Font;"))
    private static Font redirectFontConstructor(String name, String path) {
        String fontPath = necronClient$mapToFontPath(path);
        return new Font(name, fontPath);
    }

    @Unique
    private static String necronClient$mapToFontPath(String path) {
        switch (path) {
            case "/assets/oneconfig/font/Bold.otf":
                return "/assets/necron/fonts/Bold.ttf";
            case "/assets/oneconfig/font/SemiBold.otf":
                return "/assets/necron/fonts/SemiBold.ttf";
            case "/assets/oneconfig/font/Medium.otf":
                return "/assets/necron/fonts/Medium.ttf";
            case "/assets/oneconfig/font/Regular.otf":
                return "/assets/necron/fonts/Regular.ttf";
            default:
                return path;
        }
    }
}
