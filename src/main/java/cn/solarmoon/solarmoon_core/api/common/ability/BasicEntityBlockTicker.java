package cn.solarmoon.solarmoon_core.api.common.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 用于给BasicEntityBlock添加自定义tick方法
 */
public class BasicEntityBlockTicker<T> {

    public static final List<BasicEntityBlockTicker<?>> ALL = new ArrayList<>();

    private final Class<T> type;
    private Consumer<BlockEntity> ticker;
    private BiConsumer<BlockEntity, CompoundTag> synchronizer;

    public BasicEntityBlockTicker(Class<T> type) {
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public BasicEntityBlockTicker<T> add(Consumer<Pair<T, BlockEntity>> consumer) {
        ticker = tile -> {
            if (type.isInstance(tile)) {
                consumer.accept(new Pair<>((T) tile, tile));
            }
        };
        return this;
    }

    @SuppressWarnings("unchecked")
    public BasicEntityBlockTicker<T> addSynchronizer(BiConsumer<Pair<T, BlockEntity>, CompoundTag> tagConsumer) {
        synchronizer = (tile, nbt) -> {
            if (type.isInstance(tile)) {
                tagConsumer.accept(new Pair<>((T) tile, tile), nbt);
            }
        };
        return this;
    }

    public BasicEntityBlockTicker<T> build() {
        ALL.add(this);
        return this;
    }

    public Optional<BiConsumer<BlockEntity, CompoundTag>> getSynchronizer() {
        return Optional.ofNullable(synchronizer);
    }

    public Optional<Consumer<BlockEntity>> getTicker() {
        return Optional.ofNullable(ticker);
    }

    public static <B> BasicEntityBlockTicker<B> create(Class<B> type) {
        return new BasicEntityBlockTicker<>(type);
    }

}
