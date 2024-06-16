package cn.solarmoon.solarmoon_core.feature.capability;


import cn.solarmoon.solarmoon_core.api.capability.CountingDevice;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 与玩家绑定的数据
 */
public class PlayerData implements ICapabilitySerializable<CompoundTag>, IPlayerData {

    private final Player player;
    private final LazyOptional<IPlayerData> playerData;

    private final CountingDevice countingDevice;

    public PlayerData(Player player) {
        this.player = player;
        this.playerData = LazyOptional.of(() -> this);

        this.countingDevice = new CountingDevice();
    }

    @Override
    public void tick() {
        countingDevice.tick();
    }

    @Override
    public CountingDevice getCountingDevice() {
        return countingDevice;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("CountingDevice", countingDevice.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        countingDevice.deserializeNBT(nbt.getCompound("CountingDevice"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == SolarCapabilities.PLAYER_DATA) {
            return playerData.cast();
        }
        return LazyOptional.empty();
    }

    public Player getPlayer() {
        return player;
    }

}
