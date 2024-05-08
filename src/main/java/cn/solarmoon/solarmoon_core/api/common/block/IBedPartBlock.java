package cn.solarmoon.solarmoon_core.api.common.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * 像床一样的平铺方向的双格方块，此类方块必须具有FACING属性！
 */
public interface IBedPartBlock extends IHorizontalFacingBlock {

    EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;

    default Direction getNeighbourDirection(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

}
