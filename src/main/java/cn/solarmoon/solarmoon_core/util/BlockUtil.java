package cn.solarmoon.solarmoon_core.util;

import cn.solarmoon.solarmoon_core.common.block.IBedPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import static cn.solarmoon.solarmoon_core.common.block.IBedPartBlock.PART;
import static cn.solarmoon.solarmoon_core.common.block.IHorizontalFacingBlock.FACING;

public class BlockUtil {

    /**
     * 使得设置的方块具有原位方块的方向属性
     */
    public static boolean setBlockWithDirection(BlockState originState, BlockState state, Level level, BlockPos pos) {
        if (state.getValues().get(FACING) != null && originState.getValues().get(FACING) != null) {
            level.setBlock(pos, state.setValue(FACING, originState.getValue(FACING)), 3);
            return true;
        } else {
            level.setBlock(pos, state, 3);
            return false;
        }
    }

    /**
     * 快速从一个双方块替换为另一个双方块<br/>
     * 无需检测是否是双方块，如果不是的话什么也不会触发
     */
    public static boolean setBedPartBlock(BlockState originState, BlockState stateTo, Level level, BlockPos pos) {
        BedPart part = originState.getValue(PART);
        if (stateTo.getBlock() instanceof IBedPartBlock partBlock) {
            Direction direction = partBlock.getNeighbourDirection(part, originState.getValue(FACING));
            stateTo = stateTo.setValue(partBlock.PART, part).setValue(FACING, originState.getValue(FACING));
            BlockState stateTo2;
            if (part == BedPart.FOOT) {
                stateTo2 = stateTo.setValue(PART, BedPart.HEAD);
            } else stateTo2 = stateTo.setValue(PART, BedPart.FOOT);
            level.setBlock(pos, stateTo, 18);
            level.setBlock(pos.relative(direction), stateTo2, 18);
            level.updateNeighborsAt(pos, stateTo.getBlock());
            level.updateNeighborsAt(pos.relative(direction), stateTo.getBlock());
            return true;
        }
        return false;
    }

    /**
     * 移除平面双方块
     */
    public static boolean removeDoubleBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof IBedPartBlock partBlock) {
            Direction direction = partBlock.getNeighbourDirection(state.getValue(PART), state.getValue(FACING));
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 18);
            level.setBlock(pos.relative(direction), Blocks.AIR.defaultBlockState(), 18);
            level.updateNeighborsAt(pos, Blocks.AIR);
            level.updateNeighborsAt(pos.relative(direction), Blocks.AIR);
            return true;
        }
        return false;
    }

}
