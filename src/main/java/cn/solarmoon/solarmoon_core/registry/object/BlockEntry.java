package cn.solarmoon.solarmoon_core.registry.object;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockEntry<T extends Block> {

    private final DeferredRegister<Block> blockRegister;

    private String id;
    private Supplier<T> blockSupplier;
    private RegistryObject<T> blockObject;

    public BlockEntry(DeferredRegister<Block> blockRegister) {
        this.blockRegister = blockRegister;
    }

    public BlockEntry<T> id(String id) {
        this.id = id;
        return this;
    }

    public BlockEntry<T> bound(Supplier<T> blockSupplier) {
        this.blockSupplier = blockSupplier;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <I extends T> BlockEntry<I> build() {
        this.blockObject = blockRegister.register(id, blockSupplier);
        SolarMoonCore.LOGGER.debug("BLOCK NMSL");
        return (BlockEntry<I>) this;
    }

    public T get() {
        return blockObject.get();
    }

    public RegistryObject<T> getObject() {
        return blockObject;
    }

}
