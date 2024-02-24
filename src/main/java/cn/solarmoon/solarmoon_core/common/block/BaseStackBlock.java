package cn.solarmoon.solarmoon_core.common.block;

import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

/**
 * 基本的物品栈属性的方块<br/>
 * 默认能够自动堆栈相同物品直到堆栈上限，空手能取走物品
 */
public abstract class BaseStackBlock extends BaseWaterBlock implements IStackBlock {

    private final int maxStack;

    public BaseStackBlock(int maxStack, Properties properties) {
        super(properties);
        this.maxStack = maxStack;
    }

    /**
     * 存物、取物，剩1个也能取走
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack heldItem = player.getItemInHand(hand);
        int stack = state.getValue(STACK);
        if (heldItem.is(asItem()) && stack < maxStack) {
            if (!player.isCreative()) heldItem.shrink(1);
            level.setBlock(pos, state.setValue(STACK, stack + 1), 3);
            level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.PLAYERS);
            return InteractionResult.SUCCESS;
        } else if (heldItem.isEmpty() && stack > 1) {
            level.setBlock(pos, state.setValue(STACK, stack - 1), 3);
            if (!player.isCreative()) LevelSummonUtil.summonDrop(asItem(), level, pos, 1);
            return InteractionResult.SUCCESS;
        } else if (heldItem.isEmpty() && stack == 1) {
            level.removeBlock(pos, false);
            if (!player.isCreative()) LevelSummonUtil.summonDrop(asItem(), level, pos, 1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    /**
     * 将第一个从drop列表中找到的该方块的数量设为stack对应的数量
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        int count = state.getValue(STACK);
        List<ItemStack> drops = super.getDrops(state, builder);
        for (ItemStack drop : drops) {
            if (drop.is(asItem())) {
                drop.setCount(count);
                break;
            }
        }
        return drops;
    }

    public int getMaxStack() {
        return maxStack;
    }

}
