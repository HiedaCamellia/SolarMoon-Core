package cn.solarmoon.solarmoon_core.api.tile;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TileInventory extends ItemStackHandler {

    private int slotLimit = 64;
    private final BlockEntity blockEntity;

    public TileInventory(BlockEntity blockEntity) {
        this(1, blockEntity);
    }

    public TileInventory(int size, BlockEntity blockEntity) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.blockEntity = blockEntity;
    }

    public TileInventory(int size, int slotLimit, BlockEntity blockEntity) {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        this.blockEntity = blockEntity;
        this.slotLimit = slotLimit;
    }

    public TileInventory(NonNullList<ItemStack> stacks, BlockEntity blockEntity) {
        this.stacks = stacks;
        this.blockEntity = blockEntity;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotLimit;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        blockEntity.setChanged();
    }

}
