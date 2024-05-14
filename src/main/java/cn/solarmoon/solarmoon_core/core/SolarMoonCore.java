package cn.solarmoon.solarmoon_core.core;

import cn.solarmoon.solarmoon_core.api.SolarMoonBase;
import cn.solarmoon.solarmoon_core.api.ObjectRegistry;
import cn.solarmoon.solarmoon_core.api.util.static_utor.Debug;
import cn.solarmoon.solarmoon_core.api.util.static_utor.Translator;
import cn.solarmoon.solarmoon_core.core.client.registry.SolarLayers;
import cn.solarmoon.solarmoon_core.core.client.registry.SolarTooltips;
import cn.solarmoon.solarmoon_core.core.common.config.SolarConfig;
import cn.solarmoon.solarmoon_core.core.common.registry.*;
import cn.solarmoon.solarmoon_core.core.common.registry.ability.SolarPlaceableItems;
import cn.solarmoon.solarmoon_core.core.common.registry.ability.SolarTickers;
import cn.solarmoon.solarmoon_core.core.common.registry.ability.SolarTileDataHolders;
import net.minecraftforge.fml.common.Mod;

import static cn.solarmoon.solarmoon_core.core.SolarMoonCore.MOD_ID;


@Mod(MOD_ID)
public class SolarMoonCore extends SolarMoonBase {

    public static final String MOD_ID = "solarmoon_core";
    public static final Debug DEBUG = Debug.create("[§4曦月核心§f] ", SolarConfig.deBug);
    public static final Translator TRANSLATOR = Translator.create(MOD_ID);
    public static final ObjectRegistry REGISTRY = ObjectRegistry.create(MOD_ID);

    @Override
    public void objectsClientOnly() {
        SolarLayers.register();
    }

    @Override
    public void objects() {
        SolarItems.register();
        SolarNetPacks.register();
        SolarAttributes.register();
        SolarDamageTypes.register();
        SolarRecipes.register();
    }

    @Override
    public void eventObjectsClientOnly() {
        new SolarTooltips().register();
    }

    @Override
    public void eventObjects() {
        new SolarCommonEvents().register();
        new SolarCapabilities().register();
        new SolarCommands().register();
    }

    @Override
    public void xData() {
        SolarConfig.register();
    }

    @Override
    public void abilitiesClientOnly() {

    }

    @Override
    public void abilities() {
        SolarTickers.register();
        SolarTileDataHolders.register();
        SolarPlaceableItems.register();
    }

    @Override
    public void compats() {
        //JEI
    }

}