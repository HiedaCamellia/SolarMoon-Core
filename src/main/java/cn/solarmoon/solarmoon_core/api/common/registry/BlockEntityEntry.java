package cn.solarmoon.solarmoon_core.api.common.registry;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class BlockEntityEntry<E extends BlockEntity> {

    private final DeferredRegister<BlockEntityType<?>> blockEntityRegister;

    private String id;
    private Supplier<Block[]> validBlocks;
    private BlockEntityType.BlockEntitySupplier<E> blockEntitySupplier;
    private Supplier<? extends BlockEntityRendererProvider<? extends E>> blockEntityRendererSupplier;
    private RegistryObject<BlockEntityType<E>> blockEntityTypeObject;

    public BlockEntityEntry(DeferredRegister<BlockEntityType<?>> blockEntityRegister) {
        this.blockEntityRegister = blockEntityRegister;
    }

    public BlockEntityEntry<E> id(String id) {
        this.id = id;
        return this;
    }

    public final BlockEntityEntry<E> validBlock(Supplier<Block> validBlock) {
        this.validBlocks = () -> new Block[]{validBlock.get()};
        return this;
    }

    public final BlockEntityEntry<E> validBlocks(Supplier<Block[]> validBlocks) {
        this.validBlocks = validBlocks;
        return this;
    }

    public BlockEntityEntry<E> bound(BlockEntityType.BlockEntitySupplier<E> blockEntitySupplier) {
        this.blockEntitySupplier = blockEntitySupplier;
        return this;
    }

    // 没办法在共线使用，BlockEntityRendererProvider只能在客户端被读取，但是我暂时没想到别的方法把这个renderer方法专门分到客户端又不用移出这个注册表
    public <T extends E> BlockEntityEntry<E> renderer(Supplier<BlockEntityRendererProvider<T>> blockEntityRendererSupplier) {
        this.blockEntityRendererSupplier = blockEntityRendererSupplier;
        return this;
    }

    public <T extends E> BlockEntityEntry<T> build() {
        this.blockEntityTypeObject = blockEntityRegister.register(id, () ->
                BlockEntityType.Builder.of(blockEntitySupplier, validBlocks.get())
                        .build(null));
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::registerEntityRenderers);
        }
        return (BlockEntityEntry<T>) this;
    }

    public BlockEntityType<E> get() {
        return blockEntityTypeObject.get();
    }

    public RegistryObject<BlockEntityType<E>> getObject() {
        return blockEntityTypeObject;
    }

    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (blockEntityRendererSupplier != null) {
            event.registerBlockEntityRenderer(get(), (BlockEntityRendererProvider<E>) blockEntityRendererSupplier.get());
        }
    }

}
