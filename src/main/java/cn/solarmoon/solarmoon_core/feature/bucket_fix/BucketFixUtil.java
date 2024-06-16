package cn.solarmoon.solarmoon_core.feature.bucket_fix;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BucketFixUtil {

    public static final String FLUID_IN = "FluidIn";

    /**
     * 用于mixin，保存流体tag到桶
     */
    public static void saveFluidTagToBucket(FluidStack stack, ItemStack bucket) {
        if (!bucket.isEmpty() && stack.hasTag()) {
            bucket.getOrCreateTag().put(FLUID_IN, stack.getTag());
        }
    }

    /**
     * 用于mixin，保存桶存储的液体tag到放出的液体
     */
    public static void readBucketTagToFluid(ItemStack bucket, FluidStack stack) {
        CompoundTag tag = bucket.getTag();
        if (tag != null && tag.contains(FLUID_IN) && !stack.isEmpty()) {
            stack.setTag(tag.getCompound(FLUID_IN));
        }
    }

}
