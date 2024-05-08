package cn.solarmoon.solarmoon_core.core.common.config;

import cn.solarmoon.solarmoon_core.core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.config.SolarConfigBuilder;
import cn.solarmoon.solarmoon_core.api.util.RegisterHelper;
import net.minecraftforge.common.ForgeConfigSpec;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;

public class SolarConfig {

    public static final SolarConfigBuilder builder = SolarConfigBuilder.create()
            .modId(SolarMoonCore.MOD_ID)
            .side(COMMON);

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
