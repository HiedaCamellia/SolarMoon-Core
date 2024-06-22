package cn.solarmoon.solarmoon_core.api.util;

import cn.solarmoon.solarmoon_core.api.blockentity_util.IContainerBE;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;

@Deprecated
public class ContainerUtil {

    public static final String INVENTORY = "Inventory";

    /**
     * 把方块实体物品信息存入containerItem
     */
    public static void setInventory(ItemStack stack, BlockEntity blockEntity) {
        ItemStackHandler inventory = (ItemStackHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
        stack.getOrCreateTag().put(INVENTORY, inventory.serializeNBT());
    }

    /**
     * 把现成的物品信息存入containerItem
     */
    public static void setInventory(ItemStack stack, ItemStackHandler inventory) {
        stack.getOrCreateTag().put(INVENTORY, inventory.serializeNBT());
    }

    /**
     * 根据itemStack的inventoryTag获取ItemStackHandler（物品容器信息）<br/>
     * 注意，此方法获得的物品的容器直接修改是无法体现在游戏中的，必须修改后使用setInventory固定！
     * 这里不用getElement因为inventory可能会返回不匹配方块inventory的
     */
    public static ItemStackHandler getInventory(ItemStack stack) {
        ItemStackHandler inventory = new ItemStackHandler();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(INVENTORY)) {
            inventory.deserializeNBT(tag.getCompound(INVENTORY));
        }
        return inventory;
    }

    /**
     * @return 同上，但是如果没有会创建一个新的容器
     */
    public static ItemStackHandler getOrCreateInventory(ItemStack stack) {
        ItemStackHandler inventory = new ItemStackHandler();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(INVENTORY)) {
            inventory.deserializeNBT(tag.getCompound(INVENTORY));
        } else {
            setInventory(stack, inventory);
        }
        return inventory;
    }

    /**
     * 从物品中提取或创建物品栏并插入物品（会消耗输入的物品）
     * @param stackHasInv 被插入的物品
     * @param insertItem 要插入的物品
     * @param count 插入的数量
     * @return 是否成功
     */
    public static boolean insertItem(ItemStack stackHasInv, ItemStack insertItem, int count) {
        ItemStackHandler inv = getInventory(stackHasInv);
        int maxSlots = inv.getSlots();
        for (int i = 0; i < maxSlots; i++) {
            if (count <= insertItem.getCount()) {
                ItemStack simulateStack = insertItem.copyWithCount(count);
                ItemStack result = inv.insertItem(i, simulateStack, false); // 插入物品后返回计算后的物品
                if (!result.equals(simulateStack, false)) {
                    insertItem.shrink(simulateStack.getCount() - result.getCount());
                    setInventory(stackHasInv, inv);
                    return true;
                } // 如果计算后的物品没有改变，那么就继续尝试插入，反之直接插入成功
            }
        }
        return false;
    }

    /**
     * 从最后一栏开始提取物品（会消耗栏内提取的物品）
     * @param stackHasInv 被提取的有物品栏的物品
     * @param count 提取的物品量
     * @return 提取后的完整物品
     */
    public static ItemStack extractItem(ItemStack stackHasInv, int count) {
        ItemStackHandler inv = getInventory(stackHasInv);
        int maxSlots = inv.getSlots();
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < maxSlots; i++) {
            stack = inv.extractItem(maxSlots - i - 1, count, false);
            setInventory(stackHasInv, inv);
            if (!stack.isEmpty()) break;
        }
        return stack;
    }

    /**
     * 根据blockEntity的capability获取ItemStackHandler（物品容器信息）
     */
    public static ItemStackHandler getInventory(BlockEntity blockEntity) {
        return (ItemStackHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
    }

    /**
     * @return 获取物品容器内已有物品量占物品最大容量的比例（容量比）
     */
    public static float getScale(BlockEntity blockEntity) {
        if (blockEntity instanceof IContainerBE containerTile) {
            return (float) containerTile.getStacksAmount() / containerTile.maxStackCount();
        }
        return 0;
    }

}
