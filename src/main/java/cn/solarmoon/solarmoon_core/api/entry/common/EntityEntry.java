package cn.solarmoon.solarmoon_core.api.entry.common;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unchecked")
public class EntityEntry<E extends Entity> {

    private final DeferredRegister<EntityType<?>> entityRegister;
    private final String modId;

    private String id;
    private EntityRendererProvider<E> entityRenderer;
    private RegistryObject<EntityType<?>> entityObject;
    private EntityType.Builder<?> builder;

    public EntityEntry(DeferredRegister<EntityType<?>> entityRegister, String modId) {
        this.entityRegister = entityRegister;
        this.modId = modId;
    }

    public EntityEntry<E> id(String id) {
        this.id = id;
        return this;
    }

    public <I extends Entity> EntityEntry<E> builder(EntityType.Builder<I> builder) {
        this.builder = builder;
        return this;
    }

    public <I extends Entity> EntityEntry<E> renderer(EntityRendererProvider<I> entityRenderer) {
        this.entityRenderer = (EntityRendererProvider<E>) entityRenderer;
        return this;
    }

    public <I extends E> EntityEntry<I> build() {
        this.entityObject = entityRegister.register(id, () -> builder
                .build( modId + id));
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::registerEntityRenderers);
        }
        return (EntityEntry<I>) this;
    }

    public EntityType<E> get() {
        return (EntityType<E>) entityObject.get();
    }

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (entityRenderer != null) {
            event.registerEntityRenderer(get(), entityRenderer);
        }
    }

}
