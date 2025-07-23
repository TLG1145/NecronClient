package cn.boop.necron.config.sub;

import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cn.boop.necron.config.ModConfig;

public class TitleOptionsImpl extends ModConfig {
    public TitleOptionsImpl() {
        super("Title Manager", "necron/titlemanager.json");
        initialize();
    }

    @Switch(name = "Change Title", description = "Use custom window title")
    public static boolean title = true;
    @Switch(name = "Change Icon", description = "Use Custom window icon")
    public static boolean icon = true;
    @Switch(name = "Use your title text", size = 2)
    public static boolean urTitle = false;
    @Text(name = "Title Text", placeholder = "Text here")
    public static String titleText = "";
}
