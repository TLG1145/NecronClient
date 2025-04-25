package cn.boop.necron.config;

import cn.boop.necron.module.ChatCommands;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class JsonLoader {
    private static final Gson GSON = new Gson();

    public static List<String> loadTips() {
        try (InputStream is = ChatCommands.class.getResourceAsStream("/tips.json")) {
            if (is == null) throw new IOException("tips.json not found");
            JsonObject json = GSON.fromJson(new InputStreamReader(is), JsonObject.class);
            return GSON.fromJson(json.get("tips"), new TypeToken<List<String>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return getDefaultTips();
        }
    }

    private static List<String> getDefaultTips() {
        return Arrays.asList(
            "Default (JSON error)",
            "(JE) Try to join SkyBlock",
            "(JE) Wither Impact (-150 Mana)"
        );
    }
}
