package cn.solarmoon.solarmoon_core.common.item;


import cn.solarmoon.solarmoon_core.util.FluidTankUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

/**
 * 基本的有一个液体容器的物品
 */
public abstract class BaseTankItem extends BlockItem {

    private final int maxCapacity;

    public BaseTankItem(Block block, int maxCapacity, Properties properties) {
        super(block, properties
                .stacksTo(1)
        );
        this.maxCapacity = maxCapacity;
    }

    /**
     * 将其赋予一个容器<br/>
     * 不！能够实现与各类液体容器交互
     */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new FluidHandlerItemStack(stack, this.maxCapacity);
    }

    /**
     * 检查物品内液体是否大于0
     */
    public boolean remainFluid(ItemStack stack) {
        return !FluidTankUtil.getFluidStack(stack).isEmpty();
    }

    /**
     * 获取剩余容量
     */
    public int getRemainFluid(ItemStack stack) {return this.maxCapacity - FluidTankUtil.getFluidStack(stack).getAmount();}

    /**
     * 让物品根据所装溶液动态改变显示名称
     */
    @Override
    public Component getName(ItemStack stack) {
        FluidStack fluidStack = FluidTankUtil.getFluidStack(stack);
        int fluidAmount = fluidStack.getAmount();
        String fluid = fluidStack.getFluid().getFluidType().getDescription().getString();
        if(fluidAmount != 0) return Component.translatable(stack.getDescriptionId() + "_with_fluid", fluid);
        return super.getName(stack);
    }

}
