package cn.solarmoon.solarmoon_core.registry.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCapabilityRegistry {

    private AttachCapabilitiesEvent<?> event;

    protected Player player;
    protected Level level;

    private final List<Class<?>> IDatas;

    public BaseCapabilityRegistry() {
        this.IDatas = new ArrayList<>();
    }

    public abstract void addRegistry();

    public abstract void attachCapabilities();

    public void add(Class<?> IData) {
        IDatas.add(IData);
    }

    public void attach(ResourceLocation res, ICapabilityProvider capabilityProvider) {
        if (event.getObject() instanceof Player p) {
            this.player = p;
            event.addCapability(res, capabilityProvider);
        }
        if (event.getObject() instanceof Level l) {
            this.level = l;
            event.addCapability(res, capabilityProvider);
        }
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var data : IDatas) {
            event.register(data);
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<?> event) {
        this.event = event;
        attachCapabilities();
    }

    @SubscribeEvent
    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void register(IEventBus bus) {
        addRegistry();
        bus.addListener(this::onFMLCommonSetup);
    }

}
