package cn.solarmoon.solarmoon_core.util;

import cn.solarmoon.solarmoon_core.common.entity_block.entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerUtil {

    /**
     * 把方块实体物品信息存入containerItem
     */
    public static void setInventory(ItemStack stack, BlockEntity blockEntity) {
        ItemStackHandler inventory = (ItemStackHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
        stack.addTagElement(SolarNBTList.INVENTORY, inventory.serializeNBT());
    }

    /**
     * 根据itemStack的inventoryTag获取ItemStackHandler（物品容器信息）<br/>
     * 这里不用getElement因为inventory可能会返回不匹配方块inventory的
     */
    public static ItemStackHandler getInventory(ItemStack stack) {
        ItemStackHandler inventory = new ItemStackHandler();
        inventory.deserializeNBT(stack.getOrCreateTag().getCompound(SolarNBTList.INVENTORY));
        return inventory;
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
        if (blockEntity instanceof IContainerBlockEntity containerTile) {
            return (float) containerTile.getStacksAmount() / containerTile.maxStackCount();
        }
        return 0;
    }

}
