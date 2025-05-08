package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
    public static float pitch() {
        return Necron.mc.thePlayer.rotationPitch;
    }

    public static float yaw() {
        return Necron.mc.thePlayer.rotationYaw;
    }

    // 设置玩家的 pitch 和 yaw
    public static void setPitch(float pitch) {
        Necron.mc.thePlayer.rotationPitch = pitch;
    }

    public static void setYaw(float yaw) {
        Necron.mc.thePlayer.rotationYaw = yaw;
    }

    // 获取范围内最近的实体
    public static Entity getNearestEntity(double range) {
        Entity nearestEntity = null;
        double nearestDistance = range;

        for (Object obj : Necron.mc.theWorld.playerEntities) {
            if (!(obj instanceof Entity)) continue;
            Entity entity = (Entity) obj;
            if (entity == Necron.mc.thePlayer) continue;
            if (!entity.isEntityAlive()) continue;// 排除玩家自己

            double distance = Necron.mc.thePlayer.getDistanceToEntity(entity);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestEntity = entity;
            }
        }

        return nearestEntity;
    }

    // 计算目标角度
    public static float[] getRotationToEntity(Entity entity) {
        Vec3 playerPos = new Vec3(Necron.mc.thePlayer.posX, Necron.mc.thePlayer.posY + Necron.mc.thePlayer.getEyeHeight(), Necron.mc.thePlayer.posZ);
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
    public static void smoothRotateToEntity(Entity entity, float baseSpeed) {
        float[] targetRotation = getRotationToEntity(entity);
        float currentYaw = yaw();
        float currentPitch = pitch();

        float deltaYaw = MathHelper.wrapAngleTo180_float(targetRotation[0] - currentYaw);
        float deltaPitch = MathHelper.wrapAngleTo180_float(targetRotation[1] - currentPitch);
        float distance = (float) Necron.mc.thePlayer.getDistanceToEntity(entity);
        float dynamicSpeed = baseSpeed * (distance / 5);
        dynamicSpeed = Math.max(dynamicSpeed, 0.07f); // 设置最小速度限制

        setYaw(currentYaw + deltaYaw * dynamicSpeed);
        setPitch(currentPitch + deltaPitch * dynamicSpeed);
    }

    // 主逻辑：平滑旋转并瞄准最近实体
    public static void aimAtNearestEntity(double range, float speed) {
        Entity nearestEntity = getNearestEntity(range);
        if (nearestEntity != null) {
            smoothRotateToEntity(nearestEntity, speed);
        }
    }
}
