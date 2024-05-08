package cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class EmploymentData implements INBTSerializable<CompoundTag> {

    private boolean isUsing;

    public boolean isUsing() {
        return isUsing;
    }

    public void setUse(boolean using) {
        isUsing = using;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("using", isUsing);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        isUsing = nbt.getBoolean("using");
    }

}
