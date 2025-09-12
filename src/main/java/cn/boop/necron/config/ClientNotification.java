package cn.boop.necron.config;

import cc.polyfrost.oneconfig.gui.animations.Animation;
import cc.polyfrost.oneconfig.gui.animations.EaseInOutQuad;
import cc.polyfrost.oneconfig.internal.assets.SVGs;
import cc.polyfrost.oneconfig.renderer.asset.Icon;
import cc.polyfrost.oneconfig.renderer.asset.SVG;
import cc.polyfrost.oneconfig.utils.Notifications;
import cc.polyfrost.oneconfig.utils.gui.GuiUtils;

public class ClientNotification {
    public static final SVG ENABLED_ICON = new SVG("/assets/necron/icons/NotificationEnabled.svg");
    public static final SVG DISABLED_ICON = new SVG("/assets/necron/icons/NotificationDisabled.svg");

    public enum NotificationType {
        WARN, ENABLED, DISABLED
    }

    public static void sendNotification(String title, String message, NotificationType type, int duration) {
        Animation animation = new EaseInOutQuad(duration, 0, 1, false);
        Notifications.INSTANCE.send(
                title,
                message,
                getIcon(type),
                duration,
                () -> animation.get(GuiUtils.getDeltaTime()));
    }

    private static Icon getIcon(NotificationType type) {
        switch (type) {
            case WARN:
                return new Icon(SVGs.WARNING);
            case ENABLED:
                return new Icon(ENABLED_ICON);
            case DISABLED:
                return new Icon(DISABLED_ICON);
            default:
                return null;
        }
    }
}
