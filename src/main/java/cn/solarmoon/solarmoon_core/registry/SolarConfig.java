package cn.solarmoon.solarmoon_core.registry;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.registry.base.BaseConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;

public class SolarConfig extends BaseConfig {

    public static ForgeConfigSpec.ConfigValue<Boolean> deBug;

    public SolarConfig() {
        super(SolarMoonCore.MOD_ID + ".toml", COMMON);
    }

    @Override
    public void setElement() {
        addDebug(deBug);
    }

}
