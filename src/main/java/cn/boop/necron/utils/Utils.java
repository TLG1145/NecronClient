package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import kotlin.Pair;

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

    public static String getSkyBlockID(ItemStack item) {
        if(item != null) {
            NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
            if (extraAttributes != null && extraAttributes.hasKey("id")) {
                return extraAttributes.getString("id");
            }
        }
        return "";
    }

    public static <T> Pair<T, Double> weightedRandom(List<Pair<T, Double>> weightedList) {
        double total = weightedList.stream()
                .mapToDouble(Pair::getSecond)
                .sum();

        if (total == 0) return null;

        double randomPoint = random.nextDouble() * total;
        double cumulative = 0.0;

        for (Pair<T, Double> entry : weightedList) {
            cumulative += entry.getSecond();
            if (randomPoint <= cumulative) {
                return entry;
            }
        }
        return null;
    }
}
