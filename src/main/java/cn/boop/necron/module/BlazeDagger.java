package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.AxisAlignedBBUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import static cn.boop.necron.config.impl.SlayerOptionsImpl.blazeSwap;

public class BlazeDagger {
    private long lastClickTime = 0L;
    public static boolean isLeftMouseDown = false;
    private boolean lastLeftMouseDown = false;
    private boolean isLeftMouseClicked = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        boolean currentLeftClick = Mouse.isButtonDown(0);

        if (Necron.mc.theWorld == null || Necron.mc.thePlayer == null) return;
        if (Necron.mc.currentScreen != null) return;

        isLeftMouseDown = currentLeftClick;
        isLeftMouseClicked = currentLeftClick && !lastLeftMouseDown;
        lastLeftMouseDown = currentLeftClick;
    }

    /*
     *   Source code from MelodySky.
     */

    @SubscribeEvent(priority= EventPriority.LOWEST, receiveCanceled=true)
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (event.entity instanceof EntityArmorStand) {
            EntityArmorStand entity = (EntityArmorStand) event.entity;
            if (!entity.hasCustomName()) return;

            String entityName = Utils.removeFormatting(entity.getCustomNameTag());
            double x = event.entity.posX;
            double y = event.entity.posY;
            double z = event.entity.posZ;

            if (blazeSwap && (isLeftMouseDown() || isLeftMouseClicked) && PlayerStats.inSkyBlock && Necron.mc.currentScreen == null && this.shouldClick() && AxisAlignedBBUtils.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                if (entityName.startsWith("CRYSTAL")) {
                    swapToCrystal();
                    return;
                }
                if (entityName.startsWith("ASHEN")) {
                    swapToAshen();
                    return;
                }
                if (entityName.startsWith("AURIC")) {
                    swapToAuric();
                    return;
                }
                if (entityName.startsWith("SPIRIT")) {
                    swapToSprit();
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

    public static boolean isLeftMouseDown() {
        return isLeftMouseDown;
    }
}
