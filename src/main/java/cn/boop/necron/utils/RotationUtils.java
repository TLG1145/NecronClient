package cn.boop.necron.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    // 获取当前玩家的 pitch 和 yaw
    public static float pitch() {
        return mc.thePlayer.rotationPitch;
    }

    public static float yaw() {
        return mc.thePlayer.rotationYaw;
    }

    // 设置玩家的 pitch 和 yaw
    public static void setPitch(float pitch) {
        mc.thePlayer.rotationPitch = pitch;
    }

    public static void setYaw(float yaw) {
        mc.thePlayer.rotationYaw = yaw;
    }

    // 获取范围内最近的实体
    public static Entity getNearestEntity(double range) {
        Entity nearestEntity = null;
        double nearestDistance = range;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer) continue; // 排除玩家自己

            double distance = mc.thePlayer.getDistanceToEntity(entity);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestEntity = entity;
            }
        }

        if (nearestEntity != null) {
            System.out.println("找到实体: " + nearestEntity.getName());
        } else {
            System.out.println("未找到范围内的实体");
        }

        return nearestEntity;
    }

    // 计算目标角度
    public static float[] getRotationToEntity(Entity entity) {
        Vec3 playerPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        Vec3 entityPos = new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);

        double deltaX = entityPos.xCoord - playerPos.xCoord;
        double deltaY = entityPos.yCoord - playerPos.yCoord;
        double deltaZ = entityPos.zCoord - playerPos.zCoord;

        double horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        float yaw = (float) Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90;
        float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, horizontalDistance));

        return new float[]{yaw, pitch};
    }

    // 平滑旋转到目标角度
    public static void smoothRotateToEntity(Entity entity, float speed) {
        float[] targetRotation = getRotationToEntity(entity);
        float currentYaw = yaw();
        float currentPitch = pitch();

        float deltaYaw = MathHelper.wrapAngleTo180_float(targetRotation[0] - currentYaw);
        float deltaPitch = MathHelper.wrapAngleTo180_float(targetRotation[1] - currentPitch);

        System.out.println("目标 Yaw: " + targetRotation[0] + ", 当前 Yaw: " + currentYaw + ", 差值: " + deltaYaw);
        System.out.println("目标 Pitch: " + targetRotation[1] + ", 当前 Pitch: " + currentPitch + ", 差值: " + deltaPitch);

        setYaw(currentYaw + deltaYaw * speed);
        setPitch(currentPitch + deltaPitch * speed);
    }

    // 主逻辑：平滑旋转并瞄准最近实体
    public static void aimAtNearestEntity(double range, float speed) {
        Entity nearestEntity = getNearestEntity(range);
        if (nearestEntity != null) {
            smoothRotateToEntity(nearestEntity, speed);
        }
    }
}
