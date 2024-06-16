package cn.solarmoon.solarmoon_core.api.entry.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseClientEventRegistry {

    private final List<Object> clientEvents;

    public BaseClientEventRegistry() {
        this.clientEvents = new ArrayList<>();
    }

    /**
     * 用add()添加事件类型
     */
    public abstract void addRegistry();

    public void add(Object object) {
        clientEvents.add(object);
    }

    /**
     * 注册到总线
     */
    public void register() {
        if (FMLEnvironment.dist.isClient()) {
            addRegistry();
            IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
            bus.addListener(this::onFMLClientSetupEvent);
        }
    }

    @SubscribeEvent
    public void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        for (Object e : clientEvents) {
            MinecraftForge.EVENT_BUS.register(e);
        }
    }

}
