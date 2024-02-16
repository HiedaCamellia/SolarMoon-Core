package cn.solarmoon.solarmoon_core.registry.base;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

public abstract class BaseConfig {

    private final String path;
    private final ModConfig.Type type;

    protected ForgeConfigSpec common;
    protected ForgeConfigSpec.Builder builder;

    public BaseConfig(String path, ModConfig.Type type) {
        this.path = path;
        this.type = type;
        builder = new ForgeConfigSpec.Builder();
    }

    /**
     * 快速添加模版
     */
    public <T> void add(ForgeConfigSpec.ConfigValue<T> value, String commentE, String commentC, String verify, T define) {
        value = builder.comment(commentE).comment(commentC).define(verify, define);
    }

    public void addDebug(ForgeConfigSpec.ConfigValue<Boolean> deBug) {
        deBug = builder.comment("Used for test")
                .comment("用于调试")
                .define("deBug", false);
    }

    /**
     * 使用static ForgeConfigSpec.ConfigValue xxx和builder来注入元素
     */
    public abstract void setElement();

    public void register() {
        setElement();
        common = builder.build();
        ModLoadingContext.get().registerConfig(type, common,
                FMLPaths.CONFIGDIR.get().resolve(path).toString());
    }

}
