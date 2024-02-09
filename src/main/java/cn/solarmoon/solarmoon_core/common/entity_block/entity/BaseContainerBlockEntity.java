package cn.solarmoon.solarmoon_core.common.entity_block.entity;


import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的容器类，只有存储功能
 */
public class BaseContainerBlockEntity extends BlockEntity {

    public ItemStackHandler inventory;

    public BaseContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size, int slotLimit) {
        super(type, pos, state);
        this.inventory = new ItemStackHandler(size) {
            @Override
            public int getSlotLimit(int slot) {
                return slotLimit;
            }
        };
    }

    public BaseContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);
        this.inventory = new ItemStackHandler(size);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(SolarNBTList.INVENTORY, inventory.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound(SolarNBTList.INVENTORY));
    }

    /**
     * 既有储液能力，又是物品容器
     */
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.of(() -> inventory).cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * 从stack中读取inventory信息
     */
    public void setInventory(ItemStack stack) {
        inventory.deserializeNBT(stack.getOrCreateTag().getCompound(SolarNBTList.INVENTORY));
    }

    /**
     * 从tag中读取inventory信息
     */
    public void setInventory(CompoundTag tag) {
        inventory.deserializeNBT(tag.getCompound(SolarNBTList.INVENTORY));
    }

    /**
     * 插入容纳的物品（按物品栈插入）<br/>
     * 逻辑为从第一格开始尝试插入直到插入成功<br/>
     * 会返回计算消耗后的物品栈，因此不要再用shrink！用setItem！
     */
    public ItemStack insertItem(ItemStack itemStack) {
        int maxSlots = inventory.getSlots();
        ItemStack result = itemStack;
        for (int i = 0; i < maxSlots; i++) {
            result = inventory.insertItem(i, itemStack, false);
            if (!result.equals(itemStack)) break;
        }
        setChanged();
        return result;
    }

    /**
     * 从中提取物品
     * 默认逻辑从最后一栏开始提取，按物品栈提取
     * 注意，这个只适用于空手提取
     */
    public ItemStack extractItem() {
        int maxSlots = inventory.getSlots();
        ItemStack stack = ItemStack.EMPTY;
        for (int i = 0; i < maxSlots; i++) {
            stack = inventory.extractItem(maxSlots - i - 1, Integer.MAX_VALUE, false);
            if (!stack.isEmpty()) break;
        }
        setChanged();
        return stack;
    }

    /**
     * 获取容器内的所有物品
     */
    public List<ItemStack> getStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        int maxSlots = inventory.getSlots();
        for (int i = 0; i < maxSlots; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            //这里不能让stack为空，因为会插入EMPTY的stack，这样会妨碍List.isEmpty的检查
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    /**
     * 获取容器的最大物品量
     * 默认所有槽位容量都是相等的
     */
    public int maxStackCount() {
        return inventory.getSlots() * inventory.getSlotLimit(0);
    }

}
