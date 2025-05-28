package cn.boop.necron.command;

import cn.boop.necron.Necron;
import cn.boop.necron.module.ChatCommands;
import cn.boop.necron.module.PlayerStats;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

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
                case "version":
                    modMessage("当前版本 " + Necron.VERSION);
                    break;
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
                default:
                    modMessage("未知参数");
            }
        } else {
            modMessage("ClassNotFound");
        }
    }
}
