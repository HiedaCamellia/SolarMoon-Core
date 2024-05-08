package cn.solarmoon.solarmoon_core.api.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class SolarConfigBuilder extends ForgeConfigSpec.Builder {

    private String modId;
    private ModConfig.Type type;

    public SolarConfigBuilder modId(String modId) {
        this.modId = modId;
        return this;
    }

    public SolarConfigBuilder side(ModConfig.Type type) {
        this.type = type;
        return this;
    }

    public String getModId() {
        return modId;
    }

    public ModConfig.Type getType() {
        return type;
    }

    public String getFileName() {
        return getModId() + ".toml";
    }

    public static SolarConfigBuilder create() {
        return new SolarConfigBuilder();
    }

}
