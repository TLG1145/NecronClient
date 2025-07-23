package cn.boop.necron.module;

import cn.boop.necron.utils.JsonUtils;
import cn.boop.necron.utils.ScoreboardUtils;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.regex.*;

import static cn.boop.necron.config.sub.ChatCommandsOptionsImpl.*;

public class ChatCommands {
    public static final Pattern ChatRegex = Pattern.compile("^Party > (\\[[^]]*?])? ?(\\w{1,16})?: \\s*!(.+)");
    public static final List<String> tipList = new java.util.ArrayList<>(JsonUtils.loadTips());

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 0 && chatCommands) {
            String rawMessage = event.message.getUnformattedText();
            String cleanMessage = Utils.removeFormatting(rawMessage);
            Matcher matcher = ChatRegex.matcher(cleanMessage);
            if (matcher.matches()) {
                String sender = matcher.group(2);
                String command = matcher.group(3);
                switch (command.toLowerCase()) {
                    case "help":
                        if (help) Utils.chatMessage("/pc 命令列表 -> help, loc, meow, roll, sb, tips, zako");
                        else break;
                    case "loc":
                        if (location) {
                            String msg;
                            if (PlayerStats.inDungeon) msg = "/pc 当前位置: CATACOMBS_" + PlayerStats.floor;
                            else if (PlayerStats.inSkyBlock) msg = "/pc 当前位置: " + PlayerStats.getCurrentIslandName();
                            else {
                                String title = ScoreboardUtils.getScoreboardTitle();
                                if (title.isEmpty()) msg = "/pc 当前位置: Limbo";
                                else msg = "/pc " + title + " (In game or Lobby)";
                            }

                        Utils.chatMessage(msg);
                        }
                        else break;
                    case "meow":
                        if (meow) Utils.chatMessage("/pc 喵❤");
                        else break;
                    case "roll":
                        if (roll) {
                            Map<String, Integer> results = new HashMap<>();
                            for (int i = 0; i < 10; i++) {
                                String result10 = RandomRNG.getRNG();
                                if (result10 != null) {
                                    results.put(result10, results.getOrDefault(result10, 0) + 1);
                                }
                            }
                            String response = RandomRNG.sendResult(results, sender);
                            Utils.chatMessage(response);
                        }
                        else break;
                    case "sb":
                        if (sb) Utils.chatMessage("/pc ntmsb?");
                        else break;
                    case "tips":
                        if (tips) Utils.chatMessage("/pc " + Utils.randomSelect(tipList));
                        else break;
                    case "zako":
                        if (zako) Utils.chatMessage("/pc " + sender + " 杂鱼杂鱼~");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
