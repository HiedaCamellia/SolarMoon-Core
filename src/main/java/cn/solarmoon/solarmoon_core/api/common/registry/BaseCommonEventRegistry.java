package cn.solarmoon.solarmoon_core.api.common.registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommonEventRegistry {

    private final List<Object> commonEvents;

    public BaseCommonEventRegistry() {
        this.commonEvents = new ArrayList<>();
    }

    /**
     * 用add()添加事件类型
     */
    public abstract void addRegistry();

    public void add(Object object) {
        commonEvents.add(object);
    }

    /**
     * 注册到总线
     */
    public void register() {
        addRegistry();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onFMLCommonSetupEvent);
    }

    @SubscribeEvent
    public void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        for (Object e : commonEvents) {
            MinecraftForge.EVENT_BUS.register(e);
        }
    }

}
