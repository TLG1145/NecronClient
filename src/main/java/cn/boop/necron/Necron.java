package cn.boop.necron;

import cn.boop.necron.command.ClientCommands;
import cn.boop.necron.config.ModConfig;
import cn.boop.necron.gui.MainMenu;
import cn.boop.necron.module.*;
import cn.boop.necron.utils.event.MouseAimHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = Necron.MODID,
        name = Necron.MODNAME,
        version = Necron.VERSION,
        acceptedMinecraftVersions = "1.8.9",
        clientSideOnly = true
)

public class Necron {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static final String MODID = "necronclient";
    public static final String MODNAME = "Necron";
    public static final String VERSION = "0.0.1";
    public static final String CHAT_PREFIX = "§bNecron §8»§r§7 ";


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MainMenu());
        MinecraftForge.EVENT_BUS.register(new ChatCommands());
        MinecraftForge.EVENT_BUS.register(new HurtCam());
        MinecraftForge.EVENT_BUS.register(new TitleManager());
        MinecraftForge.EVENT_BUS.register(new MldNametags());
        MinecraftForge.EVENT_BUS.register(new MouseAimHandler());
        MinecraftForge.EVENT_BUS.register(new AimTest());
        MinecraftForge.EVENT_BUS.register(new AutoClicker());
        MinecraftForge.EVENT_BUS.register(new PlayerStats());
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ClientCommands());
        ModConfig.INSTANCE.preload();
    }

}
