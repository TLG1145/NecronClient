package cn.boop.necron.module;

import cn.boop.necron.config.JsonLoader;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCommands {
    public static final Pattern ChatRegex = Pattern.compile("Party > \\s*(?:\\[\\w+\\+?]\\s*)?\\w{1,16}:\\s*!(.+)");
    public static final List<String> tipList = new java.util.ArrayList<>(JsonLoader.loadTips());
    public static String tips = "";

    public static <T> T RandomSelect(List<T> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 0 && ModConfig.chatCommands) {
            String rawMessage = event.message.getUnformattedText();
            String cleanMessage = Utils.removeFormatting(rawMessage);
            Matcher matcher = ChatRegex.matcher(cleanMessage);
                if (matcher.matches()) {
                String command = matcher.group(1);  // 直接获取正则分组捕获的命令
                switch (command.toLowerCase()) {
                    case "meow":
                        Utils.chatMessage("/pc 喵❤");
                        break;
                    case "sb":
                        Utils.modMessage("Try to join SkyBlock");
                        Utils.chatMessage("/play skyblock");
                        break;
                    case "ysj":
                        Utils.chatMessage("/pc 余胜军使用jvav实现自动化skyblock");
                        break;
                    case "lobby":
                        if (matcher.group(0).contains("MixinNecron")) Utils.chatMessage("/lobby");
                        else Utils.chatMessage("/pc zako~ 不可以哦");
                        break;
                    case "tips":
                        tips = RandomSelect(tipList);
                        Utils.chatMessage("/pc " + tips);
                        break;
                    case "喵7":
                        Utils.modMessage("M7? owo");
                        Utils.chatMessage("/joindungeon MASTER_CATACOMBS 7");
                    case "help":
                        Utils.chatMessage("/pc 命令列表 -> meow, sb, ysj, lobby, tips, 喵7, help");
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
