package cn.solarmoon.solarmoon_core.api.capability.anim_ticker;

import cn.solarmoon.solarmoon_core.api.event.BasicEntityBlockTickEvent;
import cn.solarmoon.solarmoon_core.feature.capability.IBlockEntityData;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IAnimateTickerEvent {

    @SubscribeEvent
    public void onTick(BasicEntityBlockTickEvent event) {
        BlockEntity be = event.getBlockEntity();
        be.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(IBlockEntityData::tick);
    }

}
