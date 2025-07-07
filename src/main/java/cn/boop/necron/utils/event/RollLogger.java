package cn.boop.necron.utils.event;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RollLogger {
    private static final String LOG_FILE = "logs/roll_log.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logRollResult(String username, String itemName, int count) {
        String timestamp = LocalDateTime.now().format(formatter);
        String line = String.format("[%s] Player: %s, Item: %s, Count: %d%n", timestamp, username, itemName, count);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}