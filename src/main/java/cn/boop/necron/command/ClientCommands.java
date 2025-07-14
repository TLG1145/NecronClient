package cn.boop.necron.command;

import cn.boop.necron.Necron;
import cn.boop.necron.module.*;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.*;

import static cn.boop.necron.utils.Utils.modMessage;

public class ClientCommands extends CommandBase {
    @Override
    public String getCommandName() {
        return "necron";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("nc");
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
                case "rotate":
                    if (args.length < 4) {
                        modMessage("Usage: rotate <x> <y> <z>");
                        break;
                    }
                    try {
                        double x = Double.parseDouble(args[1]);
                        double y = Double.parseDouble(args[2]);
                        double z = Double.parseDouble(args[3]);
                        RotationUtils.rotatingToBlock(x, y, z, 0.5f);
                        modMessage(String.format("Rotating to Vec3d: (%.1f, %.1f, %.1f)", x, y, z));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid position format");
                    }
                    break;
                case "stats":
                    modMessage("Player Stats:\n§7§l | §r§7inSkyBlock: " + PlayerStats.inSkyBlock +
                            "\n§7§l | §r§7inDungeon: " + PlayerStats.inDungeon +
                            "\n§7§l | §r§7Island: " + PlayerStats.getCurrentIslandName() +
                            "\n§7§l | §r§7Floor: " + PlayerStats.floor);
                    break;
                case "tips":
                    modMessage(Utils.randomSelect(ChatCommands.tipList));
                    break;
                case "test":
                    break;
                case "load":
                    if (args.length < 2) {
                        modMessage("Usage: load <file>");
                        break;
                    }
                    EtherwarpRouter.loadWaypoints(args[1]);
                    Waypoint.loadWaypoints(args[1]);
                    break;
                default:
                    modMessage("Unknown command.");
            }
        } else {
            int i;
            for (i = 0; i < helpMsg.length; ++i) { // §
                Necron.mc.thePlayer.addChatMessage(new ChatComponentText(helpMsg[i]));
            }
        }
    }

    private static final String[] helpMsg = new String[]{
            "§8§m--------------------------------",
            "§b           NecronClient §7v" + Necron.VERSION,
            "§r ",
            "§b/necron rotate <x> <y> <z> §f§l»§r§7 将视角旋转至x, y, z",
            "§b/necron stats §f§l»§r§7 查看当前玩家信息",
            "§b/necron tips §f§l»§r§7 获取一些神秘文本",
            "§b/necron load <file> §f§l»§r§7 加载路径点文件",
            "§r§8§m--------------------------------"
    };
}
