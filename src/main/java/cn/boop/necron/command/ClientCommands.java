package cn.boop.necron.command;

import cn.boop.necron.Necron;
import cn.boop.necron.module.impl.ChatCommands;
import cn.boop.necron.module.impl.CropNuker;
import cn.boop.necron.module.impl.EtherwarpRouter;
import cn.boop.necron.module.impl.Waypoint;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.List;

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
                        RotationUtils.rotatingToBlock(x, y, z, 0.3f);
                        modMessage(String.format("Rotating to Vec3d: (%.1f, %.1f, %.1f)", x, y, z));
                    } catch (NumberFormatException e) {
                        modMessage("§cInvalid position format.");
                    }
                    break;
                case "setDir":
                    if (args.length < 3) {
                        modMessage("Usage: setDir <waypointID> <direction>");
                        modMessage("Available directions: forward, backward, left, right.");
                        break;
                    }
                    try {
                        int waypointId = Integer.parseInt(args[1]);
                        String direction = args[2].toLowerCase();

                        if (!Arrays.asList("forward", "backward", "left", "right").contains(direction)) {
                            modMessage("§cInvalid direction.");
                            modMessage("Available directions: forward, backward, left, right.");
                            break;
                        }

                        Waypoint.setWaypointDirection(waypointId, direction);
                    } catch (NumberFormatException e) {
                        modMessage("§cInvalid waypoint ID format.");
                    }
                    break;
                case "setRot":
                    if (args.length < 3) {
                        modMessage("Usage: setRot <waypointID> <yaw>");
                        modMessage("The range of the yaw must be in 0~360.");
                        break;
                    }
                    try {
                        int waypointId = Integer.parseInt(args[1]);
                        float rotation = Float.parseFloat(args[2]);

                        rotation = rotation % 360;
                        if (rotation < 0) rotation += 360;

                        Waypoint.setWaypointRotation(waypointId, rotation);
                    } catch (NumberFormatException e) {
                        modMessage("§cInvalid number format.");
                    }
                    break;
                case "tips":
                    Necron.mc.thePlayer.addChatMessage(new ChatComponentText("§bTips §8»§r§7 " + Utils.randomSelect(ChatCommands.tipList)));
                    break;
                case "load":
                    if (args.length < 2) {
                        modMessage("Usage: load <file>");
                        break;
                    }
                    EtherwarpRouter.loadWaypoints(args[1]);
                    Waypoint.loadWaypoints(args[1]);
                    CropNuker.setIndex(0);
                    break;
                default:
                    modMessage("§cUnknown command.");
                    break;
            }
        } else {
            int i;
            for (i = 0; i < helpMsg.length; ++i) {
                Necron.mc.thePlayer.addChatMessage(new ChatComponentText(helpMsg[i]));
            }
        }
    }

    private static final String[] helpMsg = new String[]{
            "§8§m-------------------------------------",
            "§b             NecronClient §7v" + Necron.VERSION,
            "§r ",
            "§b/necron load <file> §f§l»§r§7 加载路径点文件",
            "§b/necron rotate <x> <y> <z> §f§l»§r§7 将视角旋转至x, y, z",
            "§b/necron setDir <ID> <direction> §f§l»§r§7 设置路径点的移动方向",
            "§b/necron setRot <ID> <yaw> §f§l»§r§7 设置路径点的预设旋转角度",
            "§b/necron tips §f§l»§r§7 获取一些神秘文本",
            "§r§8§m-------------------------------------"
    };
}
