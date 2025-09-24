package cn.boop.necron.module.impl;

import cn.boop.necron.utils.LocationUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

import static cn.boop.necron.config.impl.ChatBlockerOptionsImpl.chatBlocker;

public class ChatBlocker {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!chatBlocker && !LocationUtils.inSkyBlock) return;
        String msg = event.message.getUnformattedText().toLowerCase();
        if (Pattern.matches("^\\[\\d{1,3}].*", msg)) {
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
        } else {
            if (msg.contains("this ability is on cooldown for") || msg.contains("there are blocks in the way!")) {
                event.setCanceled(true);
            }
        }
    }
}
