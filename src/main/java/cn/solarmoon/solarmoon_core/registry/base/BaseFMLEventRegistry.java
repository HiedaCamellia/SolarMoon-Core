package cn.solarmoon.solarmoon_core.registry.base;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 方便快捷的事件注册表
 */
public abstract class BaseFMLEventRegistry {

    private final List<Object> clientEvents;
    private final List<Object> commonEvents;
    private final Side side;

    /**
     * @param side CLIENT, COMMON
     */
    public BaseFMLEventRegistry(Side side) {
        this.clientEvents = new ArrayList<>();
        this.commonEvents = new ArrayList<>();
        this.side = side;
    }

    /**
     * 用add()添加事件类型
     */
    public abstract void addRegistry();

    public void add(Object object) {
        if (side == Side.CLIENT) {
            clientEvents.add(object);
        } else if (side == Side.COMMON) {
            commonEvents.add(object);
        }
    }

    /**
     * 注册到总线
     * @param bus 线
     */
    public void register(IEventBus bus) {
        addRegistry();
        if (side == Side.CLIENT) {
            bus.addListener(this::onFMLClientSetupEvent);
        } else if (side == Side.COMMON) {
            bus.addListener(this::onFMLCommonSetupEvent);
        }
    }

    @SubscribeEvent
    public void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        for (Object e : clientEvents) {
            MinecraftForge.EVENT_BUS.register(e);
        }
    }

    @SubscribeEvent
    public void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        for (Object e : commonEvents) {
            MinecraftForge.EVENT_BUS.register(e);
        }
    }

    public enum Side {
        CLIENT,
        COMMON
    }

}
