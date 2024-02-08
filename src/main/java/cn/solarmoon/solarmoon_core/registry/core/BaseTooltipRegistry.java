package cn.solarmoon.solarmoon_core.registry.core;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Function;

/**
 * 基本的tooltip渲染层注册表
 */
public abstract class BaseTooltipRegistry {

    private RegisterClientTooltipComponentFactoriesEvent event;

    /**
     * 使用add(tooltip.class, renderer)来注册
     */
    public abstract void addRegistry();

    public <T extends TooltipComponent> void add(Class<T> tClass, Function<? super T, ? extends ClientTooltipComponent> factory) {
        event.register(tClass, factory);
    }

    @SubscribeEvent
    public void tooltipRegister(RegisterClientTooltipComponentFactoriesEvent event) {
        this.event = event;
        addRegistry();
    }

    public void register(IEventBus bus) {
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::tooltipRegister);
        }
    }

}
