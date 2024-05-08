package cn.solarmoon.solarmoon_core.api.common.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.util.BiConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * 基于mixin实现远程设置特定blockentity的数据交互流程
 */
public class BlockEntityDataHolder<T> {

    public static final List<BlockEntityDataHolder<?>> ALL = new ArrayList<>();

    private final Class<T> type;
    private BiConsumer<BlockEntity, CompoundTag> saveConsumer;
    private BiConsumer<BlockEntity, CompoundTag> loadConsumer;
    private BiFunction<BlockEntity, Capability<?>, LazyOptional<?>> capFunction;

    public BlockEntityDataHolder(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public BlockEntityDataHolder<T> save(BiConsumer<T, CompoundTag> consumer) {
        this.saveConsumer = (blockEntity, nbt) -> {
            if (type.isInstance(blockEntity)) {
                consumer.accept((T) blockEntity, nbt);
            }
        };
        return this;
    }

    @SuppressWarnings("unchecked")
    public BlockEntityDataHolder<T> load(BiConsumer<T, CompoundTag> consumer) {
        this.loadConsumer = (blockEntity, nbt) -> {
            if (type.isInstance(blockEntity)) {
                consumer.accept((T) blockEntity, nbt);
            }
        };
        return this;
    }

    @SuppressWarnings("unchecked")
    public BlockEntityDataHolder<T> capability(BiFunction<T, Capability<?>, LazyOptional<?>> capFunction) {
        this.capFunction = (blockEntity, cap) -> {
            if (type.isInstance(blockEntity)) {
                return capFunction.apply((T) blockEntity, cap);
            }
            return null;
        };
        return this;
    }

    public BlockEntityDataHolder<T> build() {
        ALL.add(this);
        return this;
    }

    public Optional<BiConsumer<BlockEntity, CompoundTag>> getSaveConsumer() {
        return Optional.ofNullable(saveConsumer);
    }

    public Optional<BiConsumer<BlockEntity, CompoundTag>> getLoadConsumer() {
        return Optional.ofNullable(loadConsumer);
    }

    public Optional<BiFunction<BlockEntity, Capability<?>, LazyOptional<?>>> getCapFunction() {
        return Optional.ofNullable(capFunction);
    }

    public Class<T> getType() {
        return type;
    }

    public static <B> BlockEntityDataHolder<B> create(Class<B> type) {
        return new BlockEntityDataHolder<>(type);
    }

}
