package cn.solarmoon.solarmoon_core.registry.common;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.entry.common.AttributeEntry;

public class SolarAttributes{
    public static void register() {}

    public static final AttributeEntry THORNS = SolarMoonCore.REGISTRY.attribute()
            .id("thorns")
            .boundRanged(0, 0, 1024)
            .applyToLivingEntity()
            .build();

}
