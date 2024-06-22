package cn.solarmoon.solarmoon_core.api.tile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

/**
 * 从方块实体相关方块上的物品栏读取交互便利程序<br/>
 * 由于此处用tag实现，故所有的修改都需再次set才能生效
 */
public class TileItemFluidTankHelper {

    public static final String FLUID = "Fluid";

    public static Optional<FluidTank> getTank(ItemStack stack) {
        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        FluidTank tank = null;
        if (tag != null && tag.contains(FLUID)) {
            tank = new FluidTank(1000);
            tank.readFromNBT(tag.getCompound(FLUID));
        }
        return Optional.ofNullable(tank);
    }

    public static void setTank(ItemStack stack, FluidTank tank) {
        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        tag = tag == null ? stack.getOrCreateTagElement(BlockItem.BLOCK_ENTITY_TAG) : tag;
        tag.put(FLUID, tank.writeToNBT(new CompoundTag()));
    }

}
