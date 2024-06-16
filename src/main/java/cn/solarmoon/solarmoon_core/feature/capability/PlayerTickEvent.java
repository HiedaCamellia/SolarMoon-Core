package cn.solarmoon.solarmoon_core.feature.capability;

import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerTickEvent {

    @SubscribeEvent
    public void onPlayerTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.getCapability(SolarCapabilities.PLAYER_DATA).ifPresent(IPlayerData::tick);
        }
    }

}
