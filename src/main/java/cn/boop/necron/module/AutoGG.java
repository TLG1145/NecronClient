package cn.boop.necron.module;

import cn.boop.necron.config.ModConfig;
import cn.boop.necron.utils.Utils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        if (ModConfig.autoGG && !PlayerStats.inSkyBlock && event.type == 0) {
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
