package cn.solarmoon.solarmoon_core.api.common.capability;

import cn.solarmoon.solarmoon_core.api.common.capability.serializable.block_entity.AnimTicker;

public interface IBlockEntityData {

    AnimTicker getAnimTicker();

    void tick();

}
