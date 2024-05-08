package cn.solarmoon.solarmoon_core.api.client.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Function;

/**
 * 基本的tooltip渲染层注册表<br/>
 * 需要实现gather以收集入实际应用
 */
public abstract class BaseTooltipRegistry {

    private RegisterClientTooltipComponentFactoriesEvent event;
    protected RenderTooltipEvent.GatherComponents gatherEvent;
    protected ItemStack itemStack;

    /**
     * 使用add(tooltip.class, renderer)来注册
     */
    public abstract void addRegistry();

    public <T extends TooltipComponent> void add(Class<T> tClass, Function<? super T, ? extends ClientTooltipComponent> factory) {
        event.register(tClass, factory);
    }

    /**
     * 使用gather来设置收集内容，使用gatherEvent来获取实际信息（默认有个itemStack可用）<br/>
     */
    public abstract void gatherComponents();

    public void gather(Function<ItemStack, TooltipComponent> component) {
        gatherEvent.getTooltipElements().add(Either.right(component.apply(itemStack)));
    }

    /**
     * @param component 把内容收集到第一个空行中
     */
    public void gatherToFirstEmpty(Function<ItemStack, TooltipComponent> component) {
        int length = gatherEvent.getTooltipElements().size();
        //顺序寻找第一个空行，没找到就加在末尾
        for (int i = 0; i <= length; i++) {
            if (i < length) {
                var either = gatherEvent.getTooltipElements().get(i);
                if (either.left().isPresent() && either.left().get().getString().isEmpty()) {
                    gather(component, i);
                    break;
                }
            } else gatherEvent.getTooltipElements().add(Either.right(component.apply(itemStack)));
        }
    }

    public void gather(Function<ItemStack, TooltipComponent> component, int index) {
        gatherEvent.getTooltipElements().add(index, Either.right(component.apply(itemStack)));
    }

    @SubscribeEvent
    public void tooltipRegister(RegisterClientTooltipComponentFactoriesEvent event) {
        this.event = event;
        addRegistry();
    }

    /**
     * tooltip渲染实际应用（通过event传入item信息）
     */
    @SubscribeEvent
    public void gatherTooltips(RenderTooltipEvent.GatherComponents event) {
        gatherEvent = event;
        itemStack = event.getItemStack();
        gatherComponents();
    }

    @SubscribeEvent
    public void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::tooltipRegister);
            bus.addListener(this::onFMLClientSetupEvent);
        }
    }

}
