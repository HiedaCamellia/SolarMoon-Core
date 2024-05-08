package cn.solarmoon.solarmoon_core.core.common.event;

import cn.solarmoon.solarmoon_core.api.common.capability.IPlayerData;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerTickEvent {

    @SubscribeEvent
    public void onPlayerTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            IPlayerData playerData = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA);
            playerData.tick();
        }
    }

}
