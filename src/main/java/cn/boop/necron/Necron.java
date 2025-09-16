package cn.boop.necron;

import cn.boop.necron.command.ClientCommands;
import cn.boop.necron.command.DebugCommands;
import cn.boop.necron.config.FontManager;
import cn.boop.necron.config.NCConfig;
import cn.boop.necron.config.UpdateChecker;
import cn.boop.necron.gui.MainMenu;
import cn.boop.necron.module.*;
import cn.boop.necron.utils.DungeonUtils;
import cn.boop.necron.utils.LocationUtils;
import cn.boop.necron.utils.RotationUtils;
import cn.boop.necron.utils.event.LootEventHandler;
import cn.boop.necron.utils.event.WaypointEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Necron.MODID, name = Necron.MODNAME, version = Necron.VERSION, acceptedMinecraftVersions = "1.8.9", clientSideOnly = true)
public class Necron {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static final String MODID = "necronclient";
    public static final String MODNAME = "Necron";
    public static final String VERSION = "0.1.3";
    public static final String WP_FILE_DIR = "./config/necron/waypoints/";
    public static final String BG_FILE_DIR = "./config/necron/backgrounds/";
    public static final Logger LOGGER = LogManager.getLogger(Necron.class);

    private static boolean playerEnteredWorld = false;
    private static final AutoPath autoPath = new AutoPath();

    @SuppressWarnings("InstantiationOfUtilityClass")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new AutoGG());
        MinecraftForge.EVENT_BUS.register(autoPath);
        MinecraftForge.EVENT_BUS.register(new BlazeDagger());
        MinecraftForge.EVENT_BUS.register(new ChatBlocker());
        MinecraftForge.EVENT_BUS.register(new ChatCommands());
        MinecraftForge.EVENT_BUS.register(new CropNuker());
        MinecraftForge.EVENT_BUS.register(new DungeonUtils());
        MinecraftForge.EVENT_BUS.register(new Etherwarp());
        MinecraftForge.EVENT_BUS.register(new EtherwarpRouter());
        MinecraftForge.EVENT_BUS.register(new FailSafe());
        MinecraftForge.EVENT_BUS.register(new FakeWipe());
        //MinecraftForge.EVENT_BUS.register(new GemstoneNuker());
        MinecraftForge.EVENT_BUS.register(new HurtCam());
        MinecraftForge.EVENT_BUS.register(new LocationUtils());
        MinecraftForge.EVENT_BUS.register(new LootEventHandler());
        MinecraftForge.EVENT_BUS.register(new MainMenu());
        MinecraftForge.EVENT_BUS.register(new Nametags());
        MinecraftForge.EVENT_BUS.register(new PlayerStats());
        MinecraftForge.EVENT_BUS.register(new RandomRNG());
        MinecraftForge.EVENT_BUS.register(new TitleManager());
        MinecraftForge.EVENT_BUS.register(new Voidgloom());
        MinecraftForge.EVENT_BUS.register(new WaypointEventHandler());

        FontManager.initializeFonts();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ClientCommands());
        ClientCommandHandler.instance.registerCommand(new DebugCommands());
        NCConfig.INSTANCE.preload();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            RotationUtils.updateRotations();
        }

        if (event.phase == TickEvent.Phase.END) {
            if (mc.thePlayer != null && !playerEnteredWorld) {
                playerEnteredWorld = true;
                new UpdateChecker("TLG1145", "NecronClient", VERSION).checkForUpdates();
            }
        }
    }

    public static AutoPath getAutoPath() {
        return autoPath;
    }
}
