package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.JsonLoader;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
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
    private static int currentWaypointIndex = 0;
    private boolean isProcessing = false;
    static boolean routeCompleted = false;
    public static boolean routeCompletedNotified = false;

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
            if (!lastLeftClick && currentLeftClick && Necron.mc.currentScreen == null) {
                handleLeftClick();
            }
        }
        lastLeftClick = currentLeftClick;
    }

    private void handleLeftClick() {
        if (isProcessing) return;
        if (waypointCache.isEmpty()) {
            Utils.modMessage("Waypoints file not loaded.");
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

        Vec3 closestFaceCenter = RotationUtils.getClosestExposedFaceCenter(Necron.mc.theWorld, pos, Necron.mc.thePlayer);
        if (closestFaceCenter == null) {
            Utils.modMessage("Unable to etherwarp to waypoint #" + wp.getId());
            isProcessing = false;
            currentWaypointIndex++;
            return;
        }

        KeyBinding.setKeyBindState(Necron.mc.gameSettings.keyBindSneak.getKeyCode(), true);
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
