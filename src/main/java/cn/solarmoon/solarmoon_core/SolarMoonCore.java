package cn.solarmoon.solarmoon_core;

import cn.solarmoon.solarmoon_core.registry.Capabilities;
import cn.solarmoon.solarmoon_core.registry.Packs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.solarmoon.solarmoon_core.SolarMoonCore.MOD_ID;


@Mod(MOD_ID)
public class SolarMoonCore {

    public static final String MOD_ID = "solarmoon_core";
    public static final Logger LOGGER = LoggerFactory.getLogger("SolarMoon Core");

    public SolarMoonCore() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        new Packs().register();
        new Capabilities().register(bus);

    }

}