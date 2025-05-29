package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.JsonLoader;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class EWarpRouter {
    private boolean lastLeftClick = false;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static List<Waypoint> waypointCache = new ArrayList<>();
    private static int currentWaypointIndex = 0;
    private boolean isProcessing = false;
    private static boolean routeCompleted = false;
    private static boolean routeCompletedNotified = false;

    public static void loadWaypoints(String filename) {
        String currentFile = Necron.WP_FILE_PATH + filename + ".json";
        waypointCache = JsonLoader.loadWaypoints(currentFile);
        currentWaypointIndex = 0;
        routeCompleted = false;
        routeCompletedNotified = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        boolean currentLeftClick = Mouse.isButtonDown(0);

        if (ModConfig.router && PlayerStats.inSkyBlock) {
            if (!lastLeftClick && currentLeftClick) {
                handleLeftClick();
            }
        }
        lastLeftClick = currentLeftClick;
    }

    private void handleLeftClick() {
        if (isProcessing) return;
        if (waypointCache.isEmpty()) {
            Utils.modMessage("No waypoints loaded.");
            return;
        }
        if (currentWaypointIndex >= waypointCache.size()) {
            if (!routeCompletedNotified) {
                Utils.modMessage("Route completed.");
                routeCompleted = true;
                routeCompletedNotified = true;
            }
            return;
        }

        isProcessing = true;

        Waypoint wp = waypointCache.get(currentWaypointIndex);
        BlockPos pos = new BlockPos(wp.getX(), wp.getY(), wp.getZ());

        Vec3 closestFaceCenter = RotationUtils.getClosestExposedFaceCenter(mc.theWorld, pos, mc.thePlayer);
        if (closestFaceCenter == null) {
            Utils.modMessage("No exposed face found for waypoint " + wp.getId());
            isProcessing = false;
            currentWaypointIndex++;
            return;
        }

        RotationUtils.asyncAimAt(closestFaceCenter, 0.35f);
        Utils.modMessage("Rotating.");

        new Thread(() -> {
            try {
                Thread.sleep(350);
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
