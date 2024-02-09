package cn.solarmoon.solarmoon_core.registry.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

/**
 * 原版实例注册表
 * @param <T> 要注册的实例类型
 */
public abstract class BaseObjectRegistry<T> {

    protected DeferredRegister<T> objects;

    public BaseObjectRegistry(DeferredRegister<T> objects) {
        this.objects = objects;
    }

    public void register(IEventBus bus) {
        objects.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
    }

}
