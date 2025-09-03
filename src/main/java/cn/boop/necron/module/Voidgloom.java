package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.AxisAlignedBBUtils;
import cn.boop.necron.utils.LocationUtils;
import cn.boop.necron.utils.PlayerUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import static cn.boop.necron.config.impl.SlayerOptionsImpl.*;

public class Voidgloom {
    public static boolean isLeftMouseDown = false;
    private long lastClickTime = 0L;
    private long lastSoulcryTime = 0L;
    private long lastWandTime = 0L;
    private long lastSwitchTime = 0L;
    public static int soulcrySlot = -1;
    public static int wandSlot = -1;
    private boolean lastLeftMouseDown = false;
    private boolean isLeftMouseClicked = false;
    public static boolean tempDisable = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        boolean currentLeftClick = Mouse.isButtonDown(0);

        if (Necron.mc.theWorld == null || Necron.mc.thePlayer == null) return;
        if (Necron.mc.currentScreen != null) return;

        isLeftMouseDown = currentLeftClick;
        isLeftMouseClicked = currentLeftClick && !lastLeftMouseDown;
        lastLeftMouseDown = currentLeftClick;

        updateItemSlots();
        if (voidgloom) handleAutoUse();
    }

    private void updateItemSlots() {
        for (int i = 0; i < 8; i++) {
            ItemStack item = Necron.mc.thePlayer.inventory.mainInventory[i];
            if (item != null) {
                String name = item.getDisplayName();
                if (name.contains("Atomsplit Katana") || name.contains("Vorpal Katana")) {
                    soulcrySlot = i;
                } else if (name.contains("Wand of Atonement") || name.contains("Wand Of Restoration")) {
                    wandSlot = i;
                }
            }
        }
    }

    private void handleAutoUse() {
        if (!PlayerStats.inCombat) return;

        long currentTime = System.currentTimeMillis();

        boolean shouldUseSoulcry = !tempDisable && (currentTime - lastSoulcryTime >= 4400L);
        boolean shouldUseWand = (currentTime - lastWandTime >= 6000L);

        if (shouldUseSoulcry && shouldUseWand && (currentTime - lastSwitchTime >= 500L)) {
            System.out.println("Using both soulcry and wand");
            if (useSoulcryItem()) {
                lastSoulcryTime = currentTime;
                lastSwitchTime = currentTime;
            }

            new Thread(() -> {
                try {
                    Thread.sleep(400);
                    System.out.println("Using wand after delay");
                    useWand();
                } catch (InterruptedException e) {
                    Necron.LOGGER.warn(e.getMessage());
                }
            }).start();
        } else {
            if (shouldUseSoulcry) {
                System.out.println("Using soulcry only");
                if(useSoulcryItem()) {
                    lastSoulcryTime = currentTime;
                }
            }
            if (shouldUseWand) {
                System.out.println("Using wand only");
                useWand();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        if (event.entity instanceof EntityArmorStand) {
            EntityArmorStand entity = (EntityArmorStand) event.entity;
            if (!entity.hasCustomName()) return;

            String entityName = Utils.removeFormatting(entity.getCustomNameTag());
            double x = event.entity.posX;
            double y = event.entity.posY;
            double z = event.entity.posZ;

            if (voidgloom && (isLeftMouseDown() || isLeftMouseClicked) && LocationUtils.inSkyBlock && Necron.mc.currentScreen == null && shouldClick()) {
                if (entityName.contains(/*"Voidgloom"*/"Dummy")) {
                    if (AxisAlignedBBUtils.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        Utils.modMessage("> CLICK");
                        new Thread(() -> {
                            if (!tempDisable || !PlayerStats.inCombat) {
                                useWand();
                            }
                        }).start();
                        useSoulcry();
                    }
                } else if (entityName.contains("Hits")) {
                    if (AxisAlignedBBUtils.isFacingAABB(new AxisAlignedBB(x - 0.5, y - 3.0, z - 0.5, x + 0.5, y + 1.0, z + 0.5), 5.0f)) {
                        Utils.modMessage("Hit Phase");
                        tempDisable = true;
                    }
                } else tempDisable = false;
            }
        }
    }

    private boolean useSoulcryItem() {
        return !tempDisable && PlayerStats.inCombat && this.swapToItem(soulcrySlot, true);
    }

    private boolean useWandItem() {
        return swapToItem(wandSlot, true);
    }

    public void useSoulcry() {
        if (tempDisable || !PlayerStats.inCombat) return;
        if (swapToItem(soulcrySlot, true)) {
            this.lastClickTime = System.currentTimeMillis();
        }
    }

    public void useWand() {
        if (tempDisable || !PlayerStats.inCombat) return;
        System.out.println("useWand called. wandSlot=" + wandSlot);

        if (useWandItem()) {
            this.lastWandTime = System.currentTimeMillis();
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
            }
            swapToItem(soulcrySlot, false);
        }
//        if (swapToItem(wandSlot, true)) {
//            System.out.println("Switching to wand slot: " + wandSlot);
//            System.out.println("Performing right click for wand");
//
//            lastWandTime = System.currentTimeMillis();
//            System.out.println("Switching back to soulcry slot: " + soulcrySlot);
//            swapToItem(soulcrySlot, false);
//        }
    }

    private boolean swapToItem(int slot, boolean rightClick) {
        System.out.println("swapToItem called - slot: " + slot + ", rightClick: " + rightClick);
        if (slot == -1) return false;

        ItemStack item = Necron.mc.thePlayer.inventory.mainInventory[slot];
        if (item == null) return false;

        System.out.println("Item name: " + item.getDisplayName());

        Necron.mc.thePlayer.inventory.currentItem = slot;
        if (rightClick) PlayerUtils.rightClick();
        return true;
    }

    public boolean shouldClick() {
        return System.currentTimeMillis() - this.lastClickTime >= 4000L;
    }

    public static boolean isLeftMouseDown() {
        return isLeftMouseDown;
    }
}
