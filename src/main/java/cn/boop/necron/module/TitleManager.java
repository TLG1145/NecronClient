package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class TitleManager {
    @SubscribeEvent
    public void ClientTickEvent(TickEvent.ClientTickEvent event) {
        if (ModConfig.customTitle) {
            try  {
                Display.setTitle("Minecraft 1.8.9" + " - Spongepowered Mixin" + " v" + Necron.VERSION);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
