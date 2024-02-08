package cn.solarmoon.solarmoon_core.compat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本的联动类，有加载判别和便捷的内容注册~
 */
public abstract class BaseCompat {

    protected final String modId;
    protected final List<Object> modEvents;
    protected final List<DeferredRegister<?>> modObjects;

    public BaseCompat(String modId) {
        this.modId = modId;
        this.modEvents = new ArrayList<>();
        this.modObjects = new ArrayList<>();
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
