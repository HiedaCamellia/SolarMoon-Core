package cn.solarmoon.solarmoon_core.common.block.entity_block;

import cn.solarmoon.solarmoon_core.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 只是提供一些默认的容器实用方法，甚至不作为标识符
 */
public interface IContainerEntityBlock {

    /**
     * 单独放入物品
     * @return 成功返回true
     */
    default boolean putItem(BlockEntity blockEntity, Player player, InteractionHand hand, int count) {
        if (blockEntity instanceof IContainerBlockEntity c) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (!heldItem.isEmpty()) {
                ItemStack simulativeItem = heldItem.copyWithCount(count);
                ItemStack result = c.insertItem(simulativeItem);
                int countToShrink = count - result.getCount();
                if (!player.isCreative()) heldItem.shrink(countToShrink);
                return !result.equals(simulativeItem);
            }
        }
        return false;
    }

    /**
     * 单独拿取物品（只适用空手拿取）
     * @return 成功返回true
     */
    default boolean takeItem(BlockEntity blockEntity, Player player, InteractionHand hand, int count) {
        if (blockEntity instanceof IContainerBlockEntity c) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.isEmpty() && !c.getStacks().isEmpty()) {
                ItemStack result = c.extractItem(count);
                if (!player.isCreative()) LevelSummonUtil.addItemToInventory(player, result);
                return true;
            }
        }
        return false;
    }

    /**
     * 基本的存物逻辑，似乎可通用<br/>
     * 手不为空时存入，为空时疯狂取出<br/>
     * <b>别忘了setChanged！</b>
     * @return 无所谓装取，只要容器交互成功就返回true
     */
    default boolean storage(BlockEntity blockEntity, Player player, InteractionHand hand, int putCount, int takeCount) {
        if (putItem(blockEntity, player, hand, putCount)) return true;
        return takeItem(blockEntity, player, hand, takeCount);
    }

}
