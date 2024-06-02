package cn.solarmoon.solarmoon_core.core.common.registry;


import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.capability.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


public class SolarCapabilities {

    public static final Capability<IPlayerData> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IItemStackData> ITEMSTACK_DATA = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IBlockEntityData> BLOCK_ENTITY_DATA = CapabilityManager.get(new CapabilityToken<>(){});

    @SubscribeEvent
    public void onAttachPlayerCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            event.addCapability(id("player_data"), new PlayerData(player));
        }
    }

    @SubscribeEvent
    public void onAttachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        event.addCapability(id("itemstack_data"), new ItemStackData(event.getObject()));
    }

    @SubscribeEvent
    public void onAttachTileCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        event.addCapability(id("block_entity_data"), new BlockEntityData(event.getObject()));
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IPlayerData.class);
        event.register(IItemStackData.class);
        event.register(IBlockEntityData.class);
    }

    public void onFMLCommonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onFMLCommonSetup);
    }

    private ResourceLocation id(String id) {
        return new ResourceLocation(SolarMoonCore.MOD_ID, id);
    }

}
