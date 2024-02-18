package cn.solarmoon.solarmoon_core.common.block_entity;

import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * 具有液体储罐信息的方块实体<br/>
 * 接入后能够实现save - load容器信息、 附加forgeItemHandler能力，以及一些实用方法<br/>
 * 需手动实现tank逻辑，一般直接新建一个FluidTank即可
 */
public interface ITankBlockEntity {

    FluidTank getTank();

    int getMaxCapacity();

    /**
     * 一个强制设置储罐内容物的方法
     */
    default void setFluid(FluidStack fluidStack) {
        getTank().setFluid(fluidStack);
    }

    /**
     * 一个根据tag强制设置储罐内容物的方法
     */
    default void setFluid(CompoundTag tag) {
        getTank().readFromNBT(tag.getCompound(SolarNBTList.FLUID));
    }

}
