package cn.solarmoon.solarmoon_core.api.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

/**
 * 从方块实体相关方块上的物品栏读取交互便利程序<br/>
 * 由于此处用tag实现，故所有的修改都需再次set才能生效
 */
public class TileItemContainerHelper {

    public static final String INVENTORY = "Inventory";

    public static Optional<ItemStackHandler> getInventory(ItemStack stack) {
        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        ItemStackHandler inv = null;
        if (tag != null && tag.contains(INVENTORY)) {
            inv = new ItemStackHandler();
            inv.deserializeNBT(tag.getCompound(INVENTORY));
        }
        return Optional.ofNullable(inv);
    }

    public static void setInventory(ItemStack stack, ItemStackHandler inv) {
        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        tag = tag == null ? stack.getOrCreateTagElement(BlockItem.BLOCK_ENTITY_TAG) : tag;
        tag.put(INVENTORY, inv.serializeNBT());
    }

}
