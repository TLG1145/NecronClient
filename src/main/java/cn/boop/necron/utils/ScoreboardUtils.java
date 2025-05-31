package cn.boop.necron.utils;

import com.google.common.collect.*;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.*;
import net.minecraft.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardUtils {
    public static String cleanSB(String scoreboard) {
        char[] nvString = StringUtils.stripControlCodes(scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();

        for(char c : nvString) {
            if (c <= '\u0014' || c >= '\u007f') continue;
            cleaned.append(c);
        }

        return cleaned.toString();
    }

    @SuppressWarnings({"ExecutionException", "IllegalArgumentException"})
    public static List<String> getScoreboard() {
        List<String> lines = new ArrayList<>();
        if(Minecraft.getMinecraft().theWorld == null) return lines;
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if(scoreboard == null) return lines;
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if(objective == null) return lines;

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> list = scores.stream()
                .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName()
                        .startsWith("#"))
                .collect(Collectors.toList());

        scores = list.size() > 15 ? Lists.newArrayList(Iterables.skip(list, scores.size() - 15)) : list;

        for(Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }

        return lines;
    }

    public static boolean scoreboardContains(String string) {
        boolean result = false;
        List<String> scoreboard = getScoreboard();
        for(String line : scoreboard) {
            line = cleanSB(line);
            line = Utils.removeFormatting(line);
            if(line.contains(string)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static String getScoreboardTitle() {
        if (Minecraft.getMinecraft().theWorld == null) return "";
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) return "";

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        return (objective != null) ?
                cleanSB(objective.getDisplayName()) : "";
    }

    public static String getLineThatContains(String string) {
        String result = null;
        List<String> scoreboard = ScoreboardUtils.getScoreboard();
        for(String line : scoreboard) {
            if (!(line = ScoreboardUtils.cleanSB(line)).contains(string)) continue;
            result = line;
            break;
        }
        return result;
    }
}
