package cn.solarmoon.solarmoon_core.core.common.registry;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.registry.DamageTypeEntry;

public class SolarDamageTypes {
    public static void register() {}

    public final static DamageTypeEntry THORNS = SolarMoonCore.REGISTRY.damageType()
            .id("thorns")
            .build();
}
