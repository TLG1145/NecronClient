package cn.boop.necron.module.impl;

import cn.boop.necron.utils.ScoreboardUtils;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static cn.boop.necron.config.impl.SlayerOptionsImpl.killTime;

public class PlayerStats {
    private static long startTime = 0L;
    public static boolean inCombat = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        updateCombatState();
    }

    public static void updateCombatState() {
        boolean currentlyInCombat = ScoreboardUtils.scoreboardContains("Slay the boss!");

        if (currentlyInCombat && !inCombat) {
            inCombat = true;
            startTime = System.currentTimeMillis();
        } else if (!currentlyInCombat && inCombat) {
            inCombat = false;
            if (killTime) Utils.modMessage("Slayer took §6" + getKillTime() + "§7 to kill!");
            startTime = 0L;
        }
    }

    public static String getKillTime() {
        long durationMillis = System.currentTimeMillis() - startTime;
        double seconds = durationMillis / 1000.0;
        return String.format("%.2fs", seconds);
    }
}
