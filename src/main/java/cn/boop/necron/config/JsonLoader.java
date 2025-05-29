package cn.boop.necron.config;

import cn.boop.necron.module.ChatCommands;
import cn.boop.necron.module.Waypoint;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonLoader {
    private static final Gson GSON = new Gson();

    public static List<String> loadTips() {
        try (InputStream is = ChatCommands.class.getResourceAsStream("/tips.json")) {
            if (is == null) throw new IOException("tips.json not found");
            JsonObject json = GSON.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), JsonObject.class);
            return GSON.fromJson(json.get("tips"), new TypeToken<List<String>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultTips();
        }
    }

    private static List<String> getDefaultTips() {
        return Arrays.asList(
            "(Default) JSON file error",
            "(Default) Try to join SkyBlock",
            "(Default) Wither Impact (-150 Mana)"
        );
    }

    public static List<Waypoint> loadWaypoints(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            return GSON.fromJson(content, new TypeToken<List<Waypoint>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveWaypoints(List<Waypoint> waypoints, String filePath) {
        try {
            String json = GSON.toJson(waypoints);
            Files.write(Paths.get(filePath), json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
