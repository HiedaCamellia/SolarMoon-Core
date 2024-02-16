package cn.solarmoon.solarmoon_core.registry;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.network.handler.BaseClientPackHandler;
import cn.solarmoon.solarmoon_core.registry.core.IRegister;
import cn.solarmoon.solarmoon_core.registry.object.NetPackEntry;

public enum SolarPacks implements IRegister {
    INSTANCE;

    public static final NetPackEntry BASE_CLIENT_PACK = SolarMoonCore.REGISTRY.netPack()
            .id("base_client")
            .side(NetPackEntry.Side.CLIENT)
            .addHandler(new BaseClientPackHandler())
            .build();

}
