package cn.boop.necron.command;

import cc.polyfrost.oneconfig.utils.Notifications;
import cn.boop.necron.Necron;
import cn.boop.necron.module.PlayerStats;
import cn.boop.necron.utils.DungeonUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.boop.necron.utils.Utils.modMessage;

public class DebugCommands extends CommandBase {
    @Override
    public String getCommandName() {
        return "ncdebug";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("ncd");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "dungeonInfo":
                    for (Map.Entry<String, DungeonUtils.DungeonPlayer> entry : DungeonUtils.dungeonPlayers.entrySet()) {
                        modMessage(String.valueOf(entry.getValue()));
                    }
                    break;
                case "notification":
                    if (args.length < 4) {
                        modMessage("Usage: notification <title> <message> <duration>");
                        break;
                    }

                    try {
                        String title = args[1];
                        String message = args[2];
                        int duration = Integer.parseInt(args[3]);
                        Notifications.INSTANCE.send(title, message, duration);
                    } catch (NumberFormatException e) {
                        modMessage("§cInvalid duration time format.");
                    }
                    break;
                case "stats":
                    modMessage("Player Stats:\n§7§l | §r§7inHypixel: " + PlayerStats.inHypixel +
                            "\n§7§l | §r§7inSkyBlock: " + PlayerStats.inSkyBlock +
                            "\n§7§l | §r§7Island: " + PlayerStats.getCurrentIslandName() +
                            "\n§7§l | §r§7Held item ID: " + Utils.getSkyBlockID(Necron.mc.thePlayer.getHeldItem()) +
                            "\n§7§l | §r§7Held item Name:§r " + Necron.mc.thePlayer.getHeldItem().getDisplayName() +
                            "\n§7§l | §r§7Player health: §c" + Necron.mc.thePlayer.getHealth() +
                            "\n§7" +
                            "\n§7§l | §r§7inDungeon: " + PlayerStats.inDungeon +
                            "\n§7§l | §r§7Floor: " + PlayerStats.floor +
                            "\n§7§l | §r§7Instance player(s): " + DungeonUtils.dungeonPlayers.size());
                    break;
                default:
                    modMessage("§cUnknown debug command.");
                    break;
            }
        } else {
            modMessage("Debug Commands: dungeonInfo, notification, stats");
        }
    }
}
