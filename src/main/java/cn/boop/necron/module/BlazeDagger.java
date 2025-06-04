package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class BlazeDagger {
    private long lastClickTime = 0L;
    public static boolean isLeftMouseDown = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Necron.mc.theWorld == null || Necron.mc.thePlayer == null) return;
        if (Necron.mc.currentScreen != null) return;
        isLeftMouseDown = Mouse.isButtonDown(0);
    }

    @SubscribeEvent(priority= EventPriority.LOWEST, receiveCanceled=true)
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (event.entity instanceof EntityArmorStand) {
            EntityArmorStand entity = (EntityArmorStand) event.entity;
            if (!entity.hasCustomName()) {
                return;
            }
            String entityName = Utils.removeFormatting(entity.getCustomNameTag());
            double x = event.entity.posX;
            double y = event.entity.posY;
            double z = event.entity.posZ;
            if (ModConfig.blazeDagger && isLeftMouseDown() && PlayerStats.inSkyBlock && Necron.mc.currentScreen == null && this.shouldClick()) {
                if (entityName.startsWith("CRYSTAL")) {
                    if (this.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        swapToCrystal();
                    }
                    return;
                }
                if (entityName.startsWith("ASHEN")) {
                    if (this.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        swapToAshen();
                    }
                    return;
                }
                if (entityName.startsWith("AURIC")) {
                    if (this.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        swapToAuric();
                    }
                    return;
                }
                if (entityName.startsWith("SPIRIT")) {
                    if (this.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        swapToSprit();
                    }
                }
            }
        }
    }

    public void swapToCrystal() {
        for (int i = 0; i < 8; ++i) {
            String name;
            ItemStack item = Necron.mc.thePlayer.inventory.mainInventory[i];
            if (item == null || !(name = item.getDisplayName()).contains("Deathripper Dagger") && !name.contains("Mawdredge Dagger") && !name.contains("Twilight Dagger")) continue;
            Necron.mc.thePlayer.inventory.currentItem = i;
            if (item.getItem() != Items.diamond_sword) {
                Necron.mc.playerController.sendUseItem(Necron.mc.thePlayer, Necron.mc.theWorld, Necron.mc.thePlayer.inventory.getCurrentItem());
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToSprit() {
        for (int i = 0; i < 8; ++i) {
            String name;
            ItemStack item = Necron.mc.thePlayer.inventory.mainInventory[i];
            if (item == null || !(name = item.getDisplayName()).contains("Deathripper Dagger") && !name.contains("Mawdredge Dagger") && !name.contains("Twilight Dagger")) continue;
            Necron.mc.thePlayer.inventory.currentItem = i;
            if (item.getItem() != Items.iron_sword) {
                Necron.mc.playerController.sendUseItem(Necron.mc.thePlayer, Necron.mc.theWorld, Necron.mc.thePlayer.inventory.getCurrentItem());
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToAshen() {
        for (int i = 0; i < 8; ++i) {
            String name;
            ItemStack item = Necron.mc.thePlayer.inventory.mainInventory[i];
            if (item == null || !(name = item.getDisplayName()).contains("Pyrochaos Dagger") && !name.contains("Kindlebane Dagger") && !name.contains("Firedust Dagger")) continue;
            Necron.mc.thePlayer.inventory.currentItem = i;
            if (item.getItem() != Items.stone_sword) {
                Necron.mc.playerController.sendUseItem(Necron.mc.thePlayer, Necron.mc.theWorld, Necron.mc.thePlayer.inventory.getCurrentItem());
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public void swapToAuric() {
        for (int i = 0; i < 8; ++i) {
            String name;
            ItemStack item = Necron.mc.thePlayer.inventory.mainInventory[i];
            if (item == null || !(name = item.getDisplayName()).contains("Pyrochaos Dagger") && !name.contains("Kindlebane Dagger") && !name.contains("Firedust Dagger")) continue;
            Necron.mc.thePlayer.inventory.currentItem = i;
            if (item.getItem() != Items.golden_sword) {
                Necron.mc.playerController.sendUseItem(Necron.mc.thePlayer, Necron.mc.theWorld, Necron.mc.thePlayer.inventory.getCurrentItem());
            }
            this.lastClickTime = System.currentTimeMillis();
            break;
        }
    }

    public boolean shouldClick() {
        return System.currentTimeMillis() - this.lastClickTime >= 500L;
    }

    public boolean isFacingAABB(AxisAlignedBB aabb, float range) {
        return this.isInterceptable(aabb, range);
    }

    public Vec3 getPositionEyes() {
        return new Vec3(Necron.mc.thePlayer.posX, Necron.mc.thePlayer.posY + (double)this.fastEyeHeight(), Necron.mc.thePlayer.posZ);
    }

    public float fastEyeHeight() {
        return Necron.mc.thePlayer.isSneaking() ? 1.54f : 1.62f;
    }

    public boolean isInterceptable(AxisAlignedBB aabb, float range) {
        Vec3 position = this.getPositionEyes();
        Vec3 look = this.getVectorForRotation();
        return this.isInterceptable(position, position.addVector(look.xCoord * (double)range, look.yCoord * (double)range, look.zCoord * (double)range), aabb);
    }

    private Vec3 getVectorForRotation() {
        float f2 = -MathHelper.cos(-Necron.mc.thePlayer.rotationPitch * ((float)Math.PI / 180));
        return new Vec3(MathHelper.sin(-Necron.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI) * f2, MathHelper.sin(-Necron.mc.thePlayer.rotationPitch * ((float)Math.PI / 180)), MathHelper.cos(-Necron.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - (float)Math.PI) * f2);
    }

    public boolean isInterceptable(Vec3 start, Vec3 goal, AxisAlignedBB aabb) {
        return this.isVecInYZ(start.getIntermediateWithXValue(goal, aabb.minX), aabb) || this.isVecInYZ(start.getIntermediateWithXValue(goal, aabb.maxX), aabb) || this.isVecInXZ(start.getIntermediateWithYValue(goal, aabb.minY), aabb) || this.isVecInXZ(start.getIntermediateWithYValue(goal, aabb.maxY), aabb) || this.isVecInXY(start.getIntermediateWithZValue(goal, aabb.minZ), aabb) || this.isVecInXY(start.getIntermediateWithZValue(goal, aabb.maxZ), aabb);
    }

    public boolean isVecInYZ(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY && vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ;
    }

    public boolean isVecInXZ(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX && vec.zCoord >= aabb.minZ && vec.zCoord <= aabb.maxZ;
    }

    public boolean isVecInXY(Vec3 vec, AxisAlignedBB aabb) {
        return vec != null && vec.xCoord >= aabb.minX && vec.xCoord <= aabb.maxX && vec.yCoord >= aabb.minY && vec.yCoord <= aabb.maxY;
    }

    public static boolean isLeftMouseDown() {
        return isLeftMouseDown;
    }
}
