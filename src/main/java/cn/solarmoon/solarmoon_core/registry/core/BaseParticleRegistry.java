package cn.solarmoon.solarmoon_core.registry.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;

/**
 * 基本的粒子注册表
 */
public abstract class BaseParticleRegistry extends BaseObjectRegistry<ParticleType<?>> {

    protected ParticleEngine engine;

    public BaseParticleRegistry(DeferredRegister<ParticleType<?>> objects) {
        super(objects);
    }

    public abstract void addRegistry();

    public <T extends ParticleOptions> void add(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> spriteParticle) {
        engine.register(particleType, spriteParticle);
    }

    @SubscribeEvent
    public void RegisterParticleProviders(RegisterParticleProvidersEvent event) {
        this.engine = Minecraft.getInstance().particleEngine;
        addRegistry();
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::RegisterParticleProviders);
        }
    }

}
