package cn.solarmoon.solarmoon_core.api.entry;

import cn.solarmoon.solarmoon_core.api.entry.common.SolarConfigBuilder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

public class RegisterHelper {

    public static void register(SolarConfigBuilder builder) {
        ModLoadingContext.get().registerConfig(builder.getType(), builder.build(),
                FMLPaths.CONFIGDIR.get().resolve(builder.getFileName()).toString());
    }

}
