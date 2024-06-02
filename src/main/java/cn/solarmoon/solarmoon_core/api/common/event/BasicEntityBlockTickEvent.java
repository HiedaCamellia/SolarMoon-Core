package cn.solarmoon.solarmoon_core.api.common.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.Event;

public class BasicEntityBlockTickEvent extends Event {

    private final BlockEntity blockEntity;

    public BasicEntityBlockTickEvent(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

}
