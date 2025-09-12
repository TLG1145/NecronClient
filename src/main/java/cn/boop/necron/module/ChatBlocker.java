package cn.boop.necron.module;

import cn.boop.necron.utils.LocationUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static cn.boop.necron.config.impl.ChatBlockerOptionsImpl.chatBlocker;

public class ChatBlocker {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!chatBlocker && !LocationUtils.inSkyBlock) return;
        String msg = event.message.getUnformattedText().toLowerCase();
        if (msg.startsWith("[")) {
            if (msg.contains("map")
                    || msg.contains("my ah")
                    || msg.contains("visit ")
                    || msg.contains("lowbal")
                    || msg.contains("lb")
                    || msg.contains("free")
                    || msg.contains("buy")
                    || msg.contains("sell")
                    || msg.contains("discord")) {
            event.setCanceled(true);
            }
        }
    }
}
