package cn.solarmoon.solarmoon_core.feature.capability;


import cn.solarmoon.solarmoon_core.api.capability.CountingDevice;

public interface IPlayerData {

    void tick();

    CountingDevice getCountingDevice();

}
