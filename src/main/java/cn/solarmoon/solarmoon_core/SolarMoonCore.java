package cn.solarmoon.solarmoon_core;

import cn.solarmoon.solarmoon_core.registry.SolarCapabilities;
import cn.solarmoon.solarmoon_core.registry.SolarConfig;
import cn.solarmoon.solarmoon_core.registry.SolarNetPacks;
import cn.solarmoon.solarmoon_core.registry.core.ObjectRegistry;
import cn.solarmoon.solarmoon_core.registry.object.test;
import cn.solarmoon.solarmoon_core.registry.object.testt;
import cn.solarmoon.solarmoon_core.util.static_utor.Debug;
import cn.solarmoon.solarmoon_core.util.static_utor.Translator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.solarmoon.solarmoon_core.SolarMoonCore.MOD_ID;
import static cn.solarmoon.solarmoon_core.registry.object.testt.APPLE_TREE;


@Mod(MOD_ID)
public class SolarMoonCore {

    public static final String MOD_ID = "solarmoon_core";
    public static final Logger LOGGER = LoggerFactory.getLogger("SolarMoon Core");
    public static final Debug DEBUG = new Debug("[§4曦月核心§f] ", SolarConfig.deBug);
    public static final Translator TRANSLATOR = new Translator(MOD_ID);
    public static final ObjectRegistry REGISTRY = new ObjectRegistry(MOD_ID).create();

    public SolarMoonCore() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        new SolarCapabilities().register(bus);
        new SolarConfig().register();

        SolarNetPacks.INSTANCE.register();

    }

}