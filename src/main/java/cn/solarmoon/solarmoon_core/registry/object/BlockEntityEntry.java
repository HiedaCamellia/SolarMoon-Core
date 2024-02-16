package cn.solarmoon.solarmoon_core.registry.object;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class BlockEntityEntry<E extends BlockEntity> {

    private final DeferredRegister<BlockEntityType<?>> blockEntityRegister;

    private String id;
    private RegistryObject<? extends Block>[] validBlocks;
    private BlockEntityType.BlockEntitySupplier<E> blockEntitySupplier;
    private BlockEntityRendererProvider<E> blockEntityRenderer;
    private RegistryObject<BlockEntityType<E>> blockEntityTypeObject;

    public BlockEntityEntry(DeferredRegister<BlockEntityType<?>> blockEntityRegister) {
        this.blockEntityRegister = blockEntityRegister;
    }

    public BlockEntityEntry<E> id(String id) {
        this.id = id;
        return this;
    }

    /**
     * 需要提供延迟object来防止get null报错
     */
    @SafeVarargs
    public final BlockEntityEntry<E> validBlock(RegistryObject<? extends Block>... validBlocks) {
        this.validBlocks = validBlocks;
        return this;
    }

    public BlockEntityEntry<E> bound(BlockEntityType.BlockEntitySupplier<E> blockEntitySupplier) {
        this.blockEntitySupplier = blockEntitySupplier;
        return this;
    }

    public <T extends E> BlockEntityEntry<E> renderer(BlockEntityRendererProvider<T> blockEntityRenderer) {
        this.blockEntityRenderer = (BlockEntityRendererProvider<E>) blockEntityRenderer;
        return this;
    }

    public <T extends E> BlockEntityEntry<T> build() {
        this.blockEntityTypeObject = blockEntityRegister.register(id, () ->
                BlockEntityType.Builder.of(blockEntitySupplier, Arrays.stream(validBlocks).map(RegistryObject::get).toArray(Block[]::new))
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

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (blockEntityRenderer != null) {
            event.registerBlockEntityRenderer(get(), blockEntityRenderer);
        }
    }

}
