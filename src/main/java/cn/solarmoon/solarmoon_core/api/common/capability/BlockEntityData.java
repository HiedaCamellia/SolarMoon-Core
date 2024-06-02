package cn.solarmoon.solarmoon_core.api.common.capability;

import cn.solarmoon.solarmoon_core.api.common.capability.serializable.block_entity.AnimTicker;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockEntityData implements ICapabilitySerializable<CompoundTag>, IBlockEntityData {

    private final LazyOptional<IBlockEntityData> blockEntityData;
    private final BlockEntity blockEntity;

    private final AnimTicker animTicker;


    public BlockEntityData(BlockEntity blockEntity) {
        this.blockEntityData = LazyOptional.of(() -> this);
        this.blockEntity = blockEntity;

        animTicker = new AnimTicker(blockEntity);
    }

    public void tick() {
        animTicker.tick();
    }

    @Override
    public AnimTicker getAnimTicker() {
        return animTicker;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("AnimTicker", animTicker.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        animTicker.deserializeNBT(nbt.getCompound("AnimTicker"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == SolarCapabilities.BLOCK_ENTITY_DATA) {
            return blockEntityData.cast();
        }
        return LazyOptional.empty();
    }

}
