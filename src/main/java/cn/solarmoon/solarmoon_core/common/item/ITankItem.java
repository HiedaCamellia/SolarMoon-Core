package cn.solarmoon.solarmoon_core.common.item;

import cn.solarmoon.solarmoon_core.util.FluidUtil;
import net.minecraft.world.item.ItemStack;

/**
 * 存有液体信息的物品接口
 */
public interface ITankItem {

    /**
     * @return 液体容量
     */
    int getMaxCapacity();

    /**
     * 检查物品内液体是否大于0
     */
    default boolean remainFluid(ItemStack stack) {
        return !FluidUtil.getFluidStack(stack).isEmpty();
    }

    /**
     * 获取剩余容量
     */
    default int getRemainFluid(ItemStack stack) {return getMaxCapacity() - FluidUtil.getFluidStack(stack).getAmount();}

}
