package cn.solarmoon.solarmoon_core.core.common.registry;

import cn.solarmoon.solarmoon_core.core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.registry.AttributeEntry;

public class SolarAttributes{
    public static void register() {}

    public static final AttributeEntry THORNS = SolarMoonCore.REGISTRY.attribute()
            .id("thorns")
            .boundRanged(0, 0, 1024)
            .applyToLivingEntity()
            .build();

}
