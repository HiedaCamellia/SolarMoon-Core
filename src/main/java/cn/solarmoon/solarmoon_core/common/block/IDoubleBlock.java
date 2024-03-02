package cn.solarmoon.solarmoon_core.common.block;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * 像门一样的竖直方向的双格方块
 */
public interface IDoubleBlock {

    EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    DoubleBlockHalf getDefaultHalfValue();

}
