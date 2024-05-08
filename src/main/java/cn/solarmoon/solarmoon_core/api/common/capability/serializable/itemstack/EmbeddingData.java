package cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmbeddingData implements INBTSerializable<CompoundTag> {

    protected final List<ItemStack> stacksInSlot;

    public EmbeddingData() {
        stacksInSlot = new ArrayList<>();
    }

    public ItemStack getItemIn(int index) {
        return stacksInSlot.get(index);
    }

    /**
     * @param compareNBT 是否比较nbt，注意，这里不比较数量
     * @return 获取所有同类物品的数量
     */
    public int getItemCount(ItemStack stack, boolean compareNBT) {
        int count = 0;
        for (var item : stacksInSlot) {
            boolean hasSame = compareNBT ? item.is(stack.getItem()) && item.areShareTagsEqual(stack) : item.is(stack.getItem());
            if (hasSame) count = count + item.getCount();
        }
        return count;
    }

    public List<ItemStack> getStacks() {
        return stacksInSlot;
    }

    /**
     * 插入物品，如果有相同的物品会直接在此基础上增加（插入第一个匹配的）
     * @param compareNBT 是否在判断物品是否一致时比较nbt（不比较数量）
     */
    public void embedItem(ItemStack stack, boolean compareNBT) {
        Optional<ItemStack> sameOp = getSame(stack, compareNBT);
        if (sameOp.isPresent()) {
            sameOp.get().grow(stack.getCount());
        } else {
            stacksInSlot.add(stack);
        }
    }

    /**
     * 从中提取和输入一致的物品，原理是直接删和输入物数量一致的相同物然后返回
     * @param stack 要提取的物品（包括数量、nbt等等）
     * @param compareNBT 是否在判断物品是否一致时比较nbt（不比较数量）
     * @return 提取的物品（如果成功返回和输入的stack一致的物品，失败就返回空）
     */
    public ItemStack extractItem(ItemStack stack, boolean compareNBT) {
        Optional<ItemStack> sameOp = getSame(stack, compareNBT);
        if (sameOp.isPresent()) {
            ItemStack same = sameOp.get();
            if (same.getCount() >= stack.getCount()) {
                same.shrink(stack.getCount());
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * 清空所有槽位物品
     */
    public void clear() {
        stacksInSlot.clear();
    }

    /**
     * @param compareNBT 是否比较nbt，注意，这里不比较数量
     * @return 直接获取槽内和输入一致的物品（返回第一个匹配的）
     */
    public Optional<ItemStack> getSame(ItemStack stack, boolean compareNBT) {
        Optional<ItemStack> same;
        if (!compareNBT) {
            same = stacksInSlot.stream().filter(item ->  item.is(stack.getItem())).findFirst();
        } else {
            same = stacksInSlot.stream().filter(item -> item.is(stack.getItem()) && item.areShareTagsEqual(stack)).findFirst();
        }
        return same;
    }

    /**
     * @param compareNBT 是否比较nbt，注意，这里不比较数量
     * @return 比较是否在镶嵌物品中有和输入物品一样的物品
     */
    public boolean hasSame(ItemStack stack, boolean compareNBT) {
        return getSame(stack, compareNBT).isPresent();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < stacksInSlot.size(); i++) {
            tag.put("Slot" + i, stacksInSlot.get(i).serializeNBT());
            tag.putInt("Size", stacksInSlot.size());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        int size = nbt.getInt("Size");
        for (int i = 0; i < size; i++) {
            CompoundTag itemTag = nbt.getCompound("Slot" + i);
            ItemStack stack = ItemStack.of(itemTag);
            stacksInSlot.add(stack);
        }
    }

}
