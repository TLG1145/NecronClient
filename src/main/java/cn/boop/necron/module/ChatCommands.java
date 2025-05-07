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
    //public static final List<String> tipList = new java.util.ArrayList<>(JsonLoader.loadTips());
    public static final List<String> tipList = new java.util.ArrayList<>();
    public static String tips = "";

    public ChatCommands() {
        tipList.add("Why not try MelodySky");
        tipList.add("Try to join SkyBlock");
        tipList.add("Wither Impact (-150 Mana)");
        tipList.add("天空方块得了MVP");
        tipList.add("魔↗术↘技↑巧↓");
        tipList.add("这是什么？点一下");
        tipList.add("A player has been removed from your server!");
        tipList.add("跟我的憨豆说去吧");
        tipList.add("wtf 爱抚比");
        tipList.add("Divan钻头是把Diamond necron head围一圈divan fragment合成的");
        tipList.add("M7? owo");
        tipList.add("You cannot invite that player since they're monkey.");
        tipList.add("通过不什·戈门曲线很容易推断出人工饲养的Terminator，他是可以捕获野生的Necron's Handle");
        tipList.add("不管说Bacte的切面是否具有Chimera，jerry的n次方是否有Iron Punch都不影响Hyperion的掉落率");
        tipList.add("事态发展到这样，一切责任都在Necron。奉劝Necron认清现实，赶紧给我出个handle");
        tipList.add("嘟嘟哒嘟嘟哒");
        tipList.add("Auto SkyBlock ✖   otto SkyBlock ✔");
        tipList.add("♿♿♿");
        tipList.add("i have juju cata 24 no dupe arch");
        tipList.add("♬ 爱的民 能不能放过我这一次 ♬");
        tipList.add("wiperararat getbanned");
        tipList.add("我将使用蘑菇牛桶出击Backwater Bayou");
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
                        Utils.chatMessage("/pc ntmsb?");
                        break;
                    case "ysj":
                        Utils.chatMessage("/pc 余胜军使用jvav实现自动化skyblock");
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
