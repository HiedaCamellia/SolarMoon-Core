package cn.solarmoon.solarmoon_core.api.entry.client;

import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * 基本的gui覆盖层注册类
 */
public abstract class BaseGuiRegistry {

    public abstract void registerGUIs(RegisterGuiOverlaysEvent event);

    @SubscribeEvent
    public void registerGUI(RegisterGuiOverlaysEvent event) {
        registerGUIs(event);
    }

    public void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::registerGUI);
        }
    }

}
