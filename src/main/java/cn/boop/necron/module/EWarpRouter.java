package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.JsonLoader;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import cn.boop.necron.utils.event.WaypointEventHandler;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class EWarpRouter {
    private boolean lastLeftClick = false;
    static List<Waypoint> waypointCache = new ArrayList<>();
    public static int currentWaypointIndex = 0;
    private boolean isProcessing = false;
    static boolean routeCompleted = false;
    public static boolean routeCompletedNotified = false;

    public static void loadWaypoints(String filename) {
        waypointCache = JsonLoader.loadWaypoints(Necron.WP_FILE_PATH + filename + ".json");
        currentWaypointIndex = 0;
        routeCompleted = false;
        routeCompletedNotified = false;
        if (waypointCache.isEmpty()) {
            routeCompleted = true;
            routeCompletedNotified = true;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        boolean currentLeftClick = Mouse.isButtonDown(0);

        if (ModConfig.router && PlayerStats.inSkyBlock ) {
            if (!lastLeftClick && currentLeftClick && Necron.mc.currentScreen == null) {
                if (!WaypointEventHandler.isEditingWaypoint) handleLeftClick();
            }
        }
        lastLeftClick = currentLeftClick;
    }



    private void handleLeftClick() {
        if (isProcessing) return;
        String itemID = Utils.getSkyBlockID(Necron.mc.thePlayer.inventory.getCurrentItem());
        if (itemID.equals("ASPECT_OF_THE_END") || itemID.equals("ASPECT_OF_THE_VOID")){
            if (waypointCache.isEmpty()) {
                if (!routeCompletedNotified) {
                    Utils.modMessage("Waypoints file not loaded.");
                    routeCompletedNotified = true;
                    return;
                }
            }
            if (currentWaypointIndex >= waypointCache.size() || currentWaypointIndex < 0) {
                if (!routeCompletedNotified) {
                    Utils.modMessage("Route completed.");
                    routeCompleted = true;
                    routeCompletedNotified = true;
                }
                currentWaypointIndex = Math.max(0, waypointCache.size() - 1);
                return;
            }
        }

        isProcessing = true;

        Waypoint wp = waypointCache.get(currentWaypointIndex);
        BlockPos pos = new BlockPos(wp.getX(), wp.getY(), wp.getZ());

        Vec3 closestFaceCenter = RotationUtils.getClosestExposedFaceCenter(Necron.mc.theWorld, pos, Necron.mc.thePlayer);
        if (closestFaceCenter == null) {
            Utils.modMessage("Exception at #" + wp.getId());
            isProcessing = false;
            currentWaypointIndex++;
            return;
        }

        RotationUtils.asyncAimAt(closestFaceCenter, 0.30f);
        Utils.modMessage("Rotating.");

        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Etherwarp.useEtherwarp();
            Utils.modMessage("Etherwarp.");

            currentWaypointIndex++;
            isProcessing = false;
        }).start();
    }
}
