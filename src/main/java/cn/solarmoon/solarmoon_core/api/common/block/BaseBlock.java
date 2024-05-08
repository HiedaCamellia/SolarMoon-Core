package cn.solarmoon.solarmoon_core.api.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 有基本的朝向功能的方块（四面）<br/>
 * 并且能快速添加自定义属性
 */
public abstract class BaseBlock extends Block implements IHorizontalFacingBlock, IBlockFunctionProvider {

    public BaseBlock(Properties properties) {
        super(properties);
    }

    /**
     * 提醒设置碰撞箱
     */
    @Override
    public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    /**
     * 能发出红石信号
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

}
