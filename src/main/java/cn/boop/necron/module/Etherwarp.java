package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.PlayerUtils;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class Etherwarp {
    private boolean lastLeftClick = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        boolean currentLeftClick = Mouse.isButtonDown(0);
        if (ModConfig.etherwarp && PlayerStats.inSkyBlock && EWarpRouter.routeCompletedNotified) {
            if (!lastLeftClick && currentLeftClick) {
                useEtherwarp();
            }
        }
        else if (ModConfig.etherwarp && PlayerStats.inSkyBlock && Necron.mc.currentScreen == null) {
            if (EWarpRouter.waypointCache.isEmpty() || EWarpRouter.routeCompleted) {
                if (!lastLeftClick && currentLeftClick) {
                    useEtherwarp();
                }
            }
        }

        lastLeftClick = currentLeftClick;
    }

    public static void useEtherwarp() {
        if (Necron.mc.currentScreen != null) return;
        String itemID = Utils.getSkyBlockID(Necron.mc.thePlayer.inventory.getCurrentItem());
        if (itemID.equals("ASPECT_OF_THE_END") || itemID.equals("ASPECT_OF_THE_VOID")) {
            new Thread(() -> {
                try {
                    PlayerUtils.setSneak(true);
                    Thread.sleep(100);
                    Necron.mc.playerController.sendUseItem(Necron.mc.thePlayer, Necron.mc.theWorld, Necron.mc.thePlayer.inventory.getCurrentItem());
                    Thread.sleep(50);
                    PlayerUtils.setSneak(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
