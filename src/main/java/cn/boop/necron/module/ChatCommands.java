package cn.boop.necron.module;

import cn.boop.necron.config.JsonLoader;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCommands {
    public static final Pattern ChatRegex = Pattern.compile("Party > \\s*(?:\\[\\w+\\+?]\\s*)?\\w{1,16}:\\s*!(.+)");
    public static final List<String> tipList = new java.util.ArrayList<>(JsonLoader.loadTips());
    public static String tips = "";

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 0 && ModConfig.chatCommands) {
            String rawMessage = event.message.getUnformattedText();
            String cleanMessage = Utils.removeFormatting(rawMessage);
            Matcher matcher = ChatRegex.matcher(cleanMessage);
                if (matcher.matches()) {
                String command = matcher.group(1);
                switch (command.toLowerCase()) {
                    case "meow":
                        Utils.chatMessage("/pc 喵❤");
                        break;
                    case "sb":
                        Utils.chatMessage("/pc ntmsb?");
                        break;
                    case "stats":
                        Utils.chatMessage("/pc {inSkyBlock: " + PlayerStats.inSkyBlock + ", inDungeon: " + PlayerStats.inDungeon + "}");
                        break;
                    case "lobby":
                        if (matcher.group(0).contains("MixinNecron")) Utils.chatMessage("/lobby");
                        else Utils.chatMessage("/pc zako~ 不可以哦");
                        break;
                    case "tips":
                        tips = Utils.randomSelect(tipList);
                        Utils.chatMessage("/pc " + tips);
                        break;
                    case "喵7":
                        Utils.modMessage("M7? owo");
                        Utils.chatMessage("/joindungeon MASTER_CATACOMBS 7");
                        break;
                    case "loc":
                        String msg;
                        if (PlayerStats.inDungeon) msg = "/pc 当前位置: CATACOMBS_" + PlayerStats.floor;
                        else if (PlayerStats.inSkyBlock) msg = "/pc 当前位置: " + PlayerStats.getCurrentIslandName();
                        else msg = "/pc 未知位置";
                        Utils.chatMessage(msg);
                        break;
                    case "help":
                        Utils.chatMessage("/pc 命令列表 -> meow, sb, ysj, lobby, tips, 喵7, loc, help");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
