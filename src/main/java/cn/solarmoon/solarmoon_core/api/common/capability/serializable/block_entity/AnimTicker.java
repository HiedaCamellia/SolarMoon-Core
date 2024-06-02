package cn.solarmoon.solarmoon_core.api.common.capability.serializable.block_entity;

import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNETList;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarNetPacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;

public class AnimTicker implements INBTSerializable<CompoundTag> {

    private final BlockEntity blockEntity;
    private boolean enabled;
    private int ticks;
    private int maxTick;
    private float fixedValue;
    private FluidStack fixedFluid;

    public AnimTicker(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        maxTick = 100;
        fixedFluid = FluidStack.EMPTY;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        Level level = blockEntity.getLevel();
        if (level == null) return;
        if (level.isClientSide) {
            SolarNetPacks.SERVER.getSender().send(SolarNETList.SYNC_ANIM_TICK, blockEntity.getBlockPos(), ticks);
        }
        this.ticks = ticks;
    }

    public void setMaxTick(int maxTick) {
        Level level = blockEntity.getLevel();
        if (level == null) return;
        if (level.isClientSide) {
            SolarNetPacks.SERVER.getSender().send(SolarNETList.SYNC_ANIM_MAX_TICK, blockEntity.getBlockPos(), maxTick);
        }
        this.maxTick = maxTick;
    }

    public int getMaxTick() {
        return maxTick;
    }

    public float getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(float fixedValue) {
        Level level = blockEntity.getLevel();
        if (level == null) return;
        if (level.isClientSide) {
            SolarNetPacks.SERVER.getSender().send(SolarNETList.SYNC_ANIM_FIXED_TICK, blockEntity.getBlockPos(), fixedValue);
        }
        this.fixedValue = fixedValue;
    }

    public FluidStack getFixedFluid() {
        return fixedFluid;
    }

    public void setFixedFluid(FluidStack fixedFluid) {
        Level level = blockEntity.getLevel();
        if (level == null) return;
        if (level.isClientSide) {
            SolarNetPacks.SERVER.getSender().send(SolarNETList.SYNC_ANIM_FLUID, blockEntity.getBlockPos(), fixedFluid);
        }
        this.fixedFluid = fixedFluid;
    }

    public void tick() {
        if (enabled) {
            if (ticks < maxTick) ticks++;
            else {
                ticks = 0;
                enabled = false;
            }
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        Level level = blockEntity.getLevel();
        if (level == null) return;
        if (level.isClientSide) {
            SolarNetPacks.SERVER.getSender().send(SolarNETList.SYNC_ANIM_ENABLED, blockEntity.getBlockPos(), enabled);
        }
        this.enabled = enabled;
    }

    /**
     * 开启后，将从0tick到maxTick，然后关闭，回到0tick
     */
    public void start() {
        setEnabled(true);
        setTicks(0);
    }

    public void stop() {
        setEnabled(false);
        setTicks(0);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Enabled", enabled);
        tag.putInt("Ticks", ticks);
        tag.putInt("MaxTick", maxTick);
        tag.putFloat("FixedValue", fixedValue);
        tag.put("FixedFluid", fixedFluid.writeToNBT(new CompoundTag()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        enabled = nbt.getBoolean("Enabled");
        ticks = nbt.getInt("Ticks");
        maxTick = nbt.getInt("MaxTick");
        fixedValue = nbt.getFloat("FixedValue");
        fixedFluid = FluidStack.loadFluidStackFromNBT(nbt.getCompound("FixedFluid"));
    }

}
