package cn.solarmoon.solarmoon_core.api.common.block_entity;

import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNBTList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 具有物品容器信息的方块实体<br/>
 * 接入后能够实现save - load容器信息、 附加forgeItemHandler能力，以及一些实用方法<br/>
 * 需手动实现inventory逻辑，一般直接新建一个ItemStackHandler即可
 */
public interface IContainerBlockEntity {

    ItemStackHandler getInventory();

    /**
     * 从stack中读取inventory信息
     */
    default void setInventory(ItemStack stack) {
        getInventory().deserializeNBT(stack.getOrCreateTag().getCompound(SolarNBTList.INVENTORY));
    }

    /**
     * 从tag中读取inventory信息
     */
    default void setInventory(CompoundTag tag) {
        getInventory().deserializeNBT(tag.getCompound(SolarNBTList.INVENTORY));
    }

    /**
     * 插入容纳的物品（按物品栈插入）<br/>
     * 逻辑为从第一格开始尝试插入直到插入成功<br/>
     * 会返回计算消耗后的物品栈，并不会消耗物品，因此不要再用shrink！用setItem！<br/>
     * <b>别忘了setChanged！</b>
     */
    default ItemStack insertItem(ItemStack itemStack) {
        int maxSlots = getInventory().getSlots();
        ItemStack result = itemStack;
        for (int i = 0; i < maxSlots; i++) {
            result = getInventory().insertItem(i, itemStack, false);
            if (!result.equals(itemStack)) break;
        }
        return result;
    }

    /**
     * 从中提取物品<br/>
     * 默认逻辑从最后一栏开始提取，按物品栈提取，没提取会返回空栈<br/>
     * <b>别忘了setChanged！</b>
     */
    default ItemStack extractItem(int count) {
        int maxSlots = getInventory().getSlots();
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < maxSlots; i++) {
            stack = getInventory().extractItem(maxSlots - i - 1, count, false);
            if (!stack.isEmpty()) break;
        }
        return stack;
    }

    /**
     * 获取容器内的所有物品
     */
    default List<ItemStack> getStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        int maxSlots = getInventory().getSlots();
        for (int i = 0; i < maxSlots; i++) {
            ItemStack stack = getInventory().getStackInSlot(i);
            //这里不能让stack为空，因为会插入EMPTY的stack，这样会妨碍List.isEmpty的检查
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    /**
     * 获取容器内物品总数
     */
    default int getStacksAmount() {
        int amount = 0;
        int maxSlots = getInventory().getSlots();
        for (int i = 0; i < maxSlots; i++) {
             amount += getInventory().getStackInSlot(i).getCount();
        }
        return amount;
    }

    /**
     * 获取容器的最大物品量
     * 默认所有槽位容量都是相等的
     */
    default int maxStackCount() {
        return getInventory().getSlots() * getInventory().getSlotLimit(0);
    }

    /**
     * 单独放入玩家手中物品
     * @return 成功返回true
     */
    default boolean putItem(Player player, InteractionHand hand, int count) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (!heldItem.isEmpty()) {
            ItemStack simulativeItem = heldItem.copyWithCount(count);
            ItemStack result = insertItem(simulativeItem);
            int countToShrink = count - result.getCount();
            if (!player.isCreative()) heldItem.shrink(countToShrink);
            return !result.equals(simulativeItem);
        }
        return false;
    }

    /**
     * 玩家用手单独拿取物品（只适用空手拿取）
     * @return 成功返回true
     */
    default boolean takeItem(Player player, InteractionHand hand, int count) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.isEmpty() && !getStacks().isEmpty()) {
            ItemStack result = extractItem(count);
            if (!player.isCreative()) LevelSummonUtil.addItemToInventory(player, result);
            return true;
        }
        return false;
    }

    /**
     * 基本的存物逻辑，似乎可通用<br/>
     * 手不为空时存入，为空时疯狂取出<br/>
     * <b>别忘了setChanged！</b>
     * @return 无所谓装取，只要容器交互成功就返回true
     */
    default boolean storage(Player player, InteractionHand hand, int putCount, int takeCount) {
        if (putItem(player, hand, putCount)) return true;
        return takeItem(player, hand, takeCount);
    }

}
