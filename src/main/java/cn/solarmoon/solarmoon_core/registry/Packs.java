package cn.solarmoon.solarmoon_core.registry;


import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.network.NetworkPack;
import cn.solarmoon.solarmoon_core.network.handler.BaseClientPackHandler;
import cn.solarmoon.solarmoon_core.registry.core.BasePackRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Packs extends BasePackRegistry {

    public static final NetworkPack BASE_CLIENT_PACK = new NetworkPack(new ResourceLocation(SolarMoonCore.MOD_ID, "base_client"), NetworkPack.Side.CLIENT)
            .addHandler(new BaseClientPackHandler());

    @Override
    public void addRegistry() {
        add(BASE_CLIENT_PACK);
    }

    private static final Map<ResourceLocation, NetworkPack> packMap = new HashMap<>();

    public static NetworkPack get(ResourceLocation name) {
        return packMap.get(name);
    }

    public static Collection<NetworkPack> get() {
        return packMap.values();
    }

    public static void put(ResourceLocation name, NetworkPack pack) {
        packMap.put(name, pack);
    }

}
