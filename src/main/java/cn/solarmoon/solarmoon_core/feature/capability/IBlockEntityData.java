package cn.solarmoon.solarmoon_core.feature.capability;

import cn.solarmoon.solarmoon_core.api.capability.anim_ticker.AnimTicker;

import java.util.HashMap;

public interface IBlockEntityData {

    AnimTicker getAnimTicker(int index);

    HashMap<Integer, AnimTicker> getAnimTickerMap();

    void tick();

}
