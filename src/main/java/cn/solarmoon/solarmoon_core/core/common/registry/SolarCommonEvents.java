package cn.solarmoon.solarmoon_core.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.BaseCommonEventRegistry;
import cn.solarmoon.solarmoon_core.core.common.event.BlockUseManager;
import cn.solarmoon.solarmoon_core.core.common.event.CounterInjuryEvent;
import cn.solarmoon.solarmoon_core.core.common.event.PlayerTickEvent;
import cn.solarmoon.solarmoon_core.core.common.event.UseRecipeEvent;

public class SolarCommonEvents extends BaseCommonEventRegistry {

    @Override
    public void addRegistry() {
        add(new CounterInjuryEvent());
        add(new UseRecipeEvent());
        add(new BlockUseManager());
        add(new PlayerTickEvent());
    }

}
