package cn.boop.necron.command;

import cn.boop.necron.Necron;
import cn.boop.necron.config.JsonLoader;
import cn.boop.necron.module.ChatCommands;
import cn.boop.necron.module.EWarpRouter;
import cn.boop.necron.module.PlayerStats;
import cn.boop.necron.module.Waypoint;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

import static cn.boop.necron.utils.Utils.modMessage;

public class ClientCommands extends CommandBase {
    public static int waypointCounter = 1;

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
                case "tips":
                    modMessage(Utils.randomSelect(ChatCommands.tipList));
                    break;
                case "rotation":
                    modMessage("Rotation{Yaw: " + RotationUtils.yaw() + ", Pitch: " + RotationUtils.pitch() + "}");
                    break;
                case "stats":
                    modMessage("Player Stats:\n§7§l | §r§7inSkyBlock: " + PlayerStats.inSkyBlock + "\n§7§l | §r§7inDungeon: " + PlayerStats.inDungeon + "\n§7§l | §r§7Island: " + PlayerStats.getCurrentIslandName() + "\n§7§l | §r§7Floor: " + PlayerStats.floor);
                    break;
                case "test":
                    break;
                case "rotate":
                    if (args.length < 4) {
                        modMessage("Usage: rotate <x> <y> <z>");
                        break;
                    }
                    try {
                        double x = Double.parseDouble(args[1]);
                        double y = Double.parseDouble(args[2]);
                        double z = Double.parseDouble(args[3]);
                        RotationUtils.rotatingToBlock(x + 0.5, y + 1, z + 0.5);
                        modMessage(String.format("Rotating to Vec3d: (%.1f, %.1f, %.1f)", x, y, z));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format");
                    }
                    break;
                case "waypoint":
                    if (args.length < 2) {
                        modMessage("Usage: waypoint load <file> | add <name> | remove <name>");
                        break;
                    }
                    String subCmd = args[1];
                    switch (subCmd) {
                        case "load":
                            if (args.length < 3) {
                                modMessage("Usage: waypoint load <name>");
                                break;
                            }
                            EWarpRouter.loadWaypoints(args[2]);
                            Waypoint.loadWaypoints(args[2]);
                            break;
                        case "add":
                            Waypoint.addWaypoint();
                            break;
                        case "remove":
                            if (args.length < 3) {
                                modMessage("Usage: waypoint remove <name>");
                                break;
                            }
                            try {
                                int idToRemove = Integer.parseInt(args[2]);
                                Waypoint.removeWaypoint(idToRemove);
                            } catch (NumberFormatException e) {
                                modMessage("请输入有效的数字序号");
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    modMessage("未知参数");
            }
        } else {
            int i;
            for (i = 0; i < helpMsg.length; ++i) { // §
                Necron.mc.thePlayer.addChatMessage(new ChatComponentText(helpMsg[i]));
            }
        }
    }

    private static final String[] helpMsg = new String[]{
            "§8§m-----------------------------",
            "§b       NecronClient §7v0.0.1",
            "§r ",
            "§b/necron tips ->§r§7 获取一些神秘文本 (?",
            "§b/necron rotation ->§r§7 查看当前Yaw和Pitch",
            "§b/necron stats ->§r§7 查看当前玩家信息",
            "§b/necron rotate <x> <y> <z> ->§r§7 将视角旋转至x, y, z",
            "§b/necron waypoint load|add|remove ->§r§7 加载|添加|删除 路径点文件",
            "§r§8§m-----------------------------"
    };
}
