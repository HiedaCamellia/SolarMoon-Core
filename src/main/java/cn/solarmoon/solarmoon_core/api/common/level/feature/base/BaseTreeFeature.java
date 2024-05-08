package cn.solarmoon.solarmoon_core.api.common.level.feature.base;

import cn.solarmoon.solarmoon_core.api.common.block.crop.INoLimitAgeBlock;
import cn.solarmoon.solarmoon_core.api.common.level.feature.configuration.BaseTreeConfig;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.material.Fluids;

import java.util.Set;

public abstract class BaseTreeFeature extends Feature<BaseTreeConfig> {

    public Set<BlockPos> logPositions;
    public Set<BlockPos> foliagePositions;
    public Set<BlockPos> productPositions;
    public Set<BlockPos> bottom;

    public BaseTreeFeature() {
        super(BaseTreeConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<BaseTreeConfig> context) {
        this.logPositions = Sets.newHashSet();
        this.foliagePositions = Sets.newHashSet();
        this.productPositions = Sets.newHashSet();
        this.bottom = Sets.newHashSet();

        var config = context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        //首先要有生存条件
        if (canSurvive(level, pos)) {

            addSets(context);

            //底座检查
            for (var b : bottom) {
                if (level.getBlockState(b).isAir()) return false;
            }

            //然后要是有效的树坐标且最大高度不超过世界最大高度
            for (BlockPos logPos : this.logPositions) {
                if (!TreeFeature.validTreePos(level, logPos) || logPos.getY() > level.getMaxBuildHeight())
                    return false;
            }

            for (BlockPos foliagePos : this.foliagePositions) {
                if (!TreeFeature.validTreePos(level, foliagePos) || foliagePos.getY() > level.getMaxBuildHeight())
                    return false;
            }

            //对每个坐标放置方块
            this.logPositions.forEach(logPos -> level.setBlock(logPos, config.trunkProvider.getState(random, logPos), 19));
            this.foliagePositions.forEach(foliagePos -> {
                if (TreeFeature.validTreePos(level, foliagePos)) {
                    BlockState state = config.foliageProvider.getState(random, foliagePos);
                    if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
                        state = state.setValue(BlockStateProperties.WATERLOGGED, level.isFluidAtPosition(foliagePos, (fluidState) -> fluidState.isSourceOfType(Fluids.WATER)));
                    }
                    if (!state.isAir()) {
                        level.setBlock(foliagePos, state, 19);
                        //防止一瞬间放置distance为7叶子掉落
                        state.updateNeighbourShapes(level, foliagePos, 3);
                    }
                }
            });

            //生成果子，默认在最大阶段中随机
            this.productPositions.forEach(productPos -> setProductSpawn(productPos, level, random));

        }
        return true;
    }

    /**
     * 预输入生成方块的坐标，方便检查是否应该生长
     */
    public abstract void addSets(FeaturePlaceContext<BaseTreeConfig> context);

    /**
     * 作物生成逻辑，默认随机在最大age中选一个阶段生成
     */
    public void setProductSpawn(BlockPos productPos, WorldGenLevel level, RandomSource random) {
        if (getProduct() != null && getProduct().getBlock() instanceof INoLimitAgeBlock ageBlock && getProduct().canSurvive(level, productPos)) {
            level.setBlock(productPos, getProduct().setValue(ageBlock.AGE, random.nextInt(ageBlock.getMaxAge() + 1)), 19);
        }
    }

    /**
     * 果子生成规则
     * 默认为自动在适配的方块下寻找可生存空间随机放置
     */
    public void addProducts(RandomSource random, float scale) {
        for (var leafPos : foliagePositions) {
            if (random.nextFloat() < scale) {
                productPositions.add(leafPos.below());
            }
        }
        for (var logPos : logPositions) {
            if (random.nextFloat() < scale) {
                productPositions.add(logPos.below());
            }
        }
    }

    /**
     * 预输入木头
     * 单纯根据坐标
     */
    public void addLog(BlockPos pos) {
        this.logPositions.add(pos.immutable());
    }

    /**
     * 预删除木头
     * 单纯根据坐标
     */
    public void removeLog(BlockPos pos) {
        this.logPositions.remove(pos.immutable());
    }

    /**
     * 预输入树叶
     * 单纯输入坐标
     */
    public void addFoliage(BlockPos pos) {
        this.foliagePositions.add(pos.immutable());
    }

    /**
     * 预输入作物
     * 单纯输入坐标
     */
    public void addProduct(BlockPos pos, RandomSource random, float scale) {
        if (random.nextFloat() < scale) {
            this.productPositions.add(pos.immutable());
        }
    }

    /**
     * 添加底座检查坐标
     */
    public void addBottom(BlockPos pos) {
        this.bottom.add(pos.immutable());
    }

    /**
     * 获取对应树苗方便使用
     */
    public abstract BlockState getSapling();

    /**
     * 获取产物
     */
    public abstract BlockState getProduct();

    /**
     * 设为树苗的canSurvive
     */
    public boolean canSurvive(WorldGenLevel level, BlockPos pos) {
        return this.getSapling().canSurvive(level, pos);
    }

    /**
     * 基本的树干生成
     * 同橡树
     */
    public void setBaseTrunk(BlockPos pos, int height) {
        for (int y = 0; y < height; y++) {
            logPositions.add(pos.above(y));
        }
    }

    /**
     * 基本的树叶生成
     * 同橡树，默认以坐标为最高点向下延伸
     * @param radius0 小于等于1，每减一相当于向下延伸半径递增的新树叶层
     */
    public void setBaseFoliage(BlockPos top, int radius0, RandomSource random) {
        for (int radius = radius0; radius < 3; radius++) {
            int yy1 = -2 * (radius - 1);
            int yy2 = yy1 + 1;
            for (var leafPos : BlockPos.betweenClosed(top.offset(-radius, yy1, -radius), top.offset(radius, yy2, radius))) {
                double distance = Math.sqrt(Math.pow(leafPos.getX() - top.getX(), 2) + Math.pow(leafPos.getZ() - top.getZ(), 2));
                if (distance <= radius + 0.35 || (random.nextFloat() < 0.5 && leafPos.getY() != top.getY() + 1)) {
                    addFoliage(leafPos);
                }
            }
        }
    }

}
