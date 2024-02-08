package cn.solarmoon.solarmoon_core.registry;


import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.common.capability.IPlayerData;
import cn.solarmoon.solarmoon_core.common.capability.PlayerData;
import cn.solarmoon.solarmoon_core.registry.core.BaseCapabilityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class Capabilities extends BaseCapabilityRegistry {

    public static final Capability<IPlayerData> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>(){});

    @Override
    public void addRegistry() {
        add(IPlayerData.class);
    }

    @Override
    public void attachCapabilities() {
        attach(new ResourceLocation(SolarMoonCore.MOD_ID, "player_data"), new PlayerData(player));
    }

}
