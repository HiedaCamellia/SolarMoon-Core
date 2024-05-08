package cn.solarmoon.solarmoon_core.core.common.registry.ability;

import cn.solarmoon.solarmoon_core.api.common.ability.CustomPlaceableItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class SolarPlaceableItems {

    private static void addRegistry() {

    }

    private static void put(Item item, Block block) {
        CustomPlaceableItem.put(item, block);
    }

    private static void onFMLDefferSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(SolarPlaceableItems::addRegistry);
    }

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(SolarPlaceableItems::onFMLDefferSetup);
    }

}
