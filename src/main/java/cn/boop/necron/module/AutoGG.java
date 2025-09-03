package cn.boop.necron.module;

import cn.boop.necron.utils.LocationUtils;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static cn.boop.necron.config.impl.AutoGGOptionsImpl.autoGG;

public class AutoGG {
    private static final String[] WIN_MESSAGE = {
            "1st Killer",
            "1st Place - ",
            "Winner: ",
            "Winning Team -",
            "1st - ",
            "Winners: ",
            "Winner: ",
            "Winning Team: ",
            " won the game!",
            "Top Seeker: ",
            "1st Place: ",
            "Last team standing!",
            "Top Survivors",
            "Winners - ",
            "Duel - ",
            "GAME OVER!"
    };

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (autoGG && !LocationUtils.inSkyBlock && event.type == 0) {
            String message = event.message.getUnformattedText();
            for (String winMsg : WIN_MESSAGE) {
                if (message.contains(winMsg)) {
                    Utils.chatMessage("/ac gg");
                    break;
                }
            }
        }
    }
}
