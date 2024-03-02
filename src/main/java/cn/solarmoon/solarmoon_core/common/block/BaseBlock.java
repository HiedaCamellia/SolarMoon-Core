package cn.solarmoon.solarmoon_core.common.block;

import cn.solarmoon.solarmoon_core.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 有基本的朝向功能的方块（四面）<br/>
 * 并且能快速添加自定义属性
 */
public abstract class BaseBlock extends Block implements IHorizontalFacingBlock {

    public BaseBlock(Properties properties) {
        super(properties);
    }

    /**
     * 提醒设置碰撞箱
     */
    @Override
    public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    /**
     * 把方块快速拿到空手里
     */
    public boolean getThis(Player player, Level level, BlockPos pos, BlockState state, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(hand.equals(InteractionHand.MAIN_HAND) && heldItem.isEmpty() && player.isCrouching()) {
            ItemStack copy = getCloneItemStack(level, pos, state);
            boolean flag = BlockUtil.removeDoubleBlock(level, pos);
            if (!flag) level.removeBlock(pos, false);
            level.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
            player.setItemInHand(hand, copy);
            return true;
        }
        return false;
    }

    /**
     * 能发出红石信号
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

}
