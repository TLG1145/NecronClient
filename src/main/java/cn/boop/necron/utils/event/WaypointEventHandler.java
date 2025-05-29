package cn.boop.necron.utils.event;

import cn.boop.necron.config.ModConfig;
import cn.boop.necron.module.Waypoint;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WaypointEventHandler {
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (ModConfig.waypoints) {
            Waypoint.renderWaypoints(event.partialTicks);
        }
    }
}
