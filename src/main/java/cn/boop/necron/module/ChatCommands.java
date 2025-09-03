package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.JsonUtils;
import cn.boop.necron.utils.LocationUtils;
import cn.boop.necron.utils.ScoreboardUtils;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.regex.*;

import static cn.boop.necron.config.impl.ChatCommandsOptionsImpl.*;

public class ChatCommands {
    public static final Pattern ChatRegex = Pattern.compile("^Party > (\\[[^]]*?])? ?(\\w{1,16})?: \\s*[!.-](.+)");
    public static final List<String> tipList = new ArrayList<>(JsonUtils.loadTips());

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 0 && chatCommands) {
            String rawMessage = event.message.getUnformattedText();
            String cleanMessage = Utils.removeFormatting(rawMessage);
            Matcher matcher = ChatRegex.matcher(cleanMessage);
            if (matcher.matches()) {
                String sender = matcher.group(2);
                String command = matcher.group(3).toLowerCase();
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                        executeCommand(sender, command);
                    } catch (Exception e) {
                        Necron.LOGGER.error("Error in ChatCommands.onChat: ", e);
                    }
                }, "Chat-Commands").start();
            }
        }
    }

    private void executeCommand(String player, String cmd) {
        switch (cmd) {
            case "help":
                if (!help) return;
                Utils.chatMessage("/pc 命令列表 -> help, loc, meow, nuke, roll, sb, tips, zako");
                break;
            case "loc":
                if (!location) return;
                String msg;
                if (LocationUtils.inDungeon) msg = "/pc 当前位置: CATACOMBS_" + LocationUtils.floor;
                else if (LocationUtils.inSkyBlock) msg = "/pc 当前位置: " + LocationUtils.getCurrentIslandName();
                else {
                    String title = ScoreboardUtils.getScoreboardTitle();
                    if (title.isEmpty()) msg = "/pc 当前位置: Limbo";
                    else msg = "/pc " + title + " (In game or Lobby)";
                }

                Utils.chatMessage(msg);
                break;
            case "meow":
                if (!meow) return;
                Utils.chatMessage("/pc 喵❤");
                break;
            case "nuke":
                if (!nuke) return;
                Utils.chatMessage("/p disband");
                break;
            case "roll":
                if (!roll) return;
                Map<String, Integer> results = new HashMap<>();
                for (int i = 0; i < 10; i++) {
                    String result10 = RandomRNG.getRNG();
                    if (result10 != null) {
                        results.put(result10, results.getOrDefault(result10, 0) + 1);
                    }
                }
                String response = RandomRNG.sendResult(results, player);
                Utils.chatMessage(response);
                break;
            case "sb":
                if (!sb) return;
                Utils.chatMessage("/pc ntmsb?");
                break;
            case "tips":
                if (!tips) return;
                Utils.chatMessage("/pc " + Utils.randomSelect(tipList));
                break;
            case "zako":
                if (!zako) return;
                Utils.chatMessage("/pc " + player + " 杂鱼杂鱼~");
                break;
            default:
                break;
        }
    }
}
