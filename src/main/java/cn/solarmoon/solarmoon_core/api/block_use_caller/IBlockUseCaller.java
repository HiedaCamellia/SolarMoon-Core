package cn.solarmoon.solarmoon_core.api.block_use_caller;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.Event;

/**
 * 可以改变block#use方法的触发条件<br/>
 * 例如可以使得蹲下+右键不强制使用物品而是仍然调用block#use方法（右键存1，蹲下+右键存一组之类）
 */
public interface IBlockUseCaller {

    /**
     * @return 是否调用Block#use方法
     */
    Event.Result getUseResult(BlockState state, BlockPos pos, Level level, Player player, ItemStack heldItem, BlockHitResult hitResult, InteractionHand hand);

}
