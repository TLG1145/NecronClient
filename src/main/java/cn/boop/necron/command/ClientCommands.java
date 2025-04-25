package cn.boop.necron.command;

import cn.boop.necron.Necron;
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
                default:
                    modMessage("未知参数");
            }
        } else {
            modMessage("ClassNotFound\nParty > [MVP+] MixinNecron: !version");
        }
    }
}
