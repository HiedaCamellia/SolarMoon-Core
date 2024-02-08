package cn.solarmoon.solarmoon_core.common.entity_block.entity;

import cn.solarmoon.solarmoon_core.util.namespace.NBTList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;


/**
 * 基本的储罐类抽象实体<br/>
 * 没有多余的功能，只是添加一个储罐<br/>
 * 可以与大部分液体类工具/方块交互
 */
public abstract class BaseTankBlockEntity extends BlockEntity {

    public int ticks;
    public float lastScale;

    public int time;
    public int recipeTime;

    public FluidTank tank;

    public int maxCapacity;

    public BaseTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int maxCapacity) {
        super(type, pos, state);
        this.maxCapacity = maxCapacity;
        this.tank = new FluidTank(maxCapacity);
    }

    /**
     * 设置forge提供的储罐系统
     * @param cap The capability to check
     * @param side The Side to check from,
     *   <strong>CAN BE NULL</strong>. Null is defined to represent 'internal' or 'self'
     */
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return LazyOptional.of(() -> tank).cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * 一个强制设置储罐内容物的方法
     */
    public void setFluid(FluidStack fluidStack) {
        tank.setFluid(fluidStack);
    }

    /**
     * 一个根据tag强制设置储罐内容物的方法
     */
    public void setFluid(CompoundTag tag) {
        tank.readFromNBT(tag.getCompound(NBTList.FLUID));
    }

    /**
     * 配合setChanged调用，用以同步各类信息（主要是tag信息）
     * @param tag 读取save的tag
     */
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag fluid = tag.getCompound(NBTList.FLUID);
        tank.readFromNBT(fluid);
    }

    /**
     * 配合setChanged调用，用以同步各类信息（主要是tag信息）
     * @param tag 自定义以输入load
     */
    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag fluid = new CompoundTag();
        tank.writeToNBT(fluid);
        tag.put(NBTList.FLUID, fluid);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        ticks = 0;
    }

}
