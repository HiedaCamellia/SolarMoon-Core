package cn.solarmoon.solarmoon_core.core.common.registry;

import cn.solarmoon.solarmoon_core.core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.core.network.BaseClientPackHandler;
import cn.solarmoon.solarmoon_core.api.common.registry.NetPackEntry;

public class SolarNetPacks {
    public static void register() {}

    public static final NetPackEntry BASE_CLIENT_PACK = SolarMoonCore.REGISTRY.netPack()
            .id("base_client")
            .side(NetPackEntry.Side.CLIENT)
            .addHandler(new BaseClientPackHandler())
            .build();

}
