package cn.boop.necron.utils.event;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.module.Waypoint;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class WaypointEventHandler {
    private boolean lastLeftPressed = false;
    private boolean lastRightPressed = false;
    public static boolean isEditingWaypoint = false;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (ModConfig.waypoints) {
            Waypoint.renderWaypoints(event.partialTicks);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (Necron.mc.theWorld == null || Necron.mc.thePlayer == null) return;

        EntityPlayerSP player = Necron.mc.thePlayer;
        BlockPos lookingAt = player.rayTrace(5.0d, 1.0f).getBlockPos();
        if (lookingAt == null) return;

        boolean isAltPressed = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
        if (!isAltPressed) {
            lastLeftPressed = false;
            lastRightPressed = false;
            isEditingWaypoint = false;
            return;
        }

        isEditingWaypoint = true;
        boolean leftPressed = Mouse.isButtonDown(0);
        boolean rightPressed = Mouse.isButtonDown(1);
        if (rightPressed && !lastRightPressed) {
            int x = lookingAt.getX();
            int y = lookingAt.getY();
            int z = lookingAt.getZ();
            Waypoint.addWaypoint(x, y, z);
        }

        if (leftPressed && !lastLeftPressed) {
            Waypoint.removeWaypoint(lookingAt);
        }

        lastLeftPressed = leftPressed;
        lastRightPressed = rightPressed;
    }
}
