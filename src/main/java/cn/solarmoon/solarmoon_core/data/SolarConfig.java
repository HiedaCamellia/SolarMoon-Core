package cn.solarmoon.solarmoon_core.data;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.entry.RegisterHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.SolarConfigBuilder;
import net.minecraftforge.common.ForgeConfigSpec;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;

public class SolarConfig {

    public static final SolarConfigBuilder builder = SolarMoonCore.REGISTRY.configBuilder(COMMON);

    public static final ForgeConfigSpec.ConfigValue<Boolean> deBug;

    static {
        deBug = builder.comment("Used for test")
                .comment("用于调试")
                .define("deBug", false);
    }

    public static void register() {
        RegisterHelper.register(builder);
    }

}
