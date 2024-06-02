package cn.solarmoon.solarmoon_core.api.common.item;

import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * 存有液体信息的物品接口
 */
public interface ITankItem {

    /**
     * @return 液体容量
     */
    int getMaxCapacity();

    /**
     * 检查物品内液体是否大于指定值
     */
    default boolean remainFluid(ItemStack stack, int remainAmount) {
        return FluidUtil.getFluidStack(stack).getAmount() > remainAmount;
    }

    /**
     * 获取剩余容量
     */
    default int getRemainCapability(ItemStack stack) {return getMaxCapacity() - FluidUtil.getFluidStack(stack).getAmount();}

    default FluidStack getFluidStack(ItemStack stack) {
        return FluidUtil.getFluidStack(stack);
    }

}
