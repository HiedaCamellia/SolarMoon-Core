package cn.solarmoon.solarmoon_core.api.block_base;

import cn.solarmoon.solarmoon_core.api.matcher.BlockMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

/**
 * 悬挂式浆果丛类型的作物，成长满100才能摘，并且返回age0（一般用于水果）
 */
public abstract class BaseHangingBushCropBlock extends BaseBushCropBlock {

    public BaseHangingBushCropBlock() {
        super(BlockBehaviour.Properties
                .copy(Blocks.SWEET_BERRY_BUSH)
        );
    }

    /**
     * 自定属性
     */
    public BaseHangingBushCropBlock(Properties properties) {
        super(properties);
    }

    /**
     * 设置可放置（种植）的地方 <br/>
     * 一般情况下请修改canSurviveBlock
     */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return canSurviveBlock().isBlockEqual(state)
                && ( level.getBlockState(pos.below()).canBeReplaced() ||  level.getBlockState(pos.below()).getBlock().equals(this));
    }

    /**
     * 设置可放置（种植）的具体方块（悬挂）<br/>
     * 这个和tag选填一个
     */
    public abstract BlockMatcher canSurviveBlock();

    /**
     * 设置可存在的地方
     * 默认为橡树树叶下方（用的可放置逻辑）
     */
    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        return mayPlaceOn(levelReader.getBlockState(pos.above()), levelReader, pos.above());
    }

    /**
     * 中键物品，默认和产物一致
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        return harvestResults(null, true).getItem().getDefaultInstance();
    }

    /**
     * 设置收获产物
     * 默认产率1个，低概率2-3个，极低概率4个
     * @param level 可为null，为null时会取消随机生成，使得产物固定为0个，配合后面的boolean可以控制为1个
     * @param flag 一般决定了是否为最终成长阶段
     */
    public ItemStack harvestResults(@Nullable Level level, boolean flag) {
        int j = 0;
        if (level != null) {
            float random = level.random.nextFloat();
            if(random < 0.1) j = 1;
            else if(random < 0.01) j = 2;
            else if(random < 0.001) j = 3;
        }
        return new ItemStack(getHarvestItem(), j + (flag ? 1 : 0));
    }

    /**
     * 如果不想改倍率直接改这个
     * 获取收割物
     */
    public abstract Item getHarvestItem();

    /**
     * 这里返回age0，因为整个摘了
     */
    @Override
    public int ageAfterHarvest() {
        return 0;
    }

    /**
     * 注意看，这里因为是悬挂作物，而这里生长条件需要上方亮度大于⑨，悬挂作物上方不为空，因此需要改成检测下方亮度
     */
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = state.getValue(AGE);
        if (i < getMaxAge() && level.getRawBrightness(pos.below(), 0) >= 9 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(5) == 0)) {
            BlockState blockstate = state.setValue(AGE, i + 1);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
        }

    }

}
