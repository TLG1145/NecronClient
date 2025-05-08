package cn.boop.necron.utils.event;

import cn.boop.necron.Necron;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class MouseAimHandler {
    public static boolean isLeftMouseDown = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Necron.mc.theWorld == null || Necron.mc.thePlayer == null) return;
        if (Necron.mc.currentScreen != null) return;
        isLeftMouseDown = Mouse.isButtonDown(0);
    }

    public static boolean isLeftMouseDown() {
        return isLeftMouseDown;
    }
}
