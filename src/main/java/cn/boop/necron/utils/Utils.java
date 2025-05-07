package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.util.ChatComponentText;

import java.util.List;
import java.util.Random;

public class Utils {
    private static final Random random = new Random();

    public static void modMessage(String msg) {
        Necron.mc.thePlayer.addChatMessage(new ChatComponentText(Necron.CHAT_PREFIX + msg));
    }

    public static void chatMessage(String msg) {
        Necron.mc.thePlayer.sendChatMessage(msg);
    }

    public static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

    public static <T> T randomSelect(List<T> list) {
        int index = random.nextInt(list.size());
        return list.get(index);
    }
}
