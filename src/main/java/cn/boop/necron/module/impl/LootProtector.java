package cn.boop.necron.module.impl;

import cn.boop.necron.utils.Utils;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class LootProtector {
    private static final Set<String> RARE_ITEM = new HashSet<>();

    static {
        RARE_ITEM.add("Shiny Necron's Handle");
        RARE_ITEM.add("Necron's Handle");
        RARE_ITEM.add("Implosion");
        RARE_ITEM.add("Wither Shield");
        RARE_ITEM.add("Shadow Warp");
        RARE_ITEM.add("Dark Claymore");
        RARE_ITEM.add("Necron Dye");
        RARE_ITEM.add("Fifth Master Star");
        RARE_ITEM.add("Fourth Master Star");
        RARE_ITEM.add("Third Master Star");
        RARE_ITEM.add("Second Master Star");
        RARE_ITEM.add("First Master Star");
        RARE_ITEM.add("Giant's Sword");
        RARE_ITEM.add("Shadow Fury");
        RARE_ITEM.add("Livid Dye");
    }

    public static boolean containsRareItems(ItemStack[] items, int startSlot, int endSlot) {
        for (int i = startSlot; i <= endSlot && i < items.length; i++) {
            if (items[i] != null) {
                String itemName = Utils.removeFormatting(items[i].getDisplayName());
                if (RARE_ITEM.contains(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasRareLoot(ItemStack[] items) {
        return containsRareItems(items, 9, 17);
    }

    public static boolean isRareItemByName(String itemName) {
        for (String rareName : RARE_ITEM) {
            if (rareName.equals(itemName)) {
                return true;
            }
        }
        return false;
    }
}
