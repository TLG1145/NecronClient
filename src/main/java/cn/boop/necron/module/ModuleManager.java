package cn.boop.necron.module;

import cn.boop.necron.events.LootEventHandler;
import cn.boop.necron.events.WaypointEventHandler;
import cn.boop.necron.gui.MainMenu;
import cn.boop.necron.module.impl.*;
import cn.boop.necron.utils.DungeonUtils;
import cn.boop.necron.utils.LocationUtils;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class ModuleManager {
    private static final ArrayList<Object> modules = new ArrayList<>();
    private static final AutoPath autoPath = new AutoPath();
    
    public static void initModules() {
        // Modules
        modules.add(new AutoClicker());
        modules.add(new AutoGG());
        modules.add(new AutoPath());
        modules.add(new AutoWardrobe());
        modules.add(new BlazeDagger());
        modules.add(new ChatBlocker());
        modules.add(new ChatCommands());
        modules.add(new CropNuker());
        modules.add(new Etherwarp());
        modules.add(new EtherwarpRouter());
        modules.add(new FailSafe());
        modules.add(new FakeWipe());
        //modules.add(new GemstoneNuker());
        modules.add(new HurtCam());
        modules.add(new MainMenu());
        modules.add(new Nametags());
        modules.add(new PlayerStats());
        modules.add(new RandomRNG());
        modules.add(new TitleManager());
        modules.add(new Vampire());
        modules.add(new Voidgloom());

        // Other utils/events
        modules.add(new DungeonUtils());
        modules.add(new LocationUtils());
        modules.add(new MainMenu());
        modules.add(new LootEventHandler());
        modules.add(new WaypointEventHandler());

        for (Object module : modules) {
            MinecraftForge.EVENT_BUS.register(module);
        }
    }

    public static AutoPath getAutoPath() {
        return autoPath;
    }
}
