package cn.solarmoon.solarmoon_core.registry.core;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * 基本的实体渲染注册表（包括方块实体等）
 */
public abstract class BaseEntityRendererRegistry {

    private EntityRenderersEvent.RegisterRenderers event;

    public abstract void addRegistry();

    public <T extends BlockEntity> void add(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider) {
        event.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
    }

    public <T extends Entity> void add(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider) {
        event.registerEntityRenderer(entityType, entityRendererProvider);
    }

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        this.event = event;
        addRegistry();
    }

    public void register(IEventBus bus) {
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::registerEntityRenderers);
        }
    }

}
