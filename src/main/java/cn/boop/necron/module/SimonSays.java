package cn.boop.necron.module;

import cn.boop.necron.Necron;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.PlayerUtils;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButtonStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class SimonSays {
    public static final List<BlockPos> sequence = new ArrayList<>(); // 海晶灯出现顺序
    private boolean isRunning = false;
    private int tickCounter = 0;
    private int clickIndex = 0;

    private static final BlockPos PLAYER_PLATFORM_MIN = new BlockPos(108, 120, 93);
    private static final BlockPos PLAYER_PLATFORM_MAX = new BlockPos(107, 120, 94);

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;
        if (event.pos == null || !event.pos.equals(new BlockPos(110, 121, 91))) return;
        Utils.modMessage("[SimonSays] Start!");
        startSS();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Necron.mc.theWorld == null || Necron.mc.thePlayer == null) return;
        if (!ModConfig.simonSays && event.phase != TickEvent.Phase.START) return;
        tickCounter++;
        if (tickCounter % 4 != 0) return;
        checkLantern();
    }

    private void checkLantern() {
        if (Necron.mc.theWorld == null || Necron.mc.thePlayer == null && !isRunning) return;
        List<BlockPos> newButtons = new ArrayList<>();
        for (int y = 120; y <= 123; y++) {
            for (int z = 92; z <= 95; z++) {
                BlockPos lanternPos = new BlockPos(111, y, z);
                BlockPos buttonPos = new BlockPos(110, y, z);

                Block block = Necron.mc.theWorld.getBlockState(lanternPos).getBlock();
                if (block == Blocks.sea_lantern && !sequence.contains(buttonPos)) {
                    newButtons.add(buttonPos);
                    //Utils.modMessage("[SimonSays] Add new button: " + buttonPos);
                }
            }
        }
        if (!newButtons.isEmpty()) {
            sequence.addAll(newButtons);
            Utils.modMessage("[DEBUG] New buttons: " + newButtons);
        }
    }

//    private BlockPos getButtonPosition(BlockPos lanternPos) {
//        return new BlockPos(110, lanternPos.getY(), lanternPos.getZ());
//    }

    private void startSS() {
        sequence.clear();
        clickIndex = 0;
        isRunning = true;
//          v 测试坐标
//        sequence.add(new BlockPos(111, 120, 92)); // 假设第一个在开始按钮右边
//        sequence.add(new BlockPos(111, 122, 93));
//        sequence.add(new BlockPos(111, 120, 95));
//        sequence.add(new BlockPos(111, 122, 95));
//        sequence.add(new BlockPos(111, 123, 94));
        new Thread(() -> {
            try {
                Thread.sleep(100); // 等待海晶灯生成
                checkLantern();
                if (sequence.isEmpty()) {
                    Utils.modMessage("[ERROR] 未检测到按钮序列");
                    return;
                }
                assistClickNext();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //if (ModConfig.simonSaysMode == 0)
        // else autoClickNext();
    }

    private void compSS() {
        isRunning = false;
        sequence.clear();
    }

    private void autoClickNext() {
        if (clickIndex >= sequence.size()) {
            compSS();
            return;
        }

        BlockPos buttonPos = sequence.get(clickIndex);
        //BlockPos buttonPos = getButtonPosition(targetLantern);
        Vec3 center = new Vec3(
                buttonPos.getX() + 1,
                buttonPos.getY() + 0.5,
                buttonPos.getZ() + 0.5
        );

        RotationUtils.asyncAimAt(center, 0.8f);

        new Thread(() -> {
            try {
                Thread.sleep(500);
                PlayerUtils.rightClick();
                clickIndex++;
                //autoClickNext();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void assistClickNext() {
        if (clickIndex >= sequence.size()) {
            compSS();
            return;
        }
        checkLantern();

        BlockPos buttonPos = sequence.get(clickIndex);
        //BlockPos buttonPos = getButtonPosition(targetLantern);
        Vec3 center = new Vec3(
                buttonPos.getX() + 1,
                buttonPos.getY() + 0.5,
                buttonPos.getZ() + 0.5
        );
        RotationUtils.asyncAimAt(center, 0.6f);
        //RotationUtils.rotatingToBlock(buttonPos.getX() + 1, buttonPos.getY() + 0.5, buttonPos.getZ() + 0.5, 0.6);
        //RotationUtils.aimAtBlockPosition(center, 0.6f);

        Object listener = new Object() {
            @SubscribeEvent
            public void onPlayerInteract(PlayerInteractEvent event) {
                if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK /*&& event.pos.equals(buttonPos)*/&& sequence.equals(event.pos)) {
                    IBlockState state = Necron.mc.theWorld.getBlockState(event.pos);
                    Block block = state.getBlock();
                    if (block == Blocks.stone_button) {
                        boolean powered = state.getValue(BlockButtonStone.POWERED);
                        if (powered) {
                            //clickIndex++;
                            clickIndex = sequence.indexOf(event.pos) + 1;
                            MinecraftForge.EVENT_BUS.unregister(this);
                            assistClickNext();
//                            if (clickIndex < sequence.size() && clickIndex < 5) {
//                                assistClickNext();
//                            } else {
//                                if (!sequence.isEmpty()) {
//                                    clickIndex = 0;
//                                    assistClickNext();
//                                } else compSS();
//                            }
                        }
                    }
                }
            }
        };
        MinecraftForge.EVENT_BUS.register(listener);
    }
}
