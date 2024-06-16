package cn.solarmoon.solarmoon_core.registry.common;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.entry.common.NetPackEntry;
import cn.solarmoon.solarmoon_core.network.BaseClientPackHandler;
import cn.solarmoon.solarmoon_core.network.BaseServerPackHandler;

public class SolarNetPacks {
    public static void register() {}

    public static final NetPackEntry CLIENT = SolarMoonCore.REGISTRY.netPack()
            .id("client")
            .side(NetPackEntry.Side.CLIENT)
            .addHandler(new BaseClientPackHandler())
            .build();

    public static final NetPackEntry SERVER = SolarMoonCore.REGISTRY.netPack()
            .id("server")
            .side(NetPackEntry.Side.SERVER)
            .addHandler(new BaseServerPackHandler())
            .build();

}
