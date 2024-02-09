package cn.solarmoon.solarmoon_core.util;

import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerUtil {

    /**
     * 把方块实体物品信息存入item
     */
    public static void setInventory(ItemStack stack, BlockEntity blockEntity) {
        ItemStackHandler inventory = (ItemStackHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
        stack.getOrCreateTag().put(SolarNBTList.INVENTORY, inventory.serializeNBT());
    }

    /**
     * 把item物品信息存入方块实体
     */
    public static void setInventory(BlockEntity blockEntity, ItemStack stack) {
        blockEntity.getPersistentData().put(SolarNBTList.INVENTORY, stack.getOrCreateTag().getCompound(SolarNBTList.INVENTORY));
    }

    /**
     * 获取物品的inventory信息
     */
    public static ItemStackHandler getInventory(ItemStack stack) {
        ItemStackHandler stackHandler = new ItemStackHandler();
        stackHandler.deserializeNBT(stack.getOrCreateTag().getCompound(SolarNBTList.INVENTORY));
        return stackHandler;
    }

}
