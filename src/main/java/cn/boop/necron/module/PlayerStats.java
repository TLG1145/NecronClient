package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.ScoreboardUtils;
import cn.boop.necron.utils.TabUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class PlayerStats {
    public static boolean inSkyBlock = false;
    public static boolean inDungeon = false;
    private static final HashMap<String, Island> ISLAND_MAPPING = createIslandMapping();
    public static Island currentIsland = null;
    public static Floor floor = null;

    public enum Island {
        PRIVATE_ISLAND("Private Island"),
        HUB("Hub"),
        DARK_AUCTION("Dark Auction"),
        SPIDERS_DEN("Spider's Den"),
        CRIMSON_ISLE("Crimson Isle"),
        KUUDRAS_HOLLOW("Kuudra's Hollow"),
        THE_END("The End"),
        THE_PARK("The Park"),
        GOLD_MINE("Gold Mine"),
        DEEP_CAVERNS("Deep Caverns"),
        DWARVEN_MINES("Dwarven Mines"),
        FARMING_ISLANDS("The Farming Islands"),
        DUNGEON_HUB("Dungeon Hub"),
        CRYSTAL_HOLLOWS("Crystal Hollows"),
        JERRYS_WORKSHOP("Jerry's Workshop"),
        BACKWATER_BAYOU("Backwater Bayou"),
        MINESHAFT("Mineshaft"),
        THE_RIFT("The Rift"),
        GARDEN("Garden");
        //⏣ <- Zone prefix

        private final String tabName;

        Island(String tabName) {
            this.tabName = tabName;
        }
        public String getDisplayName() {
            return tabName;
        }
    }

    public enum Floor {
        ENTERANCE("(E)"),
        FLOOR_1("(F1)"),
        FLOOR_2("(F2)"),
        FLOOR_3("(F3)"),
        FLOOR_4("(F4)"),
        FLOOR_5("(F5)"),
        FLOOR_6("(F6)"),
        FLOOR_7("(F7)"),
        MASTER_1("(M1)"),
        MASTER_2("(M2)"),
        MASTER_3("(M3)"),
        MASTER_4("(M4)"),
        MASTER_5("(M5)"),
        MASTER_6("(M6)"),
        MASTER_7("(M7)");

        public final String name;

        Floor(String name) {
            this.name = name;
        }
    }

    private static HashMap<String, Island> createIslandMapping() {
        HashMap<String, Island> map = new HashMap<>();
        for (Island island : Island.values()) {
            map.put(island.getDisplayName(), island);
        }
        return map;
    }

    private int ticks = 0;
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote) {
            ticks = 19;
            updateWorldStates();
            detectCurrentIsland();
            updateFloor();
        }
    }

    private void updateWorldStates() {
        if (Necron.mc.thePlayer != null && Necron.mc.theWorld != null) {
            ScoreObjective scoreboardObj = Necron.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            inSkyBlock = scoreboardObj != null &&
                    Utils.removeFormatting(ScoreboardUtils.getScoreboardTitle()).contains("SKYBLOCK");
            inDungeon = inSkyBlock && ScoreboardUtils.scoreboardContains("The Catacombs");
        }
    }

    private void detectCurrentIsland() {
        currentIsland = null;
        if (!inSkyBlock) return;

        for (String tabEntry : TabUtils.getTabList()) {
            String cleaned = Utils.removeFormatting(tabEntry).trim();
            if (cleaned.startsWith("Area: ")) {
                String areaName = cleaned.substring(6).replaceAll("\\s+", " ");
                currentIsland = ISLAND_MAPPING.get(areaName);
                if (currentIsland != null) break;
            }
        }
    }

    public static void updateFloor() {
        String cataLine = ScoreboardUtils.getLineThatContains("The Catacombs");
        if (cataLine != null) {
            for(Floor floorOption : Floor.values()) {
                if(cataLine.contains(floorOption.name)) {
                    floor = floorOption;
                }
            }
        }
    }

    public static String getCurrentIslandName() {
        return currentIsland != null ? currentIsland.getDisplayName() : "N/A";
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (ticks % 20 == 0) {
            updateWorldStates();
            detectCurrentIsland();
            updateFloor();
            ticks = 0;
        }
        ticks++;
    }
}
