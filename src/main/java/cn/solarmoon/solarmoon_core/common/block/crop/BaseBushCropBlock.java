package cn.solarmoon.solarmoon_core.common.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * 类甜浆果丛类型的作物，区别是成长满100才能摘<br/>
 * 默认可种植方块也和甜浆果丛一致
 */
public abstract class BaseBushCropBlock extends SweetBerryBushBlock {

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
     * @param flag 为true则基础产物增加1个，原版甜浆果丛也是此逻辑（66%生长时会少摘一个，但这里基本都为true，因为需要100生长度才能摘）
     */
    public ItemStack harvestResults(@Nullable Level level, boolean flag) {
        int j = 0;
        if (level != null) {
            j = 1 + level.random.nextInt(2);
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
     * 区别是需要age到顶才能摘
     */
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        int i = state.getValue(AGE);
        boolean flag = i == 3;
        if (!flag && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (i > 2) {
            popResource(level, pos, harvestResults(level, flag));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState blockstate = state.setValue(AGE, 1);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

}
