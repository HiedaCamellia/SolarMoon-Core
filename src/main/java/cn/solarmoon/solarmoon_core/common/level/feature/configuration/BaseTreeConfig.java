package cn.solarmoon.solarmoon_core.common.level.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;

/**
 * 尽量没有多余内容的树配置设定
 */
public class BaseTreeConfig implements FeatureConfiguration {

    public final BlockStateProvider trunkProvider;
    public final BlockStateProvider foliageProvider;
    public final FancyTrunkPlacer trunkPlacer;

    public BaseTreeConfig(BlockStateProvider log, BlockStateProvider leaves, FancyTrunkPlacer trunkPlacer) {
        this.trunkProvider = log;
        this.foliageProvider = leaves;
        this.trunkPlacer = trunkPlacer;
    }

    public static final Codec<BaseTreeConfig> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            BlockStateProvider.CODEC
                    .fieldOf("trunk_provider").forGetter((config) -> config.trunkProvider),
                    BlockStateProvider.CODEC
                            .fieldOf("foliage_provider").forGetter((config) -> config.foliageProvider),
                    FancyTrunkPlacer.CODEC
                            .fieldOf("trunk_placer").forGetter((config) -> config.trunkPlacer))
            .apply(builder, BaseTreeConfig::new));

    public static BaseTreeConfig createBaseTree(Block log, Block leaves, int baseHeight, int heightRandomA) {
        return new BaseTreeConfig(BlockStateProvider.simple(log), BlockStateProvider.simple(leaves), new FancyTrunkPlacer(baseHeight, heightRandomA, 0));
    }

}
