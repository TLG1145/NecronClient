package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import cn.boop.necron.module.PlayerStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.regex.*;

public class DungeonUtils {
    public static ArrayList<EntityPlayer> teammates = new ArrayList<>();
    public static Map<String, DungeonPlayer> dungeonPlayers = new HashMap<>();

    public static class DungeonPlayer {
        private final String playerName;
        private DungeonClass playerClass;
        private int classLevel;

        public DungeonPlayer(String playerName) {
            this.playerName = playerName;
        }

        public DungeonClass getPlayerClass() { return playerClass; }
        public int getClassLevel() { return classLevel; }

        public void setPlayerClass(DungeonClass playerClass) { this.playerClass = playerClass; }
        public void setClassLevel(int classLevel) { this.classLevel = classLevel; }

        @Override
        public String toString() {
            return String.format("DungeonPlayer{name='%s', class=%s, level=%d}",
                    playerName, playerClass, classLevel);
        }
    }

    public static void reset() {
        teammates.clear();
        dungeonPlayers.clear();
    }

    public enum DungeonClass {
        Archer("Archer", "§c"),
        Berserk("Berserk", "§6"),
        Healer("Healer", "§d"),
        Mage("Mage", "§b"),
        Tank("Tank", "§2");

        private final String className;
        private final String color;

        DungeonClass(String className, String color) {
            this.className = className;
            this.color = color;
        }

        public String getClassName() {
            return className;
        }

        public String getColor() {
            return color;
        }

        public static DungeonClass fromName(String name) {
            for (DungeonClass clazz : values()) {
                if (clazz.className.equals(name)) {
                    return clazz;
                }
            }
            return null;
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!PlayerStats.inDungeon) return;

        List<String> tabList = getTabList();
        if (tabList != null) {
            if (teammates.isEmpty()) updateTeammates(tabList);
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!PlayerStats.inDungeon) return;

        String message = StringUtils.stripControlCodes(event.message.getFormattedText()).trim();
        if ("Starting in 4 seconds.".equals(message)) {
            reset();
        } else if ("[NPC] Mort: Here, I found this map when I first entered the dungeon.".equals(message)) {
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    updateDungeonPlayers();
                }
                catch (Exception e) {
                    Necron.LOGGER.error("Error in DungeonUtils.onChat: ", e);
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        reset();
    }

    private void updateDungeonPlayers() {
        try {
            List<String> tabList = getTabList();
            if (tabList == null) return;

            parseTabListPlayers(tabList);
            Necron.LOGGER.info("Dungeon players collected: {}", dungeonPlayers.size());
            for (Map.Entry<String, DungeonPlayer> entry : dungeonPlayers.entrySet()) {
                Necron.LOGGER.info("Player: {}", entry.getValue());
            }

            showCurrentClassInfo();
        } catch (Exception e) {
            Necron.LOGGER.error("Error updating dungeon players: ", e);
        }
    }

    private void parseTabListPlayers(List<String> tabList) {
        dungeonPlayers.clear();

        Pattern playerPattern = Pattern.compile("^\\[(\\d+)]\\s+([^()]+?)\\s*\\(([A-Za-z]+)\\s+([0IVXL]+)\\)$");

        for (int i = 0; i < 4; i++) {
            String line = tabList.get(1 + i * 4);
            String cleanLine = StringUtils.stripControlCodes(line).trim();

            Matcher matcher = playerPattern.matcher(cleanLine);
            if (matcher.matches()) {
                String playerName = matcher.group(2);
                String className = matcher.group(3);
                String romanLevel = matcher.group(4);

                playerName = Utils.clearMcUsername(playerName);

                if (playerName.isEmpty()) continue;

                Necron.LOGGER.info("Parsed player: {} | Class: {} | Level: {}",
                        playerName, className, romanLevel);

                DungeonPlayer player = new DungeonPlayer(playerName);

                if (className != null && romanLevel != null) {
                    DungeonClass dungeonClass = DungeonClass.fromName(className);
                    int classLevel = Utils.romanToInt(romanLevel);

                    if (dungeonClass != null) {
                        player.setPlayerClass(dungeonClass);
                        player.setClassLevel(classLevel);
                        Necron.LOGGER.debug("Updated player class: {} -> {} {}",
                                playerName, dungeonClass.getClassName(), classLevel);

                        dungeonPlayers.put(playerName, player);
                    }
                }
            }
        }
    }

    private void showCurrentClassInfo() {
        String currentPlayerName = Necron.mc.thePlayer.getName();
        DungeonPlayer player = dungeonPlayers.get(currentPlayerName);

        if (player != null && player.getPlayerClass() != null) {
            DungeonClass playerClass = player.getPlayerClass();
            int classLevel = player.getClassLevel();
            Utils.modMessage("You are playing " + playerClass.getColor() + playerClass.getClassName() + " " + classLevel + "§7.");
        }
    }

    public static List<String> getTabList() {
        List<String> tabList = TabUtils.getTabList();
        if(tabList.size() < 18 || !tabList.get(0).contains("§r§b§lParty §r§f(")) return null;
        return tabList;
    }

    private static void updateTeammates(List<String> tabList) {
        teammates.clear();
        for(int i = 0; i < 4; i++) {
            if (1 + i * 4 >= tabList.size()) continue;

            String text = StringUtils.stripControlCodes(tabList.get(1 + i * 4)).trim();
            String[] parts = text.split(" ");
            if (parts.length < 2) {
                Necron.LOGGER.warn("Tab list still loading: {}", text);
                continue;
            }
            String username = Utils.clearMcUsername(text.split(" ")[1]);
            if(Objects.equals(username, "")) continue;

            for(EntityPlayer playerEntity : Necron.mc.theWorld.playerEntities) {
                if(playerEntity.getName().equals(username)) {
                    teammates.add(playerEntity);
                }
            }
        }
    }

    public static String getPlayerClassColor(String playerName) {
        DungeonPlayer player = dungeonPlayers.get(playerName);
        if (player != null && player.getPlayerClass() != null) {
            return player.getPlayerClass().getColor();
        }

        String cleanName = Utils.clearMcUsername(playerName);
        player = dungeonPlayers.get(cleanName);
        if (player != null && player.getPlayerClass() != null) {
            return player.getPlayerClass().getColor();
        }

        return "§f";
    }
}
