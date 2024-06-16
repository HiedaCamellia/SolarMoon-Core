package cn.solarmoon.solarmoon_core.api.event;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Event;

public class BlockEntityDataEvent {

    public static class Save extends Event {

        private final CompoundTag save;
        private final BlockEntity blockEntity;

        public Save(CompoundTag save, BlockEntity blockEntity) {
            this.save = save;
            this.blockEntity = blockEntity;
        }

        public CompoundTag getTag() {
            return save;
        }

        public BlockEntity getBlockEntity() {
            return blockEntity;
        }

    }

    public static class Load extends Event {

        private final CompoundTag load;
        private final BlockEntity blockEntity;

        public Load(CompoundTag load, BlockEntity blockEntity) {
            this.load = load;
            this.blockEntity = blockEntity;
        }

        public CompoundTag getTag() {
            return load;
        }

        public BlockEntity getBlockEntity() {
            return blockEntity;
        }

    }

    public static class Capability extends Event {

        private final BlockEntity blockEntity;
        private final net.minecraftforge.common.capabilities.Capability<?> cap;
        private final Direction side;
        private LazyOptional<?> returnValue;

        public <T> Capability(BlockEntity blockEntity, net.minecraftforge.common.capabilities.Capability<T> cap, Direction side,
                              LazyOptional<T> defaultValue) {
            this.blockEntity = blockEntity;
            this.cap = cap;
            this.side = side;
            this.returnValue = defaultValue;
        }

        public BlockEntity getBlockEntity() {
            return blockEntity;
        }

        public net.minecraftforge.common.capabilities.Capability<?> getCap() {
            return cap;
        }

        public Direction getSide() {
            return side;
        }

        @SuppressWarnings("unchecked")
        public <T> LazyOptional<T> getReturnValue() {
            return (LazyOptional<T>) returnValue;
        }

        public <T> void setReturnValue(LazyOptional<T> returnValue) {
            this.returnValue = returnValue;
        }

    }

}
