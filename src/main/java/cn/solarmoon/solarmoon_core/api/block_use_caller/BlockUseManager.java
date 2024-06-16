package cn.solarmoon.solarmoon_core.api.block_use_caller;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 可以改变block#use方法的触发条件<br/>
 * 例如可以使得蹲下+右键不强制使用物品而是仍然调用block#use方法（右键存1，蹲下+右键存一组之类）
 */
public class BlockUseManager {

    @SubscribeEvent
    public void onPlayerUseOn(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        ItemStack heldItem = event.getItemStack();
        BlockPos pos = event.getPos();
        BlockHitResult hit = event.getHitVec();
        BlockState state = level.getBlockState(pos);
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();

        Block block = state.getBlock();
        if (block instanceof IBlockUseCaller caller) {
            event.setUseBlock(caller.getUseResult(state, pos, level, player, heldItem, hit, hand));
        }
    }

}
