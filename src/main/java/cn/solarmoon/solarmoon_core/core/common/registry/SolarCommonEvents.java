package cn.solarmoon.solarmoon_core.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.BaseCommonEventRegistry;
import cn.solarmoon.solarmoon_core.core.common.event.*;

public class SolarCommonEvents extends BaseCommonEventRegistry {

    @Override
    public void addRegistry() {
        add(new CounterInjuryEvent());
        add(new UseRecipeEvent());
        add(new BlockUseManager());
        add(new PlayerTickEvent());
        add(new IAnimateTickerEvent());
        add(new BlockEntityDataHolder());
    }

}
