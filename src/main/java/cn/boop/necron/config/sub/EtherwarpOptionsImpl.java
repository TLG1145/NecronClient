package cn.boop.necron.config.sub;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cn.boop.necron.config.ModConfig;

public class EtherwarpOptionsImpl extends ModConfig {
    public EtherwarpOptionsImpl() {
        super("Etherwarp", "necron/etherwarp.json");
        initialize();
    }

    @Switch(name = "Enabled", description = "Left click to use Etherwarp ability")
    public static boolean etherwarp = false;
}
