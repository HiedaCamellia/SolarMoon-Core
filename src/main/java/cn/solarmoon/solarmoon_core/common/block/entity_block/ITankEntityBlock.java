package cn.solarmoon.solarmoon_core.common.block.entity_block;

import cn.solarmoon.solarmoon_core.common.block_entity.BaseTankBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.ITankBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * 只是提供一些默认的容器实用方法，甚至不作为标识符
 */
public interface ITankEntityBlock {

    /**
     * <b>别忘了setChanged！</b>
     * 从手中物品放入液体
     * @return 成功会返回true
     */
    default boolean putFluid(BlockEntity blockEntity, Player player, InteractionHand hand, boolean playSound) {
        if (blockEntity instanceof ITankBlockEntity tankTile) {
            ItemStack heldItem = player.getItemInHand(hand);
            FluidTank tank = tankTile.getTank();
            FluidActionResult result = FluidUtil.tryEmptyContainer(heldItem, tank, Integer.MAX_VALUE, playSound ? player : null, true);
            if (result.isSuccess()) {
                if (!player.isCreative()) player.setItemInHand(hand, result.getResult());
                return true;
            }
        }
        return false;
    }

    /**
     * <b>别忘了setChanged！</b>
     * 从手中物品拿取液体
     * @return 成功会返回true
     */
    default boolean takeFluid(BlockEntity blockEntity, Player player, InteractionHand hand, boolean playSound) {
        if (blockEntity instanceof ITankBlockEntity tankTile) {
            ItemStack heldItem = player.getItemInHand(hand);
            FluidTank tank = tankTile.getTank();
            FluidActionResult result = FluidUtil.tryFillContainer(heldItem, tank, Integer.MAX_VALUE, playSound ? player : null, true);
            if (result.isSuccess()) {
                if (!player.isCreative()) player.setItemInHand(hand, result.getResult());
                return true;
            }
        }
        return false;
    }

    /**
     * <b>别忘了setChanged！</b>
     * @return 无所谓装取，只要液体交互成功就返回true
     */
    default boolean loadFluid(BaseTankBlockEntity tankEntity, Player player, InteractionHand hand, boolean playSound) {
        return putFluid(tankEntity, player, hand, playSound) || takeFluid(tankEntity, player, hand, playSound);
    }

}
