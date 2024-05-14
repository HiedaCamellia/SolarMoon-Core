package cn.solarmoon.solarmoon_core.api.util;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityUtil {

    public static <T> T getData(ICapabilityProvider provider, Capability<T> cap) {
        return getCapability(provider, cap).orElse(null);
    }

    public static <T> LazyOptional<T> getCapability(ICapabilityProvider provider, Capability<T> cap) {
        if (provider == null || cap == null) {
            return LazyOptional.empty();
        }
        return provider.getCapability(cap);
    }
    
    public static <T> LazyOptional<T> getCapability(ICapabilityProvider provider, Capability<T> cap, Direction side) {
        if (provider == null || cap == null) {
            return LazyOptional.empty();
        }
        return provider.getCapability(cap, side);
    }

}
