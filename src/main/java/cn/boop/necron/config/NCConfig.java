package cn.boop.necron.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cn.boop.necron.Necron;
import cn.boop.necron.config.impl.*;

public class NCConfig extends Config {
    public NCConfig() {
        super(new Mod(Necron.MODNAME, ModType.THIRD_PARTY), "necronclient.json");
        initialize();
    }

    @SubConfig
    public static AutoGGOptionsImpl autoGGOptions = new AutoGGOptionsImpl();
    @SubConfig
    public static BlazeSwapPotionsImpl blazeSwapPotions = new BlazeSwapPotionsImpl();
    @SubConfig
    public static ChatCommandsOptionsImpl chatCommandsOptions = new ChatCommandsOptionsImpl();
    @SubConfig
    public static EtherwarpOptionsImpl etherwarpOptions = new EtherwarpOptionsImpl();
    @SubConfig
    public static FakeWipeOptionsImpl fakeWipeOptions = new FakeWipeOptionsImpl();
    @SubConfig
    public static HurtCameraOptionsImpl hurtCamOptions = new HurtCameraOptionsImpl();
    @SubConfig
    public static NametagsOptionsImpl nametagsOptions = new NametagsOptionsImpl();
    @SubConfig
    public static RouterOptionsImpl routerOptions = new RouterOptionsImpl();
    @SubConfig
    public static TitleOptionsImpl titleOptions = new TitleOptionsImpl();
    @SubConfig
    public static WaypointOptionsImpl waypointOptions = new WaypointOptionsImpl();


    public static final NCConfig INSTANCE = new NCConfig();
}
