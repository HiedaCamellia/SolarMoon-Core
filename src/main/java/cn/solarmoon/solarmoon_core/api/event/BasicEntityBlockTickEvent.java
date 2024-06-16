package cn.solarmoon.solarmoon_core.api.event;

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
