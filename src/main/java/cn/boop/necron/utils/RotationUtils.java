package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.*;

public class RotationUtils {
    private static final int[][] DIRECTIONS = {
            {-1, 0, 0}, {1, 0, 0},
            {0, -1, 0}, {0, 1, 0},
            {0, 0, -1}, {0, 0, 1}
    };
    private static final java.util.concurrent.ScheduledExecutorService scheduler =
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor();


    public static float pitch() {
        return Necron.mc.thePlayer.rotationPitch;
    }

    public static float yaw() {
        return Necron.mc.thePlayer.rotationYaw;
    }

    public static void setRotation(float yaw, float pitch) {
        Necron.mc.thePlayer.rotationYaw = yaw;
        Necron.mc.thePlayer.rotationPitch = pitch;
    }

    public static void aimAtBlockPosition(Vec3 targetPos, float speed) {
        if (targetPos == null) return;
        smoothRotateToPosition(targetPos, speed);
    }

    private static void smoothRotateToPosition(Vec3 targetPos, float baseSpeed) {
        Vec3 playerPos = new Vec3(
                Necron.mc.thePlayer.posX,
                Necron.mc.thePlayer.posY + Necron.mc.thePlayer.getEyeHeight(),
                Necron.mc.thePlayer.posZ
        );

        double deltaX = targetPos.xCoord - playerPos.xCoord;
        double deltaY = targetPos.yCoord - playerPos.yCoord;
        double deltaZ = targetPos.zCoord - playerPos.zCoord;

        double horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        float targetYaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90;
        float targetPitch = (float) -Math.toDegrees(Math.atan2(deltaY, horizontalDistance));

        float currentYaw = yaw();
        float currentPitch = pitch();
        float deltaYaw = MathHelper.wrapAngleTo180_float(targetYaw - currentYaw);
        float deltaPitch = MathHelper.wrapAngleTo180_float(targetPitch - currentPitch);

        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        float dynamicSpeed = baseSpeed * (float) Math.atan(distance * 0.1);
        dynamicSpeed = Math.max(dynamicSpeed, 0.07f);
        setRotation(currentYaw + deltaYaw * dynamicSpeed, currentPitch + deltaPitch * dynamicSpeed);
    }

    public static void rotatingToBlock(double x, double y, double z) {
        new Thread(() -> {
            Vec3 target = new Vec3(x, y, z);

            while (true) {
                float currentYaw = RotationUtils.yaw();
                float currentPitch = RotationUtils.pitch();
                RotationUtils.aimAtBlockPosition(target, 0.15f);
                if (Math.abs(RotationUtils.yaw() - currentYaw) < 0.1f &&
                        Math.abs(RotationUtils.pitch() - currentPitch) < 0.1f) {
                    break;
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    public static List<Vec3> getExposedFaceCenters(World world, BlockPos pos) {
        List<Vec3> exposedFaces = new ArrayList<>();

        for (int[] dir : DIRECTIONS) {
            BlockPos offsetPos = pos.add(dir[0], dir[1], dir[2]);
            if (world.getBlockState(offsetPos).getBlock() == Blocks.air) {
                double centerX = pos.getX() + 0.5D + dir[0] * 0.5D;
                double centerY = pos.getY() + 0.5D + dir[1] * 0.5D;
                double centerZ = pos.getZ() + 0.5D + dir[2] * 0.5D;
                exposedFaces.add(new Vec3(centerX, centerY, centerZ));
            }
        }
        return exposedFaces;
    }

    public static Vec3 getClosestExposedFaceCenter(World world, BlockPos pos, EntityPlayerSP player) {
        Vec3 playerPos = new Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        List<Vec3> exposedFaceCenters = getExposedFaceCenters(world, pos);

        Vec3 closestFace = null;
        double closestDistance = Double.MAX_VALUE;
        for (Vec3 faceCenter : exposedFaceCenters) {
            double distance = playerPos.distanceTo(faceCenter);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestFace = faceCenter;
            }
        }
        return closestFace;
    }

    public static void asyncAimAt(Vec3 targetPos, float speed) {
        if (targetPos == null) return;

        scheduler.execute(() -> rotatingToPosition(targetPos, speed));
    }

    private static void rotatingToPosition(Vec3 targetPos, float baseSpeed) {
        while (true) {
            float currentYaw = yaw();
            float currentPitch = pitch();

            smoothRotateToPosition(targetPos, baseSpeed);

            if (Math.abs(yaw() - currentYaw) < 0.1f &&
                    Math.abs(pitch() - currentPitch) < 0.1f) {
                break;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
