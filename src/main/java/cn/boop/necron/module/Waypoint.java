package cn.boop.necron.module;

import cn.boop.necron.command.ClientCommands;
import cn.boop.necron.config.JsonLoader;
import cn.boop.necron.Necron;
import cn.boop.necron.utils.RenderUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static cn.boop.necron.command.ClientCommands.waypointCounter;

public class Waypoint {
    private int id, x, y, z;

    public Waypoint(int id, int x, int y, int z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }

    private static List<Waypoint> waypoints = new ArrayList<>();
    private static String currentFile = null;

    public static void loadWaypoints(String filename) {
        if (filename == null || filename.isEmpty()) return;

        currentFile = Necron.WP_FILE_PATH + filename + ".json";
        waypoints = JsonLoader.loadWaypoints(currentFile);

        if (waypoints.isEmpty()) {
            waypoints = new ArrayList<>();
            boolean created = JsonLoader.saveWaypoints(waypoints, currentFile);
            if (!created) return;
        } else {
            Utils.modMessage("Loaded " + waypoints.size() + " waypoints. §8[" + filename + "]");
            Utils.modMessage("Press LALT to edit waypoints! ");
        }

        if (!waypoints.isEmpty()) {
            int maxId = waypoints.stream()
                    .mapToInt(Waypoint::getId)
                    .max()
                    .orElse(0);
            ClientCommands.waypointCounter = maxId + 1;
        } else {
            ClientCommands.waypointCounter = 1;
        }
    }

    public static void addWaypoint(int x, int y, int z) {
        if (currentFile == null) {
            Utils.modMessage("Waypoints file not loaded.");
            return;
        }
        if (waypoints == null) waypoints = new ArrayList<>();

        Waypoint newWaypoint = new Waypoint(waypointCounter++, x, y, z);
        waypoints.add(newWaypoint);
        JsonLoader.saveWaypoints(waypoints, currentFile);
        Utils.modMessage("Added waypoint #" + (waypointCounter - 1) + " at BlockPos{x=" + x + ", y=" + y + ", z=" + z + "}");
    }

    public static void removeWaypoint(BlockPos pos) {
        if (currentFile == null) {
            Utils.modMessage("Waypoints file not loaded.");
            return;
        }
        boolean removed = waypoints.removeIf(wp -> wp.x == pos.getX() && wp.y == pos.getY() && wp.z == pos.getZ());

        if (removed) {
            for (int i = 0; i < waypoints.size(); i++) {
                waypoints.get(i).setId(i + 1);
            }
            ClientCommands.waypointCounter = waypoints.size() + 1;
            saveWaypoints();
            Utils.modMessage("Removed waypoint at BlockPos{x=" + pos.getX() + ", y=" + pos.getY() + ", z=" + pos.getZ() + "}");
        } else {
            Utils.modMessage("No waypoint found at BlockPos{x=" + pos.getX() + ", y=" + pos.getY() + ", z=" + pos.getZ() + "}");
        }
    }

    private static void saveWaypoints() {
        if (currentFile != null) {
            JsonLoader.saveWaypoints(waypoints, currentFile);
        }
    }

    public static void renderWaypoints(float partialTicks) {
        if (waypoints.isEmpty()) return;
        Minecraft mc = Minecraft.getMinecraft();
        Entity viewer = mc.getRenderViewEntity();
        if (viewer == null) return;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2F);

        for (int i = 0; i < waypoints.size() - 1; i++) {
            Waypoint wp1 = waypoints.get(i);
            Waypoint wp2 = waypoints.get(i + 1);

            double x1 = wp1.x + 0.5;
            double y1 = wp1.y + 0.5;
            double z1 = wp1.z + 0.5;
            double x2 = wp2.x + 0.5;
            double y2 = wp2.y + 0.5;
            double z2 = wp2.z + 0.5;

            Color lineColor = new Color(39, 161, 227, 255); // 红色
            float r = (float) lineColor.getRed() / 255;
            float g = (float) lineColor.getGreen() / 255;
            float b = (float) lineColor.getBlue() / 255;
            float a = (float) lineColor.getAlpha() / 255;
            RenderUtils.draw3DLine(x1, y1, z1, x2, y2, z2,
                    r, g, b, a, 1.5f);
        }

        for (Waypoint wp : waypoints) {
            RenderUtils.drawOutlinedBlockESP(
                    wp.x,
                    wp.y,
                    wp.z,
                    new Color(197, 229, 248, 255),
                    2f,
                    partialTicks
            );
        }

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
