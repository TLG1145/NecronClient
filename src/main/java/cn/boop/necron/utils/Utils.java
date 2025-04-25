package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.util.ChatComponentText;

public class Utils {
    public static void modMessage(String msg) {
        Necron.mc.thePlayer.addChatMessage(new ChatComponentText(Necron.CHAT_PREFIX + msg));
    }

    public static void chatMessage(String msg) {
        Necron.mc.thePlayer.sendChatMessage(msg);
    }

    public static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }
}
