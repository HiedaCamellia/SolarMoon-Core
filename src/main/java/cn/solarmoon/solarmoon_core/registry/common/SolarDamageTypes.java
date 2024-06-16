package cn.solarmoon.solarmoon_core.registry.common;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.entry.common.DamageTypeEntry;

public class SolarDamageTypes {
    public static void register() {}

    public final static DamageTypeEntry THORNS = SolarMoonCore.REGISTRY.damageType()
            .id("thorns")
            .build();

}
