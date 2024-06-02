package cn.solarmoon.solarmoon_core.api.common.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;


/**
 * 基本的储罐类抽象实体<br/>
 * 省去了设置tank的麻烦<br/>
 * 可以不用但不能没有
 */
public abstract class BaseTankBlockEntity extends BlockEntity implements ITankBlockEntity {

    private final FluidTank tank;

    public BaseTankBlockEntity(BlockEntityType<?> type, int maxCapacity, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.tank = new FluidTank(maxCapacity);
    }

    @Override
    public FluidTank getTank() {
        return tank;
    }

}
