package cn.solarmoon.solarmoon_core.api.common.capability;


import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.player.CountingDevice;

public interface IPlayerData {

    void tick();

    CountingDevice getCountingDevice();

}
