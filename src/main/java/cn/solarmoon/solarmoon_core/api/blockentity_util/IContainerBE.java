package cn.solarmoon.solarmoon_core.api.blockentity_util;

import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.network.NETList;
import cn.solarmoon.solarmoon_core.registry.common.SolarNetPacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 具有物品容器信息的方块实体<br/>
 * 接入后能够实现save - load容器信息、 附加forgeItemHandler能力，以及一些实用方法<br/>
 * 需手动实现inventory逻辑，一般直接新建一个ItemStackHandler即可
 */
public interface IContainerBE {

    ItemStackHandler getInventory();

    /**
     * 从stack中读取inventory信息
     */
    default void setInventory(ItemStack stack) {
        getInventory().deserializeNBT(stack.getOrCreateTag().getCompound(ContainerUtil.INVENTORY));
    }

    /**
     * 从tag中读取inventory信息
     */
    default void setInventory(CompoundTag tag) {
        getInventory().deserializeNBT(tag.getCompound(ContainerUtil.INVENTORY));
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
     * 玩家用手单独拿取物品（只适用空手拿取），超过的部分不会提取，放心使用
     * @return 成功返回true
     */
    default boolean takeItem(Player player, InteractionHand hand, int count) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.isEmpty() && !getStacks().isEmpty()) {
            ItemStack result = extractItem(count);
            if (!result.isEmpty()) {
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
    default boolean storage(Player player, InteractionHand hand, int putCount, int takeCount) {
        if (putItem(player, hand, putCount)) return true;
        return takeItem(player, hand, takeCount);
    }

    /**
     * 特殊的取物逻辑，玩家蹲下时存入手中全部物品，站立时存入一个，取出同理<br/>
     * 但要注意必须实现IBlockUseCaller接口，否则默认情况下将不调用蹲下后的逻辑
     * @return 成功返回true
     */
    default boolean specialStorage(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (!heldItem.isEmpty()) {
            if (player.isCrouching()) {
                return this.putItem(player, hand, heldItem.getCount());
            } else {
                return this.putItem(player, hand, 1);
            }
        } else {
            return player.isCrouching() ? this.takeItem(player, hand, 64) : this.takeItem(player, hand, 1);
        }
    }

    default void clearInv() {
        getStacks().forEach(stack -> stack.setCount(0));
    }

    /**
     * 泵出所有物品
     */
    default void pumpOutAllItems(Vec3 positionAddon) {
        BlockEntity blockEntity = (BlockEntity) this;
        if (blockEntity.getLevel() != null && blockEntity.getLevel().isClientSide) {
            SolarNetPacks.SERVER.getSender()
                    .pos(blockEntity.getBlockPos())
                    .stacks(getStacks())
                    .vec3List(List.of(positionAddon))
                    .send(NETList.PUMP);
        }
        clearInv();
        ((BlockEntity)this).setChanged();
    }

}
