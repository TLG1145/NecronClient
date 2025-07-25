package cn.boop.necron;

import cn.boop.necron.command.ClientCommands;
import cn.boop.necron.config.NCConfig;
import cn.boop.necron.config.UpdateChecker;
import cn.boop.necron.gui.MainMenu;
import cn.boop.necron.module.*;
import cn.boop.necron.utils.event.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = Necron.MODID, name = Necron.MODNAME, version = Necron.VERSION, acceptedMinecraftVersions = "1.8.9", clientSideOnly = true)
public class Necron {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static final String MODID = "necronclient";
    public static final String MODNAME = "Necron";
    public static final String VERSION = "0.1.1";
    public static final String WP_FILE_PATH = "./config/necron/waypoints/";

    private static boolean playerEnteredWorld = false;

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AutoGG());
        MinecraftForge.EVENT_BUS.register(new BlazeDagger());
        MinecraftForge.EVENT_BUS.register(new ChatCommands());
        MinecraftForge.EVENT_BUS.register(new Etherwarp());
        MinecraftForge.EVENT_BUS.register(new EtherwarpRouter());
        MinecraftForge.EVENT_BUS.register(new FakeWipe());
        MinecraftForge.EVENT_BUS.register(new HurtCam());
        MinecraftForge.EVENT_BUS.register(new MainMenu());
        MinecraftForge.EVENT_BUS.register(new Nametags());
        MinecraftForge.EVENT_BUS.register(new PlayerStats());
        MinecraftForge.EVENT_BUS.register(new RandomRNG());
        MinecraftForge.EVENT_BUS.register(new TitleManager());
        MinecraftForge.EVENT_BUS.register(new WaypointEventHandler());
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ClientCommands());
        NCConfig.INSTANCE.preload();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        if (mc.thePlayer != null && !playerEnteredWorld) {
            playerEnteredWorld = true;
            new UpdateChecker("TLG1145", "NecronClient", VERSION).checkForUpdates();
        }
    }
}
