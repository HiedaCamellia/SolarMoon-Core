package cn.solarmoon.solarmoon_core.api.common.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 类甜浆果丛类型的作物，区别是成长满100才能摘<br/>
 * 可自定义最大成长阶段数
 */
public abstract class BaseBushCropBlock extends BushBlock implements BonemealableBlock, INoLimitAgeBlock {

    /**
     * 默认属性
     */
    public BaseBushCropBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH));
    }

    /**
     * 自定属性
     */
    public BaseBushCropBlock(Properties properties) {
        super(properties);
    }

    /**
     * 所有生长收割都会自动根据maxAge调整到合适的状态<br/>
     * 甜浆果丛默认为3
     */
    public abstract int getMaxAge();

    @Override
    public boolean isBonemealSuccess(Level p_222558_, RandomSource p_222559_, BlockPos p_222560_, BlockState p_222561_) {
        return true;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < getMaxAge();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = state.getValue(AGE);
        if (i < getMaxAge() && level.getRawBrightness(pos.above(), 0) >= 9 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(5) == 0)) {
            BlockState blockstate = state.setValue(AGE, i + 1);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean p_57263_) {
        return state.getValue(AGE) < getMaxAge();
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = Math.min(getMaxAge(), state.getValue(AGE) + 1);
        level.setBlock(pos, state.setValue(AGE, i), 2);
    }

    /**
     * 仅仅造成减速
     */
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(entity instanceof LivingEntity living) {
            living.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));
        }
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
     * 默认产率和甜浆果丛一致（2-3个）
     * @param level 可为null，为null时会取消随机生成，使得产物固定为0个，配合后面的boolean可以控制为1个
     * @param oneMoreBaseProduct 为true则基础产物增加1个，原版甜浆果丛也是此逻辑（66%生长时会少摘一个，但这里基本都为true，因为需要100生长度才能摘）
     */
    public ItemStack harvestResults(@Nullable Level level, boolean oneMoreBaseProduct) {
        int j = 0;
        if (level != null) {
            j = 1 + level.random.nextInt(2);
        }
        return new ItemStack(getHarvestItem(), j + (oneMoreBaseProduct ? 1 : 0));
    }

    /**
     * 如果不想改倍率直接改这个
     * 获取收割物
     */
    public abstract Item getHarvestItem();

    /**
     * 收获功能
     * 直接抄的甜浆果丛
     * 区别是需要age到顶才能摘
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == getMaxAge();
        if (!flag && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (i > getMaxAge() - 1) {
            popResource(level, pos, harvestResults(level, flag));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState blockstate = state.setValue(AGE, ageAfterHarvest());
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    /**
     * @return 控制摘取后作物所返回的age阶段，默认减2段
     */
    public int ageAfterHarvest() {
        return getMaxAge() - 2;
    }

}
