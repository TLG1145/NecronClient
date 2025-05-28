package cn.boop.necron.module;

import cn.boop.necron.config.JsonLoader;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCommands {
    public static final Pattern ChatRegex = Pattern.compile("^Party > (\\[[^]]*?])? ?(\\w{1,16})?: \\s*!(.+)");
    public static final List<String> tipList = new java.util.ArrayList<>(JsonLoader.loadTips());

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 0 && ModConfig.chatCommands) {
            String rawMessage = event.message.getUnformattedText();
            String cleanMessage = Utils.removeFormatting(rawMessage);
            Matcher matcher = ChatRegex.matcher(cleanMessage);
            if (matcher.matches()) {
                String sender = matcher.group(2);
                String command = matcher.group(3);
                switch (command.toLowerCase()) {
                    case "help":
                        Utils.chatMessage("/pc 命令列表 -> help, loc, meow, roll, sb, tips, zako");
                        break;
                    case "loc":
                        String msg;
                        if (PlayerStats.inDungeon) msg = "/pc 当前位置: CATACOMBS_" + PlayerStats.floor;
                        else if (PlayerStats.inSkyBlock) msg = "/pc 当前位置: " + PlayerStats.getCurrentIslandName();
                        else msg = "/pc 未知位置";
                        Utils.chatMessage(msg);
                        break;
                    case "meow":
                        Utils.chatMessage("/pc 喵❤");
                        break;
                    case "roll":
                        Map<String, Integer> results = new HashMap<>();
                        for (int i = 0; i < 10; i++) {
                            String result10 = RandomRNG.sendRNG();
                            if (result10 != null) {
                                results.put(result10, results.getOrDefault(result10, 0) + 1);
                            }
                        }

                        if (results.isEmpty()) {
                            Utils.chatMessage("/pc " + sender + " Nothing :<");
                        } else {
                            StringBuilder sb = new StringBuilder("/pc ");
                            sb.append(sender).append(" unlocked -> ");
                            results.forEach((item, count) ->
                                    sb.append(String.format("%s ×%d ", item, count)));
                            Utils.chatMessage(sb.toString().trim());
                        }
                        break;
                    case "sb":
                        Utils.chatMessage("/pc ntmsb?");
                        break;
                    case "tips":
                        Utils.chatMessage("/pc " + Utils.randomSelect(tipList));
                        break;
                    case "zako":
                        if (sender != null) Utils.chatMessage("/pc " + sender + " 杂鱼杂鱼~");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
