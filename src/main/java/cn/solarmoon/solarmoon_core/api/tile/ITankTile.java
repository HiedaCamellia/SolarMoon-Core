package cn.solarmoon.solarmoon_core.api.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * 具有液体储罐信息的方块实体<br/>
 * 接入后能够实现save - load容器信息、 附加forgeItemHandler能力，以及一些实用方法<br/>
 * 需手动实现tank逻辑，一般直接新建一个FluidTank即可
 */
public interface ITankTile {

    FluidTank getTank();

    default int getMaxCapacity() {
        return getTank().getCapacity();
    };

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
        getTank().readFromNBT(tag.getCompound(cn.solarmoon.solarmoon_core.api.util.FluidUtil.FLUID));
    }

    /**
     * <b>别忘了setChanged！</b>
     * 从手中物品放入液体
     * @return 成功会返回true
     */
    default boolean putFluid(Player player, InteractionHand hand, boolean playSound) {
        ItemStack heldItem = player.getItemInHand(hand);
        FluidTank tank = getTank();
        FluidActionResult result = FluidUtil.tryEmptyContainer(heldItem, tank, Integer.MAX_VALUE, playSound ? player : null, true);
        if (result.isSuccess()) {
            if (!player.isCreative()) player.setItemInHand(hand, result.getResult());
            return true;
        }
        return false;
    }

    /**
     * <b>别忘了setChanged！</b>
     * 从手中物品拿取液体
     * @return 成功会返回true
     */
    default boolean takeFluid(Player player, InteractionHand hand, boolean playSound) {
        ItemStack heldItem = player.getItemInHand(hand);
        FluidTank tank = getTank();
        FluidActionResult result = FluidUtil.tryFillContainer(heldItem, tank, Integer.MAX_VALUE, playSound ? player : null, true);
        if (result.isSuccess()) {
            if (!player.isCreative()) player.setItemInHand(hand, result.getResult());
            return true;
        }
        return false;
    }

    /**
     * <b>别忘了setChanged！</b>
     * @return 无所谓装取，只要液体交互成功就返回true
     */
    default boolean loadFluid(Player player, InteractionHand hand, boolean playSound) {
        return putFluid(player, hand, playSound) || takeFluid(player, hand, playSound);
    }

    default void clearTank() {
        getTank().setFluid(FluidStack.EMPTY);
    }

}
