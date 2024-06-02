package cn.solarmoon.solarmoon_core.core.common.event;

import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.api.common.capability.IBlockEntityData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.block_entity.AnimTicker;
import cn.solarmoon.solarmoon_core.api.common.event.BasicEntityBlockTickEvent;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IAnimateTickerEvent {

    @SubscribeEvent
    public void onTick(BasicEntityBlockTickEvent event) {
        BlockEntity be = event.getBlockEntity();
        if (be instanceof IBlockEntityAnimateTicker ticker) {
            //动画计时器
            int ticks = ticker.getTicks();
            if (ticks <= 100) ticks++;
            else ticks = 0;
            ticker.setTicks(ticks);
        }
        be.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(IBlockEntityData::tick);
    }

}
