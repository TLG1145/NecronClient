package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.JsonUtils;
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

public class EtherwarpRouter {
    private boolean lastLeftClick = false;
    public static List<Waypoint> waypointCache = new ArrayList<>();
    public static int currentWaypointIndex = -1;
    private static String currentFilename = null;
    private static String lastFilename = null;
    private static int lastWaypointIndex = -1;
    private boolean isProcessing = false;
    public static boolean routeCompleted = false;
    public static boolean routerNotified = false;
    private static BlockPos targetPosition = null;

    public static void loadWaypoints(String filename) {
        currentFilename = filename;
        waypointCache = JsonUtils.loadWaypoints(Necron.WP_FILE_PATH + filename + ".json");
        routeCompleted = false;
        routerNotified = false;

        if (lastFilename != null && lastFilename.equals(filename)) {
            currentWaypointIndex = lastWaypointIndex;
            lastFilename = null;
        } else {
            currentWaypointIndex = 0;
        }

        if (waypointCache.isEmpty()) {
            routeCompleted = true;
            routerNotified = true;
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
        if (isProcessing || Necron.mc.thePlayer.inventory.getCurrentItem() == null) return;
        String itemID = Utils.getSkyBlockID(Necron.mc.thePlayer.inventory.getCurrentItem());
        if (itemID.equals("ASPECT_OF_THE_END") || itemID.equals("ASPECT_OF_THE_VOID")){
            if (waypointCache.isEmpty()) {
                if (!routerNotified) {
                    Utils.modMessage("Waypoints file not loaded.");
                    routerNotified = true;
                    return;
                }
            }
            if (currentWaypointIndex >= waypointCache.size() || currentWaypointIndex < 0) {
                if (!routerNotified) {
                    if (ModConfig.isLoop) {
                        currentWaypointIndex = 0;
                        routeCompleted = false;
                        routerNotified = false;
                    } else {
                        Utils.modMessage("Route completed.");
                        routeCompleted = true;
                        routerNotified = true;
                        waypointCache.clear();
                        lastWaypointIndex = -1;
                        currentWaypointIndex = -1;
                    }
                }

                return;
            }

            Waypoint wp = waypointCache.get(currentWaypointIndex);

            BlockPos target = new BlockPos(wp.getX(), wp.getY(), wp.getZ());
            if (targetPosition == null || !targetPosition.equals(target)) {
                targetPosition = target;
            }

            Vec3 closestFaceCenter = RotationUtils.getClosestExposedFaceCenter(Necron.mc.theWorld, target, Necron.mc.thePlayer);
            if (closestFaceCenter == null) {
                Utils.modMessage("No exposed face found at #" + wp.getId());
                isProcessing = false;
                currentWaypointIndex++;
                targetPosition = null;
                return;
            }

            isProcessing = true;

            RotationUtils.asyncAimAt(closestFaceCenter, 0.35f);

            new Thread(() -> {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Etherwarp.useEtherwarp();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}

                if (hasReachedTarget(targetPosition)) {
                    currentWaypointIndex++;
                } else {
                    Utils.modMessage("Can't use etherwarp, check blocks on the path!");
                }

                isProcessing = false;
                targetPosition = null;
            }).start();
        } else {
            if (itemID.equals("TITANIUM_DRILL_4") ||
                    itemID.equals("DIVAN_DRILL") ||
                    itemID.equals("JUJU_SHORTBOW") ||
                    itemID.equals("TERMINATOR") ||
                    itemID.equals("DIAMOND_PICKAXE") ||
                    itemID.equals("MITHRIL_PICKAXE") ||
                    itemID.equals("REFINED_MITHRIL_PICKAXE"))
                return;

            lastFilename = currentFilename;
            lastWaypointIndex = currentWaypointIndex;

            waypointCache.clear();
            currentWaypointIndex = -1;
            targetPosition = null;
            if (!routerNotified) {
                Utils.modMessage("Holding incorrect item.");
                Utils.modMessage("§cRELOAD§7 waypoint file to continue using!");
                routerNotified = true;
            }
        }
    }

    private boolean hasReachedTarget(BlockPos target) {
        if (target == null || Necron.mc.thePlayer == null) return false;
        double distance = Necron.mc.thePlayer.getDistance(target.getX(), target.getY(), target.getZ());
        return distance <= 1.5;
    }
}
