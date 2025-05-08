package cn.boop.necron.command;

import cn.boop.necron.Necron;
import cn.boop.necron.module.ChatCommands;
import cn.boop.necron.utils.LocationUtils;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.ScoreboardUtils;
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
            //modMessage("");
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
                    //modMessage("Player Stats:\n§7 | inSkyBlock: " + Utils.inSkyBlock + "\n§7 | inDungeon: " + Utils.inDungeon + "\n§7 | Island: " + LocationUtils.getIsland());
                    break;
                case "test_title":
                    //modMessage("Title: " + ScoreboardUtils.getScoreboardTitle() + ", Length: " + ScoreboardUtils.getScoreboardTitle().length());
                    System.out.println(ScoreboardUtils.getScoreboardTitle());
                    break;
                case "test_":
                    break;
                default:
                    modMessage("未知参数");
            }
        } else {
            modMessage("ClassNotFound");
        }
    }
}
