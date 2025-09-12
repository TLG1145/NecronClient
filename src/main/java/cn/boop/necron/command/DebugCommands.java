package cn.boop.necron.command;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.*;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

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
                case "findpath":
                    if (args.length < 4) {
                        modMessage("Usage: findpath <x> <y> <z>");
                        break;
                    }

                    try {
                        int x = Integer.parseInt(args[1]);
                        int y = Integer.parseInt(args[2]);
                        int z = Integer.parseInt(args[3]);

                        Necron.getAutoPath().setTarget(new BlockPos(x, y, z));

                    } catch (NumberFormatException e) {
                        Necron.LOGGER.error("§cInvalid position format");
                    }
                    break;
//                case "silent":
//                    RotationUtils.useSilent = !RotationUtils.useSilent;
//                    modMessage((RotationUtils.useSilent ? " §aEnabled" : " §cDisabled") + " §7silent rotation.");
//                    break;
                case "stats":
                    modMessage("Player Stats:\n§7§l | §r§7inHypixel: " + LocationUtils.inHypixel +
                            "\n§7§l | §r§7inSkyBlock: " + LocationUtils.inSkyBlock +
                            "\n§7§l | §r§7Island: " + LocationUtils.getCurrentIslandName() +
                            "\n§7§l | §r§7Held item ID: " + Utils.getSkyBlockID(Necron.mc.thePlayer.getHeldItem()) +
                            "\n§7§l | §r§7Held item Name:§r " + Necron.mc.thePlayer.getHeldItem().getDisplayName() +
                            "\n§7§l | §r§7Player health: §c" + Necron.mc.thePlayer.getHealth() +
                            "\n§7" +
                            "\n§7§l | §r§7inDungeon: " + LocationUtils.inDungeon +
                            "\n§7§l | §r§7Floor: " + LocationUtils.floor +
                            "\n§7§l | §r§7Instance player(s): " + DungeonUtils.dungeonPlayers.size());
                    break;
                case "test":
                    boolean inCH = LocationUtils.currentIsland.getDisplayName().equals("Crystal Hollows");
                    Utils.modMessage("§7In Crystal Hollows: " + inCH);
                    break;
                default:
                    modMessage("§cUnknown debug command.");
                    break;
            }
        } else {
            modMessage("Debug Commands: dungeonInfo, findpath, silent, stats");
        }
    }
}
