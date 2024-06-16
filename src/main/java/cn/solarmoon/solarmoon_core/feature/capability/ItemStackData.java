package cn.solarmoon.solarmoon_core.feature.capability;

import cn.solarmoon.solarmoon_core.api.capability.HiddenItemInsertionData;
import cn.solarmoon.solarmoon_core.api.optional_recipe_item.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.feature.embedding.EmbeddingData;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class ItemStackData implements ICapabilitySerializable<CompoundTag>, IItemStackData {

    private final LazyOptional<IItemStackData> itemStackData;
    private final ItemStack stack;

    private final EmbeddingData embeddingData;
    private final RecipeSelectorData recipeSelectorData;
    private final HiddenItemInsertionData hiddenItemInsertionData;

    public ItemStackData(ItemStack stack) {
        this.itemStackData = LazyOptional.of(() -> this);
        this.stack = stack;

        this.embeddingData = new EmbeddingData();
        this.recipeSelectorData = new RecipeSelectorData();
        this.hiddenItemInsertionData = new HiddenItemInsertionData();
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
    public HiddenItemInsertionData getHiddenItemInsertionData() {
        return hiddenItemInsertionData;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("EmbeddingData", embeddingData.serializeNBT());
        tag.put("RecipeSelectorData", recipeSelectorData.serializeNBT());
        tag.put("HiddenItemInsertionData", hiddenItemInsertionData.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        embeddingData.deserializeNBT(nbt.getCompound("EmbeddingData"));
        recipeSelectorData.deserializeNBT(nbt.getCompound("RecipeSelectorData"));
        hiddenItemInsertionData.deserializeNBT(nbt.getCompound("HiddenItemInsertionData"));

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
