package cn.boop.necron.module.impl;

import cn.boop.necron.Necron;
import cn.boop.necron.utils.AxisAlignedBBUtils;
import cn.boop.necron.utils.RotationUtils;
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

public class Vampire {
    private static long lastSwitchTime = 0L;
    private static final long SWITCH_COOLDOWN = 250L;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Necron.mc.theWorld == null || Necron.mc.thePlayer == null) return;
        if (Necron.mc.currentScreen != null) return;

        int playerHealth = (int) Necron.mc.thePlayer.getHealth();
        ItemStack heldItem = Necron.mc.thePlayer.getHeldItem();

        if (Necron.mc.gameSettings.keyBindUseItem.isKeyDown() && heldItem != null && heldItem.getItem() != null && heldItem.getItem() == Items.iron_sword) {
            handlePlayerActions(1);
        }
        if (playerHealth <= 8) {
            handlePlayerActions(3);
        }
    }

    private void handlePlayerActions(int actionType) {
        new Thread(() -> {
            try {
                switch (actionType) {
                    case 1:
                        Thread.sleep(50);
                        switchToItem("Holy Ice", true, true);
                        break;
                    case 2:
                        switchToItem("Steak Stake", false, false);
                        break;
                    case 3:
                        switchToItem("Luscious Healing Melon", true, true);
                        break;
                    default:
                        break;
                }
            } catch (InterruptedException ignored) {}
        }, "Actions").start();
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

            if (/*LocationUtils.inSkyBlock && */Necron.mc.currentScreen == null && AxisAlignedBBUtils.isFacingAABB(new AxisAlignedBB(x - 1.0, y - 3.0, z - 1.0, x + 1.0, y + 1.0, z + 1.0), 8.0f)) {
                if (entityName.contains("☠ Bloodfiend")) {
                    if (entityName.contains("Clotting")) return;
                    if (AutoClicker.canAim()) {
                        RotationUtils.smoothRotateToEntity(entity, 0.4f);
                    }
                }

                 if (entityName.contains("҉")) {
                    handlePlayerActions(2);
                }
            }
        }
    }

    public static void switchToItem(String itemName, boolean swapBack, boolean useItem) {
        if (Necron.mc.thePlayer == null || Necron.mc.thePlayer.inventory == null) return;
        if (System.currentTimeMillis() - lastSwitchTime < SWITCH_COOLDOWN) return;

        int lastSlot = Necron.mc.thePlayer.inventory.currentItem;

        for (int i = 0; i < 8; i++) {
            ItemStack itemStack = Necron.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() != null) {
                String displayName = Utils.removeFormatting(itemStack.getDisplayName());

                if (displayName.equals(itemName) && shouldClick()) {
                    Necron.mc.thePlayer.inventory.currentItem = i;
                    lastSwitchTime = System.currentTimeMillis();

                    if (swapBack) {
                        new Thread(() -> {
                            try {
                                if (useItem) {
                                    Thread.sleep(100);
                                    Necron.mc.playerController.sendUseItem(Necron.mc.thePlayer, Necron.mc.theWorld, itemStack);
                                }
                                Thread.sleep(100);
                                Necron.mc.thePlayer.inventory.currentItem = lastSlot;
                            } catch (InterruptedException e) {
                                Necron.LOGGER.error(e);
                            }
                        }, "Item Swap").start();
                    }
                }
            }
        }
    }

    private static boolean shouldClick() {
        return System.currentTimeMillis() - lastSwitchTime >= SWITCH_COOLDOWN;
    }
}
