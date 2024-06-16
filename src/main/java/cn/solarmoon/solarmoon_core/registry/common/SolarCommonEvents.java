package cn.solarmoon.solarmoon_core.registry.common;

import cn.solarmoon.solarmoon_core.api.block_use_caller.BlockUseManager;
import cn.solarmoon.solarmoon_core.api.blockentity_util.BlockEntityDataHolder;
import cn.solarmoon.solarmoon_core.api.capability.anim_ticker.IAnimateTickerEvent;
import cn.solarmoon.solarmoon_core.api.entry.common.BaseCommonEventRegistry;
import cn.solarmoon.solarmoon_core.element.attribute.thorns.CounterInjuryEvent;
import cn.solarmoon.solarmoon_core.feature.capability.PlayerTickEvent;
import cn.solarmoon.solarmoon_core.feature.generic_recipe.use.UseRecipeEvent;

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
