package cn.solarmoon.solarmoon_core.compat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本的联动类，有加载判别和便捷的内容注册~<br/>
 * 目前支持快速注册非注册类事件和原版类
 */
public abstract class BaseCompat {

    private final String modId;
    private final List<Object> modEvents;
    private final List<DeferredRegister<?>> modObjects;

    public BaseCompat(String modId) {
        this.modId = modId;
        this.modEvents = new ArrayList<>();
        this.modObjects = new ArrayList<>();
    }

    public void add(Object object) {
        modEvents.add(object);
    }

    public void add(DeferredRegister<?> deferredRegister) {
        modObjects.add(deferredRegister);
    }

    public String getModId() {
        return modId;
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(modId);
    }

    /**
     * 覆写这个以添加要注册的内容
     */
    public abstract void addRegistry();

    public void register(IEventBus bus) {
        addRegistry();
        if (isLoaded()) {
            for(Object event : modEvents) {
                MinecraftForge.EVENT_BUS.register(event);
            }
            for (DeferredRegister<?> object : modObjects) {
                object.register(bus);
            }
        }
    }

}
