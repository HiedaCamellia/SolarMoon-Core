package cn.solarmoon.solarmoon_core.api.common.block;

import cn.solarmoon.solarmoon_core.api.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockFunctionProvider {

    default Block block() {
        return ((Block)this);
    }

    /**
     * 把方块快速拿到空手里
     */
    default boolean getThis(Player player, Level level, BlockPos pos, BlockState state, InteractionHand hand, boolean defaultSound) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(hand.equals(InteractionHand.MAIN_HAND) && heldItem.isEmpty() && player.isCrouching()) {
            ItemStack copy = block().getCloneItemStack(level, pos, state);
            boolean flag = BlockUtil.removeDoubleBlock(level, pos);
            if (!flag) level.removeBlock(pos, false);
            if (defaultSound) {
                level.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
            }
            player.setItemInHand(hand, copy);
            return true;
        }
        return false;
    }

}
