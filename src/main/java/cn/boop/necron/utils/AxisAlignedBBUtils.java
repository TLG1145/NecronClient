package cn.boop.necron.utils;

import cn.boop.necron.Necron;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class AxisAlignedBBUtils {
    public static boolean isFacingAABB(AxisAlignedBB aabb, float range) {
        return isInterceptable(aabb, range);
    }

    public static Vec3 getPositionEyes() {
        return new Vec3(Necron.mc.thePlayer.posX, Necron.mc.thePlayer.posY + (double) fastEyeHeight(), Necron.mc.thePlayer.posZ);
    }

    public static float fastEyeHeight() {
        return Necron.mc.thePlayer.isSneaking() ? 1.54f : 1.62f;
    }

    public static boolean isInterceptable(AxisAlignedBB aabb, float range) {
        Vec3 position = getPositionEyes();
        Vec3 look = getVectorForRotation();
        return isInterceptable(position, position.addVector(look.xCoord * (double)range, look.yCoord * (double)range, look.zCoord * (double)range), aabb);
    }

    private static Vec3 getVectorForRotation() {
        float f2 = -MathHelper.cos(-Necron.mc.thePlayer.rotationPitch * ((float)Math.PI / 180));
        return new Vec3(MathHelper.sin(-Necron.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI) * f2, MathHelper.sin(-Necron.mc.thePlayer.rotationPitch * ((float)Math.PI / 180)), MathHelper.cos(-Necron.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI) * f2);
    }

    public static boolean isInterceptable(Vec3 start, Vec3 goal, AxisAlignedBB aabb) {
        return isVecInYZ(start.getIntermediateWithXValue(goal, aabb.minX), aabb) || isVecInYZ(start.getIntermediateWithXValue(goal, aabb.maxX), aabb) || isVecInXZ(start.getIntermediateWithYValue(goal, aabb.minY), aabb) || isVecInXZ(start.getIntermediateWithYValue(goal, aabb.maxY), aabb) || isVecInXY(start.getIntermediateWithZValue(goal, aabb.minZ), aabb) || isVecInXY(start.getIntermediateWithZValue(goal, aabb.maxZ), aabb);
    }

    public static boolean isVecInYZ(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY && vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ;
    }

    public static boolean isVecInXZ(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX && vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ;
    }

    public static boolean isVecInXY(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX && vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY;
    }
}
