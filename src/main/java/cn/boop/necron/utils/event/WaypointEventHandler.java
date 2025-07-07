package cn.boop.necron.utils.event;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.module.Waypoint;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
        if (!ModConfig.waypoints) {
            isEditingWaypoint = false;
            return;
        }

        BlockPos lookingAt = Necron.mc.thePlayer.rayTrace(5.0d, 1.0f).getBlockPos();
        if (lookingAt == null) return;

        IBlockState state = Necron.mc.theWorld.getBlockState(lookingAt);
        if (state == null) {
            resetEditingState();
            return;
        }
        
        Block block = state.getBlock();
        if (block.isAir(Necron.mc.theWorld, lookingAt)) return;

        boolean isAltPressed = Keyboard.isKeyDown(Keyboard.KEY_LMENU);
        if (!isAltPressed) {
            resetEditingState();
            return;
        }

        isEditingWaypoint = true;
        handleMouseInput(lookingAt);
    }

    private void resetEditingState() {
        lastLeftPressed = false;
        lastRightPressed = false;
        isEditingWaypoint = false;
    }

    private void handleMouseInput(BlockPos pos) {
        boolean leftPressed = Mouse.isButtonDown(0);
        boolean rightPressed = Mouse.isButtonDown(1);

        if (rightPressed && !lastRightPressed) {
            Waypoint.addWaypoint(pos);
            Waypoint.onWaypointsChanged();
        }

        if (leftPressed && !lastLeftPressed) {
            Waypoint.removeWaypoint(pos);
            Waypoint.onWaypointsChanged();
        }

        lastLeftPressed = leftPressed;
        lastRightPressed = rightPressed;
    }
}
