package cn.solarmoon.solarmoon_core.api.common.capability;

import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.EmbeddingData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.EmploymentData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ItemStackData implements ICapabilitySerializable<CompoundTag>, IItemStackData {

    private final LazyOptional<IItemStackData> itemStackData;
    private final ItemStack stack;

    private final EmploymentData employmentData;
    private final EmbeddingData embeddingData;
    private final RecipeSelectorData recipeSelectorData;

    public ItemStackData(ItemStack stack) {
        this.itemStackData = LazyOptional.of(() -> this);
        this.stack = stack;

        this.employmentData = new EmploymentData();
        this.embeddingData = new EmbeddingData();
        this.recipeSelectorData = new RecipeSelectorData();
    }

    @Override
    public EmploymentData getEmploymentData() {
        return employmentData;
    }

    @Override
    public EmbeddingData getEmbeddingData() {
        return embeddingData;
    }

    @Override
    public RecipeSelectorData getRecipeSelectorData() {
        return recipeSelectorData;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("EmploymentData", employmentData.serializeNBT());
        tag.put("EmbeddingData", embeddingData.serializeNBT());
        tag.put("RecipeSelectorData", recipeSelectorData.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        employmentData.deserializeNBT(nbt.getCompound("EmploymentData"));
        embeddingData.deserializeNBT(nbt.getCompound("EmbeddingData"));
        recipeSelectorData.deserializeNBT(nbt.getCompound("RecipeSelectorData"));

    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == SolarCapabilities.ITEMSTACK_DATA) {
            return itemStackData.cast();
        }
        return LazyOptional.empty();
    }

    public ItemStack getStack() {
        return stack;
    }

}