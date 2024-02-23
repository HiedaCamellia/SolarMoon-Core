package cn.solarmoon.solarmoon_core.common.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * 悬挂式浆果丛类型的作物，成长满100才能摘，并且返回age0（一般用于水果）
 */
public abstract class BaseHangingBushCropBlock extends SweetBerryBushBlock {

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
     * 设置可放置（种植）的地方
     * 默认为橡树树叶
     * 一般情况下请修改canSurviveBlock
     */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return ( canSurviveTag() == null ? state.is(canSurviveBlock()) : state.is(canSurviveTag()) )
                && ( level.getBlockState(pos.below()).canBeReplaced() ||  level.getBlockState(pos.below()).getBlock().equals(this));
    }

    /**
     * 设置可放置（种植）的具体方块（悬挂）<br/>
     * 这个和tag选填一个
     */
    public abstract Block canSurviveBlock();

    /**
     * 根据tag设定可种植的，不需要直接返回null即可<br/>
     * 这个和Block选填一个
     */
    public abstract TagKey<Block> canSurviveTag();

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
     * 收获功能
     * 直接抄的甜浆果丛
     * 区别是这里返回age0，因为整个摘了
     * 以及需要age到顶才能摘
     */
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == 3;
        if (!flag && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (i > 2) {
            popResource(level, pos, harvestResults(level, flag));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState blockstate = state.setValue(AGE, 0);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    /**
     * 注意看，这里因为是悬挂作物，而这里生长条件需要上方亮度大于⑨，悬挂作物上方不为空，因此需要改成检测下方亮度
     */
    @Override
    public void randomTick(BlockState p_222563_, ServerLevel p_222564_, BlockPos p_222565_, RandomSource p_222566_) {
        int i = p_222563_.getValue(AGE);
        if (i < 3 && p_222564_.getRawBrightness(p_222565_.below(), 0) >= 9 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(p_222564_, p_222565_, p_222563_, p_222566_.nextInt(5) == 0)) {
            BlockState blockstate = p_222563_.setValue(AGE, Integer.valueOf(i + 1));
            p_222564_.setBlock(p_222565_, blockstate, 2);
            p_222564_.gameEvent(GameEvent.BLOCK_CHANGE, p_222565_, GameEvent.Context.of(blockstate));
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(p_222564_, p_222565_, p_222563_);
        }

    }

}
