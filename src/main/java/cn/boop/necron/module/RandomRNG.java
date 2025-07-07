package cn.boop.necron.module;

import cn.boop.necron.utils.Utils;
import cn.boop.necron.utils.event.RollLogger;
import kotlin.Pair;

import java.util.*;

public class RandomRNG {
    public static final List<Pair<String, Double>> weightedItems = new ArrayList<>();

    static {
        weightedItems.add(new Pair<>("Thunderlord VII", 0.00351));
        weightedItems.add(new Pair<>("Master Skull - Tier 5", 0.00337));
        weightedItems.add(new Pair<>("Fifth Master Star",0.00334));
        weightedItems.add(new Pair<>("Implosion", 0.00179));
        weightedItems.add(new Pair<>("Shadow Warp", 0.00179));
        weightedItems.add(new Pair<>("Wither Shield", 0.00179));
        weightedItems.add(new Pair<>("Necron's Handle", 0.00129));
        weightedItems.add(new Pair<>("Dark Claymore", 0.00072));
        weightedItems.add(new Pair<>("Necron Dye", 0.00040));
        weightedItems.add(new Pair<>("Shiny Necron's Handle", 0.00006));

        double total = weightedItems.stream()
                .mapToDouble(Pair::getSecond)
                .sum();
        weightedItems.add(new Pair<>(null, 1.0 - total));
    }

    public static String getRNG () {
        Pair<String, Double> result = Utils.weightedRandom(weightedItems);
        if (result == null || result.getFirst() == null) return null;
        return String.format("%s (%.3f%%)",
                result.getFirst(),
                result.getSecond() * 100
        );
    }

    public static String sendResult(Map<String, Integer> results, String username) {
        if (results == null || results.isEmpty()) {
            return "/pc " + username + " Nothing :<";
        }

        StringBuilder sb = new StringBuilder("/pc " + username + " unlocked -> ");
        results.forEach((item, count) -> {
            sb.append(String.format("%s ×%d ", item, count));
            RollLogger.logRollResult(username, item, count);
        });

        return sb.toString().trim();
    }
}
