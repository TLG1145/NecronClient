package cn.boop.necron.module.impl;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.PathfinderUtils;
import cn.boop.necron.utils.RenderUtils;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.List;

import static cn.boop.necron.config.impl.AutoPathOptionsImpl.*;

public class AutoPath {
    private List<BlockPos> path;
    private int currentPathIndex = 0;
    private boolean isFollowing = false;
    private boolean wePressedForward = false;

    // 记录当前行进方向向量
    private double currentDirectionX = 0;
    private double currentDirectionZ = 0;
    // 记录上次设置的目标角度
    private float lastTargetYaw = 0;
    private float lastTargetPitch = 0;

    public void setTarget(BlockPos target) {
        if (Necron.mc.thePlayer == null && !autoPath) return;

        BlockPos start = Necron.mc.thePlayer.getPosition();
        path = PathfinderUtils.findPath(start, target);
        currentPathIndex = 0;
        isFollowing = !path.isEmpty();

        currentDirectionX = 0;
        currentDirectionZ = 0;
        lastTargetYaw = RotationUtils.yaw();
        lastTargetPitch = RotationUtils.pitch();

        if (isFollowing) {
            Utils.modMessage("Find the path, a total of §c"+ path.size() + " §7node(s).");
        } else {
            Utils.modMessage("Can't find path.");
        }
    }

    private boolean shouldUpdateRotation(float targetYaw, float targetPitch) {
        // 计算与上次目标角度的差异
        float yawDiff = Math.abs(RotationUtils.normalizeAngle(targetYaw - lastTargetYaw));
        float pitchDiff = Math.abs(targetPitch - lastTargetPitch);

        // 如果角度差异较大，需要更新
        if (yawDiff > 5.0f || pitchDiff > 2.0f) {
            return true;
        }

        // 如果没有设置过目标角度，需要更新
        if (lastTargetYaw == 0 && lastTargetPitch == 0) {
            return true;
        }

        return false;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Necron.mc.thePlayer == null && Necron.mc.theWorld == null) return;
        if (event.phase == TickEvent.Phase.START && isFollowing && Necron.mc.currentScreen == null) {
            if (path != null && currentPathIndex < path.size()) {
                BlockPos targetPos = path.get(currentPathIndex);
                BlockPos playerPos = Necron.mc.thePlayer.getPosition();

                if (playerPos.distanceSq(targetPos) < 1.5) {
                    currentPathIndex++;
                    if (currentPathIndex >= path.size()) {
                        isFollowing = false;
                        Utils.modMessage("Reached target: " + targetPos);
                        releaseForwardKey();
                        return;
                    }
                    targetPos = path.get(currentPathIndex);
                }

                double dx = targetPos.getX() + 0.5 - (Necron.mc.thePlayer.posX);
                double dy = targetPos.getY() - (Necron.mc.thePlayer.posY);
                double dz = targetPos.getZ() + 0.5 - (Necron.mc.thePlayer.posZ);

                double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
                // 计算目标角度
                float targetYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90;
                float targetPitch = (float) -Math.toDegrees(Math.atan2(dy - 0.7, horizontalDistance));
                if (follow) {
                    if (shouldUpdateRotation(targetYaw, targetPitch)) {
                        // 平滑旋转到目标角度
                        //RotationUtils.smoothRotateTo(targetYaw, targetPitch, 15.0f);
                        RotationUtils.smoothRotateTo(targetYaw, RotationUtils.pitch(), 15.0f);
                        lastTargetYaw = targetYaw;
                        lastTargetPitch = targetPitch;

                        currentDirectionX = dx;
                        currentDirectionZ = dz;
                    }

                    if (horizontalDistance > 0.1) {
                        KeyBinding.setKeyBindState(Necron.mc.gameSettings.keyBindForward.getKeyCode(), true);
                        wePressedForward = true;
                    } else if (wePressedForward) {
                        KeyBinding.setKeyBindState(Necron.mc.gameSettings.keyBindForward.getKeyCode(), false);
                        wePressedForward = false;
                    }
                }
            } else {
                isFollowing = false;
                releaseForwardKey();
            }
        }

        if (!isFollowing) {
            releaseForwardKey();
        }

        RotationUtils.updateRotations();
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (path != null && !path.isEmpty()) {
            renderPath(event.partialTicks);
        }
    }

    private void renderPath(float partialTicks) {
        for (int i = 0; i < path.size() - 1; i++) {
            BlockPos pos1 = path.get(i);
            BlockPos pos2 = path.get(i + 1);

            if (renderPath) {
                RenderUtils.draw3DLine(
                        pos1.getX() + 0.5, pos1.getY() + 0.1, pos1.getZ() + 0.5,
                        pos2.getX() + 0.5, pos2.getY() + 0.1, pos2.getZ() + 0.5,
                        Color.CYAN, 3.0f
                );
            }
        }

        for (int i = 0; i < path.size(); i++) {
            BlockPos pos = path.get(i);
            Color color = (i == currentPathIndex) ? Color.DARK_GRAY : Color.WHITE;

            if (renderNodes) {
                RenderUtils.draw3DText(
                        String.valueOf(i),
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        color, partialTicks
                );
            }
        }
    }

    private void releaseForwardKey() {
        if (wePressedForward) {
            KeyBinding.setKeyBindState(Necron.mc.gameSettings.keyBindForward.getKeyCode(), false);
            wePressedForward = false;
        }
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void stopFollowing() {
        isFollowing = false;
        path = null;
    }
}
