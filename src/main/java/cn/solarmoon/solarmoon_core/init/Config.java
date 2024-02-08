package cn.solarmoon.solarmoon_core.init;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import static net.minecraftforge.fml.config.ModConfig.Type.COMMON;

public class Config {

    public static final String PATH =  SolarMoonCore.MOD_ID + ".toml";
    public static ForgeConfigSpec common;

    //===

    public static ForgeConfigSpec.ConfigValue<Boolean> deBug;

    //


    //===

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        deBug = builder.comment("Used for test")
                .comment("用于调试")
                .define("deBug", false);

        //



        //

        common = builder.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(COMMON, Config.common,
                FMLPaths.CONFIGDIR.get().resolve(PATH).toString());
    }
}
